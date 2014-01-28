package daniel.robot.glWindow.model;

import daniel.IPSerialPort;

public class SensorTower  {
	
	
	DistanceSensorReadings reading;
	IPSerialPort port;
	
	public SensorTower(String serverAdress) throws Exception {
		reading = new DistanceSensorReadings();
		//port = new IPSerialPort("192.168.1.11", 6789);
		port = new IPSerialPort(serverAdress, 6789);
		
		//need to let the connection settle 
		Thread.sleep(2000);
		
		port.write("5\n");
	}
	
	public boolean update() {
		String data = port.read();
		
		if (data.equals("")) {
			return false;
		} else {
			String[] parts = data.split(":");
			
			System.out.println(data);
			if (parts.length == 5) {
			
				reading.add(Float.parseFloat(parts[0]), 
						Float.parseFloat(parts[1]), 
						Float.parseFloat(parts[2]), 
						Float.parseFloat(parts[3]), 
						Float.parseFloat(parts[4]));
			}
			return true;
		}
	}

	public DistanceSensorReadings getDistanceSensorReadings() {
		return reading;
	}
	
	

}
