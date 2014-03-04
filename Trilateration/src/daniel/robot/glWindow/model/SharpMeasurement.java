package daniel.robot.glWindow.model;

public class SharpMeasurement extends Measurement {
	public static final float BEAM_WIDTH = 1;
	
	private static final int MIN_DISTANCE = 16;
	public static final int RELIABLE_DISTANCE = 150;
	private static final int MAX_DISTANCE = 200;
	private static final float shortRangeVoltages[]= {0.37f, 0.38f, 0.415f, 0.44f, 0.48f,  0.5f,  0.55f,  0.605f, 0.66f, 0.7f,  0.76f, 0.86f, 0.92f, 1.08f, 1.26f, 1.48f, 1.84f, 2.3f, 2.65f, 2.68f};
	private static final float shortRangeDistances[]= {  212,   190,    180,   170,   160,   150,    140,     130,   120,  110,    100,    90,    80,    70,    60,    50,    40,   30,    20,   16};

	private static final float longRangeVoltages[]= {0.37f, 1.42f, 1.52f, 1.75f, 2.48f};
	private static final float longRangeDistances[]= {  500,   400,   300,   200,   100};

	public void add(float value, boolean isLongRange) {
		float v0 = getVoltage(value);
		float distance  = transformToCM(v0, isLongRange);
		super.add(distance);
	}
	
	
	public void addCM(float distance) {
		super.add(distance);
	}

	private float getVoltage(float sensorValue) {
		float voltage = 4.85f;
		
		return sensorValue * (voltage / 1023.0f);
	}

	  
	private float transformToCM(float a_voltage, boolean isLongRange) {
		float dist = 0.0f;
	 
		float voltages[] = isLongRange ? longRangeVoltages : shortRangeVoltages;
		float distance[] = isLongRange ? longRangeDistances : shortRangeDistances;
		
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
		if (getMedian() > MAX_DISTANCE)
			return false;
		if (getStdev() > 10.0f)
			return false;
		
		return true;
	}
	
	
}
