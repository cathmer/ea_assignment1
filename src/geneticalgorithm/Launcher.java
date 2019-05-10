package geneticalgorithm;

import java.io.*;
import java.nio.file.Files;

/**
 *
 * @author Marco Virgolin, with the collaboration of Anton Bouter and Hoang Ngoc Luong and the supervision of Peter A.N. Bosman
 */
public class Launcher {

    public static void main(String[] args) throws IOException {
        File directory = new File("experiments");
        if (!directory.exists()) {
            directory.mkdir();
        }

        BufferedWriter bw = null;

        try {
            File file = new File("results.csv");
            if (!file.exists()) {
                file.createNewFile();
            }

            bw = new BufferedWriter(new FileWriter(file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        bw.write("p,m,k,d,crossover,run,fitness,generation,evaluations,time,elite,isOptimum\n");

        int[] m = {1, 2, 4, 8, 16};
        int[] k = {3, 5, 10};
        int[] n = {10, 100, 1000, 10000, 100000};

        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < k.length; j++) {
                for (int l = 0; l < n.length; l++) {
                    runGA(m[i], k[j], (double) 1/k[j], n[l], CrossoverType.Uniform, bw);
                    runGA(m[i], k[j], (double) 1/k[j], n[l], CrossoverType.OnePoint, bw);
                    runGA(m[i], k[j], 1 - (double) 1/k[j], n[l], CrossoverType.Uniform, bw);
                    runGA(m[i], k[j], 1 - (double) 1/k[j], n[l], CrossoverType.OnePoint, bw);
                }
            }
        }

        bw.close();
    }

    private static void runGA(int m, int k, double d, int population_size, CrossoverType ct, BufferedWriter bw) throws IOException {
        // termination condition parameters ( 0 or negatives are ignored )
        long time_limit = 3 * 1000; // in milliseconds
        int generations_limit = -1;
        long evaluations_limit = -1;

        int i = 0;
        // Set up logging
        String output_file_name = "experiments/log_p" + population_size + "_m" + m + "_k" + k + "_d" + d + "_c" + ct + "_run" + i + ".txt";
        Files.deleteIfExists(new File(output_file_name).toPath());
        Utilities.logger = new BufferedWriter(new FileWriter(output_file_name, true));
        Utilities.logger.write("gen evals time best_fitness\n");

        // Run GA
        System.out.println("Starting run " + i + " with pop_size=" + population_size + ", m=" + m + ", k=" + k + ", d=" + d + ", crossover_type=" + ct);
        SimpleGeneticAlgorithm ga = new SimpleGeneticAlgorithm(population_size, m, k, d, ct);
        try {
            ga.run(generations_limit, evaluations_limit, time_limit);

            String res = "Best fitness " + ga.fitness_function.elite.fitness + " found at\n"
                    + "generation\t" + ga.generation + "\nevaluations\t" + ga.fitness_function.evaluations + "\ntime (ms)\t" + (System.currentTimeMillis() - ga.start_time + "\n")
                    + "elite\t\t" + ga.fitness_function.elite.toString();

            System.out.println(res);
            bw.write(population_size + "," + m + "," + k + "," + d + "," + ct + "," + i + "," + ga.fitness_function.elite.fitness + "," +
                    ga.generation + "," + ga.fitness_function.evaluations + "," + (System.currentTimeMillis() - ga.start_time) + ","
                    + ga.fitness_function.elite.toString() + ",FALSE\n");
        } catch (FitnessFunction.OptimumFoundCustomException ex) {
            String res = "Optimum " + ga.fitness_function.elite.fitness + " found at\n"
                    + "generation\t" + ga.generation + "\nevaluations\t" + ga.fitness_function.evaluations + "\ntime (ms)\t" + (System.currentTimeMillis() - ga.start_time + "\n")
                    + "elite\t\t" + ga.fitness_function.elite.toString();
            System.out.println(res);
            bw.write(population_size + "," + m + "," + k + "," + d + "," + ct + "," + i + "," + ga.fitness_function.elite.fitness + "," +
                    ga.generation + "," + ga.fitness_function.evaluations + "," + (System.currentTimeMillis() - ga.start_time) + ","
                    + ga.fitness_function.elite.toString() + ",TRUE\n");
            Utilities.logger.write(ga.generation + " " + ga.fitness_function.evaluations + " " + (System.currentTimeMillis() - ga.start_time) + " " + ga.fitness_function.elite.fitness + "\n");
        }

        bw.flush();
        Utilities.logger.close();
    }
}
