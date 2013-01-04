package pl.edu.pw.elka.pszt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;
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

	public static int NEXT_GENERATION = 200;

	public static final double MUTATION_PERCENT = 0.4;

	/** List of entities */
	private TreeSet<Entity> entities = new TreeSet<Entity>();

	/** Target segment instance */
	private final MultiSegment targetSegment;

	/** Number of generation */
	private int generationNumber = 0;

	private int mutator = 0;
	private int mutator2 = 0;
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
		System.out.println("Generating first population");
		while (entities.size() < FIRST_AMOUNT) {
			this.entities.add(createRandomEntity());
		}
		System.out.println("First population generated ");

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
	public Entity nextGeneration() {
		this.generationNumber++;
		mutator++;
		//mutator2++;
		mutator3++;
		mutator4++;
		long startTime2 = System.nanoTime();
		long startTime = System.nanoTime();
		System.out.println("start");
		
		if(mutator4 > 7)
		{
			System.out.println("population grow");
			FIRST_AMOUNT += FIRST_AMOUNT/10;
			while (entities.size() < FIRST_AMOUNT) {
				this.entities.add(createRandomEntity());
			}
			mutator4 = 0;
		}
		
		if(mutator3 > 5)
		{
			System.out.println("muting best");
			Entity e = entities.last().copulateWith(entities.last(), generationNumber);;
			e.mutateEntity();
			entities.add(e);
			
			mutator3 = 0;
		}
		
		if((mutator2 > 4)&&(entities.last().getAdaptationSize() - entities.first().getAdaptationSize() < 0.10))
		{
			System.out.println("new mutation");
			int additionalEntities = FIRST_AMOUNT/10;
			for(int i = 0; i < additionalEntities; ++i) this.entities.pollFirst();
			
			for ( int i = 0; i < additionalEntities; ++i) {
				this.entities.add(createRandomEntity());
			}
			mutator2 = 0;
		}
		
		/**
		 * Choose entities from population to copulate them
		 */
		ArrayList<Entity> evolvingEntities = getRandomEntitiesFromPopulation();
		System.out.println("getting random in " + (System.nanoTime() - startTime));
		
		Random generator = new Random();
		
//		if (mutator > 5) {
//			//this.mutateRandomly();
//			System.out.println("Additional Mutation");
//			TreeSet<Entity> children2 = new TreeSet<Entity>();
//			int mutStrenght = NEXT_GENERATION;
//			int maxDepth = 2;
//			for (int i = 0; children2.size() < mutStrenght; ++i) {
//				Entity e = getMostAdaptedEntity().copulateWith(
//						getMostAdaptedEntity(), this.generationNumber);
//				
//				for(int j = generator.nextInt(maxDepth - 1); j < maxDepth; ++j ) e.mutateEntity();
//				
//				children2.add(e);
//			}
//			evolvingEntities.addAll(children2);
//			mutator = 0;
//		}
		
		if(mutator > 5) {
			System.out.println("Additional Mutation");
			int injection = NEXT_GENERATION/5;
			for(int i = 0; i < injection ; ++i) {
				evolvingEntities.add(generator.nextInt(evolvingEntities.size()), this.createRandomEntity());
			}
			mutator = 0;
		}
		
		startTime = System.nanoTime();
		ArrayList<Entity> children = copulateEntities(evolvingEntities);
		System.out.println("copulation in " + (System.nanoTime() - startTime));
		
		/**
		 * Children mutation
		 */
		startTime = System.nanoTime();
		int numberToMutate = (int) (children.size() * MUTATION_PERCENT + 0.5);
		while (numberToMutate < 0) {
			children.get(generator.nextInt(children.size())).mutateEntity();
			--numberToMutate;
		}
		TreeSet<Entity> tree = new TreeSet<Entity>(children);
		
		System.out.println("mutation in " + (System.nanoTime() - startTime));
		
		startTime = System.nanoTime();
		this.entities.addAll(tree);
		System.out.println("add all in " + (System.nanoTime() - startTime));
		startTime = System.nanoTime();
		selectBestAdaptedEntities();

		if (getMostAdaptedEntity().getAdaptationSize() > lastBest) {
			lastBest = getMostAdaptedEntity().getAdaptationSize();
			mutator = 0;
			mutator2 = 0;
			mutator3 = 0;
			mutator4 = 0;
		}

		System.out.println("rest in " + (System.nanoTime() - startTime));
		System.out.println("all in " + (System.nanoTime() - startTime2));
		System.out.println("Minimum adaptation = " + this.entities.first() );
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
			
			//let alpha copulate with everyone
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

		// K - population position, V- child position
		TreeMap<Integer, Integer> numbers = new TreeMap<Integer, Integer>();

		//samiec alpha zawsze kopuluje
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
	 * Mutates population if next generations aren't better and the adaptation
	 * does not increase
	 */
	public void mutateRandomly() {
//		int maxIndex = -1;
//		double maxAdaptation = -2;
//
//		for (int i = 0; i < this.entities.size(); ++i) {
//			if (this.entities.get(i).getAdaptationSize() > maxAdaptation) {
//				maxIndex = i;
//				maxAdaptation = this.entities.get(i).getAdaptationSize();
//			}
//		}
//
//		for (int i = 1; i < this.entities.size(); ++i) {
//			if ((this.entities.get(maxIndex).getAdaptationSize() - this.entities
//					.get(i).getAdaptationSize()) < 0.1)
//				this.entities.get(i).mutateEntity();
//			else
//				break;
//		}
//		this.entities.get(0).mutateEntity();
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