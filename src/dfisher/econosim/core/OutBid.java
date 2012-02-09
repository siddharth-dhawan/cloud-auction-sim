package dfisher.econosim.core;

import java.util.Map;
import java.util.Set;

public class OutBid extends Action {
	private Bid loser;
	
	public OutBid(Bid loser_) {
		loser = loser_;
	}
	
	@Override
	public Set<Bid> perform(Agent agt, Map<Good, Bid> marketState) {
		return agt.outBid(loser, marketState);
	}

	@Override
	public int compareTo(Action o) {
		if (o instanceof OutBid) {
			OutBid b = (OutBid)o;
			if (loser.getAmount() > b.loser.getAmount()) {
				return -1;
			} else if (loser.getAmount() < b.loser.getAmount()) {
				return 1;
			} else {
				return loser.getGood().getName().compareTo(b.loser.getGood().getName());
			}
		} else return 1;
	}

}
