package daniel.robot.sensors;

import daniel.robot.glWindow.model.DistanceMeasurementCollection;

public abstract class SharpMeasurement extends DistanceMeasurementCollection {
	public static final float BEAM_WIDTH = 1.0f;
	
	
	

	public void add(float value) {
		float v0 = getVoltage(value);
		float distance  = transformToCM(v0);
		super.addDistance(distance);
	}
	
	
	public void addCM(float distance) {
		super.addDistance(distance);
	}

	private float getVoltage(float sensorValue) {
		float voltage = 4.85f;
		
		return sensorValue * (voltage / 1023.0f);
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
