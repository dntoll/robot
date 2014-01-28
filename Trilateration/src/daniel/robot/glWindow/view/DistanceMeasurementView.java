package daniel.robot.glWindow.view;

import static javax.media.opengl.GL.GL_LINES;
import static javax.media.opengl.GL.GL_TRIANGLE_FAN;
import javax.media.opengl.GL2;
import daniel.robot.Direction;
import daniel.robot.glWindow.model.DirectionalReading;
import daniel.robot.glWindow.model.DistanceSensorReadings;

public class DistanceMeasurementView {
	
	private ViewCore core;

	DistanceMeasurementView(ViewCore core) {
		this.core = core;
	}
	
	void drawTopDown(GL2 gl, DistanceSensorReadings reading) {
		
	    
		    
		    
		gl.glLoadIdentity();  
		
		gl.glColor3f(0, 1, 0);
		
		float cx = 320;
		float cy = 320;
		
		
		for(DirectionalReading reading1 : reading.getReadings()) {
			float sonar1 = reading1.getSonar1Distance();
			float sonar2 = reading1.getSonar2Distance();
			
			Direction direction = reading1.getDirection();
			Direction opposite = new Direction(direction.getHeadingDegrees() + 180);
			
			
			gl.glColor4f(0, 1, 0, 0.5f);
			core.drawArc(gl, cx, cy, sonar1, direction, 15);
			
			gl.glColor4f(0, 1, 0, 0.5f);
			core.drawArc(gl, cx, cy, sonar2, opposite, 15);
		}
		
		for(DirectionalReading reading1 : reading.getReadings()) {
			
			
			float sharp1 = reading1.getSharp1Distance();
			float sharp2 = reading1.getSharp2Distance();
		
			Direction direction = reading1.getDirection();
			Direction opposite = new Direction(direction.getHeadingDegrees() + 180);
			
			gl.glColor4f(1, 0, 0, 1.0f);
			core.drawArc(gl, cx, cy, sharp1, direction, 1);
			
			gl.glColor4f(1, 0, 0, 1.0f);
			core.drawArc(gl, cx, cy, sharp2, opposite, 1);
			
		}
		
		gl.glBegin(GL_LINES);
		gl.glColor4f(1, 1, 1, 0.5f);
		core.drawCircle(gl, cx, cy, 17.0f);
		core.drawCircle(gl, cx, cy, 50.0f);
		core.drawCircle(gl, cx, cy, 100.0f);
		core.drawCircle(gl, cx, cy, 140.0f);
		core.drawCircle(gl, cx, cy, 200.0f);
		
		
		
		gl.glEnd();
	}
}
