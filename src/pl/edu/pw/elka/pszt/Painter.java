package pl.edu.pw.elka.pszt;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Painter extends JFrame {

	public Painter(Entity entity)
	{
		 super(entity.id + " : " + entity.getAdaptationSize());

		this.setBounds(100, 100, 500, 500);

		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		paintEntity(entity);
	}

	private void paintEntity(final Entity entity) {
		this.add(new JPanel()
		{
			public void paintComponent(Graphics g)
			{
				this.setBackground(Color.white);
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				int scale = 20;

				ArrayList<Segment> segments = entity.getSegments();

				for (Segment segment : segments)
				{
					Random random = new Random();
					int red = random.nextInt(256);
					int green = random.nextInt(256);
					int blue = random.nextInt(256);

					g2.setColor(new Color(red, green, blue));
					g2.setStroke(new BasicStroke(new Random().nextInt(segments.size()) + 1));
					ArrayList<Vertex> vertices = segment.getVertices();

					for (int i = 0; i < vertices.size(); ++i)
					{
						g2.drawLine(250 + scale * (int) vertices.get(i).getX(),
									250 + scale * (int) -vertices.get(i).getY(),
									250 + scale * (int) vertices.get((i + 1) % vertices.size()).getX(),
									250 + scale * (int) -vertices.get((i + 1) % vertices.size()).getY());
					}
				}
			}
		});
	}

}