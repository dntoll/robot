package daniel.robot.glWindow.model;

import java.io.IOException;

import daniel.IPSerialPort;
import daniel.robot.Direction;

public class SensorTower  {
	
	
	DirectionalReadingCollection reading = null;
	IPSerialPort port;
	Long timeReceived = (long) 0;
	private Direction lastDirection;
	private boolean isComplete;
	
	public SensorTower(String serverAdress) throws Exception {
		port = new IPSerialPort(serverAdress, 6789);
		
		Thread.sleep(2000);
		timeReceived = System.currentTimeMillis();
	}
	
	
	public boolean update() throws IOException {
		String data = port.read();
		
		if (data.equals("")) {
			return false;
		} else {
			if (data.contains("DONE")) {
				isComplete = true;
				return false;
			} 
			
			timeReceived =  System.currentTimeMillis();
			String[] parts = data.split(":");
			
			//System.out.println(data);
			if (parts.length == 6 && reading != null) {
				String code= parts[0];
				int direction = Integer.parseInt(parts[1]);//5
				Direction front = new Direction(360-(direction+180));//175
				Direction left = new Direction(360-(direction+180+90));
				Direction back = new Direction(360-(direction));
				Direction right = new Direction(360-(direction+90));
				lastDirection = front;
				
				if (code.equals("sh")) {
					reading.addSharpReading(front, Float.parseFloat(parts[2]), false);
					reading.addSharpReading(back, Float.parseFloat(parts[3]), false);
					reading.addSharpReading(left, Float.parseFloat(parts[4]), true);
					reading.addSharpReading(right, Float.parseFloat(parts[5]), true);
					
				} 
				
				
			}
			return true;
		}
	}

	public void askForMeasurement(Direction compass) throws IOException {
		port.write("5\n");
		timeReceived = System.currentTimeMillis();
		
		reading = new DirectionalReadingCollection(compass);
		isComplete = false;
	}

	public DirectionalReadingCollection getDistanceSensorReadings() {
		return reading;
	}

	public Direction getLastDirection() {
		return lastDirection;
	}

	public DirectionalReadingCollection getFullDistanceReading() {
		if (isComplete == false)
			return null;
		return getDistanceSensorReadings();
	}

	public void askForCalibrationMeasurement() {
		port.write("c\n");
		timeReceived = System.currentTimeMillis();
		
		reading = new DirectionalReadingCollection(null);
		isComplete = false;
	}
	
	

}
