package pl.edu.pw.elka.pszt;

import java.util.ArrayList;

import com.vividsolutions.jts.geom.Polygon;

/**
 * Represents entity made of connectors and segments as well
 * 
 * @author Kajo
 */
public class Entity {

	/** List of connectors */
	private ArrayList<Connector> connectors = new ArrayList<Connector>();

	public void addConnector(final Connector connector)
	{
		this.connectors.add(connector);
	}

	public Entity copulateWith(final Entity other)
	{
		// TODO make them love each other
		return null;
	}

	public void mutateEntity()
	{
		// TODO mutation algorithm 
		// Access to necessary segments inside conectors 
	}

	public double getAdaptationSize()
	{
		Segment target = this.connectors.get(0).getTargetSegment().getAlignedClone();

		// Get mins
		int minX = this.connectors.get(0).getSegment().getMinX();
		int minY = this.connectors.get(0).getSegment().getMinY();

		for (int i = 0; i < this.connectors.size(); ++i)
		{
			int minXt = this.connectors.get(i).getSegment().getMinX();
			if (minX > minXt)
				minX = minXt;

			int minYt = this.connectors.get(i).getSegment().getMinY();
			if (minY > minYt)
				minY = minYt;
		}

		double sum = 0;
		for (Connector connector : this.connectors)
		{
			Segment s = connector.getSegment().clone();
			s.moveBy(-minX, -minY);
			System.out.println(minX + " + " + minY);

			Polygon p = null;
			try
			{
				p = (Polygon) target.getPolygon().intersection(s.getPolygon());
			}
			catch (Exception e) {}

			if (p != null)
				sum += p.getArea();
		}
		
		return sum / target.getPolygon().getArea();
	}

	public String toString()
	{
		StringBuilder str = new StringBuilder();

		str.append("[ ");

		for (Connector connector : this.connectors)
			str.append(connector.getSegment() + ", ");

		str.append(" ]");

		return str.toString();
	}

}
