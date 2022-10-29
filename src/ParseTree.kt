package mpp.calculator

data class ParseNode (
    var value: ParseState,
    val children: MutableList<ParseNode> = mutableListOf()
)

class ParseTree(var root: ParseNode? = null, var registers: MutableMap<String, Int> = mutableMapOf()) {
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
            sb.append(toStr(node.value))
            println(sb.toString())
            for (child in node.children) {
                prettyPrint(child, depth + 1)
            }
        }
    }

    fun evaluate(): Int {
        return evaluate(root)
    }

    private fun evaluate(node: ParseNode?): Int {
        var result: Int

        if (node == null) {
            return 0
        }

        when (node.value) {
            is ParseState.Number -> {
                result = (node.value as ParseState.Number).value
            }
            is ParseState.Literal -> {
                if (registers.containsKey((node.value as ParseState.Literal).value)) {
                    result = registers[(node.value as ParseState.Literal).value] ?: 0
                } else {
                    throw VariableNotInitializedException((node.value as ParseState.Literal).value)
                }
            }
            is ParseState.Expression, ParseState.Term -> {
                result = evaluate(node.children[0])
                for (i in 1 until node.children.size step 2) {
                    val operator = node.children[i].value as ParseState.Operator
                    val right = evaluate(node.children[i + 1])
                    result = operator.value.apply(result, right)
                }
            }
            is ParseState.Factor -> {
                result = evaluate(node.children[0])
            }
            else -> {
                throw InvalidParseTreeException()
            }
        }

        return result
    }
}