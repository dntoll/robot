package daniel.robot;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class LinearRegression {
	public static Line2D.Float GetLine(Point2D.Float[] a_points) { 

		int n = a_points.length;
        // first pass: compute xbar and ybar
		float sumx = 0.0f, sumy = 0.0f, sumx2 = 0.0f;
        for(Point2D.Float point : a_points) {
            sumx  += point.x;
            sumx2 += point.x * point.x;
            sumy  += point.y;
        }
        float xbar = sumx / n;
        float ybar = sumy / n;

        // second pass: compute summary statistics
        float xxbar = 0.0f, yybar = 0.0f, xybar = 0.0f;
        for (int i = 0; i < n; i++) {
            xxbar += (a_points[i].x - xbar) * (a_points[i].x - xbar);
            yybar += (a_points[i].y - ybar) * (a_points[i].y - ybar);
            xybar += (a_points[i].x - xbar) * (a_points[i].y - ybar);
        }
        float beta1 = xybar / xxbar;
        float beta0 = ybar - beta1 * xbar;
        
        System.out.println("y   = " + beta1 + " * x + " + beta0);
        
        //Find line
        //y = beta1 + x + beta0;
        float lsy = beta1 * a_points[0].x + beta0;
        Point2D.Float startPoint = new Point2D.Float(a_points[0].x, lsy);
        
        float ley = beta1 * a_points[a_points.length-1].x + beta0;
        Point2D.Float endPoint = new Point2D.Float(a_points[a_points.length-1].x, ley);
        
        Line2D.Float ret = new Line2D.Float(startPoint, endPoint );
        
        return ret;

        // print results
    /*    

        // analyze results
        int df = n - 2;
        double rss = 0.0;      // residual sum of squares
        double ssr = 0.0;      // regression sum of squares
        for (int i = 0; i < n; i++) {
            double fit = beta1*a_points[i].x + beta0;
            rss += (fit - a_points[i].y) * (fit - a_points[i].y);
            ssr += (fit - ybar) * (fit - ybar);
        }
        double R2    = ssr / yybar;
        double svar  = rss / df;
        double svar1 = svar / xxbar;
        double svar0 = svar/n + xbar*xbar*svar1;
        System.out.println("R^2                 = " + R2);
        System.out.println("std error of beta_1 = " + Math.sqrt(svar1));
        System.out.println("std error of beta_0 = " + Math.sqrt(svar0));
        svar0 = svar * sumx2 / (n * xxbar);
        System.out.println("std error of beta_0 = " + Math.sqrt(svar0));

        System.out.println("SSTO = " + yybar);
        System.out.println("SSE  = " + rss);
        System.out.println("SSR  = " + ssr);*/
    }
}
