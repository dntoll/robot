package daniel.robot.glWindow.model;

import java.io.IOException;

import daniel.IPSerialPort;
import daniel.robot.Direction;
import daniel.robot.FloatCollection;

public class SensorTower  {
	
	
	DirectionalReadingCollection reading = null;
	IPSerialPort port;
	Long timeReceived = (long) 0;
	private Direction lastDirection;
	private boolean isComplete;
	CalibrationModel model;
	
	public SensorTower(String serverAdress, CalibrationModel model) throws Exception {
		port = new IPSerialPort(serverAdress, 6789);
		
		Thread.sleep(2000);
		timeReceived = System.currentTimeMillis();
		this.model = model;
	}
	
	
	FloatCollection[] calibrationValues = new FloatCollection[] {
			new FloatCollection(), //sensor 1 short range
			new FloatCollection() //sensor 3 long range
		};
	
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
				Direction left = new Direction(360-(direction+180+90));//85
				Direction back = new Direction(360-(direction));
				Direction right = new Direction(360-(direction+90));
				lastDirection = front;
				
				if (code.equals("sh")) {
					float distanceCM = Float.parseFloat(parts[2]);
					reading.addSharpReading(front, distanceCM, false);
					calibrationValues[0].addValue(distanceCM);
					reading.addSharpReading(back, Float.parseFloat(parts[3]), false);
					
					distanceCM = Float.parseFloat(parts[4]);
					calibrationValues[1].addValue(distanceCM);
					reading.addSharpReading(left, distanceCM, true);
					reading.addSharpReading(right, Float.parseFloat(parts[5]), true);
				} 
				
				
			}
			return true;
		}
	}

	public void askForMeasurement(Direction compass) throws IOException {
		port.write("5\n");
		timeReceived = System.currentTimeMillis();
		
		reading = new DirectionalReadingCollection(compass, model);
		isComplete = false;
	}
	
	public void askForCalibrationMeasurement() {
		port.write("c\n");
		timeReceived = System.currentTimeMillis();
		
		reading = new DirectionalReadingCollection(null, model);
		calibrationValues = new FloatCollection[] {
				new FloatCollection(), //sensor 1 short range
				new FloatCollection() //sensor 3 long range
			};
		isComplete = false;
	}
	
	public void askForSingleRead() {
		port.write("s\n");
		timeReceived = System.currentTimeMillis();
		
		reading = new DirectionalReadingCollection(null, model);
		calibrationValues = new FloatCollection[] {
				new FloatCollection(), //sensor 1 short range
				new FloatCollection() //sensor 3 long range
			};
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
	
	public FloatCollection[] getCalibrationReading() {
		if (isComplete == false)
			return null;
		return calibrationValues;
	}

	

	
	
	

}
