package daniel.robot.glWindow.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import daniel.robot.Direction;

public class SavedReadings {
	
	
	public void save(DistanceSensorReadings dsr, File destination) throws IOException {
		
		//File readingsFile = new File("readings/" + startTime.toString() + ".csv");
		FileWriter out = new FileWriter(destination);
		
		out.write(String.format("%f\n", dsr.getCompassDirection().getHeadingDegrees()));
		for (DirectionalReading dr : dsr.getReadings().values()) {
			save(dr, out);
		}
		out.flush();
		out.close();
	}
	
	private void save(DirectionalReading dr, FileWriter csvWriter) throws IOException {
		
		save(dr.getSonar1Distance(), dr.getServoDirection(), csvWriter, "so");
		save(dr.getSharp1Distance(), dr.getServoDirection(), csvWriter, "sh");
	}

	private void save(Measurement sonar1Distance, Direction servoDirection,	FileWriter csvWriter, String type) throws IOException {
		for (Float value : sonar1Distance.getValues()) {
			String str = String.format("%s:%f:%f\n", type, servoDirection.getHeadingDegrees(), value.floatValue());
			csvWriter.write(str);
		}
	}

	public DistanceSensorReadings load(File source) throws NumberFormatException, IOException {
		
		BufferedReader in = new BufferedReader(new FileReader(source));
		
		float compassDirection = Float.parseFloat(in.readLine());
		
		DistanceSensorReadings ret = new DistanceSensorReadings(new Direction(compassDirection));
		
		String line = in.readLine(); //titles
		do {
			
			
			String[] parts = line.split(":");
			
			String type = parts[0];
			Direction servoDirection = new Direction(Float.parseFloat(parts[1]));
			float distance = Float.parseFloat(parts[2]);
			
			if (type.equals("sh"))
				ret.addSharpDistance(servoDirection, distance);
			else
				ret.addSonar(servoDirection, distance);
			
			line = in.readLine(); //titles
		} while (line != null);
		
		in.close();
		
		return ret;
	}
	
	
}
