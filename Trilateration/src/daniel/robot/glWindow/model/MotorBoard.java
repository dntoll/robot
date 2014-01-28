package daniel.robot.glWindow.model;

import daniel.IPSerialPort;
import daniel.robot.Direction;
import daniel.robot.sensors.Compass;
import daniel.robot.sensors.CompassReading;
import daniel.robot.sensors.GyroAccelerometer;
import daniel.robot.sensors.GyroAccelerometerReading;

public class MotorBoard {

	IPSerialPort port;
	Compass compass = new Compass();
	float temperature = 0;
	GyroAccelerometer gyro = new GyroAccelerometer();
	
	public MotorBoard(String serverAdress) throws Exception {
		port = new IPSerialPort(serverAdress, 6790);
		
		Thread.sleep(2000);
	}
	
	public void update() {
		String data = port.read();
		
		if (data.equals("")) {
		} else {
			String[] parts = data.split(":");
			
			try {			
				if (parts[0].equals("Compass")) {
					float cx = Float.parseFloat(parts[1]);
					float cy = Float.parseFloat(parts[2]);
					float cz = Float.parseFloat(parts[3]);
					
					compass.add(new CompassReading(cx, cy, cz));
				} else if (parts[0].equals("Temperature")) {
					temperature = Float.parseFloat(parts[1]);
					
				} else if (parts[0].equals("Accel")) {
					float accelx = Float.parseFloat(parts[2]);
					float accely = Float.parseFloat(parts[3]);
					float accelz = Float.parseFloat(parts[4]);
					float gyrox = Float.parseFloat(parts[5]);
					float gyroy = Float.parseFloat(parts[6]);
					float gyroz = Float.parseFloat(parts[7]);
					
					GyroAccelerometerReading reading = new GyroAccelerometerReading(accelx, accely, accelz,
													gyrox, gyroy, gyroz);
					gyro.add(reading);
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
				System.out.println("Error in data : " + data);
			} catch (ArrayIndexOutOfBoundsException e) {
				e.printStackTrace();
				System.out.println("Error in data : " + data);
			}
			
			
			
		}
	}

	public float getTemperature() {
		return temperature;
	}

	public Direction getCompassDirection() {
		return compass.getHeading();
	}

	public Compass getCompass() {
		return compass;
	}

}
