package daniel.robot;

import java.util.List;

import daniel.SerialPort;

public class RobotFinder {
	public Robot findRobots() throws Exception {
		List<SerialPort> ports = SerialPort.connectAndInitialize();
		
		
		for(SerialPort port : ports) {
			
			System.out.println("Robot::main port found port " + port.getName());
			if (port.getName().equals("COM14") || 
				port.getName().equals("/dev/ttyUSB0") ||
				port.getName().equals("/dev/ttyUSB1")) { //TODO better to supply this as an argument...
				
				Robot robot = new Robot(port); 
				
				return robot;
				
				
			} else {
				System.out.println("Robot::main port " + port.getName() + " is unknown, attach to Robot?");
			}
			
			
		}
		throw new Exception("No robot found");
	}
}
