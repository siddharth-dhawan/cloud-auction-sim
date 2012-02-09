package dfisher.econosim.core;

import java.util.Map;
import java.util.Set;

public class Buy extends Action {
	private Bid winner;
	
	public Buy(Bid winner_) {
		winner = winner_;
	}
	
	@Override
	public Set<Bid> perform(Agent buyer, Map<Good, Bid> marketState) {
		return buyer.buy(winner, marketState);
	}

	@Override
	public int compareTo(Action o) {
		if (o instanceof Buy) {
			Buy b = (Buy)o;
			if (winner.getAmount() > b.winner.getAmount()) {
				return -1;
			} else if (winner.getAmount() < b.winner.getAmount()) {
				return 1;
			} else {
				return winner.getGood().getName().compareTo(b.winner.getGood().getName());
			}
		} else return -1;
	}

}
