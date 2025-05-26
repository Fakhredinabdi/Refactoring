package MiniJava.codeGenerator;

/**
 * Subtraction operation strategy
 */
public class SubStrategy extends ArithmeticStrategy {
    @Override
    protected Operation getOperation() {
        return Operation.SUB;
    }
}