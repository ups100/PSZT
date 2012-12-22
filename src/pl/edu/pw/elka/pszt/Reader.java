package pl.edu.pw.elka.pszt;


import java.io.File;
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

	
	public Reader()
	{
		
	}
	
	public MultiSegment read(String s)
	{
		MultiSegment multiSegment = new MultiSegment();
		
		Scanner in = null;
		try 
		{
			 in = new Scanner(new File("src" + System.getProperty("file.separator") + s));
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
		catch (Exception e) 
		{
			System.err.println("OPERACJA NIEDOZWOLONA");
			e.printStackTrace();
			return null;
		}
		finally 
		{
			in.close();
		}
		return multiSegment;
	}

}
