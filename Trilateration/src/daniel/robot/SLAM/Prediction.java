package daniel.robot.SLAM;


class Prediction {
	private float distance;


	public Prediction(float minLenSquare, Landmark end) {
		minLenSquareDistance = minLenSquare;
		landmark = end;
	}
	
	public float getDistance() {
		if (hasCalculatedDistance == false) {
			distance = (float) Math.sqrt(minLenSquareDistance);
			hasCalculatedDistance = true;
		} 
		return distance;
		
	}
	private float minLenSquareDistance;
	Landmark landmark;
	private boolean hasCalculatedDistance = false;
	
	
	public boolean isCloserThan(Prediction ret2) {
		return minLenSquareDistance < ret2.minLenSquareDistance;
	}
}