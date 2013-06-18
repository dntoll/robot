package daniel.robot.SLAM;

import daniel.robot.sensors.SensorReading;

public class Reading {
	
	public Reading(ParticleFilter particles, SensorReading reading) {
		m_particles = particles;
		m_reading = reading;
		m_map = new Map(particles.getBestGuess(), reading);
	}
	public ParticleFilter m_particles;
	public SensorReading m_reading;
	public Map m_map;
	
	public State getBestGuess() {
		return m_particles.getBestGuess();
	}

	public float getDistance(State state, float a_servoDirection, float beamWidth) throws Exception {
		// TODO Auto-generated method stub
		return m_map.getDistance(state, a_servoDirection, beamWidth);
	}
}