package com.girdharshukla.deliverymatch.matching;

import java.util.List;

import com.girdharshukla.deliverymatch.models.Agent;
import com.girdharshukla.deliverymatch.models.Order;


public interface MatchingEngine {
    public record Candidate(Agent agent, Order order, double distanceKm){}
    
    List<Candidate> match(List<Candidate> candidates);

}
