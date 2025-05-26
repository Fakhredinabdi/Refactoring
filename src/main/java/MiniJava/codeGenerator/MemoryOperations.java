package MiniJava.codeGenerator;

/**
 * A class that separates memory operations into distinct query and modifier methods.
 */
public class MemoryOperations {
    private final Memory memory;

    public MemoryOperations(Memory memory) {
        this.memory = memory;
    }

    /**
     * Query method: Get the current code block address without modifying state
     *
     * @return current address
     */
    public int getCurrentAddress() {
        return memory.getCurrentCodeBlockAddress();
    }

    /**
     * Query method: Get the next data address without modifying state
     *
     * @return next available data address
     */
    public int getNextDataAddress() {
        return memory.getDateAddress();
    }

    /**
     * Query method: Check if an address is within valid range
     *
     * @param address
     *            the address to check
     *
     * @return true if the address is valid
     */
    public boolean isValidAddress(int address) {
        return address >= 200 && address < memory.getCurrentCodeBlockAddress(); // Using known memory boundaries
    }

    /**
     * Modifier method: Add a new three-address code instruction
     *
     * @param num
     *            instruction number
     * @param operation
     *            operation type
     * @param operand1
     *            first operand
     * @param operand2
     *            second operand
     * @param operand3
     *            third operand
     */
    public void addInstruction(int num, Operation operation, Address operand1, Address operand2, Address operand3) {
        memory.add3AddressCode(num, operation, operand1, operand2, operand3);
    }

    /**
     * Modifier method: Add a new three-address code instruction at the end
     *
     * @param operation
     *            operation type
     * @param operand1
     *            first operand
     * @param operand2
     *            second operand
     * @param operand3
     *            third operand
     */
    public void addInstruction(Operation operation, Address operand1, Address operand2, Address operand3) {
        memory.add3AddressCode(operation, operand1, operand2, operand3);
    }

    /**
     * Modifier method: Allocate new temporary memory space
     *
     * @return starting address of allocated space
     */
    public int allocateTemp() {
        return memory.getTemp();
    }

    /**
     * Query method: Get the memory state as string without modifying it
     *
     * @return memory state string
     */
    public String getMemoryState() {
        return memory.toString();
    }

    /**
     * Modifier method: Save current memory state
     *
     * @return address where the state was saved
     */
    public int saveMemoryState() {
        return memory.saveMemory();
    }
}