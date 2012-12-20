package pl.edu.pw.elka.pszt;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.geom.Ellipse2D;
import java.text.DecimalFormat;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.vividsolutions.jts.geom.Coordinate;

@SuppressWarnings("serial")
public class Statistics extends JFrame{

	private final Vector<Double> adaptations;
	private final Population p;
	double time;
	public Statistics(Population p)
	{
		super("Statistics");
		this.setBackground(Color.WHITE);
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		adaptations = new Vector<Double>();
		this.p = p;
		this.time = (double)System.currentTimeMillis();
	}
	
	public void addAdaptation(double a)
	{
		adaptations.add(a);
	}
	
	public void showResults(Population p) 
	{
		this.setTitle("Sequence reached in " + p.getGenerationNumber()+" generation ");
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int scale = (int)dim.getWidth()/this.adaptations.size();
		Vector<Coordinate> coordinates = new Vector<Coordinate>();
		for(int i = 0; i<this.adaptations.size(); ++i)
		{
			coordinates.add(new Coordinate(scale*i,(dim.getHeight()/2)*this.adaptations.get(i)));
		}
		this.time = (double)System.currentTimeMillis() - this.time;
		this.time = (double)this.time/1000;
		
		paintGraph(coordinates, this.time);
		this.setBounds(0,0,(int)dim.getWidth(),(int)dim.getHeight());
		this.setVisible(true);
		
	}
	public void paintGraph(final Vector<Coordinate> adapt, double time)
	{
		this.add(new MyPanel(adapt), BorderLayout.CENTER);	
		this.add(new UpInformation(time,p), BorderLayout.NORTH);
		this.add(new DownInformation(this), BorderLayout.SOUTH);
	}
	
	private class MyPanel extends JPanel 
	{
		Vector<Coordinate> adapt;
		public MyPanel(final Vector<Coordinate> adapt)
		{
			this.setBackground(Color.WHITE);
			this.adapt = adapt;
			setVisible(true);
		}
		public void paintComponent(Graphics g) 
		{
			this.setBackground(Color.WHITE);
			Shape circle;
			Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
			Graphics2D g2 = (Graphics2D) g;
			for(int i = 0; i<adapt.size()-1; ++i)
			{
				System.out.println("A TU TO " + adapt.get(i).y);
				g2.drawLine((int)adapt.get(i).x, (int)(dim.getHeight() - adapt.get(i).y - 200),
							(int)adapt.get(i+1).x, (int)(dim.getHeight() - adapt.get(i+1).y - 200));
				circle = new Ellipse2D.Float((int) adapt.get(i).x -3, (int)(dim.getHeight() - adapt.get(i).y - 203) -3, 6.0f,6.0f);
				g2.draw(circle);
			}
			circle = new Ellipse2D.Float((int) adapt.get(adapt.size()-1).x - 3, 
										(int)(dim.getHeight() - adapt.get(adapt.size()-1).y -200), 6.0f,6.0f);
			g2.draw(circle);
			float dash[] = {10.0f};
			BasicStroke dashed = new BasicStroke(1.0f,
                    BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_MITER, 10.0f,dash,0.0f);

			g2.setStroke(dashed);
			g2.drawLine(0, (int)(dim.getHeight() - adapt.get(adapt.size()-1).y -200), 
							(int)dim.getWidth(), (int)(dim.getHeight() - adapt.get(adapt.size()-1).y - 200));
		
			g2.drawLine(0,(int)(dim.getHeight() - (dim.getHeight()/2) * 0.5 - 200), (int)dim.getWidth(), (int)(dim.getHeight() - (dim.getHeight()/2) * 0.5) - 200);
		}
	}
	
	private class UpInformation extends JPanel
	{
		
		public UpInformation(double time, Population p)
		{
			Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
			
			JLabel timeLabel = new JLabel("Time of calculations: "+ time + " seconds");
			timeLabel.setPreferredSize(new Dimension((int)dim.getWidth(),(int)dim.getHeight()/20));
			this.add(timeLabel);
			
			JLabel generationsLabel = new JLabel("Number of generations: " + p.getGenerationNumber() + " generations");
			generationsLabel.setPreferredSize(new Dimension((int)dim.getWidth(),(int)dim.getHeight()/20));
			this.add(generationsLabel);
			
			JLabel informationLabel = new JLabel("Lower dashed line is 50% adaptation, upper is 100% adaptation");
			informationLabel.setPreferredSize(new Dimension((int)dim.getWidth(),(int)dim.getHeight()/20));
			this.add(informationLabel);
			
			this.setPreferredSize(new Dimension((int)dim.getWidth(), (int)dim.getHeight()/5));
			
		}
		public void paintComponent(Graphics g)
		{
			
		}
	}
	private class DownInformation extends JPanel
	{
		public DownInformation(Statistics s)
		{
			Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
			this.add(new JLabel("Sequence of adaptations is "));
			for(Double d : s.adaptations)
			{		
				DecimalFormat myFormatter = new DecimalFormat("#.###");
				String output = myFormatter.format(d);
				this.add(new JLabel(output));
			}
			this.setPreferredSize(new Dimension((int)dim.getWidth(), (int)dim.getHeight()/10));
		}
		public void paintComponent(Graphics g)
		{
			
		}
	}
}