package com.girdharshukla.deliverymatch.matching;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import com.girdharshukla.deliverymatch.matching.MatchingEngine.Candidate;
import com.girdharshukla.deliverymatch.models.Agent;
import com.girdharshukla.deliverymatch.models.Order;

class GreedyMatchingEngineTest {

	@Test
	void matchOrdertoNearestAgent() {
		Agent agent = new Agent();
		agent.setId(UUID.randomUUID());
		agent.setCapacity(1);
		agent.setCurrentLoad(0);

		Order order = new Order();
		order.setId(UUID.randomUUID());

		Candidate candidate = new Candidate(agent, order, 2.5);
		GreedyMatchingEngine matchingEngine = new GreedyMatchingEngine();

		List<Candidate> result = matchingEngine.match(new ArrayList<>(List.of(candidate)));

		assertThat(result).hasSize(1);
		assertThat(result.get(0).agent()).isEqualTo(agent);
		assertThat(result.get(0).order()).isEqualTo(order);
	}

}
