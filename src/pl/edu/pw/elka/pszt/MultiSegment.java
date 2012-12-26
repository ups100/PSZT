package pl.edu.pw.elka.pszt;

import java.util.ArrayList;
import java.util.Random;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;

/**
 * Class containing representation of app model such as segments and target segment made of small tangram segments
 * 
 * @author Marcin Kubik
 * @author Mikołaj Markiewicz
 * @author Krzysztof Opasiak
 */
public class MultiSegment {

	/** Union of the tangram segments = target segment */
	private MultiPolygon polygon = null;

	/** Area of polygon made of other polygons */
	private double coveredArea = 0;

	/** Area of polygon which overlaps on itself */
	private double overlapArea = 0;

	/** List of vertices making each segment */
	private ArrayList<Vertex> vertices = new ArrayList<Vertex>();

	/** List of tangram segments building big one target segment */
	private ArrayList<Segment> segments = new ArrayList<Segment>();

	/**
	 * Adds segment to list of tangram segments building target multi polygon
	 * Gather vertices information
	 * 
	 * @param segment Segment to add
	 */
	public void addSegment(final Segment segment)
	{
		this.segments.add(segment);
		this.vertices.addAll(segment.getVertices()); // TODO without duplicates

		this.createPolygon();
	}

	/**
	 * Create multi polygon from segments inside
	 */
	private void createPolygon()
	{
		Polygon[] polygons = new Polygon[this.segments.size()];
		
		for (int i = 0; i < this.segments.size(); ++i)
			polygons[i] = this.segments.get(i).getPolygon();

		this.polygon = new MultiPolygon(polygons, new GeometryFactory());

		this.calculateArea();
	}

	/**
	 * Calculates area of multipolygon (minus intersection things - only cover area)
	 */
	private void calculateArea()
	{
		double sum = 0;
		double minus = 0;
		for (int i = 0; i < this.segments.size(); ++i)
		{
			sum += this.segments.get(i).getPolygon().getArea();

			for (int j = i + 1; j < this.segments.size(); ++j)
			{
				Geometry g = this.segments.get(i).getPolygon().intersection(this.segments.get(j).getPolygon());

				minus += g.getArea();
			}
		}

		this.coveredArea = sum - minus;
		this.overlapArea = minus;
	}
	

	/**
	 * Get random vertex from segment
	 * 
	 * @return Randomly chosen vertex
	 */
	public Vertex getRandomVertex()
	{
		return this.vertices.get(new Random().nextInt(this.vertices.size()));
	}

	/**
	 * Align vertices to top left corner by removing minimum of X and Y coordinate
	 * 
	 * @return Aligned clone of segment
	 */
	public MultiSegment getAlignedMultiSegment()
	{
		double minX = this.segments.get(0).getMinX();
		double minY = this.segments.get(0).getMinY();

		for (int i = 1; i < this.segments.size(); ++i)
		{
			double minXt = this.segments.get(i).getMinX();
			if (minX > minXt)
				minX = minXt;

			double minYt = this.segments.get(i).getMinY();
			if (minY > minYt)
				minY = minYt;
		}

		MultiSegment ms = new MultiSegment();

		for (Segment segment : this.segments)
			ms.addSegment(segment.clone().moveBy(-minX, -minY));

		return ms;
	}

	/**
	 * Calculate intersection area of this and given one
	 * @param other Multi segment to check with
	 * @return intersection area
	 * @return array of sum of intersection source with target, source overlapping area, target covering area
	 */
	public double[] calculateIntersectionArea(final MultiSegment other)
	{
		MultiSegment target = this.getAlignedMultiSegment();
		MultiSegment source = other.getAlignedMultiSegment();

		ArrayList<Segment> targetSegments = target.getSegments();
		ArrayList<Segment> sourceSegments = source.getSegments();

		double sumOfIntersection = 0;
		for (int i = 0; i < sourceSegments.size(); ++i)
			for (int j = 0; j < targetSegments.size(); ++j)
			{
				Geometry g = sourceSegments.get(i).getPolygon().intersection(targetSegments.get(j).getPolygon());

				sumOfIntersection += g.getArea();
			}

		return new double[] {sumOfIntersection, source.getOverlapArea(), target.getCoveredArea()};
	}

	/**
	 * Get list of segments
	 * 
	 * @return List of segments building target tangram element
	 */
	public ArrayList<Segment> getSegments()
	{
		return this.segments;
	}

	/**
	 * Get list of vertices
	 * 
	 * @return List of vertices in all segments
	 */
	public ArrayList<Vertex> getVertices()
	{
		return this.vertices;
	}

	/**
	 * Get target multi polygon
	 * 
	 * @return Target polygon
	 */
	public MultiPolygon getPolygon()
	{
		return this.polygon;
	}

	/**
	 * Get multi polygon covered area
	 * 
	 * @return Multi polygon covered area
	 */
	public double getCoveredArea()
	{
		return this.coveredArea;
	}

	/**
	 * Get overlapping area
	 * @return
	 */
	public double getOverlapArea()
	{
		return this.overlapArea;
	}

	/**
	 * Getter for the Min X
	 * @return Min X
	 */
	public double getMinX()
	{
		double bestX = 99999;
		for(Vertex v : this.vertices)
		{
			if(v.getX() < bestX)
				bestX = v.getX();
		}
		return bestX;
	}
	
	/**
	 * Getter for the Min Y
	 * @return Min Y
	 */
	public double getMinY()
	{
		double bestY = 9999;
		for(Vertex v : this.vertices)
		{
			if(v.getY() < bestY)
				bestY = v.getY();
		}
		return bestY;
	}
	
	/**
	 * Getter for the Max X
	 * @return Max X
	 */
	public double getMaxX()
	{
		double bestX = -9999;
		for(Vertex v : this.vertices)
		{
			if(v.getX() > bestX)
				bestX = v.getX();
		}
		return bestX;
	}
	
	/**
	 * Getter for the Max Y
	 * @return Max Y
	 */
	public double getMaxY()
	{
		double bestY = -9999;
		for(Vertex v : this.vertices)
		{
			if(v.getY() > bestY)
				bestY = v.getY();
		}
		return bestY;
	}
	
	/**
	 * Get information about segments building target segment
	 * 
	 * @return Multi Polygon info
	 */
	public String toString()
	{
		if (this.polygon != null)
			return this.polygon.toString();
		else
			return "No polygon attached";
	}

}
