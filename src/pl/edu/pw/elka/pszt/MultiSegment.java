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
 * @author Kajo
 */
public class MultiSegment {

	/** Union of the tangram segments = target segment */
	private MultiPolygon polygon = null;

	/** Area of polygon made of other poygons */
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
	 * Get segment instance by given id from segment list
	 * 
	 * @param id Segment id
	 * @return Segment instance if found equal id
	 * @throws Exception if no id found
	 */
	public Segment getSegmentById(final int id) throws Exception
	{
		for (Segment s : this.segments)
			if (id == s.getId())
				return s;

		throw new Exception("No segment found in model");
	}

	/**
	 * Get random vertex from segment
	 * 
	 * @return Randomly chosen vertex
	 */
	public Vertex getRandomVertex()
	{
		// TODO maybe put 'except' those numbers or sth like that, depends of algorithm
		return this.vertices.get(new Random().nextInt(this.vertices.size()));
	}

	/**
	 * Align vertices to top left corner by removing minimum of X and Y coordinate
	 * 
	 * @return Aligned clone of segment
	 */
	public MultiSegment getAlignedMultiSegment()
	{
		int minX = this.segments.get(0).getMinX();
		int minY = this.segments.get(0).getMinY();

		for (int i = 0; i < this.segments.size(); ++i)
		{
			int minXt = this.segments.get(i).getMinX();
			if (minX > minXt)
				minX = minXt;

			int minYt = this.segments.get(i).getMinY();
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
	 * 
	 * @param other Multi segment to check with
//	 * @return intersection area
	 * @return array of sum of intersection source with target, source overlapping area, target covering area
	 */
	public double[] calculateIntersectionArea(MultiSegment other)
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

////		If wanted to show what with what is intersecting
//		System.out.println(target);
//		System.out.println(source);

//		return (sumOfIntersection - source.getOverlapArea()) / target.getCoveredArea();
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
	 * Get area which is sum of segments areas
	 * 
	 * @return Sum of segments areas
	 */
	public double getSumOfSegmentsArea()
	{
		return this.coveredArea + this.overlapArea;
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
