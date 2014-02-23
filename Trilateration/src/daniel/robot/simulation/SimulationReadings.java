package daniel.robot.simulation;

import java.awt.image.BufferedImage;
import java.io.IOException;

import daniel.robot.Direction;
import daniel.robot.SLAM.Movement;
import daniel.robot.glWindow.model.DistanceSensorReadings;
import daniel.robot.glWindow.model.IRobotInterface;
import daniel.robot.glWindow.model.State;
import daniel.robot.sensors.Compass;

public class SimulationReadings implements IRobotInterface {
	
	World surroundings = new World(512);
	
	private State robot = new State(surroundings.getStartPosition(), surroundings.getStartDirection() );
	Compass compass = new Compass();
	DistanceSensorReadings latest = null;
	
	@Override
	public DistanceSensorReadings makeReading() throws IOException,
			InterruptedException {
		latest = surroundings.makeReading(robot );
		
		return latest;
	}

	
	int state = 0;
	@Override
	public Movement makeMove() throws InterruptedException {
		
		float distance = 15.0f;
		float turn = 0.0f;
		
		if (state == 0) {
			if (robot.getRobotPosition().x > 420) {
				turn = 90.0f;
				state++;
			}
		} else if (state == 1) {
			if (robot.getRobotPosition().y > 460) {
				turn = 90.0f;
				state++;
			}
		}  else if (state == 2) {
			if (robot.getRobotPosition().x < 50) {
				turn = 180.0f;
				state++;
			}
		}
		
		
		
		
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

	@Override
	public BufferedImage getPanoramaImage() {
		// TODO Auto-generated method stub
		return null;
	}

}
