package pl.edu.pw.elka.pszt;
import java.awt.Toolkit;
import java.util.LinkedList;

import javax.swing.JFrame;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;

/**
 * Main app class
 * 
 * @author Kajo
 */
public class Main {

	/**
	 * @param args No arguments
	 * @throws Exception // Don't ask :P 
	 */
	public static void main(String[] args) throws Exception // Don't ask :P
	{
		int[][] trianglePoints = { {0, 6}, {3, 9}, {3, 6} };
		int[][] bigSquarePoints = { {0, 6}, {3, 6}, {3, 3}, {0, 3} };
		int[][] smallSquarePoints = { {1, 3}, {3, 3}, {3, 1}, {1, 1} };
		
		Segment triangleSegment = new Segment(trianglePoints);
		Segment bigSquareSegment = new Segment(bigSquarePoints);
		Segment smallSquareSegment = new Segment(smallSquarePoints);

		MultiSegment multiSegment = new MultiSegment();
		multiSegment.addSegment(triangleSegment);
		multiSegment.addSegment(bigSquareSegment);
		multiSegment.addSegment(smallSquareSegment);

		Population p = new Population(multiSegment);
		System.out.println(p);
		
	//	p.copulateEntities();
	}

	/**
	 * Nothing special, just for test library linking
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
