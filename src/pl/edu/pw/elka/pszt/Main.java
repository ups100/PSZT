package pl.edu.pw.elka.pszt;



import java.util.Vector;


/**
 * Main app class
 * 
 * @author Marcin Kubik
 * @author Mikołaj Markiewicz
 * @author Krzysztof Opasiak
 */
public class Main {

	/**
	 * @brief main function that tries to solve tangram
	 * @param args name of file, that contains tangram to make            
	 */
	public static void main(String[] args)
	{	
		Reader reader = new Reader();
		MultiSegment multiSegment;
		if(args.length < 1) 
			multiSegment = reader.read("generation1.txt");
		else
			multiSegment = reader.read(args[0]);
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
			
			if (generationNumber > 10)
			{
				generationNumber = 0;
				condition -= 0.01;
			}
		}
	}
}

	