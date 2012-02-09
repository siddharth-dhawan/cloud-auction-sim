package dfisher.econosim.basic;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import dfisher.econosim.core.Agent;
import dfisher.econosim.core.Bid;
import dfisher.econosim.core.Good;

public class Eater extends Agent {

	private double nutritionPreference;
	private double flavorPreference;
	
	Set<Bid> buys;
	Set<Bid> sells;
	
	protected Eater(String name_, double d, double e) {
		super(name_);
		nutritionPreference = d;
		flavorPreference = e;
		buys = new HashSet<Bid>();
		sells = new HashSet<Bid>();
	}
	
	public Set<Bid> getBuys() {
		return buys;
	}

	public Set<Bid> getSells() {
		return sells;
	}

	private Set<Bid> getNewBids(Map<Good, Bid> marketState) {
		Set<Bid> activeBids = getActiveBids();
		Set<Good> goodsWithActiveBids = new HashSet<Good>();
		for (Bid b : activeBids) {
			goodsWithActiveBids.add(b.getGood());
		}
		Comparator<Good> comp = new Comparator<Good>() {
			@Override
			public int compare(Good arg0, Good arg1) {
				if (arg0 instanceof Food) {
					Food f0 = (Food)arg0;
					if (arg1 instanceof Food) {
						Food f1 = (Food)arg1;
						double p0 = 
								nutritionPreference * f0.getNutrition()
								+ flavorPreference * f0.getFlavor();
						double p1 = 
								nutritionPreference * f1.getNutrition()
								+ flavorPreference * f1.getFlavor();
						if (p0 == p1) {
							return arg0.getName().compareTo(arg1.getName());
						}
						return Double.compare(p1, p0);
					} else {
						return -1;
					}
				} else {
					if (arg1 instanceof Food) {
						return 1;
					} else {
						return arg0.getName().compareTo(arg1.getName());
					}
				}
			}
		};
		SortedSet<Good> goodsToBidOn = new TreeSet<Good>(comp);
		for (Good g : marketState.keySet()) {
			if (!goodsWithActiveBids.contains(g)) {
				goodsToBidOn.add(g);
			}
		}
		Set<Bid> newBids = new HashSet<Bid>();
		for (Good g : goodsToBidOn) {
			int minimumBid = getMinimumBidOnGood(g, marketState);
			if (minimumBid <= getUncommittedMoney()) {
				Bid b = new Bid(this, g, minimumBid);
				newBids.add(b);
				addBid(b);
			}
		}
		return newBids;
	}
	
	@Override
	public Set<Bid> sell(Bid winningBid, Map<Good, Bid> marketState) {
		if (winningBid.getGood() != null)
			sells.add(winningBid);
		addMoney(winningBid.getAmount());
		return getNewBids(marketState);
	}

	@Override
	public Set<Bid> buy(Bid winningBid, Map<Good, Bid> marketState) {
		if (winningBid.getGood() != null)
			buys.add(winningBid);
		removeBid(winningBid);
		subMoney(winningBid.getAmount());
		return getNewBids(marketState);
	}

	@Override
	public Set<Bid> outBid(Bid losingBid, Map<Good, Bid> marketState) {
		removeBid(losingBid);
		return getNewBids(marketState);
	}

}
