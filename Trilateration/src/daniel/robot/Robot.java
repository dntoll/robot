package daniel.robot;

import daniel.SerialPort;
import daniel.robot.SLAM.Movement;
import daniel.robot.sensors.IRandSonarReading;
import daniel.robot.sensors.GyroAccelerometerReading;
import daniel.robot.sensors.IRReading;
import daniel.robot.sensors.SensorReading;
import daniel.robot.sensors.SonarReading;

public class Robot {
	
	
	private Protocoll m_protocol = new Protocoll();
	private SerialPort m_port;

	public void Wait() {
		m_protocol.waitForDone(m_port);
	}
	
	public void CalibrateCompass() {	
		m_protocol.calibrate(m_port);
		System.out.println("Started Calibration");
	}
	
	public void StopCalibrateCompass() {	
		m_protocol.stopCalibrate(m_port);
		System.out.println("Ended Calibration");
	}

	public SensorReading SenseAll() throws Exception {
		
		System.out.println("Sending sensor request to Robot");
		m_port.write("cgqit\n");
		
		Direction compassDirection = m_protocol.readCompass(m_port.readSyncronosly());
		GyroAccelerometerReading gyroAccelerator = m_protocol.readGyroAndAccelerometer(m_port.readSyncronosly());
		SonarReading[] sonar = m_protocol.readSonar(m_port.readSyncronosly());
		IRReading[] ir = m_protocol.readIR(m_port.readSyncronosly());
		float temperature = m_protocol.readTemperature(m_port.readSyncronosly());
		
		SensorReading reading = new SensorReading(compassDirection,
												  gyroAccelerator,
												  sonar,
												  ir,
												  temperature);
		System.out.println("Done reading sensor input");
		return reading;
	}
	
	public SensorReading SenseSome() throws Exception  {
		
		m_port.write("cgt\n");
		
		Direction compassDirection = m_protocol.readCompass(m_port.readSyncronosly());
		GyroAccelerometerReading gyroAccelerator = m_protocol.readGyroAndAccelerometer(m_port.readSyncronosly());
		
		
		SonarReading[] sonar = new SonarReading[0];//m_protocol.readSonar(m_port.readSyncronosly());
		IRReading[] ir = new IRReading[0];
		/*
		IRandSonarReading d = m_protocol.readDistanceMeasurement(m_port.readSyncronosly());
		sonar[0] = d.m_sonar;
		ir[0] = d.m_ir;*/
		float temperature = m_protocol.readTemperature(m_port.readSyncronosly());
		
		SensorReading reading = new SensorReading(compassDirection,
												  gyroAccelerator,
												  sonar,
												  ir,
												  temperature);
		return reading;
	}
	
	public void move(Movement a_move) throws Exception {
		//TURN
		if (a_move.m_turnRight > 0.0f) {
			m_port.write("d\n");
			m_protocol.readDoneMoving(m_port.readSyncronosly());
		} else if (a_move.m_turnRight < 0.0f) {
			m_port.write("a\n");
			m_protocol.readDoneMoving(m_port.readSyncronosly());
		}
		
		//MOVE
		if (a_move.m_distance > 0.0f) {
			m_port.write("ww\n");
			m_protocol.readDoneMoving(m_port.readSyncronosly());
			m_protocol.readDoneMoving(m_port.readSyncronosly());
		} else if (a_move.m_distance < 0.0f) {
			m_port.write("ss\n");
			m_protocol.readDoneMoving(m_port.readSyncronosly());
			m_protocol.readDoneMoving(m_port.readSyncronosly());
		}
		
		
	}
	
	

	

	
	
	public Robot(SerialPort port) {
		this.m_port = port;
		m_port.write("z\n");
	}
	
	/*private String read() {
		String data = m_port.read();
		return data;
	}*/
	
	
	public static void main(String[] args) {
		
		try
        {
			RobotFinder finder = new RobotFinder();
			Robot robot = finder.findRobots();
			robot.Wait();

			//long lastTime = 0;
			while(true) {
				//String content = robot.read();
				//System.out.print(content);
				
				SensorReading reading = robot.SenseAll();
				System.out.println(reading.toString());
				
				Thread.sleep(10000);
				
			}
        } catch (Exception e) {
        	System.out.print(e);
        	e.printStackTrace();
        	
        }
	}
	
	
}
