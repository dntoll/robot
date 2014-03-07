package daniel.robot.glWindow.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Measurement {
	protected List<Float> values = new ArrayList<Float>();
	
	float min = Float.MAX_VALUE;
	float max = Float.MIN_VALUE;
	float total = 0;
	
	
	public void addDistance(float distanceCM) {
		
			
		values.add(new Float(distanceCM));
		Collections.sort(values);
		if (distanceCM < min)
			min = distanceCM;
		if (distanceCM > max)
			max = distanceCM;
		total += distanceCM;
	}
	
	public float getMin() {
		return min;
	}
	public float getMax() {
		return max;
	}

	public float getMean() {
		
		if (values.size() > 0) {
			return total / values.size();
		} else
			return 0;
	}
	
	public float getMedian() {
		
		if (values.size() > 0) {
			if (values.size() % 2 == 1)
				return values.get(values.size() / 2);
			else 
				return (values.get(values.size() / 2) + values.get(values.size() / 2-1))/ 2.0f;
		} else {
			return 0;
		}
	}

	public float getQ1() {
		if (values.size() > 0) {
			return values.get(values.size() / 4)-1.0f;
		} else {
			return 0;
		}
	}
	
	public float getQ3() {
		if (values.size() > 0) {
			return values.get(3 * values.size() / 4);
		} else {
			return 0;
		}
	}
	
	//http://statistics.about.com/od/HelpandTutorials/a/How-To-Calculate-A-Standard-Deviation.htm
	public float getStdev() {
		if (values.size() < 2)
			return 0;
		float mean = getMean();
		float totalsq = 0;
		for(Float value : values) {
			float diff = value.floatValue() - mean;
			float sq = diff * diff;
			
			totalsq += sq;
		}
		float sq = totalsq / (values.size() - 1);
		return (float) Math.sqrt(sq);
	}
	
	public String toString() {
		return String.format("%03.0f %03.0f %03.0f %03.0f %03.0f", min, getMedian(), getMean(), max, getStdev());
	}

	public boolean toLarge() {
		return getMedian() > 300;
	}

	public List<Float> getValues() {
		return values;
	}

	

}
