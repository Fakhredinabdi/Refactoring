package MiniJava.codeGenerator;

/**
 * Base class for arithmetic operations (ADD, SUB, MULT)
 */
public abstract class ArithmeticStrategy implements OperationStrategy {
    @Override
    public Address execute(Memory memory, Address s1, Address s2) {
        if (s1.varType != varType.Int || s2.varType != varType.Int) {
            throw new RuntimeException("In " + getOperation() + " operation, operands must be integer");
        }
        Address temp = new Address(memory.getTemp(), varType.Int);
        memory.add3AddressCode(getOperation(), s1, s2, temp);
        return temp;
    }

    /**
     * Get the operation type
     *
     * @return operation type
     */
    protected abstract Operation getOperation();
}