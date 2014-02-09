package daniel.robot.sensors;

import java.util.ArrayList;
import java.util.List;

import daniel.robot.Direction;

public class Compass {
	
	List<CompassReading> allReadings = new ArrayList<CompassReading>();
	
	public void add(CompassReading compassReading) {
		
		allReadings.add(compassReading);
	}

	public Direction getHeading() {
		
		if (allReadings.size() > 0)
			return allReadings.get(allReadings.size()-1).getHeading();
		else 
			return null;
	}

	public List<CompassReading> getAllReadings() {
		return allReadings;
	}

}
