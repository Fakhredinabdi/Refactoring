package MiniJava.codeGenerator;

/**
 * Strategy interface for different operation types
 */
public interface OperationStrategy {
    /**
     * Execute the operation
     * @param memory memory instance for code generation
     * @param s1 first operand
     * @param s2 second operand
     * @return result address
     */
    Address execute(Memory memory, Address s1, Address s2);
} 