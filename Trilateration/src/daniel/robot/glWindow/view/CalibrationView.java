package daniel.robot.glWindow.view;

import java.awt.Dimension;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

import daniel.robot.glWindow.model.DirectionalReading;
import daniel.robot.glWindow.model.DirectionalReadingCollection;

public class CalibrationView {

	private ViewCore core;
	private DirectionalReadingCollection calibration = null;
	
	public CalibrationView(ViewCore core ) {
		
		this.core = core;
	}

	public void doDraw(GL2 gl, GLU glu, Dimension windowSize) {
		// TODO Auto-generated method stub
		gl.glLoadIdentity(); 
		
		if (calibration == null) {
			core.drawText(gl, "calibration no data", 30, 30);
		} else {
			int i = 0;
			for (DirectionalReading value : calibration.getReadings().values() ) {
				if (value.getShortDistance().getValues().size() > 0) { 
					gl.glLoadIdentity(); 
					core.drawText(gl, "" + value.getShortDistance().getMedian() + " " + value.getShortDistance().getStdev(), 30, 400 + 30 *i);
					i++;
				} else if (value.getLongDistance().getValues().size() > 0) { 
					gl.glLoadIdentity(); 
					core.drawText(gl, "" + value.getLongDistance().getMedian() + " " + value.getLongDistance().getStdev(), 500, 400 + 30 *i);
					
				}
				
			}
		}
		
		
	}

	public void setCalibrationData(DirectionalReadingCollection calibration2) {
		calibration = calibration2;
	}

	
	/*private void drawGyro(GL2 gl) {
	float cx = 75;
	float cy = 75;
	GyroAccelerometer compass = model.getGyro();
	
	List<GyroAccelerometerReading> allReadings = compass.getAllReadings();
	gl.glColor3f(1, 1, 1);
	for (CompassReading reading : allReadings) {
		Direction dir = reading.getHeading();
		float len = reading.getLength() * 0.1f;
		drawLine(gl, cx, cy, len, dir);
	}
	
	gl.glColor3f(1, 0, 0);
	drawLine(gl, cx, cy, 50.0f, compassDirection);
	}*/
}
