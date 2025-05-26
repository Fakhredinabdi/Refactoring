package MiniJava;

import MiniJava.compiler.CompilerFacade;

public class Main {
    public static void main(String[] args) {
        compileSourceCode();
    }

    private static void compileSourceCode() {
        CompilerFacade compiler = new CompilerFacade();
        compiler.compile("src/main/resources/code");
    }
}
