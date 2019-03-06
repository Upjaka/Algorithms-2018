package lesson5;


import kotlin.NotImplementedError;
import lesson5.impl.GraphBuilder;

import java.util.*;

@SuppressWarnings("unused")
public class JavaGraphTasks {
    /**
     * Эйлеров цикл.
     * Средняя
     * <p>
     * Дан граф (получатель). Найти по нему любой Эйлеров цикл.
     * Если в графе нет Эйлеровых циклов, вернуть пустой список.
     * Соседние дуги в списке-результате должны быть инцидентны друг другу,
     * а первая дуга в списке инцидентна последней.
     * Длина списка, если он не пуст, должна быть равна количеству дуг в графе.
     * Веса дуг никак не учитываются.
     * <p>
     * Пример:
     * <p>
     * G -- H
     * |    |
     * A -- B -- C -- D
     * |    |    |    |
     * E    F -- I    |
     * |              |
     * J ------------ K
     * <p>
     * Вариант ответа: A, E, J, K, D, C, H, G, B, C, I, F, B, A
     * <p>
     * Справка: Эйлеров цикл -- это цикл, проходящий через все рёбра
     * связного графа ровно по одному разу
     * <p>
     * Трудоемкость - Т = o(E + V)
     * Ресурсоемкость - R = o(E + V)
     */
    public static List<Graph.Edge> findEulerLoop(Graph graph) {
        List<Graph.Edge> result = new ArrayList<>();
        Set<Graph.Vertex> vertices = graph.getVertices();
        List<Graph.Vertex> eulerCycle = new ArrayList<>();
        Iterator iterator = vertices.iterator();
        Set<Graph.Edge> edges = graph.getEdges();

        if (!checkCycleExistence(graph))
            return result;

        Stack<Graph.Vertex> stack = new Stack<>();
        Graph.Vertex first = (Graph.Vertex) iterator.next();
        stack.push(first);
        iterator = edges.iterator();

        while (iterator.hasNext()) {
            result.add((Graph.Edge) iterator.next());
        }
        while (!stack.empty()) {
            int n = 0;
            Graph.Vertex vertex = stack.peek();
            for (Graph.Edge edge : result) {
                if (edge.getBegin() == vertex || edge.getEnd() == vertex)
                    n++;
            }
            if (n == 0) {
                eulerCycle.add(stack.pop());
            } else {
                for (int i = 0; i < result.size(); i++) {
                    if (result.get(i).getBegin() == vertex) {
                        stack.push(result.get(i).getEnd());
                        result.remove(i);
                        break;
                    }
                    if (result.get(i).getEnd() == vertex) {
                        stack.push(result.get(i).getBegin());
                        result.remove(i);
                        break;
                    }
                }
            }
        }
        result.clear();
        Iterator iterator1;
        for (int i = 0; i < eulerCycle.size() - 1; i++) {
            iterator1 = edges.iterator();
            while (iterator1.hasNext()) {
                Graph.Edge edge = (Graph.Edge) iterator1.next();
                if ((edge.getBegin().equals(eulerCycle.get(i)) && edge.getEnd().equals(eulerCycle.get(i + 1)))
                        || (edge.getEnd().equals(eulerCycle.get(i)) && edge.getBegin().equals(eulerCycle.get(i + 1))))
                    result.add(edge);
            }
        }
        return result;
    }

    private static boolean checkCycleExistence(Graph graph) {
        Set<Graph.Edge> edges = graph.getEdges();
        Map<Graph.Vertex, Integer> verticesCount = new HashMap<>();
        Set<Graph.Vertex> keys = new HashSet<>();
        for (Graph.Edge edge : edges) {
            if (!verticesCount.containsKey(edge.getBegin())) {
                verticesCount.put(edge.getBegin(), 1);
                keys.add(edge.getBegin());
            } else verticesCount.put(edge.getBegin(), verticesCount.get(edge.getBegin()) + 1);
            if (!verticesCount.containsKey(edge.getEnd())) {
                verticesCount.put(edge.getEnd(), 1);
                keys.add(edge.getEnd());
            } else verticesCount.put(edge.getEnd(), verticesCount.get(edge.getEnd()) + 1);
        }
        for (Graph.Vertex key : keys) {
            if (verticesCount.get(key) % 2 != 0)
                return false;
        }
        return true;
    }

    /**
     * Минимальное остовное дерево.
     * Средняя
     * <p>
     * Дан граф (получатель). Найти по нему минимальное остовное дерево.
     * Если есть несколько минимальных остовных деревьев с одинаковым числом дуг,
     * вернуть любое из них. Веса дуг не учитывать.
     * <p>
     * Пример:
     * <p>
     * G -- H
     * |    |
     * A -- B -- C -- D
     * |    |    |    |
     * E    F -- I    |
     * |              |
     * J ------------ K
     * <p>
     * Ответ:
     * <p>
     * G    H
     * |    |
     * A -- B -- C -- D
     * |    |    |
     * E    F    I
     * |
     * J ------------ K
     * <p>
     * Трудоемкость - Т = o(E + V)
     * Ресурсоемкость - R = o(E + V)
     */
    public static Graph minimumSpanningTree(Graph graph) {
        GraphBuilder resultBuilder = new GraphBuilder();
        Set sedgeSet = graph.getEdges();
        Set vertexSet = graph.getVertices();
        List<Graph.Edge> edges = new ArrayList<>();
        for (Object o : sedgeSet) {
            Graph.Edge cur = (Graph.Edge) o;
            edges.add(cur);
        }

        List<Graph.Vertex> vertices = new ArrayList<>();
        for (Object o : vertexSet) {
            Graph.Vertex current = (Graph.Vertex) o;
            vertices.add(current);
        }
        for (Graph.Vertex vertex : vertices) {
            resultBuilder.addVertex(vertex.getName());
        }
        Set<Graph.Vertex> used = new HashSet<>();
        used.add(vertices.get(0));
        Stack<Graph.Vertex> stack = new Stack<>();
        stack.push(vertices.get(0));
        while (!stack.isEmpty()) {
            Graph.Vertex upper = stack.peek();
            int count = 0;
            for (int i = 0; i < edges.size(); i++) {
                if (edges.get(i).getBegin() == upper && !used.contains(edges.get(i).getEnd())) {
                    resultBuilder.addConnection(edges.get(i).getBegin(), edges.get(i).getEnd(), 1);
                    used.add(edges.get(i).getEnd());
                    count++;
                    stack.push(edges.get(i).getEnd());
                    edges.remove(i);
                    break;
                }
                if (edges.get(i).getEnd() == upper && !used.contains(edges.get(i).getBegin())) {
                    resultBuilder.addConnection(edges.get(i).getBegin(), edges.get(i).getEnd(), 1);
                    used.add(edges.get(i).getBegin());
                    count++;
                    stack.push(edges.get(i).getBegin());
                    edges.remove(i);
                    break;
                }
            }
            if (count == 0) {
                stack.pop();
            }
        }
        return resultBuilder.build();
    }

    /**
     * Максимальное независимое множество вершин в графе без циклов.
     * Сложная
     * <p>
     * Дан граф без циклов (получатель), например
     * <p>
     * G -- H -- J
     * |
     * A -- B -- D
     * |         |
     * C -- F    I
     * |
     * E
     * <p>
     * Найти в нём самое большое независимое множество вершин и вернуть его.
     * Никакая пара вершин в независимом множестве не должна быть связана ребром.
     * <p>
     * Если самых больших множеств несколько, приоритет имеет то из них,
     * в котором вершины расположены раньше во множестве this.vertices (начиная с первых).
     * <p>
     * В данном случае ответ (A, E, F, D, G, J)
     * <p>
     * Эта задача может быть зачтена за пятый и шестой урок одновременно
     */
    public static Set<Graph.Vertex> largestIndependentVertexSet(Graph graph) {
        throw new NotImplementedError();
    }

    /**
     * Наидлиннейший простой путь.
     * Сложная
     * <p>
     * Дан граф (получатель). Найти в нём простой путь, включающий максимальное количество рёбер.
     * Простым считается путь, вершины в котором не повторяются.
     * Если таких путей несколько, вернуть любой из них.
     * <p>
     * Пример:
     * <p>
     * G -- H
     * |    |
     * A -- B -- C -- D
     * |    |    |    |
     * E    F -- I    |
     * |              |
     * J ------------ K
     * <p>
     * Ответ: A, E, J, K, D, C, H, G, B, F, I
     */
    public static Path longestSimplePath(Graph graph) {
        throw new NotImplementedError();
    }
}
