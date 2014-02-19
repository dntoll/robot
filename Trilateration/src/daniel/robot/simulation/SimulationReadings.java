package daniel.robot.simulation;

import java.io.IOException;

import daniel.robot.Direction;
import daniel.robot.SLAM.Movement;
import daniel.robot.glWindow.model.DistanceSensorReadings;
import daniel.robot.glWindow.model.IRobotInterface;
import daniel.robot.glWindow.model.State;
import daniel.robot.sensors.Compass;

public class SimulationReadings implements IRobotInterface {
	
	World surroundings = new World(250);
	private State robot = new State(surroundings.getStartPosition(), surroundings.getStartDirection() );
	Compass compass = new Compass();
	DistanceSensorReadings latest = null;
	
	@Override
	public DistanceSensorReadings makeReading() throws IOException,
			InterruptedException {
		latest = surroundings.makeReading(robot );
		
		return latest;
	}

	@Override
	public Movement makeMove() throws InterruptedException {
		float distance = 15.0f;
		float turn = -15.0f;
		Movement move = new Movement(distance, turn);
		
		robot.moveFlawLess(move);
		return move;
	}

	@Override
	public void update() {
		
	}

	@Override
	public DistanceSensorReadings getDistanceSensorReadings() {
		return latest;
	}

	@Override
	public Compass getCompass() {
		return compass;
	}

	@Override
	public float getTemperature() {
		return 22.5f;
	}

	@Override
	public Direction getCompassDirection() {
		return robot.m_heading;
	}

}
