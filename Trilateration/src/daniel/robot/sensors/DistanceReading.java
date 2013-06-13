package daniel.robot.sensors;


public class DistanceReading {
	public SonarReading m_sonar;
	public IRReading m_ir;
	public DistanceReading(SonarReading s, IRReading i) {
		m_sonar=s;
		m_ir= i;
	}

}
