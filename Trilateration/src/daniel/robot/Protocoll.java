package daniel.robot;

import daniel.SerialPort;
import daniel.robot.sensors.IRandSonarReading;
import daniel.robot.sensors.GyroAccelerometerReading;
import daniel.robot.sensors.IRReading;
import daniel.robot.sensors.SonarReading;


public class Protocoll {

	public Direction readCompass(String string) throws Exception {
		String[] parts = Split(string);
		if (parts[0].equals("Compass")) {
			return new Direction(Float.parseFloat(parts[1]));
		} else {
			throw new Exception("No header found" +string);
		}
	}
	
	public GyroAccelerometerReading readGyroAndAccelerometer(
			String readSyncronosly) throws Exception {
		String[] parts = Split(readSyncronosly);
		if (parts[0].equals("Accel") && parts[1].equals("Gyro")) {
			return new GyroAccelerometerReading(Float.parseFloat(parts[2]), 
												Float.parseFloat(parts[3]), 
												Float.parseFloat(parts[4]), 
												Float.parseFloat(parts[5]), 
												Float.parseFloat(parts[6]), 
												Float.parseFloat(parts[7]));
		} else {
			throw new Exception("No header found");
		}
	}


	public SonarReading[] readSonar(String readSyncronosly) throws Exception {
		
		
		String[] parts = Split(readSyncronosly);
		if (parts[0].equals("Servo") && parts[1].equals("Sonar")) {
			int numMeasurements = (parts.length - 2)/2;
			SonarReading[] all = new SonarReading[numMeasurements];
			for (int i = 0; i < numMeasurements; i++) {
				int index = 2 + i*2;
				all[i] = new SonarReading(Float.parseFloat(parts[index]), 
										  Float.parseFloat(parts[index+1]));
			}
			
			return all;
		} else {
			throw new Exception("No header found" + parts[0] + parts[1]);
		}
	}


	public IRReading[] readIR(String readSyncronosly) throws Exception {
		String[] parts = Split(readSyncronosly);
		if (parts[0].equals("Servo") && parts[1].equals("Sharp")) {
			int numMeasurements = (parts.length - 2)/2;
			IRReading[] all = new IRReading[numMeasurements];
			for (int i = 0; i < numMeasurements; i++) {
				int index = 2 + i*2;
				all[i] = new IRReading(Float.parseFloat(parts[index]), 
										  Float.parseFloat(parts[index+1]));
			}
			
			return all;
		} else {
			throw new Exception("No header found" + parts[0] + parts[1]);
		}
	}


	public float readTemperature(String readSyncronosly) throws Exception {
		String[] parts = Split(readSyncronosly);
		if (parts[0].equals("Temperature")) {
			return Float.parseFloat(parts[1]);
		} else {
			throw new Exception("No header found");
		}
	}

	
	private String[] Split(String data) throws Exception {
		if (data != "") {
			if (data.endsWith("\n")) {
				data = data.substring(0, data.length()-2);
			}
			String[] splitted = data.split(":");
			
			/*for(String str : splitted) {
				System.out.println("[" + str + "]");
			}*/
			
			return splitted;
		}
		throw new Exception("no data provided");
	}

	public void waitForDone(SerialPort port) {
		String data;
		
		while(true) {
			data = port.read();
			
			if (data != ""){
				System.out.print("Startup input : " + data);
			}
			
			if (data.startsWith("DONE")) {
				break;
			} 
		}
	}

	public void calibrate(SerialPort port) {
		port.write("n\n");
	}
	
	public void stopCalibrate(SerialPort port) {
		port.write("n\n");
	}

	public IRandSonarReading readDistanceMeasurement(String readSyncronosly) throws Exception {
		String[] parts = Split(readSyncronosly);
		if (parts[0].equals("Sharp") && parts[1].equals("Sonar")) {
			IRReading i = new IRReading(90.0f, 
					  Float.parseFloat(parts[2]));
			SonarReading s = new SonarReading(90.0f, 
										  Float.parseFloat(parts[3]));
			
			return new IRandSonarReading(s, i); 
			
		} else {
			throw new Exception("No header found" + parts[0] + parts[1]);
		}
	}


	
}
