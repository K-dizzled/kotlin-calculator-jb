package mpp.calculator

class Evaluator {
    var registers: MutableMap<String, Int> = mutableMapOf()

    fun evaluateLine(str: String): Int? {
        val tokens = ExpressionParser.tokenize(str)

        return if (tokens.size >= 3 && tokens[0] == "let" && tokens[2] == "=") {
            evaluateStatement(tokens)
            null
        } else {
            evaluateExpression(tokens)
        }
    }

    private fun evaluateExpression(tokens: MutableList<String>): Int {
        val parser = ExpressionParser()
        val root = parser.parseExpression(tokens)
        return ParseTree(root, registers).evaluate()
    }

    private fun evaluateStatement(tokens: MutableList<String>) {
        val parser = ExpressionParser()
        val variable = tokens[1]
        val expr = tokens.drop(3).toMutableList()

        val root = parser.parseExpression(expr)
        registers[variable] = ParseTree(root, registers).evaluate()
    }

}