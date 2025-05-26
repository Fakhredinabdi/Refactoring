package MiniJava.codeGenerator;

/**
 * Base class for logical operations (AND, NOT)
 */
public abstract class LogicalStrategy implements OperationStrategy {
    @Override
    public Address execute(Memory memory, Address s1, Address s2) {
        if (s1.varType != varType.Bool || (!isSingleOperand() && s2.varType != varType.Bool)) {
            throw new RuntimeException("In " + getOperation() + " operation, operands must be boolean");
        }
        Address temp = new Address(memory.getTemp(), varType.Bool);
        memory.add3AddressCode(getOperation(), s1, s2, temp);
        return temp;
    }

    /**
     * Get the operation type
     * @return operation type
     */
    protected abstract Operation getOperation();

    /**
     * Check if operation has single operand
     * @return true if operation has single operand
     */
    protected abstract boolean isSingleOperand();
} 