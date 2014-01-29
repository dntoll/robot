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

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.fixedfunc.GLMatrixFunc;
import javax.media.opengl.glu.GLU;

import com.jogamp.opengl.util.gl2.GLUT;



import daniel.robot.Direction;
import daniel.robot.glWindow.model.DirectionalReading;
import daniel.robot.glWindow.model.DistanceSensorReadings;
import daniel.robot.glWindow.model.RobotModel;
import daniel.robot.sensors.Compass;
import daniel.robot.sensors.CompassReading;
import daniel.robot.sensors.GyroAccelerometer;
import daniel.robot.sensors.GyroAccelerometerReading;

public class MetaView extends GLCanvas  
implements GLEventListener, KeyListener {

	private RobotModel model;
	private static final long serialVersionUID = -5995442581511590711L;
	
	private ViewCore core = new ViewCore();
	
	private DistanceMeasurementView distances = new DistanceMeasurementView(core);
	private CompassView compass = new CompassView(core);
	private Dimension windowSize;


	public  MetaView(RobotModel model, Dimension windowSize ) {
		this.model = model;
		this.addGLEventListener(this);
	    // For Handling KeyEvents
	    this.addKeyListener(this);
	    this.setFocusable(true);
	    this.requestFocus();
	    this.windowSize = windowSize;
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
		core.setupOGL(gl);
	}

	
	
	public void batchTextures(BufferedImage left, BufferedImage right) {
		
	}

	

	@Override
	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		
	}
	
	//http://www.java-tips.org/other-api-tips/jogl/outline-fonts-nehe-tutorial-jogl-port.html
	
	
	
	
	
	

	@Override
	public void display(GLAutoDrawable drawable) {
		model.update();
		
		GL2 gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context
	    gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear color and depth buffers
	    
	    GLU glu = new GLU();
		gl.glViewport(0, 0, windowSize.width, windowSize.height);
	    gl.glMatrixMode(GL_PROJECTION);  // choose projection matrix
	    gl.glLoadIdentity();             // reset projection matrix
	    glu.gluOrtho2D(0, windowSize.width, 0, windowSize.height); 
	    gl.glMatrixMode(GL_MODELVIEW);

	    distances.drawTopDown(gl, model.getDistanceSensorReadings());
	    compass.drawCompassArrow(gl, model.getCompass());
	    
	    float temp = model.getTemperature();
	    float direction = model.getCompassDirection().getHeadingDegrees();
	    String out = "Temperature " + temp + " direction " + direction; 
	    core.renderStrokeString(gl, GLUT.STROKE_ROMAN, out); // Print GL Text To The Screen
	    
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




	
	

	

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
	  GL2 gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context
	  
	  if (height == 0) 
    	  height = 1;   // prevent divide by zero
	  
	       
	}

	

}
