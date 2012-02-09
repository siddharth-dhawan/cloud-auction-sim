package dfisher.econosim.core;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class Agent {

	private String name;
	private int money;
	private Set<Bid> activeBids;
	
	protected Agent(String name_) {
		name = name_;
		money = 0;
		activeBids = new HashSet<Bid>();
	}
	
	
	public String getName() {
		return name;
	}

	public int getMoney() {
		return money;
	}
	
	protected void addMoney(int amt) {
		money += amt;
	}

	protected void subMoney(int amt) {
		money -= amt;
	}
	
	public Set<Bid> getActiveBids() {
		return activeBids;
	}
	
	protected void addBid(Bid b) {
		activeBids.add(b);
	}
	
	protected void removeBid(Bid b) {
		activeBids.remove(b);
	}
	
	protected int getUncommittedMoney() {
		int uncommitted = money;
		for (Bid b : activeBids) {
			uncommitted -= b.getAmount();
		}
		return uncommitted;
	}
	
	protected int getMinimumBidOnGood(Good g, Map<Good, Bid> marketState) {
		Bid winner = marketState.get(g);
		if (winner == null) {
			return 1;
		} else {
			return winner.getAmount() + 1;
		}
	}
	
	public abstract Set<Bid> sell(Bid winningBid, Map<Good, Bid> marketState);
	
	public abstract Set<Bid> buy(Bid winningBid, Map<Good, Bid> marketState);
	
	public abstract Set<Bid> outBid(Bid losingBid, Map<Good, Bid> marketState);
	
}
