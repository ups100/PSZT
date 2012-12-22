package pl.edu.pw.elka.pszt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Vector;

/**
 * Represents population -> some entities
 * 
 * @author Marcin Kubik
 * @author Mikołaj Markiewicz
 * @author Krzysztof Opasiak
 */
public class Population {

	/** Amount of first population at least 2! */
	public static final int FIRST_AMOUNT = 100;

	/** List of entities */
	private ArrayList<Entity> entities = new ArrayList<Entity>();

	/** Target segment instance */
	private final MultiSegment targetSegment;

	/** Number of generation */
	private int generationNumber = 0;
	
	private int mutator = 0;
	private double lastBest;
	
	/**
	 * C-tor, creates first population entities
	 * 
	 * @param targetSegment Multi segment with target and all segments building target inside
	 */
	public Population(final MultiSegment targetSegment) 
	{
		this.targetSegment = targetSegment;
		lastBest = -1;
		for (int i = 0; i < FIRST_AMOUNT; ++i)
			this.entities.add(createRandomEntity());
	}

	/**
	 * Create random generated entity, useful especially for first generation
	 * 
	 * @return Generated entity
	 */
	private Entity createRandomEntity()
	{
		ArrayList<Segment> segments = this.targetSegment.getSegments();
		Entity entity = new Entity(this.generationNumber);

		for (Segment segment : segments) 
		{
			Connector connector = new Connector(segment, this.targetSegment, false);
			entity.addConnector(connector);
		}
		
		return entity;
	}

	/**
	 * Make next generation by coitus, mutation and natural selection
	 * @return Best adapted entity
	 */
	public Entity nextGeneration()
	{
		this.generationNumber++;
		mutator++;
		/**
		 * Choose entities from population to copulate them
		 */
		ArrayList<Entity> evolvingEntities = getRandomEntitiesFromPopulation();

		/** 
		 * Maybe some other variable will be useful but by now
		 * I will just set the result of copulation to the existing
		 * evolvingEntities variable
		 */
		ArrayList<Entity> children = copulateEntities(evolvingEntities);

		/**
		 * Children mutation
		 */
		for (Entity e: children)
		{
			e.mutateEntity();
		}
			
		this.entities.addAll(children);
	
		selectBestAdaptedEntities();
	
		if(getMostAdaptedEntity().getAdaptationSize() > lastBest)
		{
			lastBest = getMostAdaptedEntity().getAdaptationSize();
			mutator = 0;
		}
	
		if(mutator > 5)
		{
			this.mutateRandomly();
			mutator = 0;
		}
		
		return getMostAdaptedEntity();
	}


	/**
	 * Selection from all entities in population
	 * Remove(kill) weak entities from population
	 * Leave only best adapted
	 * Each population has at least as many entities as there was in first population
	 */
	private void selectBestAdaptedEntities() 
	{
		ArrayList<Entity> check = new ArrayList<Entity>(this.entities);

		Collections.sort(check);
		Collections.reverse(check);

		for (int i = 0; i < check.size(); ++i)
		{
			if (i >= FIRST_AMOUNT)
			{
				this.entities.remove(check.get(i));
			}
		}
	}

	/**
	 * Append new entities to population There is a need to change from public
	 * to private (I guess so)
	 */
	public ArrayList<Entity> copulateEntities(ArrayList<Entity> evolvingEntities)
	{
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
				if (i == j) continue;
				afterCopulationList.add(evolvingEntities.get(i).copulateWith(evolvingEntities.get(j), this.generationNumber));
			}
		}
		return afterCopulationList;

	}

	/**
	 * Get random amount of entities from population
	 * At least 2 entities necessary in population !
	 * 
	 * @return Random amount of entities
	 */
	private ArrayList<Entity> getRandomEntitiesFromPopulation()
	{
		Random generator = new Random();	
		int theChosenOnes = 0;
		while((theChosenOnes<FIRST_AMOUNT/10) || (theChosenOnes>(FIRST_AMOUNT/2)))
			theChosenOnes = generator.nextInt(this.entities.size()-1) + 1;
		Vector<Integer> previousNumbers = new Vector<>();
		ArrayList<Entity> list = new ArrayList<Entity>();
		int i = 0;
		while (i != theChosenOnes)
		{
			Integer tmp = generator.nextInt(this.entities.size());
			if(!previousNumbers.contains(tmp))
			{			
				previousNumbers.add(tmp);				
				list.add(this.entities.get(tmp));
				i++;
			}
					
		}
		return list;
	}

	/**
	 * Get most adapted entity
	 * 
	 * @return Most adapted entity
	 */
	private Entity getMostAdaptedEntity()
	{
		int max = -1;
		double maxAdaptation = -2;
		for(int i = 0; i<this.entities.size();++i)
		{
			if(this.entities.get(i).getAdaptationSize() > maxAdaptation)
			{
				max = i;
				maxAdaptation = this.entities.get(i).getAdaptationSize();			
			}
		}
		return this.entities.get(max);
	}

	public void mutateRandomly()
	{	
		int maxIndex = -1;
		double maxAdaptation = -2;
		
		for(int i = 0; i< this.entities.size(); ++i)
		{
			if(this.entities.get(i).getAdaptationSize() > maxAdaptation) 
			{
				maxIndex = i;
				maxAdaptation = this.entities.get(i).getAdaptationSize();
			}
		}
		
		for(int i = 1; i <this.entities.size(); ++i)
		{			
			if((this.entities.get(maxIndex).getAdaptationSize() - this.entities.get(i).getAdaptationSize()) < 0.1)
				this.entities.get(i).mutateEntity();
			else break;
		}	
		this.entities.get(0).mutateEntity();
	}
	
	/**
	 * Get number of actual generation
	 * 
	 * @return Generation number
	 */
	public int getGenerationNumber() 
	{
		return this.generationNumber;
	}

	public String toString() 
	{
		StringBuilder str = new StringBuilder();
		str.append("\t{ \n");
		
		for (Entity entity : this.entities)
			str.append(entity + "\n");
		
		str.append("\t}");
		
		return str.toString();
	}

	public void mutateThem() 
	{
		for (Entity e : this.entities) 
		{
			e.mutateEntity();
		}	
	}
	
	public ArrayList<Entity> getEntities()
	{
		return this.entities;
	}
	
	public MultiSegment getMultiSegment()
	{
		return this.targetSegment;
	}
}