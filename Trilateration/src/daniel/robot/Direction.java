package daniel.robot;

public class Direction {

	private static final float RADIANS_TO_DEGREES = 57.2957795f;
	private final float m_heading;
	private final float x;
	private final float y;
	
	public Direction(float a_heading) {
		m_heading = wrap(a_heading);
		x = (float) Math.cos( toRadians(m_heading ));
		y = (float) Math.sin( toRadians(m_heading ));
	}
	
	public static Direction RadiansToDegrees(float radians) {
		return new Direction(radians * RADIANS_TO_DEGREES);
	}

	public Direction getTurned(float a_dh) {
		return new Direction(m_heading + a_dh);
		
	}

	public float GetDifferenceInDegrees(Direction a_compassDirection) {
		return getDegreeDifference(m_heading, a_compassDirection.m_heading);
	}
	
	public Direction getHeadDirection(Direction servo) {
		Direction dir = new Direction(m_heading - servo.getHeadingDegrees() + 90.0f);
		return dir;
	}

	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;//
	}

	public float getHeadingDegrees() {
		return m_heading;
	}
	
	public String toString() {
		return " " + m_heading + " degrees";
	}
	
	private float getDegreeDifference(float dir1, float direction) {
		
		float diff = 0.0f;
		if (dir1 >= direction)
			diff = dir1 - direction;
		else
			diff = direction - dir1;
		
		if (diff > 180) {
			return 360-diff;
		} 
		return diff;
	}
	
	private double toRadians(float a_heading2) {
		return a_heading2 / RADIANS_TO_DEGREES;
	}
	
	private float wrap(float a_heading) {
		while (a_heading > 360) {
			a_heading -= 360;
		}

		while (a_heading < 0) {
			a_heading += 360;
		}
		return a_heading;
	}

}
