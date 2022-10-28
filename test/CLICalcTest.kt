package mpp.calculator

import org.junit.Test
import kotlin.test.assertEquals

class CLICalcTest {

    @Test
    fun testTokenize() {
        val parser = CalcParser()
        val tokens = parser.tokenize("1+2")
        assertEquals(listOf("1", "+", "2"), tokens)

        val tokens2 = parser.tokenize("1+2*3")
        assertEquals(listOf("1", "+", "2", "*", "3"), tokens2)

        val tokens3 = parser.tokenize("1 + 2 * 3 + 4")
        assertEquals(listOf("1", "+", "2", "*", "3", "+", "4"), tokens3)

        val tokens4 = parser.tokenize("(1+2)*3 +4 *(5)+7")
        assertEquals(listOf("(", "1", "+", "2", ")", "*", "3", "+", "4", "*", "(", "5", ")", "+", "7"), tokens4)
    }

    @Test
    fun testAdd() {
        val parser = CalcParser()

        val tree = parser.evaluateExpression("(2 + 7) + 3")

        tree.prettyPrint()
    }
}