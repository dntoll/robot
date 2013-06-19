package daniel.robot.sensors;


public class IRandSonarReading {
	public SonarReading m_sonar;
	public IRReading m_ir;
	
	public IRandSonarReading(SonarReading s, IRReading i) {
		m_sonar=s;
		m_ir= i;
	}

}
