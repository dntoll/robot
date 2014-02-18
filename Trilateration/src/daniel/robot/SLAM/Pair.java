package daniel.robot.SLAM;


class Pair {
	public Pair(float minLenSquare, Landmark end) {
		minLenSquareDistance = minLenSquare;
		landmark = end;
	}
	
	public float getDistance() {
		return (float) Math.sqrt(minLenSquareDistance);
	}
	private float minLenSquareDistance;
	Landmark landmark;
}