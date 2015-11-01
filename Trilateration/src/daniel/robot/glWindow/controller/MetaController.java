package daniel.robot.glWindow.controller;

import java.awt.Dimension;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicReference;

import javax.media.opengl.GL2;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import daniel.robot.FloatCollection;
import daniel.robot.glWindow.model.CalibrationModel;
import daniel.robot.glWindow.model.DirectionalReading;
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
	private Dimension windowSize;
	private AtomicReference<Integer> calibrationDistance = new AtomicReference<Integer>();
	private int selectedSensor = 0;
	FloatCollection[] calibration;
	Collection<DirectionalReading> readings;
	private CalibrationModel calibrationModel;
	private ViewCore core;
	

	public MetaController(IRobotInterface robotInterface, SLAM slam, ViewCore core, Dimension windowSize, Input input, PoseCollection world, CalibrationModel calibrationModel) {
		this.calibrationModel = calibrationModel;
		this.robotInterface = robotInterface;
		this.slam = slam;
		this.windowSize = windowSize;
		this.core = core;
		calibrationDistance.set(0);
		slamView = new SLAMView(robotInterface, world, core);
		
		calibrationView = new CalibrationView(calibrationModel, core, input);
	}
	
	
	
	public void update(GL2 gl) {
		robotInterface.update();
		
		core.startDraw(gl, windowSize);
		
		if (isCalibrating) {
			if (calibrationView.userWantsToStopCalibrating()) {
				slam.start();
				System.out.println("stop calibrating starting slam");
				isCalibrating = false;
			}
			
			doCalibration(gl);
		} else {
			if (calibrationView.userWantsToStartCalibrating()) {
				slam.stop();
				System.out.println("stop slam starting calibration");
				isCalibrating = true;
				
			}
			slamView.doSlam(gl, windowSize);
		}
		
		
		
	}

	long lastReadTime = 0;
	public void doCalibration(GL2 gl) {
		
		if (calibrationView.userEntersCalibrationDistance()) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					String input = JOptionPane.showInputDialog(null, "Enter a distance:");
					
					calibrationDistance.set(Integer.parseInt(input));
					calibration = robotInterface.makeCalibration();
					readings = robotInterface.getDistanceSensorReadings().getReadings().values();
					calibrationModel.addValues(selectedSensor, calibrationDistance.get(), calibration[selectedSensor]);
				}
			});
		} else if (calibrationView.userSelectSensor0()) {
			selectedSensor = 0;
		} else if (calibrationView.userSelectSensor1()) {
			selectedSensor = 1;
		} else {
			if (System.currentTimeMillis() - lastReadTime > 3000) {
				calibration = robotInterface.makeSingleDistanceRead();
				lastReadTime = System.currentTimeMillis();
			}
		}
		
		calibrationView.doDraw(gl, windowSize, calibrationDistance.get(), calibration, readings, selectedSensor);
	}

	

	
}
