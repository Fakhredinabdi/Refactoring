package MiniJava.codeGenerator;

/**
 * AND operation strategy
 */
public class AndStrategy extends LogicalStrategy {
    @Override
    protected Operation getOperation() {
        return Operation.AND;
    }

    @Override
    protected boolean isSingleOperand() {
        return false;
    }
}