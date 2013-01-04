package pl.edu.pw.elka.pszt;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;

/*
 * Class that is used to to show the results of each generation
 * @author Marcin Kubik
 * @author Mikołaj Markiewicz
 * @author Krzysztof Opasiak
 */

@SuppressWarnings("serial")
public class Painter extends JFrame {
	
	private static final boolean CONTOURS = true;
	public static double X = 500;
	public static double Y = 500;

	public Painter(Entity entity, Entity multiEntity, String s)
	{
		super(s);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setBounds(0,0, 500 ,500);
		this.setVisible(true);
		ArrayList<Segment> segments = entity.getSegments();
		ArrayList<Segment> multiSegments = multiEntity.getSegments();
		int moveX = this.findChangeX(segments);
		int moveY = this.findChangeY(segments);
		paintEntity(segments, multiSegments, moveX, moveY, getScaleX(segments),getScaleY(segments));
	}
	
	public Painter(Entity entity, Population p)
	{
		this(entity, new Entity(p.getMultiSegment()), "Generation " + p.getGenerationNumber() + ", Adaptation = " + entity.getAdaptationSize());
		
	}
	

	public int findChangeX(ArrayList<Segment> segments) 
	{
		int tmpMinX  = 0;
		for(Segment s : segments)
		{
			ArrayList<Vertex> vertices = s.getVertices();
			for(int i = 0; i<vertices.size();++i)
			{
				if((int)vertices.get(i).getX() < tmpMinX) 
				{
					tmpMinX = (int)vertices.get(i).getX();
				}
			}
		}
		return tmpMinX;
	}
	
	public int findChangeY(ArrayList<Segment> segments)
	{
		int tmpMinY  = 0;
		for(Segment s : segments)
		{
			ArrayList<Vertex> vertices = s.getVertices();
			for(int i = 0; i<vertices.size();++i)
			{
				if((int)vertices.get(i).getY() < tmpMinY) 
				{
					tmpMinY = (int)vertices.get(i).getY();
				}
			}
		}
		return tmpMinY;
	}
	
	public double getScaleX(ArrayList<Segment> segments)
	{
		double tmpX  = -9999;
		for(Segment s : segments)
		{
			ArrayList<Vertex> vertices = s.getVertices();
			for(int i = 0; i<vertices.size();++i)
			{
				if(vertices.get(i).getX() > tmpX) 
				{
					tmpX = vertices.get(i).getX();
				}
			}
		}
		return tmpX/X;
	}
		
	public double getScaleY(ArrayList<Segment> segments)
	{
		double tmpY  = -9999;
		for(Segment s : segments)
		{
			ArrayList<Vertex> vertices = s.getVertices();
			for(int i = 0; i<vertices.size();++i)
			{
				if(vertices.get(i).getY() > tmpY) 
				{
					tmpY = vertices.get(i).getY();
				}
			}
		}
		return tmpY/Y;

	}
	private void paintEntity(final ArrayList<Segment> segments, final ArrayList<Segment> multiSegments,
								final int moveX, final int moveY, final double sX, final double sY) 
	{
		this.add(new JPanel()
		{
			public void paintComponent(Graphics g)
			{
				this.setBackground(Color.white);
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				double scaleX = 1/sX;
				double scaleY = 1/sY;
				scaleX = scaleX/4;
				scaleY = scaleY/4;
				if(scaleX < 1) scaleX = 1;
				if(scaleY < 1) scaleY = 1;
				int correctionX = moveX>=0 ? 0 : moveX;
				int correctionY = moveY>=0 ? 0 : moveY;
				if(CONTOURS) 
				{
					for (Segment segment : multiSegments)
					{
						float dash[] = {10.0f};
						BasicStroke dashed = new BasicStroke(1.0f,
								BasicStroke.CAP_BUTT,
								BasicStroke.JOIN_MITER, 10.0f,dash,0.0f);

						g2.setStroke(dashed);
						ArrayList<Vertex> multiVertices = segment.getVertices();
						for (int i = 0; i < multiVertices.size(); ++i)
						{
							g2.drawLine(50 + correctionX + (int)scaleX *(int) multiVertices.get(i).getX(),
										50 + correctionY + (int)scaleY *(int) multiVertices.get(i).getY(),
										50 + correctionX + (int)scaleX *(int) multiVertices.get((i + 1) % multiVertices.size()).getX(),
										50 + correctionY + (int)scaleY *(int) multiVertices.get((i + 1) % multiVertices.size()).getY());
						}				
					}	
				}
				
				for (Segment segment : segments)
				{
					Random random = new Random();
					int red = random.nextInt(256);
					int green = random.nextInt(256);
					int blue = random.nextInt(256);

					g2.setColor(new Color(red, green, blue));
					g2.setStroke(new BasicStroke(5));
					ArrayList<Vertex> vertices = segment.getVertices();
					for (int i = 0; i < vertices.size(); ++i)
					{
						g2.drawLine(50 + correctionX + (int)scaleX *(int) vertices.get(i).getX(),
									50 + correctionY + (int)scaleY *(int) vertices.get(i).getY(),
									50 + correctionX + (int)scaleX *(int) vertices.get((i + 1) % vertices.size()).getX(),
									50 + correctionY + (int)scaleY *(int) vertices.get((i + 1) % vertices.size()).getY());
					}
				}
			}
		});
	}

}
