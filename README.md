# language-analyzer

A language lexical analyzer, parser, and interpreter.

# Code Organization
The main files of the analyzer is the `Parser.jj` and the `Interpreter.java` files. `Parser.jj` defines the context free grammar as defined in the Lab document. `Interpreter.java` defines different classes to hold semantics of the program execution.

### `Interpreter.java` ###
It follows the interpreter design pattern. Each statement (if, while, assignment, skip) implements the interface `Statement` and each class implements the `execute()` accrodingly.

### `Parser.jj` ###
The parser encapsulates the classes defined in the `Interpreter.java` and holds a `Context` which acts as a symbols table for the variables in the program.

# Tests
We wrote tests which read program from strings and files in the `examples` folder. 

# Note
We introduced the `block` production to the grammar.
```
block -> Com; Com
Com -> skip | var := AExp | if BExp then Block else Block | while BExp do Block
```
That does not change the grammar at all, since it accepts the same strings as the original grammar. It is just more convenient since a block can be defined semantically as a list of commands.
