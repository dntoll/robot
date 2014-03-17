package daniel.robot.glWindow.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import daniel.robot.FloatCollection;

public class CalibrationModel {

	String file = "sharpCalibration.csv";
	
	
	
	private class DistanceValuesCollection {
		HashMap<Float, FloatCollection> calibrations = new HashMap<Float, FloatCollection>();

		public void add(float distance, float value) {
			Float key = new Float(distance);
			if (calibrations.containsKey(key)) {
				
			} else {
				calibrations.put(key, new FloatCollection());
			}
			
			calibrations.get(key).addValue(value);
		}	
	}
	
	DistanceValuesCollection calibrations[] = new DistanceValuesCollection[2];
	
	public CalibrationModel()  {
		
		calibrations[0] = new DistanceValuesCollection();
		calibrations[1] = new DistanceValuesCollection();
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(file)));
			
			
			String thisLine;
			while ((thisLine = reader.readLine()) != null) { 
				String parts[] = thisLine.split(";");
				
				int sensorType = Integer.parseInt(parts[0]);
				float distance = Float.parseFloat(parts[1]);
				float value = Float.parseFloat(parts[2]);
				
				calibrations[sensorType].add(distance, value);
			} 
			
			reader.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
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
				calibrations[sensorType].add(distance, value);
			}
			
			writer.close();
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public float sensorToDistance(float sensorValue, int sensorType) {
		Set<Float> keySet = calibrations[sensorType].calibrations.keySet();
		
		Float[] distances = keySet.toArray(new Float[keySet.size()]);
		Arrays.sort(distances);
		
		float dist[] = new float[distances.length];
		float sensorValues[] = new float[distances.length];
		
		int i = 0;;
		for(Float key : distances) {
		  float median = calibrations[sensorType].calibrations.get(key).getMedian();
		  dist[i] = key.floatValue();
		  sensorValues[i] = median;
		  i++;
		}
		
		
		return transformToCM(sensorValue, sensorValues, dist);
		
	}
	
	public float distanceToSensor(float distance, int sensorType) {
		Set<Float> keySet = calibrations[sensorType].calibrations.keySet();
		
		Float[] distances = keySet.toArray(new Float[keySet.size()]);
		Arrays.sort(distances);
		
		float dist[] = new float[distances.length];
		float sensorValues[] = new float[distances.length];
		
		int i = 0;;
		for(Float key : distances) {
		  float median = calibrations[sensorType].calibrations.get(key).getMedian();
		  dist[i] = key.floatValue();
		  sensorValues[i] = median;
		  i++;
		}
		
		
		return fromDistance(distance, sensorValues, dist);
		
	}
	
	
	
	private float floatMap(float sourceValue, float sourceMin, float sourceMax, float destMin, float destMax) {
		float sourceRange = sourceMax - sourceMin;
		float percent = ((sourceValue - sourceMin ) / sourceRange);
		float destRange = destMax - destMin;
		return destMin + percent * destRange;
    }
	
	private float transformToCM(float sensorValue, float sensorValues[], float distance[]) {
		if (distance.length == 0)
			return 0;
		
		float dist = distance[0];
	 
		for (int i = 0; i < sensorValues.length - 1; i++) {
			if (sensorValue > sensorValues[i+1]) {
			     return floatMap(sensorValue, sensorValues[i], sensorValues[i+1], distance[i], distance[i+1]);
			}
		}
  
		return distance[distance.length-1];
	}
	
	/**
	 * Creates sensor value from distance
	 */
	private float fromDistance(float distanceCM, float sensorValues[], float distance[]) {
		float reading = 0.0f;
		 
		for (int i = 0; i < distance.length - 1; i++) {
			if (distanceCM > distance[i+1]) {
				return floatMap(distanceCM, distance[i], distance[i+1], sensorValues[i], sensorValues[i+1]);
			     
			}
		}
  
		return reading;
	}

}
