package MiniJava.codeGenerator;

/**
 * Base class for arithmetic operations (ADD, SUB, MULT)
 */
public abstract class ArithmeticOperation implements OperationStrategy {
    protected abstract Operation getOperation();

    @Override
    public Address execute(Memory memory, Address s1, Address s2) {
        if (s1.varType != varType.Int || s2.varType != varType.Int) {
            throw new RuntimeException("In " + getOperation() + " operation, operands must be integer");
        }
        Address temp = new Address(memory.getTemp(), varType.Int);
        memory.add3AddressCode(getOperation(), s1, s2, temp);
        return temp;
    }
}

/**
 * Addition operation
 */
class AddOperation extends ArithmeticOperation {
    @Override
    protected Operation getOperation() {
        return Operation.ADD;
    }
}

/**
 * Subtraction operation
 */
class SubOperation extends ArithmeticOperation {
    @Override
    protected Operation getOperation() {
        return Operation.SUB;
    }
}

/**
 * Multiplication operation
 */
class MultOperation extends ArithmeticOperation {
    @Override
    protected Operation getOperation() {
        return Operation.MULT;
    }
} 