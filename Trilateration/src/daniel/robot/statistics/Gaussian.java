package daniel.robot.statistics;

public class Gaussian {
	public static float gaussian(float mu, float sigma, float x) {
		return phi((x - mu) / sigma) / sigma;
	}
	
	public static float phi(float x) {
        return (float) (Math.exp(-x*x / 2) / Math.sqrt(2.0f * Math.PI));
    }
}
