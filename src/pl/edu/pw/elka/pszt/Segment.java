package pl.edu.pw.elka.pszt;

import java.util.ArrayList;
import java.util.Random;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;

/**
 * Representation of one tangram segment polygon with id identifying it
 * 
 * @author Marcin Kubik
 * @author Mikołaj Markiewicz
 * @author Krzysztof Opasiak
 */
public class Segment {

	/** ID iterator */
	private static int i = 0;

	/** Array of vertices */
	private ArrayList<Vertex> vertices = new ArrayList<Vertex>();

	/** Polygon created from coordinates */
	private Polygon polygon;

	/** Unique segment ID */
	private int id = i++;

	/**
	 * C-tor,
	 * creates vertex representation from coordinate int pairs 
	 * 
	 * @param vertices array of array with pairs x and y as coordinate
	 * @throws Exception if not enough coordinates
	 */
	public Segment(final double[][] coordinates) throws Exception
	{
		if (coordinates.length < 3)
			throw new Exception("Not enough coordinates");

		for (double[] coord : coordinates)
			this.vertices.add(new Vertex(coord[0], coord[1]));

		this.createPolygon();
	}

	/**
	 * C-tor
	 * 
	 * @param vertices array of coordinates
	 * @throws Exception if not enough coordinates
	 */
	public Segment(final Coordinate[] vertices) throws Exception
	{
		if (vertices.length < 3)
			throw new Exception("Not enough coordinates");
		
		for (Coordinate vertex : vertices)
			this.vertices.add(new Vertex(vertex));

		this.createPolygon();
	}

	/**
	 * C-tor
	 * 
	 * @param vertices ArrayList of vertices
	 * @throws Exception if not enough vertices
	 */
	public Segment(final ArrayList<Vertex> vertices) throws Exception
	{
		if (vertices.size() < 3)
			throw new Exception("Not enough coordinates");

		for (Vertex vertex : vertices)
			this.vertices.add(new Vertex(vertex.getCoordinate(), vertex.getId()));

		this.createPolygon();
	}

	/**
	 * Create polygon from set vertices
	 */
	private void createPolygon()
	{
		GeometryFactory geometryFactory = new GeometryFactory();
		LinearRing holes[] = null;

		Coordinate[] coordinates = new Coordinate[this.vertices.size() + 1];

		int i = 0;
		for (; i < this.vertices.size(); ++i)
			coordinates[i] = this.vertices.get(i).getCoordinate();
		
		coordinates[i] = this.vertices.get(0).getCoordinate();

		LinearRing polygonRing = geometryFactory.createLinearRing(coordinates);

		this.polygon = geometryFactory.createPolygon(polygonRing, holes);
	}

	/**
	 * Move segment by given values
	 * 
	 * @param x Horizontal movement
	 * @param y Vertical movement
	 */
	public Segment moveBy(double x, double y)
	{
		for (Vertex vertex : this.vertices)
		{
			vertex.setX(vertex.getX() + x);
			vertex.setY(vertex.getY() + y);
		}

		this.createPolygon();

		return this;
	}

	/**
	 * Move segment by attached by vertex to from vertex
	 * 
	 * @param to Vertex to reach
	 * @param by Vertex reaching
	 */
	public void moveToVertexByVertex(final Vertex to, final Vertex by)
	{
		this.moveBy(to.getX() - by.getX(), to.getY() - by.getY());
	}

	/**
	 * Get minimum X coordinate
	 * 
	 * @return minimum X coordinate from segment
	 */
	public double getMinX()
	{
		double minX = 999999999;
		
		for (Vertex vertex : this.vertices)
			if (minX > vertex.getX())
				minX = vertex.getX();

		return minX;
	}
	
	/**
	 * Get minimum Y coordinate
	 * 
	 * @return minimum Y coordinate from segment
	 */
	public double getMinY()
	{
		double minY = 999999999;
		
		for (Vertex vertex : this.vertices)
			if (minY > vertex.getY())
				minY = vertex.getY();
	
		return minY;
	}



	/**
	 * Clone this segment, by making union with itself
	 * Copy id from old one to new one
	 * @return Cloned segment with same id
	 */
	public Segment clone()
	{
		try
		{
			Segment segment = new Segment(this.getVertices());
			segment.setId(this.getId());
			return segment;
		}
		catch (Exception e)
		{
			System.err.println("Segment cloning exception.");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Get vertex instance by given id from vertices list
	 * 
	 * @param id Vertex id
	 * @return Vertex instance if found equal id
	 * @throws Exception if no id found
	 */
	public Vertex getVertexById(final int id)
	{
		for (Vertex v : this.vertices)
			if (id == v.getId())
				return v;

		return null;
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
	 * Get vertices list from segment
	 * 
	 * @return Vertices list (Without duplicated closing one vertex)
	 */
	public ArrayList<Vertex> getVertices()
	{
		return this.vertices;
	}

	/**
	 * Get segment polygon
	 * 
	 * @return Segment polygonto
	 */
	public Polygon getPolygon()
	{
		return this.polygon;
	}

	/**
	 * Get id of segment
	 * 
	 * @return ID of segment
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Set id
	 * 
	 * @param id New Id
	 */
	private void setId(int id)
	{
		this.id = id;
	}

	/**
	 * Get segment information
	 * 
	 *  @return segment ID and polygon contained information about coordinates
	 */
	@Override
	public String toString()
	{
		return this.id + " : " + this.polygon.toString();
	}

}
