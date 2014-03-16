package daniel.robot.glWindow.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import daniel.robot.FloatCollection;

public class CalibrationModel {

	String file = "sharpCalibration.csv";
	
	/**
	 * 
	 * @param sensorType 0== shortrange, 1== longrange
	 * @param distance
	 * @param values
	 */
	public void addValues(int sensorType, int distance, FloatCollection values) {
		File valueCollection = new File(file);
		try {
			FileWriter writer = new FileWriter(valueCollection, true);
		
		
			for (Float value : values.getValues()) {
				writer.write("" + sensorType + "; "+ distance + "; " + value.floatValue() +"\n");
			}
			
			writer.close();
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
