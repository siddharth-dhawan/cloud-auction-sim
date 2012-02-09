package dfisher.econosim.basic;

import dfisher.econosim.core.Bid;
import dfisher.econosim.core.Market;

public class Test {

	private static void dumpEater(Eater e) {
		System.out.println(e.getName());
		for (Bid b : e.getSells()) {
			System.out.println("Sold " + b.getGood().getName() + " for " + b.getAmount());
		}
		for (Bid b : e.getBuys()) {
			System.out.println("Bought " + b.getGood().getName() + " for " + b.getAmount());
		}
		System.out.println("Cash on hand: " + e.getMoney());
	}
	
	public static void main(String[] args) {

		Market market = new Market();
		Eater alice = new Eater("Alice", 0.3, 0.7);
		Eater bob = new Eater("Bob", 0.5, 0.5);
		Eater carol = new Eater("Carol", 0.7, 0.3);

		market.addAgent(alice);
		market.addAgent(bob);
		market.addAgent(carol);
		market.addGood(alice, new Food("ice cream", 0.1, 0.9));
		market.addGood(bob, new Food("lobster", 0.3, 0.7));
		market.addGood(carol, new Food("cheeseburger", 0.4, 0.6));
		market.addGood(alice, new Food("hamburger", 0.6, 0.4));
		market.addGood(bob, new Food("salad", 0.7, 0.3));
		market.addGood(carol, new Food("quinoa", 0.9, 0.1));
		
		market.run(100);
		market.closeMarket();
		
		System.out.println("RESULTS");
		
		dumpEater(alice);
		dumpEater(bob);
		dumpEater(carol);
	}

}
