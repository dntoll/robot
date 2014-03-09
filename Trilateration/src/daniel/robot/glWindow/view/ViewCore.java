package daniel.robot.glWindow.view;

import static javax.media.opengl.GL.GL_BLEND;
import static javax.media.opengl.GL.GL_DEPTH_TEST;
import static javax.media.opengl.GL.GL_LEQUAL;
import static javax.media.opengl.GL.GL_NICEST;
import static javax.media.opengl.GL.GL_ONE;
import static javax.media.opengl.GL.GL_SRC_ALPHA;
import static javax.media.opengl.GL.GL_TRIANGLE_STRIP;
import static javax.media.opengl.GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_LIGHTING;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_SMOOTH;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.fixedfunc.GLMatrixFunc;

import com.jogamp.opengl.util.gl2.GLUT;

import daniel.robot.Direction;
import daniel.robot.glWindow.model.DistanceMeasurementCollection;

public class ViewCore {
	
	private GLUT glut = new GLUT();
	
	public void setupOGL(GL2 gl) {
		  gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // set background (clear) color
		  gl.glClearDepth(1.0f);      // set clear depth value to farthest
		  gl.glEnable(GL_DEPTH_TEST); // enables depth testing
		  gl.glDepthFunc(GL_LEQUAL);  // the type of depth test to do
		  gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST); // best perspective correction
		  gl.glShadeModel(GL_SMOOTH); // blends colors nicely, and smoothes out lighting
		  gl.glDisable(GL_LIGHTING); // But disable lighting
		  // Blending control
		  // Full Brightness with specific alpha (1 for opaque, 0 for transparent)
		  gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		  // Used blending function based On source alpha value
		  gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE);
		  
		  gl.glEnable(GL_BLEND);
		  gl.glDisable(GL_DEPTH_TEST);
	}
	
	
		

	public void drawArc(GL2 gl, float cx, float cy, float scale, DistanceMeasurementCollection distance,
			Direction direction, int degrees) {
		gl.glBegin(GL_TRIANGLE_STRIP);
		
		
		
		Direction left = new Direction(direction.getHeadingDegrees()-degrees/2);
		Direction middle = direction;
		Direction right = new Direction(direction.getHeadingDegrees()+degrees/2);
		float q1 = distance.getQ1() * scale;
		float q3 = distance.getQ3() * scale;
		/*if (q1 > 200)
			q1 = 200;
		if (q3 > 200)
			q3 = 200;*/
		
		drawPoint(gl, cx, cy, q1, left);
		
		drawPoint(gl, cx, cy, q3, left);
		drawPoint(gl, cx, cy, q1, middle);
		drawPoint(gl, cx, cy, q3, middle);
		drawPoint(gl, cx, cy, q1, right);
		drawPoint(gl, cx, cy, q3, right);
		gl.glEnd();
	}
	
	public void fillArc(GL2 gl, float cx, float cy, float scale, DistanceMeasurementCollection distance,
			Direction direction, int degrees) {
		gl.glBegin(GL_TRIANGLE_STRIP);
		//gl.glVertex2f(cx, cy);
		
		
		Direction left = new Direction(direction.getHeadingDegrees()-degrees/2);
		Direction middle = direction;
		Direction right = new Direction(direction.getHeadingDegrees()+degrees/2);
		drawPoint(gl, cx, cy, 0, left);
		float min = distance.getMin() * scale;
		
		/*if (min > 200)
			min = 200;*/
		drawPoint(gl, cx, cy, min, left);
		drawPoint(gl, cx, cy, 0, middle);
		drawPoint(gl, cx, cy, min, middle);
		drawPoint(gl, cx, cy, 0, right);
		drawPoint(gl, cx, cy, min, right);
		gl.glEnd();
		
	}



	private void drawPoint(GL2 gl, float centerx, float centery, float radius, Direction direction) {
		float dx = direction.getX();
		float dy = direction.getY();
		float x = centerx + dx * radius;
		float y = centery + dy * radius;
		
		gl.glVertex2f(x, y);
	}
	
	private void drawPoint(GL2 gl, float centerx, float centery, float radius, int directionDegrees) {
		float dy = (float) Math.sin((float)directionDegrees * 2.0 * Math.PI / 360.0f);
		float dx = (float) Math.cos((float)directionDegrees * 2.0 * Math.PI / 360.0f);
		
		float px = centerx + dx * radius;
		float py = centery + dy * radius;
		
		gl.glVertex2f(px, py);
	}
	
	void renderStrokeString(GL gl, String string) {
		((GLMatrixFunc) gl).glLoadIdentity();
        // Center Our Text On The Screen
        //float width = glut.glutStrokeLength(font, string);
        ((GLMatrixFunc) gl).glTranslatef(25.75f, 540, 0);
        ((GLMatrixFunc) gl).glScalef(0.08f, 0.08f, 1.0f);
        // Render The Text
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            glut.glutStrokeCharacter(GLUT.STROKE_ROMAN, c);
        }
    }



	public void drawCircle(GL2 gl, float centerx, float centery, float radius) {
		
		for (int i = 0; i < 360; i+=3) {
			drawPoint(gl, centerx, centery, radius, i);
			drawPoint(gl, centerx, centery, radius, i+1);
			//gl.glVertex2f(x, y);
		}
	}




	public void drawText(GL gl, String string, int x, int y) {
		//System.out.println(string);
		
		((GLMatrixFunc) gl).glLoadIdentity();
		// Center Our Text On The Screen
        //float width = glut.glutStrokeLength(font, string);
        ((GLMatrixFunc) gl).glTranslatef(x, y, 0.5f);
        ((GLMatrixFunc) gl).glScalef(0.2f, 0.2f, 1.0f);
        // Render The Text
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            glut.glutStrokeCharacter(GLUT.STROKE_ROMAN, c);
        }
	}




	public void drawQuad(GL2 gl, float vx, float vy, float cellSize,
			float cellSize2) {
		gl.glVertex2f(vx, vy);
		gl.glVertex2f(vx, vy+cellSize);
		gl.glVertex2f(vx+cellSize, vy+cellSize);
		
		gl.glVertex2f(vx+cellSize, vy+cellSize);
		gl.glVertex2f(vx+cellSize, vy);
		gl.glVertex2f(vx, vy);
	}




	



	



	

}
