package daniel.robot.sensors;



public class LongRangeSharp extends SharpMeasurement {
	private static final float voltages[]= {1.15f, 1.22f, 1.42f, 1.54f, 1.68f, 1.78f, 1.83f, 2.0f, 2.06f, 2.22f, 2.48f, 3.07f};
	private static final float distance[]= {  500,   400,   300,  260,    200,   180,   160,  150,   140,   120,  100, 70};
	@Override
	protected float getMaxDistance() {
		return 500;
	}
	@Override
	public float getReliableDistance() {
		return 450;
	}
	@Override
	protected float transformToCM(float a_voltage) {
		return super.transformToCM(a_voltage, voltages, distance);
	}
	
}
