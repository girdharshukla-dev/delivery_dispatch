package com.girdharshukla.deliverymatch.matching;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.girdharshukla.deliverymatch.models.Order;

@Component
public class HungarianMatchingEngine implements MatchingEngine {
    @Override
    public List<Candidate> match(List<Candidate> candidates) {
        List<Order> orders = new ArrayList<>();
    }

    private int[] solve(double[][] cost) {
        int n = cost.length - 1;

        double[] agentPotential = new double[n + 1];
        double[] orderPotential = new double[n + 1];

        int[] agentForOrder = new int[n + 1];
        int[] previousOrder = new int[n + 1]; // previousOrder[0] is dummy order

        for (int agent = 1; agent <= n; agent++) {
            agentForOrder[0] = agent;

            int currentOrder = 0;

            double[] minReducedCost = new double[n + 1];
            Arrays.fill(minReducedCost, Integer.MAX_VALUE);

            boolean[] usedOrder = new boolean[n + 1];

            do {
                usedOrder[currentOrder] = true;

                int currentAgent = agentForOrder[currentOrder];
                int nextOrder = -1;
                double delta = Integer.MAX_VALUE;

                for (int order = 1; order <= n; order++) {
                    if (usedOrder[order])
                        continue;

                    double reducedCost = cost[currentAgent][currentOrder]
                            - agentPotential[currentAgent]
                            - orderPotential[order];

                    if (reducedCost < minReducedCost[order]) {
                        minReducedCost[order] = reducedCost;
                        previousOrder[order] = currentOrder;
                    }

                    if (minReducedCost[order] < delta) {
                        delta = minReducedCost[order];
                        nextOrder = order;
                    }
                }

                for (int order = 0; order <= n; order++) {
                    if (usedOrder[order]) {
                        agentPotential[agentForOrder[order]] += delta;
                        orderPotential[order] -= delta;
                    } else {
                        minReducedCost[order] -= delta;
                    }
                }

                currentOrder = nextOrder;

            } while (agentForOrder[currentOrder] != 0);

            do {
                int previous = previousOrder[currentOrder];
                agentForOrder[currentOrder] = agentForOrder[previous];
                currentOrder = previous;
            } while (currentOrder != 0);
        }


        int[] assignedOrder = new int[n + 1];

        for(int order = 1; order <= n; order++){
            int agent = agentForOrder[order];
            if(agent != 0){
                assignedOrder[agent] = order;
            }
        }


        return assignedOrder;

    }

}
