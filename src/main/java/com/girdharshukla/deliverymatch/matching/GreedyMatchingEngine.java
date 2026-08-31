package com.girdharshukla.deliverymatch.matching;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.springframework.stereotype.Component;

@Component
public class GreedyMatchingEngine implements MatchingEngine {

    @Override
    public List<Candidate> matching(List<Candidate> candidates) {
        candidates.sort(Comparator.comparingDouble(Candidate::distanceKm));

        List<Candidate> result = new ArrayList<>();

        Set<UUID> matchedOrders = new HashSet<>();
        Map<UUID, Integer> currentLoad = new HashMap<>();

        for(Candidate candidate: candidates){
            UUID orderId = candidate.order().getId();
            UUID agentId = candidate.agent().getId();

            if(matchedOrders.contains(orderId)) continue;

            if(candidate.agent().getCurrentLoad() == candidate.agent().getCapacity()) continue;

            matchedOrders.add(orderId);
            currentLoad.merge(agentId, 1, Integer::sum);
            result.add(candidate);
        }

        return result;
    }
    
}
