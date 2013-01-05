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
	 * main function that tries to solve tangram
	 * @param args name of file, that contains tangram to make            
	 */
	public static void main(String[] args)
	{	
		Reader reader = new Reader();
		MultiSegment multiSegment;
		double condition;
		if (args.length == 1) 
		{
			multiSegment = reader.read(args[0]);
			condition = 0.99;
		}
		else if (args.length == 2)
		{
			multiSegment = reader.read(args[0]);
			condition = Double.parseDouble(args[1]);
		}
		else
		{
			multiSegment = reader.read("generation1");
			condition = 0.99;
		}
			
        new Painter(new Entity(multiSegment), new Entity(multiSegment), "Target Tangram");
        
        System.out.println("Generating first population");
        Population p = new Population(multiSegment);
        System.out.println("First population generated");
		
		Vector<Painter> painters = new Vector<Painter>();
		Statistics statistics = new Statistics(p);
		double[] worst = new double[1];
		int generationNumber = 0;
		while(true)
		{
			generationNumber++;
			long startTime2 = System.currentTimeMillis();
			Entity adapt = p.nextGeneration(worst);
			
			System.out.println("Generation " + p.getGenerationNumber());
			System.out.println("Generation created in " + (System.currentTimeMillis() - startTime2) + " ms");
			
			painters.add(new Painter(adapt, p));
			if(painters.size() >= 2) painters.get(painters.size() -2).dispose();
			
			System.out.println("Best entity adaptation = " + ((int)(adapt.getAdaptationSize()*10000))*0.01 + " %");
			System.out.println("Worst entity adaptation = " + ((int)(worst[0]*10000))*0.01 + " %");
			statistics.addBestAdaptation(adapt.getAdaptationSize());
			statistics.addWorstAdaptation(worst[0]);
			

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
}

	