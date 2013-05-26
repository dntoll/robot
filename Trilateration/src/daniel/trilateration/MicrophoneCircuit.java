package daniel.trilateration;

import java.util.Arrays;

import daniel.SerialPort;

public class MicrophoneCircuit {
	
	SerialPort port;
	float error = 0;
	public MicrophoneCircuit(SerialPort port) {
		this.port = port;
		
		measurements = new float[NUM_CALIBRATIONS];
		for (int i = 0;i < NUM_CALIBRATIONS; i++) {
			measurements[i] = 0;
		}
		
	}
	public void send() {
		port.write("s\n");
	}
	
	public static final int NUM_CALIBRATIONS = 25; 
	float measurements[];
	
	public float readDistance(int index, float initialDistance) {
		float distance = getDistance();
		
		if (distance > 0) {
			measurements[index] = distance - initialDistance;
			return measurements[index];
		}
		return -1.0f;
	}
	
	public float getMedian() {
		Arrays.sort(measurements);
		for (int i = 0; i< NUM_CALIBRATIONS; i++) {
			if (measurements[i] > 0) {
				int numIndicies = NUM_CALIBRATIONS - i;
				return measurements[i + numIndicies/2];
			}
		}
		return -1;
	}
	
	public void doneCalibration() {
		System.out.print(port.getName() + " : ");
		Arrays.sort(measurements);
		for (int i = 0; i< NUM_CALIBRATIONS; i++) {
			System.out.print(" " + measurements[i]);
		}
		System.out.println();
		error = getMedian();
		System.out.println(port.getName() + " Error : " + error);
	}
	
	
	public float getDistance() {
		String data = port.read();
		
		if (data.length() > 0) {
			try {
				float measuredDistance = Float.parseFloat(data);
				if (measuredDistance > 0) {
					return measuredDistance;
				}
			} catch(Exception e) {
				
			}
		}
		return -1.0f;
	}
	
	
	public String getName() {
		return port.getName();
	}
	public void startAccumulate() {
		for (int i = 0;i < NUM_CALIBRATIONS; i++) {
			measurements[i] = 0;
		}
	}
	
}
