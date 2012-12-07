package pl.edu.pw.elka.pszt;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;

public class Main {

	/**
	 * @param args No arguments
	 */
	public static void main(String[] args)
	{
	}

	/**
	 * Nothing special, just for test linking
	 */
	public void testFunction()
	{
		Coordinate[] traingleCoords = new Coordinate[] {	new Coordinate(0,  0),
															new Coordinate(0,  40),
															new Coordinate(40, 40),
															new Coordinate(0,   0) };
		Coordinate[] squareCoords = new Coordinate[] {	new Coordinate(0,  0),
														new Coordinate(0,  20),
														new Coordinate(20, 20),
														new Coordinate(20, 0),
														new Coordinate(0,  0) };

		GeometryFactory geometryFactory = new GeometryFactory();

		LinearRing trianleRing = geometryFactory.createLinearRing(traingleCoords);
		LinearRing squareRing = geometryFactory.createLinearRing(squareCoords);
		LinearRing holes[] = null; // advise if any i think, 'use LinearRing[] to represent holes'

		Polygon triangle = geometryFactory.createPolygon(trianleRing, holes);
		Polygon square = geometryFactory.createPolygon(squareRing, holes);

		Polygon intersected = (Polygon) triangle.intersection(square);

		System.out.println("Triangle area:	" + triangle.getArea());
		System.out.println("Square area:	" + square.getArea());
		System.out.println("Intersection:	" + intersected.getArea());
		System.out.println("Sum:		" + triangle.union(square).getArea());
	}

}
