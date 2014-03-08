package daniel.robot.glWindow.view;

import static java.awt.event.KeyEvent.VK_C;
import static javax.media.opengl.GL.GL_COLOR_BUFFER_BIT;
import static javax.media.opengl.GL.GL_DEPTH_BUFFER_BIT;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;

import daniel.robot.glWindow.model.IRobotInterface;
import daniel.robot.glWindow.model.PoseCollection;

public class MetaView extends GLCanvas  
implements GLEventListener, KeyListener {

	private static final long serialVersionUID = -5995442581511590711L;
	
	private ViewCore core = new ViewCore();
	private Dimension windowSize;
	
	
	SLAMView slam;
	CalibrationView calibration;
	
	
	private IRobotInterface robot;
	PoseCollection world;
	
	private Input input = new Input();

	public  MetaView(IRobotInterface model, PoseCollection world, Dimension windowSize ) {
		//
		this.world = world;
		this.robot = model;
		slam = new SLAMView(robot, world, core);
		calibration = new CalibrationView(core);
		
		this.addGLEventListener(this);
	    // For Handling KeyEvents
	    this.addKeyListener(this);
	    this.setFocusable(true);
	    this.requestFocus();
	    this.windowSize = windowSize;
	}

	
    
	
	@Override
   public void keyPressed(KeyEvent e) {
      input.keyPressed(e);
   }
	
	@Override
	public void keyReleased(KeyEvent arg0) {
		input.keyReleased(arg0);
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

	@Override
	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		
	}
	
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
	    
	    gl.glLoadIdentity(); 
	    
	    if (isCalibrating() == false) {
	    	slam.doSlam(gl, glu, windowSize);
	    } else {
	    	calibration.doDraw(gl, glu, windowSize);
	    }
	}

	boolean isDoingCalibration = true;
	private boolean isCalibrating() {
		return isDoingCalibration;
	}
	
	public void setCalibration(boolean calibrate) {
		isDoingCalibration = calibrate;
	}

	public boolean userWantsToStopCalibrating() {
		return input.wasClicked(VK_C);
	}
	
	public boolean userWantsToStartCalibrating() {
		return input.wasClicked(VK_C);
	}


	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
	  drawable.getGL().getGL2();
	  
	  if (height == 0) 
    	  height = 1;   // prevent divide by zero
	  
	       
	}

}
