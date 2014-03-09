package daniel.robot;

import java.util.Random;

public class Gaussian {
	private static Random random = new Random();    // pseudo-random number generator
	
	public static double gaussian(float mu, float sigma, float x) {
		return phi((x - mu) / sigma) / sigma;
	}
	
	public static double phi(float x) {
        return (Math.exp(-x*x / 2.0) / Math.sqrt(2.0 * Math.PI));
    }
	
	
	
	 /**
     * Return real number uniformly in [a, b).
     */
    private static float uniform(float a, float b) {
        return a + random.nextFloat() * (b-a);
    }
	
	/**
     * Return a real number with a standard Gaussian distribution.
     */
    private static float gaussian() {
        // use the polar form of the Box-Muller transform
    	float r, x, y;
        do {
            x = uniform(-1.0f, 1.0f);
            y = uniform(-1.0f, 1.0f);
            r = x*x + y*y;
        } while (r >= 1 || r == 0);
        return (float) (x * Math.sqrt(-2 * Math.log(r) / r));

        // Remark:  y * Math.sqrt(-2 * Math.log(r) / r)
        // is an independent random gaussian
    }

    /**
     * Return a real number from a gaussian distribution with given mean and stddev
     */
    public static float getRandomGaussian(float mean, float stddev) {
        return mean + stddev * gaussian();
    }
}
