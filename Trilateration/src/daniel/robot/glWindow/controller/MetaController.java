package daniel.robot.glWindow.controller;

import daniel.robot.SLAM.SLAM;
import daniel.robot.glWindow.model.DistanceSensorReadings;
import daniel.robot.glWindow.model.IRobotInterface;
import daniel.robot.glWindow.view.MetaView;

public class MetaController {
	
	
	private MetaView view;
	
	boolean isCalibrating = true;

	private IRobotInterface robotInterface;

	private SLAM slam;

	public MetaController(MetaView view, IRobotInterface robotInterface, SLAM slam) {
		this.view = view;
		this.robotInterface = robotInterface;
		this.slam = slam;
	}

	public void update() {
		
		
		if (isCalibrating) {
			if (view.userWantsToStopCalibrating()) {
				slam.start();
				System.out.println("stop calibrating starting slam");
				isCalibrating = false;
			}
			
			DistanceSensorReadings calibration = robotInterface.makeCalibration();
			
			view.setCalibrationData(calibration);
		} else {
			if (view.userWantsToStartCalibrating()) {
				slam.stop();
				System.out.println("stop slam starting calibration");
				isCalibrating = true;
			}
		}
		
		view.setCalibration(isCalibrating);
		//must be last since it resets input
		view.display();
	}
}
