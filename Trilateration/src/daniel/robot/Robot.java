package daniel.robot;

import daniel.SerialPort;

public class Robot {
	
	
	Protocoll m_protocol = new Protocoll();

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
		
		float compassDirection = m_protocol.readCompass(m_port.readSyncronosly());
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
		
		m_port.write("cgmt\n");
		
		float compassDirection = m_protocol.readCompass(m_port.readSyncronosly());
		GyroAccelerometerReading gyroAccelerator = m_protocol.readGyroAndAccelerometer(m_port.readSyncronosly());
		
		
		SonarReading[] sonar = new SonarReading[1];//m_protocol.readSonar(m_port.readSyncronosly());
		IRReading[] ir = new IRReading[1];
		
		DistanceReading d = m_protocol.readDistanceMeasurement(m_port.readSyncronosly());
		sonar[0] = d.m_sonar;
		ir[0] = d.m_ir;
		float temperature = m_protocol.readTemperature(m_port.readSyncronosly());
		
		SensorReading reading = new SensorReading(compassDirection,
												  gyroAccelerator,
												  sonar,
												  ir,
												  temperature);
		return reading;
	}

	private SerialPort m_port;
	
	public Robot(SerialPort port) {
		this.m_port = port;
	}
	
	public String read() {
		String data = m_port.read();
		
		
		return data;
	}
	
	
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
