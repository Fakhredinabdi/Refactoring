package MiniJava.codeGenerator;

/**
 * NOT operation strategy
 */
public class NotStrategy extends LogicalStrategy {
    @Override
    protected Operation getOperation() {
        return Operation.NOT;
    }

    @Override
    protected boolean isSingleOperand() {
        return true;
    }
}