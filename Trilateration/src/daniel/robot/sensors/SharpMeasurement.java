package daniel.robot.sensors;

import daniel.robot.FloatCollection;

public abstract class SharpMeasurement extends FloatCollection {
	public static final float BEAM_WIDTH = 1.0f;
	float voltage = 4.85f;
	
	

	/*public void add(float reading) {
		float v0 = getVoltage(reading);
		float distance  = transformToCM(v0);
		super.addValue(distance);
	}*/
	
	protected float convert(float reading) {
		float v0 = getVoltage(reading);
		float distance  = transformToCM(v0);
		return distance;
	}
	
	
	/*public void addCM(float distance) {
		super.addValue(distance);
	}*/

	
	private float getVoltage(float sensorValue) {
		return sensorValue * (voltage / 1023.0f);
	}
	
	private float getSensorValue(float mv) {
		return mv / (voltage / 1023.0f);
	}

	protected abstract float getMaxDistance();
	public abstract float getReliableDistance();
	public String toString() {
		return super.toString();
	}
	protected abstract float transformToCM(float a_voltage);
	  
	protected float transformToCM(float a_voltage, float voltages[], float distance[]) {
		float dist = 0.0f;
	 
		for (int i = 0; i < voltages.length - 1; i++) {
			if (a_voltage < voltages[i+1]) {
			     return floatMap(a_voltage, voltages[i], voltages[i+1], distance[i], distance[i+1]);
			}
		}
  
		return dist;
	}
	
	/**
	 * Creates reading value from distance
	 */
	protected float fromDistance(float distanceCM, float voltages[], float distance[]) {
		float reading = 0.0f;
		 
		for (int i = 0; i < distance.length - 1; i++) {
			if (distanceCM > distance[i+1]) {
			     float voltage= floatMap(distanceCM, distance[i], distance[i+1], voltages[i], voltages[i+1]);
			     return getSensorValue(voltage);
			}
		}
  
		return reading;
	}
	
	private 
	float floatMap(float sourceValue, float sourceMin, float sourceMax, float destMin, float destMax) {
		float sourceRange = sourceMax - sourceMin;
		float percent = ((sourceValue - sourceMin ) / sourceRange);
		float destRange = destMax - destMin;
		return destMin + percent * destRange;
    }

	public float getBeamWidth() {
		return BEAM_WIDTH;
	}

	public boolean okDistance() {
		if (values.size() < 2)
			return false;
		if (getMedian() > getMaxDistance())
			return false;
		if (getStdev() > 10.0f)
			return false;
		
		return true;
	}


	


	
	
	
}
