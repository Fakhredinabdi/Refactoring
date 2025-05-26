package MiniJava.codeGenerator;

/**
 * Addition operation strategy
 */
public class AddStrategy extends ArithmeticStrategy {
    @Override
    protected Operation getOperation() {
        return Operation.ADD;
    }
}