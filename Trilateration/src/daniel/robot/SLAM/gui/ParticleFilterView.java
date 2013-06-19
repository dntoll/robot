package daniel.robot.SLAM.gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;

import daniel.robot.SLAM.ParticleFilter;
import daniel.robot.SLAM.State;

public class ParticleFilterView {

	public void draw(Graphics2D a_grapics, ParticleFilter a_filter, float a_scale) {
		int size = a_filter.getSize();
		
		a_grapics.setColor(Color.RED);
		a_grapics.setComposite(AlphaComposite.getInstance(
		        AlphaComposite.SRC_OVER, 0.03f));
		
		for (int p = 0; p < size; p++) {
			State s = a_filter.getState(p);
			
			a_grapics.fillRect((int)(s.m_position.x*a_scale), (int)(s.m_position.y*a_scale), (int)a_scale, (int)a_scale);
		}
	}

}
