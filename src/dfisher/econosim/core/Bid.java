package dfisher.econosim.core;

public class Bid {

	Agent from;
	Good good;
	int amount;

	public Bid(Agent from, Good good, int amount) {
		super();
		this.from = from;
		this.good = good;
		this.amount = amount;
	}

	public Agent getFrom() {
		return from;
	}
	
	public Good getGood() {
		return good;
	}
	
	public int getAmount() {
		return amount;
	}
	
}
