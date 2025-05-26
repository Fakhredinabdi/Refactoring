package MiniJava.codeGenerator;

/**
 * Base class for logical operations (AND, NOT)
 */
public abstract class LogicalOperation implements OperationStrategy {
    protected abstract Operation getOperation();
    protected abstract boolean isSingleOperand();

    @Override
    public Address execute(Memory memory, Address s1, Address s2) {
        if (s1.varType != varType.Bool || (!isSingleOperand() && s2.varType != varType.Bool)) {
            throw new RuntimeException("In " + getOperation() + " operation, operands must be boolean");
        }
        Address temp = new Address(memory.getTemp(), varType.Bool);
        memory.add3AddressCode(getOperation(), s1, s2, temp);
        return temp;
    }
}

/**
 * AND operation
 */
class AndOperation extends LogicalOperation {
    @Override
    protected Operation getOperation() {
        return Operation.AND;
    }

    @Override
    protected boolean isSingleOperand() {
        return false;
    }
}

/**
 * NOT operation
 */
class NotOperation extends LogicalOperation {
    @Override
    protected Operation getOperation() {
        return Operation.NOT;
    }

    @Override
    protected boolean isSingleOperand() {
        return true;
    }
} 