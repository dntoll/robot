package daniel.robot.glWindow.controller;

import static java.awt.event.KeyEvent.VK_C;
import static javax.media.opengl.GL.GL_COLOR_BUFFER_BIT;
import static javax.media.opengl.GL.GL_DEPTH_BUFFER_BIT;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;

import java.awt.Dimension;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

import daniel.robot.SLAM.SLAM;
import daniel.robot.glWindow.adapter.AdapterCanvas;
import daniel.robot.glWindow.model.DistanceSensorReadings;
import daniel.robot.glWindow.model.IRobotInterface;
import daniel.robot.glWindow.model.PoseCollection;
import daniel.robot.glWindow.view.CalibrationView;
import daniel.robot.glWindow.view.Input;
import daniel.robot.glWindow.view.SLAMView;
import daniel.robot.glWindow.view.ViewCore;

public class MetaController {
	private boolean isCalibrating = true;
	private IRobotInterface robotInterface;
	private SLAM slam;
	private SLAMView slamView;
	private CalibrationView calibrationView;
	private Input input;
	private Dimension windowSize;

	public MetaController(IRobotInterface robotInterface, SLAM slam, ViewCore core, Dimension windowSize, Input input, PoseCollection world) {
		this.robotInterface = robotInterface;
		this.slam = slam;
		this.windowSize = windowSize;
		this.input = input;
		
		
		slamView = new SLAMView(robotInterface, world, core);
		calibrationView = new CalibrationView(core);
	}
	
	public boolean userWantsToStopCalibrating() {
		return input.wasClicked(VK_C);
	}
	
	public boolean userWantsToStartCalibrating() {
		return input.wasClicked(VK_C);
	}

	public void update(GL2 gl) {
		robotInterface.update();
		
		
	    gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear color and depth buffers
	    
	    GLU glu = new GLU();
		gl.glViewport(0, 0, windowSize.width, windowSize.height);
	    gl.glMatrixMode(GL_PROJECTION);  // choose projection matrix
	    gl.glLoadIdentity();             // reset projection matrix
	    glu.gluOrtho2D(0, windowSize.width, 0, windowSize.height); 
	    gl.glMatrixMode(GL_MODELVIEW);
	    gl.glLoadIdentity(); 
	    
	    gl.glLoadIdentity(); 
	    
	 
		
		if (isCalibrating) {
			if (userWantsToStopCalibrating()) {
				slam.start();
				System.out.println("stop calibrating starting slam");
				isCalibrating = false;
			}
			
			DistanceSensorReadings calibration = robotInterface.makeCalibration();
			
			calibrationView.setCalibrationData(calibration);
			calibrationView.doDraw(gl, glu, windowSize);
		} else {
			if (userWantsToStartCalibrating()) {
				slam.stop();
				System.out.println("stop slam starting calibration");
				isCalibrating = true;
				
			}
			slamView.doSlam(gl, glu, windowSize);
		}
		
		
		
	}
}
