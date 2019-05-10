
package geneticalgorithm;

import java.util.Optional;

/**
 *
 * @author Marco Virgolin, with the collaboration of Anton Bouter and Hoang Ngoc Luong and the supervision of Peter A.N. Bosman
 */
public class FitnessFunction {

    int m, k;
    double d;
    long evaluations;
    double optimum;

    Individual elite = null;

    FitnessFunction(int m, int k, double d) {
        this.m = m;
        this.k = k;
        this.d = d;
        this.evaluations = 0;

        if (d >= 0.0) {
            this.optimum = m;
        } else {
            this.optimum = m * (1 - d);
        }
    }
    
    // The purpose of this custom exception is to perform a naughty trick: halt the GA as soon as the optimum is found
    // do not modify it
    class OptimumFoundCustomException extends Exception {
        public OptimumFoundCustomException(String message) {
            super(message);
        }
    }

    public void Evaluate(Individual individual) throws OptimumFoundCustomException, OptimumFoundCustomException {

        evaluations++;

        double result = 0;

        for (int i = 0; i < m; i++) {
            result += fSub(individual, i);
        }

        // set the fitness of the individual
        individual.fitness = result;

        // update elite
        if (elite == null || elite.fitness < individual.fitness) {
            elite = individual.Clone();
        }

        // check if optimum has been found
        if (result == optimum) {
            // naughty trick in action: throw our custom exception
            throw new OptimumFoundCustomException("GG EZ");
        }
    }

    private double fSub(Individual individual, int i) {
        int ub = sumOfOnes(individual, i * k);
        if (ub == k) {
            return 1.0;
        } else {
            return (double)(1 - d) * (k - 1 - ub) / (k - 1);
        }
    }

    private int sumOfOnes(Individual individual, int startIndex) {
        int sum = 0;

        for (int i = 0; i < k; i++) {
            sum += individual.genotype[startIndex + i];
        }

        return sum;
    }

}
