package utility;

import java.util.Random;

public class Utility {

	public static double getRandomFromRange(Random random, double lowerLimit, double upperLimit) {
		
		double randomNumber;
		boolean isUpper;
		
		isUpper = random.nextBoolean();
		
		if(isUpper) {
			randomNumber = random.nextDouble() * upperLimit;
		} else {
			randomNumber = random.nextDouble() * lowerLimit;
		}
		
		return randomNumber;
		
	}
	
}
