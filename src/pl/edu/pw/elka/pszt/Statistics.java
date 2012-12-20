package pl.edu.pw.elka.pszt;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.vividsolutions.jts.geom.Coordinate;

@SuppressWarnings("serial")
public class Statistics extends JFrame{

	Vector<Double> adaptations;
	
	public Statistics()
	{
		super("Statistics");
		//this.setVisible(true);
		adaptations = new Vector<Double>();
	}
	
	public void addAdaptation(double a)
	{
		adaptations.add(a);
	}
	
	public void showResults(int s) 
	{
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int scale = (int)dim.getWidth()/this.adaptations.size();
		Vector<Coordinate> coordinates = new Vector<Coordinate>();
		for(int i = 0; i<this.adaptations.size(); ++i)
		{
			coordinates.add(new Coordinate(scale*i,dim.getHeight()*this.adaptations.get(i)));
		}
		
		this.setVisible(true);
		this.setBounds(0,0,(int)dim.getWidth(),(int)dim.getHeight());
		paintGraph(coordinates);
		
	}
	public void paintGraph(final Vector<Coordinate> adapt)
	{
		this.add(new JPanel()
		{
			public void paintComponent(Graphics g)
			{
				Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
				Graphics2D g2 = (Graphics2D) g;
				for(int i = 0; i<adapt.size()-1; ++i)
				{
					g2.drawLine((int)adapt.get(i).x, (int)(dim.getHeight() - adapt.get(i).y),
								(int)adapt.get(i+1).x, (int)(dim.getHeight() - adapt.get(i+1).y));
				}
			}
			
		});
	}
}
