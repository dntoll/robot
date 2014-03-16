package daniel.robot.glWindow.view;

import java.awt.Dimension;
import java.util.Collection;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

import daniel.robot.FloatCollection;
import daniel.robot.glWindow.model.DirectionalReading;
import daniel.robot.glWindow.model.DirectionalReadingCollection;

public class CalibrationView {

	private ViewCore core;
	
	
	public CalibrationView(ViewCore core ) {
		
		this.core = core;
	}

	public void doDraw(GL2 gl, GLU glu, Dimension windowSize, int calibrationDistance, FloatCollection[] calibration, Collection<DirectionalReading> readings) {
		// TODO Auto-generated method stub
		gl.glLoadIdentity(); 
		
		if (calibration == null) {
			core.drawText(gl, "calibration no data", 30, 30);
		} else {
			int i = 0;
			gl.glLoadIdentity(); 
			core.drawText(gl, "" +calibration[0].getMedian() + " " + calibration[0].getStdev(), 30, 400 + 30 *i);
			core.drawText(gl, "" +calibration[1].getMedian() + " " + calibration[1].getStdev(), 500, 400 + 30 *i);
			core.drawText(gl, "Calibration Distance : " +calibrationDistance, 30, 400 + 30);
			
			i-=2;
			for (DirectionalReading r : readings) {
				if (r.getShortDistance().getValues().size() > 0) {
					core.drawText(gl, "" + r.getServoDirection() + " sr: " + r.getShortDistance().getMedian() + " " 
										 + r.getShortDistance().getStdev(), 30, 400 + 30 *i);
					i--;
				}
				if (r.getLongDistance().getValues().size() > 0) {
					core.drawText(gl, "" + r.getServoDirection() + " lr: " + r.getLongDistance().getMedian() + " " 
										 + r.getLongDistance().getStdev(), 30, 400 + 30 *i);
					i--;
				}
					
			}
		}
		
		
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
