package daniel.robot.glWindow.adapter;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLCanvas;
import daniel.robot.glWindow.controller.MetaController;
import daniel.robot.glWindow.view.Input;
import daniel.robot.glWindow.view.ViewCore;



/**
 * Adapts media.opengl so that we can have a simpler MVC
 * controlled from a single update
 */
public class AdapterCanvas extends GLCanvas  
implements GLEventListener, KeyListener, GLAutoDrawable{

	private static final long serialVersionUID = -5995442581511590711L;
	private ViewCore core = new ViewCore();
	private Input input;
	private MetaController controller;

	public  AdapterCanvas(MetaController controller, Input input, ViewCore core) {
		this.controller = controller;
		this.input = input;
		this.core = core;
		
		
		this.addGLEventListener(this);
	    // For Handling KeyEvents
	    this.addKeyListener(this);
	    this.setFocusable(true);
	    this.requestFocus();
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
		GL2 gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context
		controller.update(gl);
		
		
	}

	


	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
	  drawable.getGL().getGL2();
	  
	  if (height == 0) 
    	  height = 1;   // prevent divide by zero
	  
	       
	}

}
