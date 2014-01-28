package daniel.robot;

public class Direction {

	private static final float RADIANS_TO_DEGREES = 57.2957795f;
	private float m_heading = 0.0f;
	
	public Direction(float a_heading) {
		m_heading = a_heading;
		wrap();
	}
	
	public static Direction RadiansToDegrees(float radians) {
		return new Direction(radians * RADIANS_TO_DEGREES);
	}

	public void Turn(float a_dh) {
		m_heading += a_dh;
		wrap();
	}

	public float GetDifferenceInDegrees(Direction a_compassDirection) {
		return getDegreeDifference(m_heading, a_compassDirection.m_heading);
	}
	
	public Direction getHeadDirection(float a_servo) {
		Direction dir = new Direction(m_heading - a_servo + 90);
		return dir;
	}

	public float getX() {
		return (float) Math.cos( toRadians(m_heading ));
	}
	
	public float getY() {
		return (float) Math.sin( toRadians(m_heading ));
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
	
	private void wrap() {
		while (m_heading > 360) {
			m_heading -= 360;
		}

		while (m_heading < 0) {
			m_heading += 360;
		}
	}

}
