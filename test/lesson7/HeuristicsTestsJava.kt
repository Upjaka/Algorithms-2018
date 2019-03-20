package lesson7

import kotlin.test.Test
import org.junit.jupiter.api.Tag

class HeuristicsTestsJava : AbstractHeuristicsTests() {
    @Test
    @Tag("Impossible")
    fun testFillKnapsackCompareWithGreedyTest() {
        fillKnapsackCompareWithGreedyTest { load, items -> JavaHeuristicsTasks.fillKnapsackHeuristics(load, items) }
    }

    @Test
    @Tag("Impossible")
    fun testFindVoyagingPathHeuristics() {
        findVoyagingPathHeuristics { let { JavaHeuristicsTasks.findVoyagingPathHeuristics(it,
                1.0, 1.0, 0.7, 100, 20, 0.01) } }
    }

}