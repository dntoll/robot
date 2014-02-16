package daniel.robot.glWindow.view;

import static java.awt.event.KeyEvent.VK_B;
import static java.awt.event.KeyEvent.VK_L;
import static javax.media.opengl.GL.GL_COLOR_BUFFER_BIT;
import static javax.media.opengl.GL.GL_DEPTH_BUFFER_BIT;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;

import daniel.robot.Direction;
import daniel.robot.SLAM.Map;
import daniel.robot.glWindow.model.IRobotInterface;
import daniel.robot.glWindow.model.PoseCollection;

public class MetaView extends GLCanvas  
implements GLEventListener, KeyListener {

	private static final long serialVersionUID = -5995442581511590711L;
	
	private ViewCore core = new ViewCore();
	
	private DistanceMeasurementView distances = new DistanceMeasurementView(core);
	private CompassView compass = new CompassView(core);
	private Dimension windowSize;
	private CameraView cameraView = new CameraView();
	private IRobotInterface robot;
	PoseCollection world;

	public  MetaView(IRobotInterface model, PoseCollection world, Dimension windowSize ) {
		//
		this.world = world;
		this.robot = model;
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
		robot.update();
		
		GL2 gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context
	    gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear color and depth buffers
	    
	    GLU glu = new GLU();
		gl.glViewport(0, 0, windowSize.width, windowSize.height);
	    gl.glMatrixMode(GL_PROJECTION);  // choose projection matrix
	    gl.glLoadIdentity();             // reset projection matrix
	    glu.gluOrtho2D(0, windowSize.width, 0, windowSize.height); 
	    gl.glMatrixMode(GL_MODELVIEW);
	    gl.glLoadIdentity(); 
	    
	    try {
		   /* for (IPose pose : world.poses) {
			   distances.drawTopDown(gl, pose.getDistanceSensorReadings(), pose.getBestGuessPosition());
		    }*/
		    if (world.getLastPose() != null) {
			    Map lastMap = world.getLastPose().getBestMap();
			    distances.drawMap(gl,lastMap); 
		    }
	    } catch (Exception e) {
	    	System.err.println(e.getMessage());
	    }
	    if (robot.getDistanceSensorReadings() != null)
	    	distances.drawTopDown(gl, robot.getDistanceSensorReadings(), new daniel.robot.glWindow.model.State(new Point2D.Float(450,0), new Direction(0)));
	    
	    compass.drawCompassArrow(gl, robot.getCompass());
	    
	    float temp = robot.getTemperature();
	    if (robot.getCompassDirection() != null) {
		    float direction = robot.getCompassDirection().getHeadingDegrees();
		    String out = "Temperature " + temp + " direction " + direction; 
		    core.renderStrokeString(gl, out); // Print GL Text To The Screen
		   // cameraView.updateCameraTexture(gl, model.getPanoramaImage(), windowSize);
	    }
	    gl.glLoadIdentity(); 
	    
	    
	//    cameraView.drawCamera(gl, glu, windowSize);
	 //   cameraView.drawPerspective(gl, glu, windowSize, model.getDistanceSensorReadings());
	    
	    
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
	  drawable.getGL().getGL2();
	  
	  if (height == 0) 
    	  height = 1;   // prevent divide by zero
	  
	       
	}

	

}
