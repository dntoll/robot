package daniel.robot.glWindow.view;

import java.awt.Dimension;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

public class CalibrationView {

	private ViewCore core;
	
	public CalibrationView(ViewCore core ) {
		
		this.core = core;
	}

	public void doDraw(GL2 gl, GLU glu, Dimension windowSize) {
		// TODO Auto-generated method stub
		gl.glLoadIdentity(); 
		core.drawText(gl, "calibration", 30, 30);
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
