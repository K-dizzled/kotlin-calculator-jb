package mpp.calculator

enum class ArithmeticCommand {
    MINUS,
    PLUS,
    MULTIPLY,
    DIVISION;

    fun order(): Int = when (this) {
        MINUS -> 1
        PLUS -> 1
        MULTIPLY -> 2
        DIVISION -> 2
    }

    fun apply(a: Int, b: Int): Int = when (this) {
        MINUS -> a - b
        PLUS -> a + b
        MULTIPLY -> a * b
        DIVISION -> a / b
    }
}

sealed interface ParseState {
    object Expression : ParseState
    object Term : ParseState
    object Factor : ParseState
    data class Number(val value: Int) : ParseState
    data class Operator(val value: ArithmeticCommand) : ParseState
    data class Literal(val value: String) : ParseState
    object LeftBracket : ParseState
    object RightBracket : ParseState
}

fun isNumeric(toCheck: String): Boolean {
    return toCheck.all { char -> char.isDigit() }
}

fun fromStr(str: String): ParseState = when (str) {
    (if (isNumeric(str)) str else "") -> ParseState.Number(str.toInt())
    "+" -> ParseState.Operator(ArithmeticCommand.PLUS)
    "-" -> ParseState.Operator(ArithmeticCommand.MINUS)
    "*" -> ParseState.Operator(ArithmeticCommand.MULTIPLY)
    "/" -> ParseState.Operator(ArithmeticCommand.DIVISION)
    "(" -> ParseState.LeftBracket
    ")" -> ParseState.RightBracket
    else -> ParseState.Literal(str)
}

fun toStr(state: ParseState): String = when (state) {
    is ParseState.Number -> state.value.toString()
    is ParseState.Operator -> when (state.value) {
        ArithmeticCommand.PLUS -> "+"
        ArithmeticCommand.MINUS -> "-"
        ArithmeticCommand.MULTIPLY -> "*"
        ArithmeticCommand.DIVISION -> "/"
    }
    is ParseState.Literal -> state.value
    is ParseState.Expression -> "Expression"
    is ParseState.Term -> "Term"
    is ParseState.Factor -> "Factor"
    is ParseState.LeftBracket -> "("
    is ParseState.RightBracket -> ")"
}