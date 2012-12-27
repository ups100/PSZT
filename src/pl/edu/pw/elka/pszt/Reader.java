package pl.edu.pw.elka.pszt;


import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Vector;

/**
 * Class for reading segments vertices from file
 * 
 * @author Marcin Kubik
 * @author Mikołaj Markiewicz
 * @author Krzysztof Opasiak
 */
public class Reader {

	/**
	 * c-tor 
	 * As long as class has no variables, nothing should be done in c-tor
	 */
	public Reader()
	{
		
	}
	
	/**
	 * Gets the informations about the tangram from a file
	 * @param s Name of the filename
	 * @return Created multi segment
	 */
	public MultiSegment read(String s)
	{
		MultiSegment multiSegment = new MultiSegment();
		
		Scanner in = null;
		try 
		{		 
			 in = new Scanner(this.getClass().getResourceAsStream(s+".txt"));			 
			 Vector<Double> XCoords = new Vector<Double>();
			 Vector<Double> YCoords = new Vector<Double>();
			
			 while(in.hasNext())
			 {
				 
				 if(in.hasNextInt())
				 {
					 XCoords.add((double)in.nextInt());
					 if((((String)in.next()).equals(",")) && (in.hasNextInt()))
						 YCoords.add((double)in.nextInt());

				 }
				 else
				 {
					if(XCoords.size() != YCoords.size()) throw new Exception("Syntax of file is wrong");
					double[][] coordinates = new double[XCoords.size()][2];
					for(int i = 0; i < XCoords.size(); ++i)
					{
						coordinates[i][0] = XCoords.get(i);
						coordinates[i][1] = YCoords.get(i);
					}
					multiSegment.addSegment(new Segment(coordinates));					
					XCoords.clear();
					YCoords.clear();
					if(in.hasNext())
						in.next();
					else break;
				 }			 
			 }
		} 
		catch (FileNotFoundException e) 
		{
			System.err.println("Something went wrong");
			e.printStackTrace();
			return null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally 
		{
			in.close();
		}
		return multiSegment;
	}

}
