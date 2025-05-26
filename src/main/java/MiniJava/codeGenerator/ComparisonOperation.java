package MiniJava.codeGenerator;

/**
 * Base class for comparison operations (EQ, LT)
 */
public abstract class ComparisonOperation implements OperationStrategy {
    protected abstract Operation getOperation();

    protected abstract boolean requiresIntOperands();

    @Override
    public Address execute(Memory memory, Address s1, Address s2) {
        if (requiresIntOperands()) {
            if (s1.varType != varType.Int || s2.varType != varType.Int) {
                throw new RuntimeException("In " + getOperation() + " operation, operands must be integer");
            }
        } else if (s1.varType != s2.varType) {
            throw new RuntimeException("In " + getOperation() + " operation, operands must be of the same type");
        }
        Address temp = new Address(memory.getTemp(), varType.Bool);
        memory.add3AddressCode(getOperation(), s1, s2, temp);
        return temp;
    }
}

/**
 * Equal operation
 */
class EqualOperation extends ComparisonOperation {
    @Override
    protected Operation getOperation() {
        return Operation.EQ;
    }

    @Override
    protected boolean requiresIntOperands() {
        return false;
    }
}

/**
 * Less than operation
 */
class LessThanOperation extends ComparisonOperation {
    @Override
    protected Operation getOperation() {
        return Operation.LT;
    }

    @Override
    protected boolean requiresIntOperands() {
        return true;
    }
}