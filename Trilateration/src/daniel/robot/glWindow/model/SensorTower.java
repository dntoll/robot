package daniel.robot.glWindow.model;

import java.io.IOException;
import java.net.UnknownHostException;

import daniel.IPSerialPort;

public class SensorTower  {
	
	
	DistanceSensorReadings reading;
	IPSerialPort port;
	
	public SensorTower() throws Exception {
		reading = new DistanceSensorReadings();
		port = new IPSerialPort("192.168.1.8", 6789);
		
		Thread.sleep(2000);
		port.write("5\n");
	}
	
	public void update() {
		String data = port.read();
		
		if (data.equals("")) {
			return;
		} else {
			String[] parts = data.split(":");
			
			System.out.println(parts[0]);
			if (parts.length == 5) {
			
				reading.add(Float.parseFloat(parts[0]), 
						Float.parseFloat(parts[1]), 
						Float.parseFloat(parts[2]), 
						Float.parseFloat(parts[3]), 
						Float.parseFloat(parts[4]));
			}
		}
	}

	public DistanceSensorReadings getDistanceSensorReadings() {
		
		update();
		
		
		return reading;
	}
	
	

}
