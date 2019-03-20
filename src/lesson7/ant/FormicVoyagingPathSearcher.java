package lesson7.ant;

import lesson5.Graph;
import lesson5.Path;

import java.util.*;

public class FormicVoyagingPathSearcher {

    private static class Ant {
        private List<Graph.Vertex> path;
        private final Graph.Vertex startVertex;
        private Graph.Vertex currentVertex;

        private Ant(Graph.Vertex start) {
            currentVertex = start;
            startVertex = start;
            path = new ArrayList<>();
            path.add(start);
        }

        private void findPath(Graph graph, double a, double b, Map<Graph.Edge, Double> pheromone) {
            while (path.size() != graph.getVertices().size()) {
                Set<Graph.Vertex> neighbours = graph.getNeighbors(currentVertex);
                Map<Graph.Vertex, Integer> probabilities = new HashMap<>();
                for (Graph.Vertex vi : neighbours) {
                    if (vi == startVertex) {
                        if (path.size() == graph.getVertices().size()) return;
                    } else {
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
                }
                // Выбор следующей вершины
                Random random = new Random();
                int sumProbability = 0;
                for (int probability : probabilities.values()) {
                    sumProbability += probability;
                }
                if (sumProbability == 0) {
                    path.add(currentVertex);
                } else {
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

        private Integer getSumPath(Graph graph) {
            int result = 0;
            if (isHamiltonLoop(graph)) {
                for (int i = 0; i < path.size() - 1; i++) {
                    result += graph.getConnection(path.get(i), path.get(i + 1)).getWeight();
                }
                result += graph.getConnection(path.get(0), path.get(path.size() - 1)).getWeight();
            } else return Integer.MAX_VALUE;
            return result;
        }

        private boolean isHamiltonLoop(Graph graph) {
            Set<Graph.Vertex> vertices = new HashSet<>();
            for (Graph.Vertex vertex : path) vertices.add(vertex);
            if (vertices.size() != path.size()) return false;
            return (graph.getConnection(path.get(0), path.get(path.size() - 1)) != null);
        }
    }

    public Path findVoyagingPath(Graph graph,
                                 double a, double b, double p, int q,
                                 int iterations,
                                 double initialPheromone) {
        Map<Graph.Edge, Double> pheromone = new HashMap<>();
        List<Ant> ants = new ArrayList<>();

        for (Graph.Edge edge : graph.getEdges()) {
            pheromone.put(edge, initialPheromone);
        }

        for (int iterator = 1; iterator <= iterations; iterator++) {
            ants = new ArrayList<>();

            for (Graph.Vertex vertex : graph.getVertices()) {
                Ant ant = new Ant(vertex);
                ants.add(ant);
            }

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
                if (ant.isHamiltonLoop(graph)) {
                    for (int i = 0; i < ant.path.size() - 1; i++) {
                        Graph.Edge edge = graph.getConnection(ant.path.get(i), ant.path.get(i + 1));
                        double value = pheromone.get(edge);
                        pheromone.remove(edge, value);
                        pheromone.put(edge, value + q / ant.getSumPath(graph));
                    }
                    Graph.Edge edge = graph.getConnection(ant.path.get(0), ant.path.get(ant.path.size() - 1));
                    double value = pheromone.get(edge);
                    pheromone.remove(edge, value);
                    pheromone.put(edge, value + q / ant.getSumPath(graph));
                }
            }
        }
        int minPathLength = Integer.MAX_VALUE;
        List<Graph.Vertex> minPath = new ArrayList<>();
        for (Ant ant : ants) {
            int pathLength = ant.getSumPath(graph);
            if (pathLength < minPathLength) {
                minPathLength = pathLength;
                minPath = ant.path;
            }
        }
        for (Ant ant : ants) {
            System.out.print(ant.path.toString() + " ");
            System.out.print(ant.getSumPath(graph) + " ");
            System.out.println(ant.isHamiltonLoop(graph));
        }
        System.out.println(listToPath(minPath, graph));
        return listToPath(minPath, graph);
    }

    private Path listToPath(List<Graph.Vertex> path, Graph graph) {
        Path result = new Path(path.get(0));
        for (int i = 1; i < path.size(); i++) {
            result = new Path(result, graph, path.get(i));
        }
        return new Path(result, graph, path.get(0));
    }
}