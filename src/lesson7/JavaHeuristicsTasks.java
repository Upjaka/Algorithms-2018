package lesson7;

import kotlin.NotImplementedError;
import lesson5.Graph;
import lesson5.Path;
import lesson6.knapsack.Fill;
import lesson6.knapsack.Item;
import lesson7.ant.FormicVoyagingPathSearcher;

import java.util.*;

// Примечание: в этом уроке достаточно решить одну задачу
@SuppressWarnings("unused")
public class JavaHeuristicsTasks {

    /**
     * Решить задачу о ранце (см. урок 6) любым эвристическим методом
     *
     * Очень сложная
     *
     * load - общая вместимость ранца, items - список предметов
     *
     * Используйте parameters для передачи дополнительных параметров алгоритма
     * (не забудьте изменить тесты так, чтобы они передавали эти параметры)
     */
    public static Fill fillKnapsackHeuristics(int load, List<Item> items, Object... parameters) {
        throw new NotImplementedError();
    }

    /**
     * Решить задачу коммивояжёра (см. урок 5) методом колонии муравьёв
     * или любым другим эвристическим методом, кроме генетического и имитации отжига
     * (этими двумя методами задача уже решена в под-пакетах annealing & genetic).
     *
     * Очень сложная
     *
     * Граф передаётся через параметр graph
     *
     * Используйте parameters для передачи дополнительных параметров алгоритма
     * (не забудьте изменить тесты так, чтобы они передавали эти параметры)
     *
     * Параметры a, b, p, g - константные параметры для регулировки алгоритма
     *              a, b - степени вляния феромонов и расстояния соответственно
     *              р - интенсивность испарения
     *              q - параметр,  имеющий значение порядка цены оптимального решения
     *           iterations - количество итераций алгоритма
     *           initialPheromone - начальное значение феромона на ребрах
     */
    public static Path findVoyagingPathHeuristics(Graph graph,
                                                  double a, double b, double p, int q,
                                                  int iterations,
                                                  double initialPheromone) {
        FormicVoyagingPathSearcher formicVoyagingPathSearcher = new FormicVoyagingPathSearcher();
        return formicVoyagingPathSearcher.findVoyagingPath(graph, a, b, p, q, iterations, initialPheromone);
    }
}
