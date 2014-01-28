package daniel.robot.glWindow.view;

import static javax.media.opengl.GL.GL_LINES;

import java.util.List;

import javax.media.opengl.GL2;

import daniel.robot.Direction;
import daniel.robot.sensors.Compass;
import daniel.robot.sensors.CompassReading;

public class CompassView {
	
	private ViewCore core;

	CompassView(ViewCore core) {
		this.core = core;
	}
	
	public void drawCompassArrow(GL2 gl, Compass compass) {
		float centerx= 75;
		float centery = 75;
		float scale = 0.1f;
		
		gl.glLoadIdentity(); // reset
		gl.glBegin(GL_LINES);
		drawAllMeasurements(gl, compass, centerx, centery, scale);
		
		
		Direction compassDirection = compass.getHeading();
		
		
		gl.glColor3f(1, 0, 0);
		core.drawArc(gl, centerx, centery, 50.0f, compassDirection, 0.0f);
		
		gl.glEnd();
	}

	private void drawAllMeasurements(GL2 gl, Compass compass, float centerx,
			float centery, float scale) {
		List<CompassReading> allReadings = compass.getAllReadings();
		gl.glColor3f(1, 1, 1);
		for (CompassReading reading : allReadings) {
			Direction dir = reading.getHeading();
			float len = reading.getFlatLength() * scale;
			core.drawArc(gl, centerx , centery, len, dir, 0);
		}
	}
}
