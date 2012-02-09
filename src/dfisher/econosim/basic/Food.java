package dfisher.econosim.basic;

import dfisher.econosim.core.Good;

public class Food extends Good {
	
	private String name;
	private double nutrition;
	private double flavor;
	
	public Food(String name_, double nutrition_, double flavor_) {
		name = name_;
		nutrition = nutrition_;
		flavor = flavor_;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDescription() {
		return "Food with nutritional value " + nutrition + " and flavor " + flavor;
	}
	
	double getNutrition() {
		return nutrition;
	}
	
	double getFlavor() {
		return flavor;
	}

}
