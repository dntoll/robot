package daniel.robot.glWindow.model;

import java.io.IOException;

import daniel.IPSerialPort;
import daniel.robot.Direction;

public class SensorTower  {
	
	
	DistanceSensorReadings reading = null;
	IPSerialPort port;
	
	public SensorTower(String serverAdress) throws Exception {
		port = new IPSerialPort(serverAdress, 6789);
		
		Thread.sleep(2000);
		timeReceived = System.currentTimeMillis();
	}
	Long timeReceived = (long) 0;
	private Direction lastDirection;
	private boolean isComplete;
	
	public boolean update(Direction compass) throws IOException {
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
			if (parts.length == 4) {
				String code= parts[0];
				int direction = Integer.parseInt(parts[1]);
				Direction front = new Direction(direction);
				Direction back = new Direction(180+direction);
				lastDirection = front;
				
				if (code.equals("sh")) {
					reading.addSharpReading(front, Float.parseFloat(parts[2]));
					reading.addSharpReading(back, Float.parseFloat(parts[3]));
					
				} else {
					reading.addSonar(front, Float.parseFloat(parts[2]));
					reading.addSonar(back, Float.parseFloat(parts[3]));
				}
				
				
			}
			return true;
		}
	}

	public void askForMeasurement(Direction compass) throws IOException {
		{
			port.write("5\n");
			timeReceived = System.currentTimeMillis();
			
			reading = new DistanceSensorReadings(compass);
			isComplete = false;
		}
	}

	public DistanceSensorReadings getDistanceSensorReadings() {
		return reading;
	}

	public Direction getLastDirection() {
		return lastDirection;
	}

	public DistanceSensorReadings getFullCompassReading() {
		if (isComplete == false)
			return null;
		return getDistanceSensorReadings();
	}
	
	

}
