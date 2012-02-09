package dfisher.econosim.core;

import java.util.Map;
import java.util.Set;

public class Sell extends Action {
	private Bid winner;

	public Sell(Bid winner_) {
		winner = winner_;
	}
	
	@Override
	public Set<Bid> perform(Agent seller, Map<Good, Bid> marketState) {
		return seller.sell(winner, marketState);
	}

	@Override
	public int compareTo(Action o) {
		if (o instanceof Buy) return 1;
		else if (o instanceof OutBid) return -1;
		else {
			Sell b = (Sell)o;
			if (winner.getAmount() > b.winner.getAmount()) {
				return -1;
			} else if (winner.getAmount() < b.winner.getAmount()) {
				return 1;
			} else {
				return winner.getGood().getName().compareTo(b.winner.getGood().getName());
			}
		}
	}
	
}
