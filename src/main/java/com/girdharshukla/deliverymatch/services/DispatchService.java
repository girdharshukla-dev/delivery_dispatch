package com.girdharshukla.deliverymatch.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.girdharshukla.deliverymatch.matching.MatchingEngine;
import com.girdharshukla.deliverymatch.matching.MatchingEngine.Candidate;
import com.girdharshukla.deliverymatch.models.Agent;
import com.girdharshukla.deliverymatch.models.Assignment;
import com.girdharshukla.deliverymatch.models.Order;
import com.girdharshukla.deliverymatch.repositories.AgentRepository;
import com.girdharshukla.deliverymatch.repositories.AssignmentRepository;
import com.girdharshukla.deliverymatch.repositories.OrderRepository;
import com.uber.h3core.H3Core;

@Service
public class DispatchService {

    private final OrderRepository orderRepository;
    private final AgentRepository agentRepository;
    private final AssignmentRepository assignmentRepository;
    private final MatchingEngine matchingEngine;
    private final H3Core h3Core;

    @Value("${h3.resolution}")
    int resolution;

    @Value("${h3.k}")
    int h3_k;

    public DispatchService(OrderRepository orderRepository, AgentRepository agentRepository,
            AssignmentRepository assignmentRepository, MatchingEngine matchingEngine, H3Core h3Core) {
        this.orderRepository = orderRepository;
        this.agentRepository = agentRepository;
        this.assignmentRepository = assignmentRepository;
        this.matchingEngine = matchingEngine;
        this.h3Core = h3Core;
    }

    @Transactional
    public void runDispatch() {
        List<Order> pendingOrders = orderRepository.findByStatus(Order.Status.PENDING);
        List<Agent> availableAgents = agentRepository.findAvailableAgents(Agent.Status.IDLE);

        List<Candidate> candidates = new ArrayList<>();

        Map<Long, List<Agent>> agentsByCell = new HashMap<>();
        for (Agent agent : availableAgents) {
            agentsByCell.computeIfAbsent(agent.getH3Cell(), k -> new ArrayList<>()).add(agent);
        }

        for (Order order : pendingOrders) {
            List<Long> ring = h3Core.gridDisk(order.getH3Cell(), h3_k);

            for (Long cell : ring) {
                List<Agent> nearbyAgents = agentsByCell.get(cell);
                if (nearbyAgents == null)
                    continue;

                for (Agent agent : nearbyAgents) {
                    double distance = haversineKm(agent.getLatitude(), agent.getLongitude(),
                            order.getLatitude(), order.getLongitude());

                    candidates.add(new Candidate(agent, order, distance));
                }
            }
        }

        

        List<Candidate> result = matchingEngine.match(candidates);

        for(Candidate candidate: result){
            Agent agent = candidate.agent();
            Order order = candidate.order();

            Assignment assignment = new Assignment();
            assignment.setId(UUID.randomUUID());
            assignment.setAgent(agent);
            assignment.setOrder(order);
            assignment.setMatchedAt(LocalDateTime.now());
            assignmentRepository.save(assignment);

            agent.setCurrentLoad(agent.getCurrentLoad() + 1);
            if(agent.getCurrentLoad() == agent.getCapacity()){
                agent.setStatus(Agent.Status.BUSY);
            }
            agentRepository.save(agent);

            order.setStatus(Order.Status.MATCHED);
            orderRepository.save(order);
        }
    }

    private double haversineKm(double lat1, double long1, double lat2, double long2) {
        double radius = 6371.0;
        double dlat = Math.toRadians(lat2 - lat1);
        double dlong = Math.toRadians(long2 - long1);

        double a = Math.pow(Math.sin(dlat / 2), 2) +
                Math.pow(Math.sin(dlong / 2), 2) * Math.cos(Math.toRadians(lat1))
                        * Math.cos(Math.toRadians(lat2));

        return 2 * radius * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }

}
