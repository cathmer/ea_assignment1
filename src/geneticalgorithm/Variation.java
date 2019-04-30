
package geneticalgorithm;

import java.util.ArrayList;

/**
 *
 * @author Marco Virgolin, with the collaboration of Anton Bouter and Hoang Ngoc Luong and the supervision of Peter A.N. Bosman
 */
enum CrossoverType {
    Uniform,
    OnePoint
}

public class Variation {

    CrossoverType crossover_type;

    public Variation(CrossoverType crossover_type) {
        this.crossover_type = crossover_type;
    }

    public ArrayList<Individual> PerformCrossover(Individual parent1, Individual parent2) {

        if (crossover_type == CrossoverType.OnePoint) {
            return OnePointCrossover(parent1, parent2);
        } else {
            return UniformCrossover(parent1, parent2);
        }

    }

    private ArrayList<Individual> UniformCrossover(Individual parent1, Individual parent2) {

        Individual child1 = parent1.Clone(); // explicit call to clone because otherwise Java copies the reference
        Individual child2 = parent2.Clone();

        // Remember to use the rng in Utilities to sample random numbers, e.g., Utilities.rng.nextDouble();

        for (int i = 0; i < parent1.genotype.length; i++) {
            if (Utilities.rng.nextBoolean()) {
                child1.genotype[i] = parent1.genotype[i];
                child2.genotype[i] = parent2.genotype[i];
            } else {
                child1.genotype[i] = parent2.genotype[i];
                child2.genotype[i] = parent1.genotype[i];
            }
        }

        ArrayList<Individual> result = new ArrayList<Individual>();
        result.add(child1);
        result.add(child2);

        return result;
    }

    private ArrayList<Individual> OnePointCrossover(Individual parent1, Individual parent2) {

        Individual child1 = parent1.Clone();
        Individual child2 = parent2.Clone();

        // Remember to use the rng in Utilities to sample random numbers, e.g., Utilities.rng.nextDouble();

        int crossoverPoint = Utilities.rng.nextInt(parent1.genotype.length - 1);

        for (int i = 0; i < parent1.genotype.length; i++) {
            if (i >= crossoverPoint) {
                child1.genotype[i] = parent1.genotype[i];
                child2.genotype[i] = parent2.genotype[i];
            } else {
                child1.genotype[i] = parent2.genotype[i];
                child2.genotype[i] = parent1.genotype[i];
            }
        }

        ArrayList<Individual> result = new ArrayList<Individual>();
        result.add(child1);
        result.add(child2);

        return result;
    }

}
