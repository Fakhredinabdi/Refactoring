package MiniJava.codeGenerator;

/**
 * Base class for comparison operations (EQ, LT)
 */
public abstract class ComparisonStrategy implements OperationStrategy {
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

    protected abstract Operation getOperation();

    protected abstract boolean requiresIntOperands();
}