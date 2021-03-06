package daniel.robot.simulation;

import java.awt.image.BufferedImage;
import java.io.IOException;

import daniel.robot.Direction;
import daniel.robot.FloatCollection;
import daniel.robot.glWindow.model.CalibrationModel;
import daniel.robot.glWindow.model.DirectionalReadingCollection;
import daniel.robot.glWindow.model.IRobotInterface;
import daniel.robot.glWindow.model.State;
import daniel.robot.sensors.Compass;
import daniel.robot.slam.Movement;

public class SimulationReadings implements IRobotInterface {
	
	World surroundings;
	
	private State robot;
	Compass compass = new Compass();
	DirectionalReadingCollection latest = null;
	
	
	public SimulationReadings(CalibrationModel cm) {
		surroundings = new World(512, cm);
		robot = new State(surroundings.getStartPosition(), surroundings.getStartDirection() );
	}
	
	@Override
	public DirectionalReadingCollection makeReading() throws IOException,
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
				distance = 0.0f;
				state++;
			}
		} else if (state == 1) {
			if (robot.getRobotPosition().y > 460) {
				turn = 90.0f;
				distance = 0.0f;
				state++;
			}
		}  else if (state == 2) {
			if (robot.getRobotPosition().x < 50) {
				turn = 90.0f;
				distance = 0.0f;
				state++;
			}
		} else if (state == 3) {
			if (robot.getRobotPosition().y < 280) {
				turn = 90.0f;
				distance = 0.0f;
				state = 0;
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
	public DirectionalReadingCollection getDistanceSensorReadings() {
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

	@Override
	public FloatCollection[] makeCalibration() {
		return null;
	}

	@Override
	public FloatCollection[] makeSingleDistanceRead() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
