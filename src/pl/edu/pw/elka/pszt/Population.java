package pl.edu.pw.elka.pszt;

import java.util.ArrayList;
import java.util.Random;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Iterator;

/**
 * Represents population -> some entities
 * 
 * @author Marcin Kubik
 * @author Mikołaj Markiewicz
 * @author Krzysztof Opasiak
 */
public class Population {

	/** Amount of first population at least 2! */
	public static int FIRST_AMOUNT = 900;

	public static final int MAX_POPULATION = 6000;
	
	public static int NEXT_GENERATION = 200;

	public static final double MUTATION_PERCENT = 0.4;

	/** List of entities */
	private TreeSet<Entity> entities = new TreeSet<Entity>();

	/** Target segment instance */
	private final MultiSegment targetSegment;

	/** Number of generation */
	private int generationNumber = 0;

	private int mutator = 0;
	private int mutator3 = 0;
	private int mutator4 = 0;
	private double lastBest;

	/**
	 * C-tor, creates first population entities
	 * 
	 * @param targetSegment
	 *            Multi segment with target and all segments building target
	 *            inside
	 */
	public Population(final MultiSegment targetSegment) {
		this.targetSegment = targetSegment;
		lastBest = -1;
		int j = 10, i =0;
		while (entities.size() < FIRST_AMOUNT) {
			this.entities.add(createRandomEntity());
			if(((double)(++i*100) /FIRST_AMOUNT) > j) {
				System.out.println("First population generation completed in " + j + " %");
				j += 10;
			}
		}
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
			Connector connector = new Connector(segment, this.targetSegment,
					false);
			entity.addConnector(connector);
		}

		return entity;
	}

	/**
	 * Make next generation by copulateThem, mutation and natural selection
	 * 
	 * @return Best adapted entity
	 */
	public Entity nextGeneration(double[] worst) {
		this.generationNumber++;
		mutator++;
		mutator3++;
		mutator4++;
		
		if(mutator4 > 7)
		{
			if(FIRST_AMOUNT*1.1 > MAX_POPULATION)
			{
				System.out.println("Replace worst entities");
				while(FIRST_AMOUNT - entities.size() < FIRST_AMOUNT/10) entities.pollFirst();
				
			} else {
				System.out.println("Extending population");
				FIRST_AMOUNT += FIRST_AMOUNT/10;
			}
			
			while (entities.size() < FIRST_AMOUNT) {
				this.entities.add(createRandomEntity());
			}
			mutator4 = 0;
		}
		
		if(mutator3 > 5)
		{
			System.out.println("Mutating best entity");
			Entity e = entities.last().copulateWith(entities.last(), generationNumber);;
			e.mutateEntity();
			entities.add(e);
			
			mutator3 = 0;
		}
		
		/**
		 * Choose entities from population to copulate them
		 */
		ArrayList<Entity> evolvingEntities = getRandomEntitiesFromPopulation();
		
		Random generator = new Random();
		
		
		if(mutator > 5) {
			System.out.println("Additional random entities to copulation");
			int injection = NEXT_GENERATION/5;
			for(int i = 0; i < injection ; ++i) {
				evolvingEntities.add(generator.nextInt(evolvingEntities.size()), this.createRandomEntity());
			}
			mutator = 0;
		}
		
		ArrayList<Entity> children = copulateEntities(evolvingEntities);
		
		
		/**
		 * Children mutation
		 */
		int numberToMutate = (int) (children.size() * MUTATION_PERCENT + 0.5);
		while (numberToMutate < 0) {
			children.get(generator.nextInt(children.size())).mutateEntity();
			--numberToMutate;
		}
		TreeSet<Entity> tree = new TreeSet<Entity>(children);
		

		
		this.entities.addAll(tree);
		
		selectBestAdaptedEntities();

		if (getMostAdaptedEntity().getAdaptationSize() > lastBest) {
			lastBest = getMostAdaptedEntity().getAdaptationSize();
			mutator = 0;
			mutator3 = 0;
			mutator4 = 0;
		}

		
		worst[0] = getWorstAdaptedEntity().getAdaptationSize();
		return getMostAdaptedEntity();
	}

	/**
	 * Selection from all entities in population Remove(kill) weak entities from
	 * population Leave only best adapted Each population has at least as many
	 * entities as there was in first population
	 */
	private void selectBestAdaptedEntities() {
		while(this.entities.size() > FIRST_AMOUNT) {
			this.entities.pollFirst();
		}
	}

	/**
	 * Append new entities to population
	 */
	private ArrayList<Entity> copulateEntities(ArrayList<Entity> evolvingEntities)
	{
		ArrayList<Entity> afterCopulationList = new ArrayList<Entity>();
		for(int i = 0; i < evolvingEntities.size(); ++i)
		{
			afterCopulationList.add(evolvingEntities.get(i).copulateWith(
					evolvingEntities.get((i+1)%evolvingEntities.size()), this.generationNumber));
			
			afterCopulationList.add(evolvingEntities.get(i).copulateWith(
					this.getMostAdaptedEntity(), this.generationNumber));
		}
		
		return afterCopulationList;
	}

	/**
	 * Get random amount of entities from population At least 2 entities
	 * necessary in population !
	 * 
	 * @return Random amount of entities
	 */
	private ArrayList<Entity> getRandomEntitiesFromPopulation() {
		Random generator = new Random();

		TreeMap<Integer, Integer> numbers = new TreeMap<Integer, Integer>();

		numbers.put(entities.size() -1, 0);
		
		for (int i = 1; i < NEXT_GENERATION; ++i) {
			Integer tmp;
			do {
				tmp = generator.nextInt(this.entities.size());
			} while(numbers.containsKey(tmp));
			
			Integer tmp2 = new Integer(i);
			numbers.put(tmp, tmp2);
		}

		ArrayList<Entity> list = new ArrayList<Entity>();
		list.ensureCapacity(NEXT_GENERATION);
		for(int i = 0; i < NEXT_GENERATION; ++i) {
			list.add(null);
		}
		
		Iterator<Entity> it = this.entities.iterator();
		for (int i = 0; it.hasNext(); ++i) {
			Entity e = it.next();
			
			if (numbers.containsKey(i)) {
				list.set(numbers.get(i), e);
			}
		}
		
		return list;
	}

	/**
	 * Get most adapted entity
	 * 
	 * @return Most adapted entity
	 */
	private Entity getMostAdaptedEntity() {
		return this.entities.last();
	}
	
	/**
	 * Get worst adapted entity
	 * @return Worst adapted entity
	 * 
	 */
	private Entity getWorstAdaptedEntity() {
		return this.entities.first();
	}

	/**
	 * Get number of actual generation
	 * 
	 * @return Generation number
	 */
	public int getGenerationNumber() {
		return this.generationNumber;
	}

	/**
	 * Used to get information about the population
	 */
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("\t{ \n");

		for (Entity entity : this.entities)
			str.append(entity + "\n");

		str.append("\t}");

		return str.toString();
	}

	/**
	 * Allows to mutate each entity independently
	 */
	public void mutateThem() {
		for (Entity e : this.entities) {
			e.mutateEntity();
		}
	}

	/**
	 * Getter for all entities in population
	 * 
	 * @return ArrayList of entities
	 */
	public TreeSet<Entity> getEntities() {
		return this.entities;
	}

	/**
	 * Getter for the reference to MultiSegment
	 * 
	 * @return Multisegment
	 */
	public MultiSegment getMultiSegment() {
		return this.targetSegment;
	}
}