package phidgetController;

import java.util.Scanner;
import com.phidget22.*; 

public class Controller {

	public static void main(String[] args) throws Exception {
		//Create your Phidget channels
		RCServo rcServo0 = new RCServo();
		RCServo rcServo1 = new RCServo();
		RCServo rcServo2 = new RCServo();
		RCServo rcServo3 = new RCServo();
		RCServo rcServo4 = new RCServo();
		RCServo rcServo5 = new RCServo();
		
		//Set addressing parameters to specify which channel to open (if any)
		rcServo0.setChannel(0); //Shoulder rotation
		rcServo1.setChannel(2);	//Shoulder angle
		rcServo2.setChannel(3);	//Elbow angle
		rcServo3.setChannel(4); //Wrist rotation
		rcServo4.setChannel(6);	//Hand open/close
		rcServo5.setChannel(7);	//Wrist angle
		
		//Assign events to notify on attach, and notify and end program on detach
		rcServo0.addAttachListener(new AttachListener() {
			public void onAttach(AttachEvent e) {
				System.out.println("Attach [0]!");
			}
		});
		
		rcServo0.addDetachListener(new DetachListener() {
			public void onDetach(DetachEvent e) {
				System.out.println("Detach [0], terminate!");
				System.exit(0);
			}
		});
		
		rcServo1.addAttachListener(new AttachListener() {
			public void onAttach(AttachEvent e) {
				System.out.println("Attach [1]!");
			}
		});
		
		rcServo1.addDetachListener(new DetachListener() {
			public void onDetach(DetachEvent e) {
				System.out.println("Detach [1], terminate!");
				System.exit(0);
			}
		});
		
		rcServo2.addAttachListener(new AttachListener() {
			public void onAttach(AttachEvent e) {
				System.out.println("Attach [2]!");
			}
		});
		
		rcServo2.addDetachListener(new DetachListener() {
			public void onDetach(DetachEvent e) {
				System.out.println("Detach [2], terminate!");
				System.exit(0);
			}
		});
		
		rcServo3.addAttachListener(new AttachListener() {
			public void onAttach(AttachEvent e) {
				System.out.println("Attach [3]!");
			}
		});
		
		rcServo3.addDetachListener(new DetachListener() {
			public void onDetach(DetachEvent e) {
				System.out.println("Detach [3], terminate!");
				System.exit(0);
			}
		});
		
		rcServo4.addAttachListener(new AttachListener() {
			public void onAttach(AttachEvent e) {
				System.out.println("Attach [4]!");
			}
		});
		
		rcServo4.addDetachListener(new DetachListener() {
			public void onDetach(DetachEvent e) {
				System.out.println("Detach [4], terminate!");
				System.exit(0);
			}
		});
		
		rcServo5.addAttachListener(new AttachListener() {
			public void onAttach(AttachEvent e) {
				System.out.println("Attach [5]!");
			}
		});
		
		rcServo5.addDetachListener(new DetachListener() {
			public void onDetach(DetachEvent e) {
				System.out.println("Detach [5], terminate!");
				System.exit(0);
			}
		});
		
		//Open Phidgets
		rcServo0.open(5000);
		rcServo1.open(5000);
		rcServo2.open(5000);
		rcServo3.open(5000);
		rcServo4.open(5000);
		rcServo5.open(5000);
	
		
		
		
		//Controlling the arm
		
		//Variable for y < 0
		int reverse; 
		
		//Start/end points, new values, distances, start/end/gripping angle
		float startX = 20, startY = 20, endX = 20, endY = 20;
		float[] startPos = {0, 0}, endPos = {0, 0};
		float[] changedPos = {0, 0, 0, 0};
		float distStart, distEnd;
		float alpha, beta;
		float gripping = 50;
		
		//Length of parts, default angles and the three angles
		final float SHOULDERLEN = 0.125f, NULLDIST = 11.4f, HANDLEN = 0.194f;
		final float DEFBASEANGLE = 90, DEFSHOULDERANGLE = 45, DEFELBOWANGLE = 180, 
				DEFWRISTANGLE = 180, DEFHANDOPEN = 80, DEFWRISTROT = 90;
		float shoulderAngle, elbowAngle, wristAngle;
		
		//Move distance, vector with straight line, current position to calculate
		float moveDist;
		float[] moveVector = {0, 0}, currentPos = {0, 0};
		
		//Traveled length, total length to travel
		float traveled, travelLen, round, distCurrent, prevPos = 0;
		final float ROUNDLENGTH = 0.01f;
		
		//Variable for input, option to change and new round
		String input, changeOpt, newRound;
		
		//Scanner for input reading
		Scanner in = new Scanner(System.in);
		
		
		
		//Reach default position
		rcServo0.setTargetPosition(DEFBASEANGLE);
		rcServo0.setEngaged(true);
		rcServo1.setTargetPosition(DEFSHOULDERANGLE);
		rcServo1.setEngaged(true);
		rcServo2.setTargetPosition(DEFELBOWANGLE);
		rcServo2.setEngaged(true);
		rcServo3.setTargetPosition(DEFWRISTROT);
		rcServo3.setEngaged(true);
		rcServo4.setTargetPosition(DEFHANDOPEN);
		rcServo4.setEngaged(true);
		rcServo5.setTargetPosition(DEFWRISTANGLE);
		rcServo5.setEngaged(true);
		
		//Set pick-up and end point
		changedPos = changeValues.main(args, startX, startY, endX, endY, gripping);
		
		
	
		//Main loop for moving along straight line
		newRound = "y";
		while(newRound.equals("y")) { //Option to run several times
			//Set pick-up and end point position
			startPos[0] = -changedPos[0]/100;
			startPos[1] = changedPos[1]/100;
			endPos[0] = -changedPos[2]/100;
			endPos[1] = changedPos[3]/100;
			
			//Check for negative y
			reverse = 1;
			if(startPos[1] < 0) {
				reverse = -1;
			}
			
			//Calculate start and end value distance and base angle
			distStart = (float) Math.sqrt(Math.pow(startPos[0], 2) + Math.pow(startPos[1], 2));
			alpha = (float) (Math.acos(startPos[0] / distStart)/Math.acos(-1)*180);
			distEnd = (float) Math.sqrt(Math.pow(endPos[0], 2) + Math.pow(endPos[1], 2));
			beta = (float) (Math.acos(endPos[0] / distEnd) / Math.acos(-1)*180);
			
			//Set default values
			shoulderAngle = 90;
			elbowAngle = 180;
			wristAngle = 180;
			
			//Change side if y value < 0
			if(startPos[1] < 0) {
				shoulderAngle = 100;
				elbowAngle = 0;
				wristAngle = 0;
				alpha = 180 - alpha;
			}
			
			//Calculate angles for start position
			moveDist = (distStart - NULLDIST);
			shoulderAngle += reverse*Math.asin(moveDist/SHOULDERLEN * 125/319)/Math.acos(-1)*180;
			elbowAngle -= reverse*Math.asin(moveDist/SHOULDERLEN*125/319)/Math.acos(-1)*180;
			wristAngle -= reverse*Math.asin(moveDist/HANDLEN*194/319)/Math.acos(-1)*180;
			
			
			
			//Open hand and set hand rotation
			rcServo3.setTargetPosition(90);
			rcServo3.setEngaged(true);
			rcServo4.setTargetPosition(1);
			rcServo4.setEngaged(true);
			
			//Move to startPoint
			rcServo0.setTargetPosition(alpha);
			rcServo0.setEngaged(true);
			rcServo1.setTargetPosition(shoulderAngle - 8*reverse);
			rcServo1.setEngaged(true);
			rcServo2.setTargetPosition(elbowAngle);
			rcServo2.setEngaged(true);
			rcServo5.setTargetPosition(wristAngle - 5*reverse);
			rcServo5.setEngaged(true);
			
			Thread.sleep(1000);
			
			//Move down on start point
			rcServo1.setTargetPosition(shoulderAngle + 2*reverse);
			rcServo1.setEngaged(true);
			rcServo5.setTargetPosition(wristAngle);
			rcServo5.setEngaged(true);
			
			Thread.sleep(1000);
			
			//Close hand
			rcServo4.setTargetPosition(changedPos[4] + 28);
			rcServo4.setEngaged(true);
			
			Thread.sleep(1000);
		
		
			//Move arm in straight line
			moveVector[0] = endPos[0] - startPos[0];
			moveVector[1] = endPos[1] - startPos[1];
			traveled = 0;
			currentPos[0] = 0;
			currentPos[1] = 0;
			distCurrent = 0;
			
			System.out.format("%f, %f", moveVector[0], moveVector[1]);
			
			//Set values for keeping track  of number of rounds
			round = 0;
			travelLen = (float) Math.sqrt(Math.pow(moveVector[0], 2) + Math.pow(moveVector[1], 2));
			
			
			//Increment through vector along straight line
			while (traveled < travelLen) {
				//Calculate current target position
				currentPos[0] = startPos[0] + moveVector[0] * round;
				currentPos[1] = startPos[1] + moveVector[1] * round;
				
				//Calculate angles based or target position
				distCurrent = (float) Math.sqrt(Math.pow(currentPos[0], 2) + Math.pow(currentPos[1], 2));
				alpha = (float) (Math.acos(currentPos[0] / distCurrent)/Math.acos(-1)*180);	
				shoulderAngle = 90;
				elbowAngle = 180;
				wristAngle = 180;
				
				//Reverse if y < 0
				reverse = 1;
				if(currentPos[1] < 0) {
					reverse = -1;
					shoulderAngle = 100;
					elbowAngle = 0;
					wristAngle = 0;
					alpha = 180 - alpha;
				}
				
				//Calculate distance from origo to angles
				moveDist = (distCurrent - NULLDIST);
				if(moveDist < 0) { moveDist = 0; }
				shoulderAngle += reverse*Math.asin(moveDist/SHOULDERLEN*125/319)/Math.acos(-1)*180;
				elbowAngle -= reverse*Math.asin(moveDist/SHOULDERLEN*125/319)/Math.acos(-1)*180;
				wristAngle -= reverse*Math.asin(moveDist/HANDLEN*194/319)/Math.acos(-1)*180;
				
				//Values for next increment
				round += ROUNDLENGTH;
				traveled = (float) Math.sqrt(Math.pow(moveVector[0]*round, 2) + Math.pow(moveVector[1]*round, 2));
				
				//Move to found angles
				rcServo0.setTargetPosition(alpha);
				rcServo0.setEngaged(true);
				rcServo1.setTargetPosition(shoulderAngle - 4*reverse);
				rcServo1.setEngaged(true);
				rcServo2.setTargetPosition(elbowAngle);
				rcServo2.setEngaged(true);
				rcServo5.setTargetPosition(wristAngle);
				rcServo5.setEngaged(true);
				
				//When turning, wait to reach all the way around
				if(currentPos[1]*prevPos < 0) {
					while(rcServo0.getIsMoving() || rcServo1.getIsMoving() || rcServo5.getIsMoving()) {
						//Waits as long as while is running
					}
				}
				prevPos = currentPos[1];
			}
			
			//Set last point and lower crane a bit
			
			shoulderAngle = 90;
			elbowAngle = 180;
			wristAngle = 180;
			
			//Turn if y < 0
			reverse = 1;
			if(endPos[1] < 0) {
				reverse = -1;
				shoulderAngle = 97;
				elbowAngle = 0;
				wristAngle = 0;
				beta = 180 - beta;
			}
			
			//Calculate angles
			moveDist = (distEnd - NULLDIST);
			shoulderAngle += reverse*Math.asin(moveDist/SHOULDERLEN*125/319)/Math.acos(-1)*180;
			elbowAngle -= reverse*Math.asin(moveDist/SHOULDERLEN*125/319)/Math.acos(-1)*180;
			wristAngle -= reverse*Math.asin(moveDist/HANDLEN*194/319)/Math.acos(-1)*180;
			
			//Move to found angles
			rcServo0.setTargetPosition(beta);
			rcServo0.setEngaged(true);
			rcServo1.setTargetPosition(shoulderAngle);
			rcServo1.setEngaged(true);
			rcServo2.setTargetPosition(elbowAngle);
			rcServo2.setEngaged(true);
			rcServo5.setTargetPosition(wristAngle);
			rcServo5.setEngaged(true);
			
			Thread.sleep(1000);
			
			//Open hand: let go
			rcServo4.setTargetPosition(50);
			rcServo4.setEngaged(true);
			
			Thread.sleep(1000);
			
			//Move above to stay clear
			rcServo1.setTargetPosition(shoulderAngle - 15*reverse);
			rcServo1.setEngaged(true);
			rcServo5.setTargetPosition(wristAngle - 5*reverse);
			rcServo5.setEngaged(true);
			
			Thread.sleep(1000);
			
			//Reach default position
			rcServo0.setTargetPosition(DEFBASEANGLE);
			rcServo0.setEngaged(true);
			rcServo1.setTargetPosition(DEFSHOULDERANGLE);
			rcServo1.setEngaged(true);
			rcServo2.setTargetPosition(DEFELBOWANGLE);
			rcServo2.setEngaged(true);
			rcServo3.setTargetPosition(DEFWRISTROT);
			rcServo3.setEngaged(true);
			rcServo4.setTargetPosition(DEFHANDOPEN);
			rcServo4.setEngaged(true);
			
			Thread.sleep(500);
			
			rcServo5.setTargetPosition(DEFWRISTANGLE);
			rcServo5.setEngaged(true);
			
			Thread.sleep(1000);
			
			//Give choice of starting new round
			input = "p";
			while(!input.equals("y") && !input.equals("n")) {
				System.out.println("\nNew round? (y/n): ");
				input = in.next();
			}
			newRound = input;
			
			//Give option to change values
			if (newRound.equals("y")) {
				input = "p";
				while (!input.equals("y") && !input.equals("n")) {
					System.out.println("\nChange values? (y/n): ");
					input = in.next();
				}
				changeOpt = input;
				
				if (changeOpt.equals("y")) {
					changedPos = changeValues.main(args, changedPos[0], changedPos[1], changedPos[2], changedPos[3], changedPos[4]);
				}
			}
		}
		
		in.close(); //Close input read
	
		
		//Close phidgets, leaving channels open for other use
		rcServo0.close();
		rcServo1.close();
		rcServo2.close();
		rcServo3.close();
		rcServo4.close();
		rcServo5.close();
		
		
		System.out.println("System exit");
	}
}
