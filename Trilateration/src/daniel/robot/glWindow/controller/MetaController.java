package daniel.robot.glWindow.controller;

import static java.awt.event.KeyEvent.*;
import static javax.media.opengl.GL.GL_COLOR_BUFFER_BIT;
import static javax.media.opengl.GL.GL_DEPTH_BUFFER_BIT;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;

import java.awt.Dimension;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicReference;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import daniel.robot.FloatCollection;
import daniel.robot.glWindow.model.CalibrationModel;
import daniel.robot.glWindow.model.DirectionalReading;
import daniel.robot.glWindow.model.DirectionalReadingCollection;
import daniel.robot.glWindow.model.IRobotInterface;
import daniel.robot.glWindow.model.PoseCollection;
import daniel.robot.glWindow.view.CalibrationView;
import daniel.robot.glWindow.view.Input;
import daniel.robot.glWindow.view.SLAMView;
import daniel.robot.glWindow.view.ViewCore;
import daniel.robot.slam.SLAM;

public class MetaController {
	private boolean isCalibrating = true;
	private IRobotInterface robotInterface;
	private SLAM slam;
	private SLAMView slamView;
	private CalibrationView calibrationView;
	private Input input;
	private Dimension windowSize;
	private AtomicReference<Integer> calibrationDistance = new AtomicReference<Integer>();
	private AtomicReference<Integer> selectedSensor = new AtomicReference<Integer>();
	FloatCollection[] calibration;
	Collection<DirectionalReading> readings;
	private CalibrationModel calibrationModel = new CalibrationModel();
	

	public MetaController(IRobotInterface robotInterface, SLAM slam, ViewCore core, Dimension windowSize, Input input, PoseCollection world) {
		this.robotInterface = robotInterface;
		this.slam = slam;
		this.windowSize = windowSize;
		this.input = input;
		calibrationDistance.set(0);
		selectedSensor.set(0);
		
		slamView = new SLAMView(robotInterface, world, core);
		calibrationView = new CalibrationView(core);
	}
	
	public boolean userWantsToStopCalibrating() {
		return input.wasClicked(VK_C);
	}
	
	public boolean userWantsToStartCalibrating() {
		return input.wasClicked(VK_C);
	}
	private boolean userEntersCalibrationDistance() {
		return input.wasClicked(VK_D);
	}
	
	private boolean userStartsMeasurement() {
		return input.wasClicked(VK_M);
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
			
			doCalibration(gl, glu);
		} else {
			if (userWantsToStartCalibrating()) {
				slam.stop();
				System.out.println("stop slam starting calibration");
				isCalibrating = true;
				
			}
			slamView.doSlam(gl, glu, windowSize);
		}
		
		
		
	}

	public void doCalibration(GL2 gl, GLU glu) {
		
		if (userEntersCalibrationDistance()) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					String input = JOptionPane.showInputDialog(null, "Enter a distance:");
					
					calibrationDistance.set(Integer.parseInt(input));
				}
			});
		} else if (userEntersCalibrationDistance()) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					String input = JOptionPane.showInputDialog(null, "select sensor : ");
					
					selectedSensor.set(Integer.parseInt(input));
				}
			});
		} else if(userStartsMeasurement()) {
			calibration = robotInterface.makeCalibration();
			readings = robotInterface.getDistanceSensorReadings().getReadings().values();
			calibrationModel.addValues(selectedSensor.get(), calibrationDistance.get(), calibration[selectedSensor.get()]);
		}
		
		calibrationView.doDraw(gl, glu, windowSize, calibrationDistance.get(), calibration, readings);
	}

	

	
}
