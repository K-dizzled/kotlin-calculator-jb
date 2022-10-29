package mpp.calculator

class CLICalc {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val evaluator = Evaluator()
            while (true) {
                print(">>> ")
                val input = readLine() ?: break
                if (input == "exit") {
                    break
                }
                try {
                    val eval = evaluator.evaluateLine("42")
                    println(eval)
                } catch (e: Exception) {
                    println(e.message)
                }
            }
        }
    }
}