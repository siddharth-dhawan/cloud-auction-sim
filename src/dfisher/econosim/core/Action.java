package dfisher.econosim.core;

import java.util.Map;
import java.util.Set;

public abstract class Action implements Comparable<Action> {
	
	public abstract Set<Bid> perform(Agent agt, Map<Good, Bid> marketState);
	
}
