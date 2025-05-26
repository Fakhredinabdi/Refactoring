package MiniJava.codeGenerator;

/**
 * Multiplication operation strategy
 */
public class MultStrategy extends ArithmeticStrategy {
    @Override
    protected Operation getOperation() {
        return Operation.MULT;
    }
}