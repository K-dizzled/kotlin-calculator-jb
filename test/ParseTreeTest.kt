package mpp.calculator

import org.junit.Test
import kotlin.test.assertEquals

class CLICalcTest {

    private fun traverseParseTree(node: ParseNode, result: MutableList<String>) {
        result.add(toStr(node.value))
        node.children.forEach { traverseParseTree(it, result) }
    }

    @Test
    fun testTokenize() {
        val parser = ExpressionParser()
        val tokens = parser.tokenize("1+2")
        assertEquals(listOf("1", "+", "2"), tokens)

        val tokens2 = parser.tokenize("1+2*3")
        assertEquals(listOf("1", "+", "2", "*", "3"), tokens2)

        val tokens3 = parser.tokenize("1 + 2 * 3 + 4")
        assertEquals(listOf("1", "+", "2", "*", "3", "+", "4"), tokens3)

        val tokens4 = parser.tokenize("(1+2)*3 +4 *(5)+7")
        assertEquals(listOf("(", "1", "+", "2", ")", "*", "3", "+", "4", "*", "(", "5", ")", "+", "7"), tokens4)

        val tokens5 = parser.tokenize("y_10_x + 2 * (xxx_ + 4) * 5 + !228")
        assertEquals(listOf("y_10_x", "+", "2", "*", "(", "xxx_", "+", "4", ")", "*", "5", "+", "!228"), tokens5)
    }

    @Test
    fun testAdd() {
        val parser = ExpressionParser()

        val tree = parser.evaluateExpression("(1 + 2) * (7 + 3) + 9")
        val result = mutableListOf<String>()
        tree.root?.let { traverseParseTree(it, result) }
        assertEquals(listOf("Expression", "Term", "Factor", "Expression", "Term", "Factor",
            "1", "+", "Term", "Factor", "2", "*", "Factor", "Expression", "Term", "Factor",
            "7", "+", "Term", "Factor", "3", "+", "Term", "Factor", "9"), result)

        val tree2 = parser.evaluateExpression("1 + 2 * 3 + 4")
        val result2 = mutableListOf<String>()
        tree2.root?.let { traverseParseTree(it, result2) }
        assertEquals(listOf("Expression", "Term", "Factor", "1", "+", "Term", "Factor", "2", "*",
            "Factor", "3", "+", "Term", "Factor", "4"), result2)

        val tree3 = parser.evaluateExpression("1 + 2 * (3 + 4) * 5")
        val result3 = mutableListOf<String>()
        tree3.root?.let { traverseParseTree(it, result3) }
        assertEquals(listOf("Expression", "Term", "Factor", "1", "+", "Term", "Factor", "2", "*",
            "Factor", "Expression", "Term", "Factor", "3", "+", "Term", "Factor", "4", "*", "Factor", "5"), result3)

        val tree4 = parser.evaluateExpression("y_10_x + 2 * (xxx_ + 4) * 5 + !228")
        val result4 = mutableListOf<String>()
        tree4.root?.let { traverseParseTree(it, result4) }
        assertEquals(listOf("Expression", "Term", "Factor", "y_10_x", "+", "Term", "Factor", "2",
            "*", "Factor", "Expression", "Term", "Factor", "xxx_", "+", "Term", "Factor", "4", "*",
            "Factor", "5", "+", "Term", "Factor", "!228"), result4)

        tree4.prettyPrint()
    }

    @Test
    fun testEvaluate() {
        val parser = ExpressionParser()

        val tree = parser.evaluateExpression("(1 + 2) * (7 + 3) + 9")
        assertEquals(39, tree.evaluate())

        val tree2 = parser.evaluateExpression("1 + 2 * 3 + 4")
        assertEquals(11, tree2.evaluate())

        val tree3 = parser.evaluateExpression("1 + 2 * (3 + 4) * 5")
        assertEquals(71, tree3.evaluate())
    }

    @Test
    fun testPerformance() {
        val parser = ExpressionParser()

        var ans = parser.evaluateLine("42")
        assertEquals(42, ans)

        ans = parser.evaluateLine("10 * (5 + 7)")
        assertEquals(120, ans)

        ans = parser.evaluateLine("let x = 2 * 7")
        assertEquals(null, ans)

        ans = parser.evaluateLine("x + 1")
        assertEquals(15, ans)

        ans = parser.evaluateLine("let x = x / 5")
        assertEquals(null, ans)

        ans = parser.evaluateLine("x")
        assertEquals(2, ans)
    }
}