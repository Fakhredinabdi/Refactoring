package MiniJava.codeGenerator;

import MiniJava.scanner.token.Token;

public class CodeGenerationFacade {
    private final CodeGenerator codeGenerator;

    public CodeGenerationFacade() {
        this.codeGenerator = new CodeGenerator();
    }

    public void executeSemanticFunction(int func, Token next) {
        codeGenerator.semanticFunction(func, next);
    }

    public void printGeneratedCode() {
        codeGenerator.printMemory();
    }
}