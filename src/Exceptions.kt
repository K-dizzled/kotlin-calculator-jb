package mpp.calculator

class BadSyntaxException(msg: String = "Bad Syntax") : Exception(msg)

class VariableNotInitializedException(msg: String = "No such variable") : Exception(msg)

class InvalidParseTreeException(msg: String = "Invalid Parse Tree") : Exception(msg)