package phidgetController;

import java.util.Scanner;

public class changeValues {
	public static float[] main(String[] args, float wasStartX, float wasStartY, float wasEndX, float wasEndY, float wasGripping) {
		
		Scanner in = new Scanner(System.in);
		boolean fail = true;
		float startX = 0, startY = 0, endX = 0, endY = 0;
		float gripping = 0;
		float maxStartY = 0, minStartY, maxEndY, minEndY = 0;
				
		//Set start x
		while (fail) {
			System.out.format("Enter start X in cm (15-40, was: %s): ", wasStartX);
			startX = in.nextFloat();
			if (Math.abs(startX) <= 40) {
				fail = false;
			}
		}
		
		//Set start y
		fail = true;
		while (fail) {
			System.out.format("Enter start Y in cm (15-40, was: %s): ", wasStartY);
			startY = in.nextFloat();
			if (Math.abs(startY) <= 40) {
				if (Math.sqrt(Math.pow(startX, 2) + Math.pow(startY, 2)) >= 40 || 
						Math.sqrt(Math.pow(startX, 2) + Math.pow(startY, 2)) <= 15) {
					maxStartY = (float) Math.sqrt(1600 - Math.pow(startX, 2));
					minStartY = (float) Math.sqrt(225 - Math.pow(startX, 2));
					System.out.format("With startX = %2.0f, startY must be <= %2.0f, and >= %2.0f\n", startX, maxStartY, minStartY);
					continue;
				}
				fail = false;
			}
		}
		
		//Set end x
		fail = true;
		while (fail) {
			System.out.format("Enter end X in cm (15-40, was: %s): ", wasEndX);
			endX = in.nextFloat();
			if (Math.abs(endX) <= 40) {
				fail = false;
			}
		}
		
		//Set end y
		fail = true;
		while (fail) {
			System.out.format("Enter end Y in cm (15-40, was: %s): ", wasEndY);
			endY = in.nextFloat();
			if (Math.abs(endY) <= 40) {
				if (Math.sqrt(Math.pow(endX, 2) + Math.pow(endY, 2)) >= 40 ||
						Math.sqrt(Math.pow(endX, 2) + Math.pow(endY, 2)) <= 15) {
					maxEndY = (float) Math.sqrt(1600 - Math.pow(endX, 2));
					minEndY = (float) Math.sqrt(225 - Math.pow(endX, 2));
					System.out.format("With endX = %2.0f, endY must be <= %2.0f, and >= %2.0f\n", endX, maxEndY, minEndY);
					continue;
				}
				fail = false;
			}
		}
		
		//Set gripping
		fail = true;
		while (fail) {
			System.out.format("Enter degree of gripping (0-68, was: %s): ", wasGripping);
			gripping = in.nextFloat();
			if (gripping >= 0 && gripping <= 100) {
				fail = false;
			}
		}
		
		float[] changedPos = {startX, startY, endX, endY, gripping};
		return changedPos;
	}
}
