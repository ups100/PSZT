package pl.edu.pw.elka.pszt;



import java.util.Calendar;
import java.util.Vector;

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
		
		/*double[][] trianglePoints = { { 0, 6 }, { 3, 9 }, { 3, 6 } };
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
		multiSegment.addSegment(new Segment(smallSquarePoints3));*/
		
		MultiSegment multiSegment = new MultiSegment();
		 multiSegment.addSegment(new Segment(new double[][] {{77,1},{102,40},{61,67},{35,24}}));
         multiSegment.addSegment(new Segment(new double[][] {{8,66},{154,67},{105,163}}));
         multiSegment.addSegment(new Segment(new double[][] {{154,67},{201,116},{201,163},{154,116}}));
         multiSegment.addSegment(new Segment(new double[][] {{49,107},{144,205},{49,205}}));
         multiSegment.addSegment(new Segment(new double[][] {{131,191},{180,191},{179,239}}));
         multiSegment.addSegment(new Segment(new double[][] {{0,211,},{49,163},{48,258}}));
         multiSegment.addSegment(new Segment(new double[][] {{49,241},{82,273},{49,308}}));
         new Painter(new Entity(multiSegment), new Entity(multiSegment), "Target Tangram");
         Population p = new Population(multiSegment);
		
		Vector<Painter> painters = new Vector<Painter>();
		Statistics statistics = new Statistics(p);
		double condition = 0.99;
		int generationNumber = 0;
		while(true)
		{
			generationNumber++;			
			Entity adapt = p.nextGeneration();
			painters.add(new Painter(adapt, p));
			if(painters.size() >= 2) painters.get(painters.size() -2).dispose();
			System.out.println("Generation " + p.getGenerationNumber());
			System.out.println(adapt);
			statistics.addAdaptation(adapt.getAdaptationSize());

			if (adapt.getAdaptationSize() > condition)
			{
				System.out.println("Adaptation reached in " + p.getGenerationNumber() + "th" +  " generation");
				statistics.showResults(p);
				break;
			}
			
			if (generationNumber > 100)
			{
				generationNumber = 0;
				condition -= 0.01;
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
