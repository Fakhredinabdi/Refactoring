package MiniJava.codeGenerator;

/**
 * Less than operation strategy
 */
public class LessThanStrategy extends ComparisonStrategy {
    @Override
    protected Operation getOperation() {
        return Operation.LT;
    }

    @Override
    protected boolean requiresIntOperands() {
        return true;
    }
}