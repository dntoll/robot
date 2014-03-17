package daniel.robot.sensors;

import daniel.robot.glWindow.model.CalibrationModel;


public class ShortRangeSharp extends SharpMeasurement {
	private static final int MIN_DISTANCE = 16;
	public static final int RELIABLE_DISTANCE = 150;
	private static final int MAX_DISTANCE = 200;
	
	public ShortRangeSharp(CalibrationModel model) {
		super(model);
	}
	@Override
	protected float getMaxDistance() {
		return MAX_DISTANCE;
	}
	@Override
	public float getReliableDistance() {
		return RELIABLE_DISTANCE;
	}
	@Override
	protected float transformToCM(float sensorValue) {
		return model.sensorToDistance(sensorValue, 0);
	}
	@Override
	public float fromDistance(float distance) {
		return model.distanceToSensor(distance, 0);
	}

}
