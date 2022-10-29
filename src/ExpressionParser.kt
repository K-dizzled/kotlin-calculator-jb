package mpp.calculator

class ExpressionParser {
    var registers: MutableMap<String, Int> = mutableMapOf()

    fun tokenize(expression: String): MutableList<String> {
        val tokens = mutableListOf<String>()
        val specialSymbols = listOf("+", "-", "*", "/", "(", ")")
        var currentToken = ""
        for (char in expression) {
            when {
                char.isWhitespace() -> {
                    if (currentToken.isNotEmpty()) {
                        tokens.add(currentToken)
                        currentToken = ""
                    }
                }
                specialSymbols.contains(char.toString()) -> {
                    if (currentToken.isNotEmpty()) {
                        tokens.add(currentToken)
                        currentToken = ""
                    }
                    tokens.add(char.toString())
                }
                else -> {
                    currentToken += char
                }
            }
        }
        if (currentToken.isNotEmpty()) {
            tokens.add(currentToken)
        }
        return tokens
    }

    fun evaluateLine(str: String): Int? {
        var tokens = tokenize(str)
        return if (tokens.size >= 3 && tokens[0] == "let" && tokens[2] == "=") {
            val variable = tokens[1]
            tokens = tokens.drop(3).toMutableList()

            val root = parseExpression(tokens)
            registers[variable] = ParseTree(root, registers).evaluate()

            null
        } else {
            val root = parseExpression(tokens)
            ParseTree(root, registers).evaluate()
        }
    }

    fun evaluateExpression(str: String): ParseTree {
        val tokens = tokenize(str)
        val root = parseExpression(tokens)
        return ParseTree(root)
    }

    private fun addAndNext(tokens: MutableList<String>, node: ParseNode) {
        val token = tokens.removeAt(0)
        node.children.add(ParseNode(fromStr(token)))
    }

    private fun next(tokens: MutableList<String>) {
        tokens.removeAt(0)
    }

    private fun parseExpression(tokens: MutableList<String>): ParseNode {
        val node = ParseNode(ParseState.Expression)
        node.children.add(parseTerm(tokens))

        while (tokens.isNotEmpty()) {
            val state = fromStr(tokens.first())
            if (state is ParseState.Operator && state.value.order() == 1) {
                addAndNext(tokens, node)
                node.children.add(parseTerm(tokens))
            } else {
                break
            }
        }
        return node
    }

    private fun parseTerm(tokens: MutableList<String>): ParseNode {
        val node = ParseNode(ParseState.Term)
        node.children.add(parseFactor(tokens))

        while (tokens.isNotEmpty()) {
            val state = fromStr(tokens.first())
            if (state is ParseState.Operator && state.value.order() == 2) {
                addAndNext(tokens, node)
                node.children.add(parseFactor(tokens))
            } else {
                break
            }
        }
        return node
    }

    private fun parseFactor(tokens: MutableList<String>): ParseNode {
        val node = ParseNode(ParseState.Factor)
        var state = fromStr(tokens.first())

        when (state) {
            is ParseState.Number -> {
                addAndNext(tokens, node)
            }
            is ParseState.Literal -> {
                addAndNext(tokens, node)
            }
            is ParseState.LeftBracket -> {
                next(tokens)
                node.children.add(parseExpression(tokens))

                state = fromStr(tokens.first())
                if (state is ParseState.RightBracket) {
                    next(tokens)
                } else {
                    throw BadSyntaxException("Expected ')', got ${toStr(state)}")
                }
            }
            else -> {
                throw BadSyntaxException("Expected number or '(', got ${toStr(state)}")
            }
        }
        return node
    }
}