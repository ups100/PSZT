package pl.edu.pw.elka.pszt;

import java.util.ArrayList;
import java.util.Random;



/**
 * Represents population -> some entities
 * 
 * @author Kajo
 */
public class Population {

	/** Amount of first population at least 2! */
	public static final int FIRST_AMOUNT = 10;
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
	 * C-tor,
	 * creates first population entities
	 * 
	 * @param targetSegment Multi segment with target and all segments building target inside
	 */
	public Population(final MultiSegment targetSegment)
	{
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
	private Entity createRandomEntity()
	{
		ArrayList<Segment> segments = this.targetSegment.getSegments();
		Entity entity = new Entity(this.generationNumber);

		for (Segment segment : segments)
		{
			// TODO check random algorithm, except some already chosen vertices ?
			Connector connector = new Connector(segment, this.targetSegment);

			entity.addConnector(connector);
		}

		return entity;
	}

	/**
	 * Make next generation by coitus, mutation and natural selection
	 */
	public void nextGeneration()
	{
		this.generationNumber++;
		Random generator = new Random();
		int theChosenOnes = generator.nextInt(numberOfEntities) +1;
		ArrayList<Entity> evolvingEntities = this.getEntities(theChosenOnes);
		copulateEntities(evolvingEntities);
		// TODO mutate entities every time ?
		// TODO sometime select entities
	}

	/**
	 * Append new entities to population
	 * There is a need to change from public to private
	 * (I guess so)
	 */
	public void copulateEntities(ArrayList<Entity> evolvingEntities)
	{
		
		// TODO choose entities and copulate them
	}
	
	/** Get exact amount of entities that will evolve */
	private ArrayList<Entity> getEntities(int theChosenOnes) 
	{
		Random generator = new Random();
		ArrayList<Entity> list = new ArrayList<Entity>();
		int i = 0;
		while(i != theChosenOnes)
		{
			int tmp = generator.nextInt(numberOfEntities);
			if(!list.contains(this.entities.get(tmp)))
			{
				list.add(this.entities.get(tmp));
				i++;
			}
		}		
		return list;
	}

	/**
	 * Select more adapted entites
	 */
	private void selectEntites()
	{
		// TODO sometime remove weak entities
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

	/**
	 * Test things if few entities only
	 */
	public String toString()
	{
		StringBuilder str = new StringBuilder();

		str.append("\t{ \n");

		for (Entity entity : this.entities)
			str.append(entity + "\n");

		str.append("\t}");

		return str.toString();
	}

}
