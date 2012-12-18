package pl.edu.pw.elka.pszt;



import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;

/**
 * Main app class
 * 
 * @author Kajo
 */
public class Main {

	/**
	 * @param args
	 *            No arguments
	 * @throws Exception
	 *             // Don't ask :P
	 */
	public static void main(String[] args) throws Exception // Don't ask :P
	{
		double[][] trianglePoints = { { 0, 6 }, { 3, 9 }, { 3, 6 } };
		double[][] bigSquarePoints = { { 0, 6 }, { 3, 6 }, { 3, 3 }, { 0, 3 } };
		double[][] smallSquarePoints = { { 1, 3 }, { 3, 3 }, { 3, 1 }, { 1, 1 } };
		double[][] smallSquarePoints1 = { { 1, -3 }, { 3, -3 }, { 3, 1 }, { 1, 1 } };
		double[][] smallSquarePoints2 = { { 1, -3 }, { 3, -3 }, { 3, -5 }, { 1, -5 } };
		double[][] smallSquarePoints3 = { { -1, -7 }, { 4, -8 }, { 3, -5 }, { 1, -5 } };

		Segment triangleSegment = new Segment(trianglePoints);
		Segment bigSquareSegment = new Segment(bigSquarePoints);
		Segment smallSquareSegment = new Segment(smallSquarePoints);

		MultiSegment multiSegment = new MultiSegment();
		multiSegment.addSegment(triangleSegment);
		multiSegment.addSegment(bigSquareSegment);
		multiSegment.addSegment(smallSquareSegment);
		multiSegment.addSegment(new Segment(smallSquarePoints1));
		multiSegment.addSegment(new Segment(smallSquarePoints2));
		multiSegment.addSegment(new Segment(smallSquarePoints3));

		new Painter(new Entity(multiSegment));
		Population p = new Population(multiSegment);
		
		
		int a = 0;
		double best = 0;
		double last = 0;
		while(true)
		{
			a++;
			Entity adapt = p.nextGeneration();
			System.out.println(adapt.getAdaptationSize());

			last = adapt.getAdaptationSize();
			if (last > best) {
				best = last;
				a = 0;
			}
			
			if (a > 500) {
				System.out.println(new Entity(multiSegment));
				System.out.println(p);
				new Painter(adapt);
				a = 0;
			}

			if (adapt.getAdaptationSize() > 0.99)
			{
				System.out.println(adapt);
				new Painter(adapt);
				System.out.println("Adaptacja " + adapt.getAdaptationSize() + " w " + p.getGenerationNumber() + " generacji");
				break;
			}
		}
	}

	/**
	 * Nothing special, just for test library linking
	 */
	public void testFunction() {
		Coordinate[] traingleCoords = new Coordinate[] { new Coordinate(0, 0),
				new Coordinate(0, 40), new Coordinate(40, 40),
				new Coordinate(0, 0) };
		Coordinate[] squareCoords = new Coordinate[] { new Coordinate(0, 0),
				new Coordinate(0, 20), new Coordinate(20, 20),
				new Coordinate(20, 0), new Coordinate(0, 0) };

		GeometryFactory geometryFactory = new GeometryFactory();

		LinearRing trianleRing = geometryFactory
				.createLinearRing(traingleCoords);
		LinearRing squareRing = geometryFactory.createLinearRing(squareCoords);
		LinearRing holes[] = null; // advise if any i think, 'use LinearRing[]
									// to represent holes'

		Polygon triangle = geometryFactory.createPolygon(trianleRing, holes);
		Polygon square = geometryFactory.createPolygon(squareRing, holes);

		Polygon intersected = (Polygon) triangle.intersection(square);

		System.out.println("Triangle area:	" + triangle.getArea());
		System.out.println("Square area:	" + square.getArea());
		System.out.println("Intersection:	" + intersected.getArea());
		System.out.println("Sum:		" + triangle.union(square).getArea());
	}

}
