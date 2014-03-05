package daniel.robot.sensors;



public class LongRangeSharp extends SharpMeasurement {
	private static final float voltages[]= {0.37f, 1.42f, 1.52f, 1.75f, 2.48f};
	private static final float distance[]= {  500,   400,   300,   200,   100};
	@Override
	protected float getMaxDistance() {
		return 500;
	}
	@Override
	public float getReliableDistance() {
		return 450;
	}
	@Override
	protected float transformToCM(float a_voltage, boolean isLongRange) {
		return super.transformToCM(a_voltage, isLongRange, voltages, distance);
	}
	
}
