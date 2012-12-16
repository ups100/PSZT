package pl.edu.pw.elka.pszt;

import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

/**
 * Represents population -> some entities
 * 
 * @author Kajo
 */
public class Population {

	/** Amount of first population at least 2! */
	public static final int FIRST_AMOUNT = 5;
	public static final int CHILDREN = 15;
	/** List of entities */
	private ArrayList<Entity> entities = new ArrayList<Entity>();

	/** To know, how many loops we need to do or sth */
	private int numberOfEntities;

	/** Target segment instance */
	private final MultiSegment targetSegment;

	/** Number of generation */
	private int generationNumber = 0;

	/**
	 * C-tor, creates first population entities
	 * 
	 * @param targetSegment
	 *            Multi segment with target and all segments building target
	 *            inside
	 */
	public Population(final MultiSegment targetSegment) {
		this.targetSegment = targetSegment;

		for (int i = 0; i < FIRST_AMOUNT; ++i)
			this.entities.add(createRandomEntity());

		this.numberOfEntities = this.entities.size();
	}

	/**
	 * Create random generated entity, useful especially for first generation
	 * 
	 * @return Generated entity
	 */
	private Entity createRandomEntity() {
		ArrayList<Segment> segments = this.targetSegment.getSegments();
		Entity entity = new Entity(this.generationNumber);

		for (Segment segment : segments) {
			// TODO check random algorithm, except some already chosen vertices
			// 
			Connector connector = new Connector(segment, this.targetSegment);
<<<<<<< HEAD
=======

>>>>>>> c257d910922c3a729b61a0f3fa47ab3400876e1e
			entity.addConnector(connector);
		}
		
		return entity;
	}

	/**
	 * Make next generation by coitus, mutation and natural selection
	 */
	public boolean nextGeneration() {
		
		this.generationNumber++;
		ArrayList<Entity> evolvingEntities = this.getEntities();
		
		/** 
		 * Maybe some other variable will be useful but by now
		 * I will just set the result of copulation to the existing
		 * evolvingEntities variable
		 */
		evolvingEntities = this.copulateEntities(evolvingEntities);
		
		/**
		 * Mutation
		 */
		
		this.mutateEvolvingPopulation(evolvingEntities);
<<<<<<< HEAD
		
	
		
		this.entities = this.getTheBestOnes(evolvingEntities);
		
		/**
		 * This update is unnecessary 
		 * The number of Entities should be const
		 */
		this.numberOfEntities = this.entities.size();
		
		
=======
		System.out.println("TE NOWE TO");
		for (Entity e : evolvingEntities)
		{
			System.out.println(e);
		}
		
		this.entities = this.getTheBestOnes(evolvingEntities);
>>>>>>> c257d910922c3a729b61a0f3fa47ab3400876e1e
		System.out.println("PO " + this.generationNumber+" GENERACJI TO");
		for (Entity e : this.entities)
		{
			System.out.println(e);
		}
		
<<<<<<< HEAD
		
		
=======
>>>>>>> c257d910922c3a729b61a0f3fa47ab3400876e1e
		for(Entity e : this.entities)
		{
			if(e.getAdaptationSize() > 0.9) 
			{
				System.out.println("KONIEC, ZNALEŹLIŚMY");
				return true;
			}
				
		}
		return false;	
	}

	private ArrayList<Entity> getTheBestOnes(ArrayList<Entity> evolvingEntities) 
	{
<<<<<<< HEAD
		
		this.entities = this.removeDouble(this.entities);
		
		for (int i = 0; i<this.entities.size();++i)
=======
		for (int i = 0; i<numberOfEntities;++i)
>>>>>>> c257d910922c3a729b61a0f3fa47ab3400876e1e
		{
			evolvingEntities.add(this.entities.get(i));
		}
		
<<<<<<< HEAD
		
		
		/** 
		 * Used for tests, both children and parents
		 */		
		
		
		ArrayList<Entity> newGeneration = new ArrayList<Entity>();
		Vector<Integer> actuallyAdded = new Vector<Integer>();
		double maxAdaptation = -2;
		int maxIndex;
		System.out.println("numberOfEntities " + numberOfEntities);
		System.out.println("evolvingEntities.size() " + evolvingEntities.size());
		for (int i = 0; i<numberOfEntities;++i)
		{		
			maxAdaptation = -2;
			maxIndex = -1;
			for(int j = 0;j< evolvingEntities.size();++j)
			{			
				if (actuallyAdded.contains(j)) continue;
				if(evolvingEntities.get(j).getAdaptationSize() > maxAdaptation)
				{					
					maxAdaptation = evolvingEntities.get(j).getAdaptationSize();
					maxIndex = j;
				}
				
=======
		/** 
		 * Used for tests
		 */
		System.out.println("A NAJNOWSZE TO");
		for (Entity e : evolvingEntities)
		{
			System.out.println(e);
		}
		
		ArrayList<Entity> newGeneration = new ArrayList<Entity>();
		Vector<Integer> actuallyAdded = new Vector<>();
		double maxAdaptation = -2;
		int maxIndex;
		for (int i = 0; i<numberOfEntities;++i)
		{
			maxAdaptation = -2;
			maxIndex = -1;
			for(int j = 0;j< evolvingEntities.size();++j)
			{
				
				if (actuallyAdded.contains(j)) continue;
				if(evolvingEntities.get(j).getAdaptationSize() > maxAdaptation)
				{
					maxAdaptation = evolvingEntities.get(j).getAdaptationSize();
					maxIndex = j;						
				}
>>>>>>> c257d910922c3a729b61a0f3fa47ab3400876e1e
			}
			actuallyAdded.add(maxIndex);
			newGeneration.add(evolvingEntities.get(maxIndex));
		}
<<<<<<< HEAD
		
=======
>>>>>>> c257d910922c3a729b61a0f3fa47ab3400876e1e
		return newGeneration;
	}

	private void mutateEvolvingPopulation(ArrayList<Entity> evolvingEntities) 
	{
		for (Entity e: evolvingEntities)
		{
			e.mutateEntity();
		}		
	}

	/**
	 * Append new entities to population There is a need to change from public
	 * to private (I guess so)
	 */
	public ArrayList<Entity> copulateEntities(ArrayList<Entity> evolvingEntities) {
		/** Size of afterCopulationList is size of evolvingEntities ^ 2 
		 * I don't know if it is right (or especially fast) but it is very hippie-style
		 * everyone with everyone
		 * It can be easily changed
		 * */
		ArrayList<Entity> afterCopulationList = new ArrayList<Entity>();
		for(int i = 0; i < evolvingEntities.size(); ++i)
		{
			for (int j = 0; j < evolvingEntities.size(); ++j)
			{
				if (i == j) continue; //no self-love!
				afterCopulationList.add(new Entity(evolvingEntities.get(i).copulateWith(evolvingEntities.get(j))));
			}
		}
		return afterCopulationList;
	}
	


	/** Get exact amount of entities that will evolve */
	private ArrayList<Entity> getEntities() 
	{
		Random generator = new Random();
		
		/** 
		 * Number is random now, but it can be easily changed 
		 */
<<<<<<< HEAD
		int theChosenOnes = 0;
		while(theChosenOnes<4)
		theChosenOnes = generator.nextInt(this.numberOfEntities-1) + 1;
=======
		int theChosenOnes = generator.nextInt(this.numberOfEntities) + 1;
>>>>>>> c257d910922c3a729b61a0f3fa47ab3400876e1e
		Vector<Integer> previousNumbers = new Vector<>();
		ArrayList<Entity> list = new ArrayList<Entity>();
		int i = 0;
		while (i != theChosenOnes)
		{
			Integer tmp = generator.nextInt(numberOfEntities);
			if(!previousNumbers.contains(tmp))
			{			
				previousNumbers.add(tmp);
				/**
				 * Just getting reference to parents
				 * instead of cloning them
				 */
				//list.add(new Entity(this.entities.get(tmp)));	
				list.add(this.entities.get(tmp));
				i++;
			}
					
		}
		/** Just for tests whether the copy of entities is shallow or deep */
		//this.entities.clear();
		return list;
	}


<<<<<<< HEAD
	private ArrayList<Entity> removeDouble(ArrayList<Entity> list)
	{
		for (int i = 0; i<list.size()-1; ++i)
		{
			for (int j = i+1; j<list.size(); ++j)
			{
				if (list.get(i).compare(list.get(j)))
				{
					list.remove(j);
				}
			}
		}
		return list;
	}
	
=======

	/**
	 * Select more adapted entites
	 */
	private void selectEntites() {
		// TODO sometime remove weak entities
	}
>>>>>>> c257d910922c3a729b61a0f3fa47ab3400876e1e

	/**
	 * Get number of actual generation
	 * 
	 * @return Generation number
	 */
	public int getGenerationNumber() {
		return this.generationNumber;
	}

	/**
	 * Test things if few entities only
	 */
	public String toString() {
		StringBuilder str = new StringBuilder();

		str.append("\t{ \n");

		for (Entity entity : this.entities)
			str.append(entity + "\n");

		str.append("\t}");

		return str.toString();
	}

}
