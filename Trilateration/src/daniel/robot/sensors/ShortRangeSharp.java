package daniel.robot.sensors;


public class ShortRangeSharp extends SharpMeasurement {
	private static final int MIN_DISTANCE = 16;
	public static final int RELIABLE_DISTANCE = 150;
	private static final int MAX_DISTANCE = 200;
	private static final float voltages[]= {0.37f, 0.38f, 0.415f, 0.44f, 0.48f,  0.5f,  0.55f,  0.605f, 0.66f, 0.7f,  0.76f, 0.86f, 0.92f, 1.08f, 1.26f, 1.48f, 1.84f, 2.3f, 2.65f, 2.68f};
	private static final float distance[]= {  212,   190,    180,   170,   160,   150,    140,     130,   120,  110,    100,    90,    80,    70,    60,    50,    40,   30,    20,   16};

	@Override
	protected float getMaxDistance() {
		return MAX_DISTANCE;
	}
	@Override
	public float getReliableDistance() {
		return RELIABLE_DISTANCE;
	}
	@Override
	protected float transformToCM(float a_voltage) {
		return super.transformToCM(a_voltage, voltages, distance);
	}
	public float fromDistance(float a_voltage) {
		return super.fromDistance(a_voltage, voltages, distance);
	}

}
