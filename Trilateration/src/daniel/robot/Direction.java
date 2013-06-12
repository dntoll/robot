package daniel.robot;

public class Direction {

	public static float getDegreeDifference(float dir1, float dir2) {
		
		float diff = 0.0f;
		if (dir1 >= dir2)
			diff = dir1 - dir2;
		else
			diff = dir2 - dir1;
		
		if (diff > 180) {
			return 360-diff;
		} 
		return diff;
	}

}
