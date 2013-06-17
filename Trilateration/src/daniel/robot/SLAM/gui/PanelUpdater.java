package daniel.robot.SLAM.gui;

class PanelUpdater implements Runnable  {
	RobotPanel m_roboPanel;
	public PanelUpdater(RobotPanel a_roboPanel) {
		m_roboPanel = a_roboPanel;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		System.out.println("Panel Updater done waiting ");
		
		
		try {
			while(true) {
				m_roboPanel.updateRobot();
				
				
			}
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
