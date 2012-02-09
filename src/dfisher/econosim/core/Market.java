package dfisher.econosim.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class Market {

	private Map<Good, Agent> goodsOnMarket;
	private Map<Good, Bid> marketState;
	private Map<Agent, SortedSet<Action>> pendingQueue;
	
	private int maxAgentId;
	private Map<Agent, Integer> agentIds;
	
	private int maxGoodId;
	private Map<Good, Integer> goodIds;
	
	public Market() {
		goodsOnMarket = new HashMap<Good, Agent>();
		marketState = new HashMap<Good, Bid>();
		pendingQueue = new HashMap<Agent, SortedSet<Action>>();
		maxAgentId = 0;
		agentIds = new HashMap<Agent, Integer>();
		maxGoodId = 0;
		goodIds = new HashMap<Good, Integer>();
	}
	
	public void addAgent(Agent agt) {
		pendingQueue.put(agt, new TreeSet<Action>());
		agentIds.put(agt, maxAgentId++);
	}
	
	public void addGood(Agent agt, Good gd) {
		goodsOnMarket.put(gd, agt);
		marketState.put(gd, null);
		goodIds.put(gd, maxGoodId++);
	}

	private int bidPriority(Bid b, int round) {
		return (agentIds.get(b.getFrom()) + goodIds.get(b.getGood()) + round) % maxGoodId;
	}
	
	private Bid getWinningBid(Collection<Bid> bids, int round) {
		Bid winningBid = null;
		for (Bid b: bids) {
			int bidAmount = b.getAmount();
			int winningBidAmount = 0;
			if (winningBid != null) {
				winningBidAmount = winningBid.getAmount();
			}
			if ((bidAmount > winningBidAmount)
				|| ((bidAmount == winningBidAmount)
					&& (bidPriority(b, round) > bidPriority(winningBid, round)))) {
				winningBid = b;
			}
		}
		return winningBid;
	}
	
	
	/**
	 * Phase 1 empties the pending event queues and gathers new bids for the goods on the market.
	 */
	private Map<Good, Set<Bid>> runPhase1() {
		Map<Good, Set<Bid>> pendingBids = new HashMap<Good, Set<Bid>>();
		for (Good good : goodsOnMarket.keySet()) {
			pendingBids.put(good, new HashSet<Bid>());
		}
		for (Map.Entry<Agent, SortedSet<Action>> entry : pendingQueue.entrySet()) {
			Agent agt = entry.getKey();
			SortedSet<Action> events = entry.getValue();
			for (Action event : events) {
				Set<Bid> newBids = event.perform(agt, marketState);
				for (Bid b : newBids) {
					System.out.println(agt.getName() + " bids " + b.getAmount() + " for " + b.getGood().getName());
					pendingBids.get(b.getGood()).add(b);
				}
			}
		}
		return pendingBids;
	}
	
	/**
	 * Phase 2 determines winning bids, buys and sells, and sends the appropriate events to the appropriate queues.
	 */
	private void runPhase2(Map<Good, Set<Bid>> pendingBids, int round) {
		for (Agent agt : pendingQueue.keySet()) {
			pendingQueue.put(agt, new TreeSet<Action>());
		}
		Set<Good> goods = new HashSet<Good>(goodsOnMarket.keySet());
		for (Good good : goods) {
			Set<Bid> bids = pendingBids.get(good);
			if (bids.isEmpty()) {
				Bid winner = marketState.get(good);
				Agent seller = goodsOnMarket.get(good);
				if (winner == null) {
					System.out.println(good.getName() + " has no bids. Returned to " + seller.getName());
					pendingQueue.get(seller).add(new Buy(new Bid(seller, good, 0)));
				} else {
					Agent buyer = winner.getFrom();
					System.out.println(
						good.getName() 
						+ " sold by " + seller.getName() 
						+ " to " + buyer.getName() 
						+ " for " + winner.getAmount()
					);
					pendingQueue.get(seller).add(new Sell(winner));
					pendingQueue.get(buyer).add(new Buy(winner));
				}
				goodsOnMarket.remove(good);
				marketState.remove(good);
			} else {
				Bid winner = getWinningBid(bids, round);
				Bid previousWinner = marketState.get(good);
				if ((previousWinner != null) && (previousWinner.getAmount() >= winner.getAmount())) {
					System.out.println("ERROR: New winning bid for " + good.getName() + " is not greater than previous winner!");
					winner = previousWinner;
				}
				for (Bid b : bids) {
					if (b != winner) pendingQueue.get(b.getFrom()).add(new OutBid(b));
				}
				if ((previousWinner != null) && (previousWinner != winner))
					pendingQueue.get(previousWinner.getFrom()).add(new OutBid(previousWinner));
				marketState.put(good, winner);
			}
		}
	}
	
	public void run(int initialGrant) {
		int round = 0;
		for (Agent agt : agentIds.keySet()) {
			pendingQueue.get(agt).add(new Sell(new Bid(null, null, initialGrant)));
		}
		while (!goodsOnMarket.isEmpty()) {
			System.out.println("ROUND " + round);
			Map<Good, Set<Bid>> pendingBids = runPhase1();
			runPhase2(pendingBids, round);
			round++;
		}
	}
	
	public void closeMarket() {
		runPhase1();
	}
}
