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


@SuppressWarnings("serial")
public class Painter extends JFrame {
	
	public static double X = 500;
	public static double Y = 500;
	
	
	public Painter(Entity entity, String s)
	{
		super(s);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setBounds(0,0,(int)dim.getWidth(),(int)dim.getHeight());
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		ArrayList<Segment> segments = entity.getSegments();
		int moveX = this.findChangeX(segments);
		int moveY = this.findChangeY(segments);
		paintEntity(segments, moveX, moveY, getScaleX(segments),getScaleY(segments));
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
	private void paintEntity(final ArrayList<Segment> segments, final int moveX, final int moveY,
																final double sX, final double sY) 
	{
		this.add(new JPanel()
		{
			public void paintComponent(Graphics g)
			{
				this.setBackground(Color.white);
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				//int scale = 20;
				double scaleX = 1/sX;
				double scaleY = 1/sY;
				scaleX = scaleX/4;
				scaleY = scaleY/4;
				if(scaleX < 1) scaleX = 1;
				if(scaleY < 1) scaleY = 1;
				int correctionX = moveX>=0 ? 0 : moveX;
				int correctionY = moveY>=0 ? 0 : moveY;

				
				for (Segment segment : segments)
				{
					Random random = new Random();
					int red = random.nextInt(256);
					int green = random.nextInt(256);
					int blue = random.nextInt(256);

					g2.setColor(new Color(red, green, blue));
					g2.setStroke(new BasicStroke(random.nextInt(segments.size()) + 1));
					ArrayList<Vertex> vertices = segment.getVertices();

					for (int i = 0; i < vertices.size(); ++i)
					{
						g2.drawLine(250 + correctionX + (int)scaleX *(int) vertices.get(i).getX(),
									250 + correctionY + (int)scaleY *(int) vertices.get(i).getY(),
									250 + correctionX + (int)scaleX *(int) vertices.get((i + 1) % vertices.size()).getX(),
									250 + correctionY + (int)scaleY *(int) vertices.get((i + 1) % vertices.size()).getY());
					}
				}
			}
		});
	}

}
