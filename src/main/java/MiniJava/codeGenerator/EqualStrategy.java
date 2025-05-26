package MiniJava.codeGenerator;

/**
 * Equal operation strategy
 */
public class EqualStrategy extends ComparisonStrategy {
    @Override
    protected Operation getOperation() {
        return Operation.EQ;
    }

    @Override
    protected boolean requiresIntOperands() {
        return false;
    }
} 