package daniel.robot.sensors;

import daniel.robot.glWindow.model.CalibrationModel;



public class LongRangeSharp extends SharpMeasurement {
	public LongRangeSharp(CalibrationModel model) {
		super(model);
	}
	
	@Override
	protected float getMaxDistance() {
		return 500;
	}
	@Override
	public float getReliableDistance() {
		return 450;
	}
	@Override
	protected float transformToCM(float sensorValue) {
		return model.sensorToDistance(sensorValue, 1);
	}
	@Override
	public float fromDistance(float distance) {
		return model.distanceToSensor(distance, 1);
	}
	
}
