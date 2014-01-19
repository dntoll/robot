package daniel.robot.glWindow.view;

import static java.awt.event.KeyEvent.VK_B;
import static java.awt.event.KeyEvent.VK_L;
import static javax.media.opengl.GL.GL_BLEND;
import static javax.media.opengl.GL.GL_COLOR_BUFFER_BIT;
import static javax.media.opengl.GL.GL_DEPTH_BUFFER_BIT;
import static javax.media.opengl.GL.GL_DEPTH_TEST;
import static javax.media.opengl.GL.GL_LEQUAL;
import static javax.media.opengl.GL.GL_NICEST;
import static javax.media.opengl.GL.GL_ONE;
import static javax.media.opengl.GL.*;
import static javax.media.opengl.GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_LIGHTING;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_SMOOTH;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;

import daniel.robot.Direction;
import daniel.robot.glWindow.model.DirectionalReading;
import daniel.robot.glWindow.model.DistanceSensorReadings;
import daniel.robot.glWindow.model.RobotModel;

public class MetaView extends GLCanvas  
implements GLEventListener, KeyListener {

	private RobotModel model;
	private static final long serialVersionUID = -5995442581511590711L;

	public  MetaView(RobotModel model) {
		this.model = model;
		this.addGLEventListener(this);
	    // For Handling KeyEvents
	    this.addKeyListener(this);
	    this.setFocusable(true);
	    this.requestFocus();
	}

	
    
	
	@Override
   public void keyPressed(KeyEvent e) {
      int keyCode = e.getKeyCode();
      switch (keyCode) {
         case VK_B: // toggle blending on/off
//            doTakeImages = true;
            break;
         case VK_L: // toggle light on/off
//        	 doShowMeasurements = !doShowMeasurements;
            break;
      }
      
   }

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();      // get the OpenGL graphics context
		setupOGL(gl);
	}

	private void setupOGL(GL2 gl) {
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
	
	public void batchTextures(BufferedImage left, BufferedImage right) {
		
	}

	

	@Override
	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context
	    gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear color and depth buffers

	    drawTopDown(gl);
	}

	
	void drawTopDown(GL2 gl) {
		GLU glu = new GLU();
		gl.glViewport(0, 0, 640, 640);
	    gl.glMatrixMode(GL_PROJECTION);  // choose projection matrix
	    gl.glLoadIdentity();             // reset projection matrix
	    glu.gluOrtho2D(0, 640, 0, 640); 
	    gl.glMatrixMode(GL_MODELVIEW);
	    gl.glLoadIdentity(); // reset
		    
		    
		gl.glLoadIdentity();  
		//gl.glTranslatef(0.0f, 0.0f, -4);
		
		gl.glBegin(GL_LINES);
		
		gl.glColor3f(0, 1, 0);
		
		float cx = 320;
		float cy = 320;
		DistanceSensorReadings reading = model.getDistanceSensorReadings();
		
		for(DirectionalReading reading1 : reading.getReadings()) {
			
			
			float sharp1 = reading1.getSharp1Distance();
			float sharp2 = reading1.getSharp2Distance();
			float sonar1 = reading1.getSonar1Distance();
			float sonar2 = reading1.getSonar2Distance();
			
			
			
			Direction direction = reading1.getDirection();
			Direction opposite = new Direction(direction.getHeadingDegrees() + 180);
			
			gl.glColor4f(0, 1, 0, 0.5f);
			drawLine(gl, cx, cy, sharp1, direction);
			
			gl.glColor4f(1, 0, 0, 0.5f);
			drawLine(gl, cx, cy, sonar1, direction);
			
			gl.glColor4f(0, 1, 0, 0.5f);
			drawLine(gl, cx, cy, sharp2, opposite);
			
			gl.glColor4f(1, 0, 0, 0.5f);
			drawLine(gl, cx, cy, sonar2, opposite);
			
			
			
		}
		
		
		gl.glEnd();
	}




	private void drawLine(GL2 gl, float cx, float cy, float distance,
			Direction direction) {
		float dx = direction.getX();
		float dy = direction.getY();
		float x = cx + dx * distance;
		float y = cy + dy * distance;
		gl.glVertex2f(cx, cy);
		gl.glVertex2f(x, y);
	}
	

	

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
	  GL2 gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context
	  
	  if (height == 0) 
    	  height = 1;   // prevent divide by zero
	  
	       
	}

	

}
