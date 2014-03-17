package daniel.robot.sensors;

import daniel.robot.FloatCollection;
import daniel.robot.glWindow.model.CalibrationModel;

public abstract class SharpMeasurement extends FloatCollection {
	public static final float BEAM_WIDTH = 1.0f;
	protected  CalibrationModel model;
	
	public SharpMeasurement(CalibrationModel model) {
		this.model = model;
	}

	protected float convert(float sensorValue) {
		float distance  = transformToCM(sensorValue);
		return distance;
	}
	

	protected abstract float getMaxDistance();
	public abstract float getReliableDistance();

	protected abstract float transformToCM(float sensorValue);
	protected abstract float fromDistance(float distance);
	

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
