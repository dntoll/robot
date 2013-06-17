package daniel.robot.SLAM.gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;

import daniel.robot.SLAM.ParticleFilter;
import daniel.robot.SLAM.State;

public class ParticleFilterView {

	public void draw(Graphics2D g2d, ParticleFilter filter, float scale) {
		int size = filter.getSize();
		
		g2d.setColor(Color.RED);
		g2d.setComposite(AlphaComposite.getInstance(
		        AlphaComposite.SRC_OVER, 0.03f));
		
		for (int p = 0; p < size; p++) {
			State s = filter.getState(p);
			
			g2d.fillRect((int)(s.m_position.x*scale), (int)(s.m_position.y*scale), (int)scale, (int)scale);
		}
	}

}
