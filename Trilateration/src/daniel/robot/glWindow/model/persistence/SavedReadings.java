package daniel.robot.glWindow.model.persistence;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import daniel.robot.Direction;
import daniel.robot.FloatCollection;
import daniel.robot.glWindow.model.DirectionalReading;
import daniel.robot.glWindow.model.DirectionalReadingCollection;

public class SavedReadings {
	
	
	public void save(DirectionalReadingCollection dsr, File destination) throws IOException {
		
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
		
		save(dr.getShortDistance(), dr.getServoDirection(), csvWriter, "sh");
		save(dr.getLongDistance(), dr.getServoDirection(), csvWriter, "lo");
	}

	private void save(FloatCollection sonar1Distance, Direction servoDirection,	FileWriter csvWriter, String type) throws IOException {
		for (Float value : sonar1Distance.getValues()) {
			String str = String.format("%s:%f:%f\n", type, servoDirection.getHeadingDegrees(), value.floatValue());
			csvWriter.write(str);
		}
	}

	public DirectionalReadingCollection load(File source) throws NumberFormatException, IOException {
		
		BufferedReader in = new BufferedReader(new FileReader(source));
		
		float compassDirection = Float.parseFloat(in.readLine());
		
		DirectionalReadingCollection ret = new DirectionalReadingCollection(new Direction(compassDirection));
		
		String line = in.readLine(); //titles
		do {
			
			
			String[] parts = line.split(":");
			
			String type = parts[0];
			Direction servoDirection = new Direction(Float.parseFloat(parts[1]));
			float reading = Float.parseFloat(parts[2]);
			
			if (type.equals("sh"))
				ret.addSharpReading(servoDirection, reading, false);
			if (type.equals("lo"))
				ret.addSharpReading(servoDirection, reading, true);
			
			
			line = in.readLine(); //titles
		} while (line != null);
		
		in.close();
		
		return ret;
	}
	
	
}
