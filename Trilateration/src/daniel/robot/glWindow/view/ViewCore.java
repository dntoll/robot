package daniel.robot.glWindow.view;

import static javax.media.opengl.GL.GL_BLEND;
import static javax.media.opengl.GL.GL_DEPTH_TEST;
import static javax.media.opengl.GL.GL_LEQUAL;
import static javax.media.opengl.GL.GL_NICEST;
import static javax.media.opengl.GL.GL_ONE;
import static javax.media.opengl.GL.GL_SRC_ALPHA;
import static javax.media.opengl.GL.GL_TRIANGLE_FAN;
import static javax.media.opengl.GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_LIGHTING;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_SMOOTH;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.fixedfunc.GLMatrixFunc;

import com.jogamp.opengl.util.gl2.GLUT;

import daniel.robot.Direction;

public class ViewCore {
	
	private GLUT glut = new GLUT();
	
	void setupOGL(GL2 gl) {
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
	
	

	void drawArc(GL2 gl, float cx, float cy, float distance, Direction direction, float degrees) {
		gl.glBegin(GL_TRIANGLE_FAN);
		gl.glVertex2f(cx, cy);
		
		Direction left = new Direction(direction.getHeadingDegrees()-degrees/2);
		Direction middle = direction;
		Direction right = new Direction(direction.getHeadingDegrees()+degrees/2);
		
		drawPoint(gl, cx, cy, distance, left);
		drawPoint(gl, cx, cy, distance, middle);
		drawPoint(gl, cx, cy, distance, right);
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
	
	void renderStrokeString(GL gl, int font, String string) {
        // Center Our Text On The Screen
        //float width = glut.glutStrokeLength(font, string);
        ((GLMatrixFunc) gl).glTranslatef(0.5f, 0, 0);
        ((GLMatrixFunc) gl).glScalef(0.1f, 0.1f, 1.0f);
        // Render The Text
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            glut.glutStrokeCharacter(font, c);
        }
    }



	public void drawCircle(GL2 gl, float centerx, float centery, float radius) {
		
		for (int i = 0; i < 360; i+=3) {
			drawPoint(gl, centerx, centery, radius, i);
			drawPoint(gl, centerx, centery, radius, i+1);
			//gl.glVertex2f(x, y);
		}
	}



	

}
