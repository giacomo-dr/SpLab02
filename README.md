# SpLab02
Software Performance Lab 02 - Simple Bytecode Metrics

---

Write a Java class that analyzes all methods of all classes in the specified JAR files. The analysis has to compute the following metrics:

- Number of classes (incl. interfaces and enums)
- Number of concrete non-native, non-abstract methods (methods with code)
- Total number of instructions (you have to avoid ASM internal instructions with opcode -1)
- Total number of instructions by opcode (ignoring instructions with opcode -1)
- Total number of method invocation instructions (== call sites)
- Total number of conditional branch instructions (this excludes GOTO, but it includes multi-way branches)
