package daniel.robot;

public class DistanceReading {
	SonarReading m_sonar;
	IRReading m_ir;
	public DistanceReading(SonarReading s, IRReading i) {
		m_sonar=s;
		m_ir= i;
	}

}
