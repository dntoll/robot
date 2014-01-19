package daniel.robot.glWindow.controller;

import daniel.robot.glWindow.model.RobotModel;
import daniel.robot.glWindow.view.MetaView;

public class MetaController {
	private MetaView view;
	private RobotModel robot;
	
	public MetaController(MetaView view, RobotModel robot) {
		this.view = view;
		this.robot = robot;
	}

	public void update() {
		
		/*if (view.isShowingMeasurement())
			rawInputController.update();
		else
			showModelController.update();*/
		
	}
}
