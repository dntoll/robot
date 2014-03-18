package daniel.robot.glWindow.view;

import static java.awt.event.KeyEvent.VK_0;
import static java.awt.event.KeyEvent.VK_1;
import static java.awt.event.KeyEvent.VK_C;
import static java.awt.event.KeyEvent.VK_D;
import static javax.media.opengl.GL.GL_LINES;

import java.awt.Dimension;
import java.util.Collection;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

import daniel.robot.FloatCollection;
import daniel.robot.glWindow.model.CalibrationModel;
import daniel.robot.glWindow.model.DirectionalReading;

public class CalibrationView {

	private ViewCore core;
	private Input input;
	private GLU glu;
	private CalibrationModel calibrationModel;
	
	
	public CalibrationView(CalibrationModel calibrationModel, ViewCore core, Input input ) {
		
		this.core = core;
		this.input = input;
		this.calibrationModel = calibrationModel;
		glu = new GLU();
	}
	
	public boolean userWantsToStopCalibrating() {
		return input.wasClicked(VK_C);
	}
	
	public boolean userWantsToStartCalibrating() {
		return input.wasClicked(VK_C);
	}
	public boolean userEntersCalibrationDistance() {
		return input.wasClicked(VK_D);
	}
	public boolean userSelectSensor0() {
		return input.wasClicked(VK_0);
	}
	public boolean userSelectSensor1() {
		return input.wasClicked(VK_1);
	}

	public void doDraw(GL2 gl, Dimension windowSize, int calibrationDistance, FloatCollection[] calibration, Collection<DirectionalReading> readings, int selectedSensor) {
		// TODO Auto-generated method stub
		gl.glLoadIdentity(); 
		gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		if (calibration == null) {
			core.drawText(gl, "calibration no data " + selectedSensor, 30, 30);
		} else {
			int i = 0;
			gl.glLoadIdentity(); 
			
			float shortRange = calibrationModel.sensorToDistance(calibration[0].getMedian(), 0);
			float longRange = calibrationModel.sensorToDistance(calibration[1].getMedian(), 1);
			core.drawText(gl, "sr: " +calibration[0].getMedian() + " stdev: " + calibration[0].getStdev() + " dist: " + shortRange, 30, 400 + 30 *i);
			
			i--;
			core.drawText(gl, "lr: " +calibration[1].getMedian() + " stdev: " + calibration[1].getStdev() + " dist: " + longRange, 30, 400 + 30 *i);
			core.drawText(gl, "Calibration Distance : " + calibrationDistance + " sensor " + selectedSensor, 30, 400 + 30);
			
			i-=2;
			if (readings != null) {
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
		gl.glColor4f(0.0f, 1.0f, 0.0f, 1.0f);
		gl.glLoadIdentity(); 
		gl.glBegin(GL_LINES);
		for (int sensor = 0; sensor < 1024; sensor+= 10 ) {
			float longRange = calibrationModel.sensorToDistance(sensor, 1);
			gl.glVertex2f(sensor, longRange);
		}
		gl.glEnd();
		
		
		gl.glBegin(GL_LINES);
		gl.glColor4f(1.0f, 0.0f, 0.0f, 1.0f);
		for (int sensor = 0; sensor < 1024; sensor+= 10 ) {
			float shortRange = calibrationModel.sensorToDistance(sensor, 0);
			gl.glVertex2f(sensor, shortRange);
		}
		gl.glEnd();
		
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
