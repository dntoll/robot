package daniel.robot.sensors;



public class LongRangeSharp extends SharpMeasurement {
	private static final float voltages[]= {1.15f, 1.22f, 1.42f, 1.54f, 1.68f, 1.78f, 1.83f, 2.0f, 2.06f, 2.22f, 2.48f, 3.07f};
	private static final float distance[]= {  500,   400,   300,  260,    200,   180,   160,  150,   140,   120,  100, 70};
	
	/*
	private static final float voltages[]= {  	1.09f, 1.1f, 
		1.11f, 1.12f, 
		1.13f, 1.17f, 
		1.19f, 1.21f,
		1.23f,
		1.27f,
		1.36f,
		1.38f,
		1.43f,
		1.48f,
		1.55f,
		1.62f,
		1.65f,
		1.71f,
		1.74f,
		1.81f,
		1.87f,
		1.95f,
		2.01f,
		2.09f,
		2.2f,
		2.3f,
		2.42f,
		2.58f,
		2.74f,
		2.97f,
		};
private static final float distance[]= {  600,    
	  520,   500, 480, 460, 440, 420, 400, 380, 350, 325,  300, 280, 260, 230, 210, 200, 190, 180, 170, 160, 150, 140, 130, 120, 110, 100, 90, 80, 70};
*/
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
