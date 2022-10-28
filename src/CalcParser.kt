package mpp.calculator

enum class ArithmeticCommand {
    MINUS,
    PLUS,
    MULTIPLY,
    DIVISION;
}

sealed interface ParseState {
    object Expression : ParseState
    object Term : ParseState
    object Factor : ParseState
    data class Number(val value: Int) : ParseState
    data class Operator(val value: ArithmeticCommand) : ParseState
    data class Literal(val value: String) : ParseState
}

fun fromStr(str: String): ParseState = when (str) {
    "+" -> ParseState.Operator(ArithmeticCommand.PLUS)
    "-" -> ParseState.Operator(ArithmeticCommand.MINUS)
    "*" -> ParseState.Operator(ArithmeticCommand.MULTIPLY)
    "/" -> ParseState.Operator(ArithmeticCommand.DIVISION)
    else -> ParseState.Literal(str)
}

//enum class ParseState(value: Any) {
//    EXPR, TERM, FACTOR, NUMBER, OPERATOR
//}

data class ParseNode (
    var value: String,
    var type: ParseState,
    val children: MutableList<ParseNode> = mutableListOf()
)

class ParseTree(var root: ParseNode? = null) {
    fun prettyPrint() {
        if (root != null) {
            prettyPrint(root, 0)
        }
    }

    private fun prettyPrint(node: ParseNode?, depth: Int) {
        if (node != null) {
            val sb = StringBuilder()
            for (i in 0 until depth) {
                sb.append("  ")
            }
            sb.append(node.value)
            println(sb.toString())
            for (child in node.children) {
                prettyPrint(child, depth + 1)
            }
        }
    }
}

class Validator {
    fun isOperator(char: Char): Boolean {
        return char == '+' || char == '-' || char == '*' || char == '/'
    }
}

class CalcParser {
    fun tokenize(expression: String): MutableList<String> {
        val tokens = mutableListOf<String>()
        for (c in expression) {
            if (c != ' ') {
                tokens.add(c.toString())
            }
        }
        return tokens
    }

    fun evaluateExpression(str: String): ParseTree {
        val tokens = tokenize(str)
        val root = parseExpression(tokens)
        return ParseTree(root)
    }

    private fun parseExpression(tokens: MutableList<String>): ParseNode {
        val node = ParseNode(value = "EXPR", type = ParseState.EXPR)
        node.children.add(parseTerm(tokens))
        while (tokens.isNotEmpty()) {
            if (tokens[0] == "+" || tokens[0] == "-") {
                node.children.add(parseOperator(tokens))
                node.children.add(parseTerm(tokens))
            } else {
                break
            }
        }
        return node
    }

    private fun parseTerm(tokens: MutableList<String>): ParseNode {
        val node = ParseNode(value = "TERM", type = ParseState.TERM)
        node.children.add(parseFactor(tokens))
        while (tokens.isNotEmpty()) {
            if (tokens[0] == "*" || tokens[0] == "/") {
                node.children.add(parseOperator(tokens))
                node.children.add(parseFactor(tokens))
            } else {
                break
            }
        }
        return node
    }

    private fun parseFactor(tokens: MutableList<String>): ParseNode {
        val node = ParseNode(value = "FACTOR", type = ParseState.FACTOR)
        if (tokens[0].matches("-?\\d+".toRegex())) {
            node.children.add(parseNumber(tokens))
        } else if (tokens[0] == "(") {
            tokens.removeAt(0)
            node.children.add(parseExpression(tokens))
            if (tokens[0] == ")") {
                tokens.removeAt(0)
            } else {
                throw IllegalArgumentException("Expected ')', got ${tokens[0]}")
            }
        } else {
            throw IllegalArgumentException("Expected number or '(', got ${tokens[0]}")
        }
        return node
    }

    private fun parseNumber(tokens: MutableList<String>): ParseNode {
        val node = ParseNode(value = tokens[0], type = ParseState.NUMBER)
        tokens.removeAt(0)
        return node
    }

    private fun parseOperator(tokens: MutableList<String>): ParseNode {
        val node = ParseNode(value = tokens[0], type = ParseState.OPERATOR)
        tokens.removeAt(0)
        return node
    }
}