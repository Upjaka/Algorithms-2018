package lesson7.ant;

import lesson5.Graph;
import lesson5.Path;

import java.util.*;

public class FormicVoyagingRathSearcher {

    private static class Ant {
        private List<Graph.Vertex> path;
        private final Graph.Vertex startVertex;
        private Graph.Vertex currentVertex;

        private Ant(Graph.Vertex start) {
            currentVertex = start;
            startVertex = start;
            path.add(start);
        }

        private void findPath(Graph graph, double a, double b, Map<Graph.Edge, Double> pheromone) {
            Set<Graph.Vertex> neighbours = graph.getNeighbors(currentVertex);
            Map<Graph.Vertex, Integer> probabilities = new HashMap<>();
            while (path.size() != graph.getVertices().size()) {
                for (Graph.Vertex vi : neighbours) {
                    if (vi == startVertex) {
                        if (path.size() == graph.getVertices().size()) return;
                        else {
                            if (!path.contains(vi)) {
                                // Вычисление вероятности перехода в i вершину.
                                // Находим сначала числитель, затем знаменатель дроби
                                double numerator =
                                        Math.pow(1.0 / graph.getConnection(currentVertex, vi).getWeight(), b) *
                                                Math.pow(pheromone.get(graph.getConnection(currentVertex, vi)), a);
                                double denominator = 0.0;
                                for (Graph.Vertex vk : neighbours) {
                                    if (!this.path.contains(vi)) {
                                        denominator +=
                                                Math.pow(1.0 / graph.getConnection(currentVertex, vk).getWeight(), b) *
                                                        Math.pow(pheromone.get(graph.getConnection(currentVertex, vk)), a);
                                    }
                                }
                                probabilities.put(vi, (int) (numerator / denominator * 100));
                            }
                        }
                        // Выбор следующей вершины
                        Random random = new Random();
                        int sumProbability = 0;
                        for (int probability : probabilities.values()) {
                            sumProbability += probability;
                        }
                        int number = random.nextInt(sumProbability + 1) + 1;
                        Set<Graph.Vertex> vertices = probabilities.keySet();
                        for (Graph.Vertex vertex : vertices) {
                            if (number > probabilities.get(vertex)) {
                                number -= probabilities.get(vertex);
                            } else {
                                path.add(vertex);
                                currentVertex = vertex;
                                break;
                            }
                        }
                    }
                }
            }
        }

        private Integer getSumPath(Graph graph) {
            Integer result = 0;
            for (int i = 0; i < path.size() - 1; i++) {
                result += graph.getConnection(path.get(i), path.get(i + 1)).getWeight();
            }
            result += graph.getConnection(path.get(path.size()), path.get(0)).getWeight();
            return result;
        }
    }

    public Path findVoyagingPath(Graph graph,
                                 double a, double b, double p, int q,
                                 int iter,
                                 double initialPheromone) {
        Map<Graph.Edge, Double> pheromone = new HashMap<>();
        List<Ant> ants = new ArrayList<>();

        for (Graph.Edge edge : graph.getEdges()) {
            pheromone.put(edge, initialPheromone);
        }

        for (Graph.Vertex vertex : graph.getVertices()) {
            Ant ant = new Ant(vertex);
            ants.add(ant);
        }

        for (int iterator = 1; iterator <= iter; iterator++) {
            for (Ant ant : ants) {
                ant.findPath(graph, a, b, pheromone);
            }
            // Обновление феромона
            for (Graph.Edge edge : graph.getEdges()) {
                double value = pheromone.get(edge);
                pheromone.remove(edge, value);
                pheromone.put(edge, value * (1 - p));
            }
            for (Ant ant : ants) {
                for (int i = 0; i < ant.path.size() - 1; i++) {
                    Graph.Edge edge = graph.getConnection(ant.path.get(i), ant.path.get(i + 1));
                    double value = pheromone.get(edge);
                    pheromone.remove(edge, value);
                    pheromone.put(edge, value + q / ant.getSumPath(graph));
                }
            }
            // Если это не последняя итерация, обнуляем пути муравьев
            if (iterator <= iter) {
                for (Graph.Vertex vertex : graph.getVertices()) {
                    Ant ant = new Ant(vertex);
                    ants.add(ant);
                }
            }
        }
        int minPathLenght = Integer.MAX_VALUE;
        List<Graph.Vertex> minPath = new ArrayList<>();
        for (Ant ant :ants) {
            int pathLenght = ant.getSumPath(graph);
            if (pathLenght < minPathLenght) {
                minPathLenght = pathLenght;
                minPath = ant.path;
            }
        }
        return new Path(minPath.get(0));
    }
}
