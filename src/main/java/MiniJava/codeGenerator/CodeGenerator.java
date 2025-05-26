package MiniJava.codeGenerator;

import MiniJava.Log.Log;
import MiniJava.errorHandler.ErrorHandler;
import MiniJava.scanner.token.Token;
import MiniJava.semantic.SymbolOperations;
import MiniJava.semantic.symbol.Symbol;
import MiniJava.semantic.symbol.SymbolTable;
import MiniJava.semantic.symbol.SymbolType;

import java.util.Stack;

/**
 * Created by Alireza on 6/27/2015.
 */
public class CodeGenerator {
    private final MemoryOperations memoryOps;
    private final SymbolOperations symbolOps;
    private final Stack<Address> ss;
    private final Stack<String> symbolStack;
    private final Stack<String> callStack;
    private final Memory memory;

    public CodeGenerator() {
        this.memory = new Memory();
        this.memoryOps = new MemoryOperations(memory);
        this.symbolOps = new SymbolOperations(new SymbolTable(memory));
        this.ss = new Stack<>();
        this.symbolStack = new Stack<>();
        this.callStack = new Stack<>();
    }

    /**
     * Query method: Get the current memory state
     */
    public String getMemoryState() {
        return memoryOps.getMemoryState();
    }

    /**
     * Query method: Get the current stack state
     */
    public Stack<Address> getStackState() {
        return new Stack<Address>() {{ addAll(ss); }};
    }

    /**
     * Query method: Check if symbol exists
     */
    public boolean hasSymbol(String className, String methodName, String symbolName) {
        return symbolOps.hasSymbol(className, methodName, symbolName);
    }

    /**
     * Modifier method: Print the memory contents
     */
    public void printMemory() {
        memory.pintCodeBlock();
    }

    /**
     * Modifier method: Process semantic function
     */
    public void semanticFunction(int func, Token next) {
        Log.print("codegenerator : " + func);
        switch (func) {
            case 0:
                return;
            case 1:
                checkID();
                break;
            case 2:
                pid(next);
                break;
            case 3:
                fpid();
                break;
            case 4:
                kpid(next);
                break;
            case 5:
                intpid(next);
                break;
            case 6:
                startCall();
                break;
            case 7:
                call();
                break;
            case 8:
                arg();
                break;
            case 9:
                assign();
                break;
            case 10:
                add();
                break;
            case 11:
                sub();
                break;
            case 12:
                mult();
                break;
            case 13:
                label();
                break;
            case 14:
                save();
                break;
            case 15:
                _while();
                break;
            case 16:
                jpf_save();
                break;
            case 17:
                jpHere();
                break;
            case 18:
                print();
                break;
            case 19:
                equal();
                break;
            case 20:
                less_than();
                break;
            case 21:
                and();
                break;
            case 22:
                not();
                break;
            case 23:
                defClass();
                break;
            case 24:
                defMethod();
                break;
            case 25:
                popClass();
                break;
            case 26:
                extend();
                break;
            case 27:
                defField();
                break;
            case 28:
                defVar();
                break;
            case 29:
                methodReturn();
                break;
            case 30:
                defParam();
                break;
            case 31:
                lastTypeBool();
                break;
            case 32:
                lastTypeInt();
                break;
            case 33:
                defMain();
                break;
        }
    }

    /**
     * Query method: Check ID validity
     */
    private boolean isValidID() {
        return ss.peek().varType != varType.Non;
    }

    /**
     * Modifier method: Check and process ID
     */
    private void checkID() {
        symbolStack.pop();
        if (!isValidID()) {
            ErrorHandler.printError("Invalid ID type");
        }
    }

    /**
     * Query method: Get symbol information
     */
    private Symbol getSymbolInfo(String className, String methodName, String symbolName) {
        return symbolOps.getSymbol(className, methodName, symbolName);
    }

    /**
     * Modifier method: Process identifier
     */
    private void pid(Token next) {
        if (symbolStack.size() > 1) {
            String methodName = symbolStack.pop();
            String className = symbolStack.pop();
            
            Symbol symbol = getSymbolInfo(className, methodName, next.value);
            if (symbol != null) {
                varType type = (symbol.type == SymbolType.Bool) ? varType.Bool : varType.Int;
                ss.push(new Address(symbol.address, type));
            } else {
                ss.push(new Address(0, varType.Non));
            }
            
            symbolStack.push(className);
            symbolStack.push(methodName);
        } else {
            ss.push(new Address(0, varType.Non));
        }
        symbolStack.push(next.value);
    }

    /**
     * Modifier method: Process field identifier
     */
    private void fpid() {
        ss.pop();
        ss.pop();

        String methodName = symbolStack.pop();
        String className = symbolStack.pop();

        Symbol symbol = symbolOps.getMethodSymbol(className, methodName);
        if (symbol != null) {
            varType type = (symbol.type == SymbolType.Bool) ? varType.Bool : varType.Int;
            ss.push(new Address(symbol.address, type));
        }
    }

    /**
     * Query method: Get method address
     */
    private int getMethodAddress(String className, String methodName) {
        return symbolOps.getMethodAddress(className, methodName);
    }

    /**
     * Modifier method: Add new instruction
     */
    private void addInstruction(int num, Operation operation, Address op1, Address op2, Address op3) {
        memoryOps.addInstruction(num, operation, op1, op2, op3);
    }

    /**
     * Query method: Get current address
     */
    private int getCurrentAddress() {
        return memoryOps.getCurrentAddress();
    }

    /**
     * Modifier method: Allocate memory
     */
    private int allocateMemory(int size) {
        return memoryOps.allocateTemp();
    }

    private void defMain() {
        Address jpAddress = new Address(memoryOps.getCurrentAddress(), varType.Address);
        memoryOps.addInstruction(ss.pop().num, Operation.JP, jpAddress, null, null);
        String methodName = "main";
        String className = symbolStack.pop();

        symbolOps.addMethod(className, methodName, memoryOps.getCurrentAddress());

        symbolStack.push(className);
        symbolStack.push(methodName);
    }

    public void kpid(Token next) {
        Symbol symbol = symbolOps.getMethodSymbol(next.value, "");
        if (symbol != null) {
            varType type = (symbol.type == SymbolType.Bool) ? varType.Bool : varType.Int;
            ss.push(new Address(symbol.address, type));
        } else {
            ss.push(new Address(0, varType.Non));
        }
    }

    public void intpid(Token next) {
        ss.push(new Address(Integer.parseInt(next.value), varType.Int, TypeAddress.Imidiate));
    }

    public void startCall() {
        ss.pop();
        ss.pop();
        String methodName = symbolStack.pop();
        String className = symbolStack.pop();
        symbolOps.startCall(className, methodName);
        callStack.push(className);
        callStack.push(methodName);
    }

    public void call() {
        String methodName = callStack.pop();
        String className = callStack.pop();
        try {
            symbolOps.getNextParam(className, methodName);
            ErrorHandler.printError("The few argument pass for method");
        } catch (IndexOutOfBoundsException e) {
        }
        varType t = varType.Int;
        switch (symbolOps.getMethodReturnType(className, methodName)) {
            case Bool:
                t = varType.Bool;
                break;
            case Int:
                t = varType.Int;
                break;
        }
        Address temp = new Address(memoryOps.allocateTemp(), t);
        ss.push(temp);
        memoryOps.addInstruction(Operation.ASSIGN, new Address(temp.num, varType.Address, TypeAddress.Imidiate), 
                               new Address(symbolOps.getMethodReturnAddress(className, methodName), varType.Address), null);
        memoryOps.addInstruction(Operation.ASSIGN, new Address(memoryOps.getCurrentAddress() + 2, varType.Address, TypeAddress.Imidiate), 
                               new Address(symbolOps.getMethodCallerAddress(className, methodName), varType.Address), null);
        memoryOps.addInstruction(Operation.JP, new Address(symbolOps.getMethodAddress(className, methodName), varType.Address), null, null);
    }

    public void arg() {
        String methodName = callStack.pop();
        try {
            Symbol s = symbolOps.getNextParam(callStack.peek(), methodName);
            varType t = varType.Int;
            switch (s.type) {
                case Bool:
                    t = varType.Bool;
                    break;
                case Int:
                    t = varType.Int;
                    break;
            }
            Address param = ss.pop();
            if (param.varType != t) {
                ErrorHandler.printError("The argument type isn't match");
            }
            memoryOps.addInstruction(Operation.ASSIGN, param, new Address(s.address, t), null);

        } catch (IndexOutOfBoundsException e) {
            ErrorHandler.printError("Too many arguments pass for method");
        }
        callStack.push(methodName);
    }

    public void assign() {
        Address s1 = ss.pop();
        Address s2 = ss.pop();
        if (s1.varType != s2.varType) {
            ErrorHandler.printError("The type of operands in assign is different ");
        }
        memoryOps.addInstruction(Operation.ASSIGN, s1, s2, null);
    }

    public void add() {
        Address temp = new Address(memoryOps.allocateTemp(), varType.Int);
        Address s2 = ss.pop();
        Address s1 = ss.pop();

        if (s1.varType != varType.Int || s2.varType != varType.Int) {
            ErrorHandler.printError("In add two operands must be integer");
        }
        memoryOps.addInstruction(Operation.ADD, s1, s2, temp);
        ss.push(temp);
    }

    public void sub() {
        Address temp = new Address(memoryOps.allocateTemp(), varType.Int);
        Address s2 = ss.pop();
        Address s1 = ss.pop();
        if (s1.varType != varType.Int || s2.varType != varType.Int) {
            ErrorHandler.printError("In sub two operands must be integer");
        }
        memoryOps.addInstruction(Operation.SUB, s1, s2, temp);
        ss.push(temp);
    }

    public void mult() {
        Address temp = new Address(memoryOps.allocateTemp(), varType.Int);
        Address s2 = ss.pop();
        Address s1 = ss.pop();
        if (s1.varType != varType.Int || s2.varType != varType.Int) {
            ErrorHandler.printError("In mult two operands must be integer");
        }
        memoryOps.addInstruction(Operation.MULT, s1, s2, temp);
        ss.push(temp);
    }

    public void label() {
        ss.push(new Address(memory.getCurrentCodeBlockAddress(), varType.Address));
    }

    public void save() {
        ss.push(new Address(memoryOps.saveMemoryState(), varType.Address));
    }

    public void _while() {
        memoryOps.addInstruction(ss.pop().num, Operation.JPF, ss.pop(), new Address(memoryOps.getCurrentAddress() + 1, varType.Address), null);
        memoryOps.addInstruction(Operation.JP, ss.pop(), null, null);
    }

    public void jpf_save() {
        Address save = new Address(memoryOps.saveMemoryState(), varType.Address);
        memoryOps.addInstruction(ss.pop().num, Operation.JPF, ss.pop(), new Address(memoryOps.getCurrentAddress(), varType.Address), null);
        ss.push(save);
    }

    public void jpHere() {
        memoryOps.addInstruction(ss.pop().num, Operation.JP, new Address(memoryOps.getCurrentAddress(), varType.Address), null, null);
    }

    public void print() {
        memoryOps.addInstruction(Operation.PRINT, ss.pop(), null, null);
    }

    public void equal() {
        Address temp = new Address(memoryOps.allocateTemp(), varType.Bool);
        Address s2 = ss.pop();
        Address s1 = ss.pop();
        if (s1.varType != s2.varType) {
            ErrorHandler.printError("The type of operands in equal operator is different");
        }
        memoryOps.addInstruction(Operation.EQ, s1, s2, temp);
        ss.push(temp);
    }

    public void less_than() {
        Address temp = new Address(memoryOps.allocateTemp(), varType.Bool);
        Address s2 = ss.pop();
        Address s1 = ss.pop();
        if (s1.varType != varType.Int || s2.varType != varType.Int) {
            ErrorHandler.printError("The type of operands in less than operator is different");
        }
        memoryOps.addInstruction(Operation.LT, s1, s2, temp);
        ss.push(temp);
    }

    public void and() {
        Address temp = new Address(memoryOps.allocateTemp(), varType.Bool);
        Address s2 = ss.pop();
        Address s1 = ss.pop();
        if (s1.varType != varType.Bool || s2.varType != varType.Bool) {
            ErrorHandler.printError("In and operator the operands must be boolean");
        }
        memoryOps.addInstruction(Operation.AND, s1, s2, temp);
        ss.push(temp);
    }

    public void not() {
        Address temp = new Address(memoryOps.allocateTemp(), varType.Bool);
        Address s2 = ss.pop();
        Address s1 = ss.pop();
        if (s1.varType != varType.Bool) {
            ErrorHandler.printError("In not operator the operand must be boolean");
        }
        memoryOps.addInstruction(Operation.NOT, s1, s2, temp);
        ss.push(temp);
    }

    public void defClass() {
        ss.pop();
        symbolOps.addClass(symbolStack.peek());
    }

    public void defMethod() {
        ss.pop();
        String methodName = symbolStack.pop();
        String className = symbolStack.pop();

        symbolOps.addMethod(className, methodName, memoryOps.getCurrentAddress());

        symbolStack.push(className);
        symbolStack.push(methodName);
    }

    public void popClass() {
        symbolStack.pop();
    }

    public void extend() {
        ss.pop();
        symbolOps.setSuperClass(symbolStack.pop(), symbolStack.peek());
    }

    public void defField() {
        ss.pop();
        symbolOps.addField(symbolStack.pop(), symbolStack.peek());
    }

    public void defVar() {
        ss.pop();

        String var = symbolStack.pop();
        String methodName = symbolStack.pop();
        String className = symbolStack.pop();

        symbolOps.addMethodLocalVariable(className, methodName, var);

        symbolStack.push(className);
        symbolStack.push(methodName);
    }

    public void methodReturn() {
        String methodName = symbolStack.pop();
        Address s = ss.pop();
        SymbolType t = symbolOps.getMethodReturnType(symbolStack.peek(), methodName);
        varType temp = varType.Int;
        switch (t) {
            case Bool:
                temp = varType.Bool;
                break;
            case Int:
                break;
        }
        if (s.varType != temp) {
            ErrorHandler.printError("The type of method and return address was not match");
        }
        memoryOps.addInstruction(Operation.ASSIGN, s, 
                               new Address(symbolOps.getMethodReturnAddress(symbolStack.peek(), methodName), varType.Address, TypeAddress.Indirect), null);
        memoryOps.addInstruction(Operation.JP, 
                               new Address(symbolOps.getMethodCallerAddress(symbolStack.peek(), methodName), varType.Address), null, null);
    }

    public void defParam() {
        ss.pop();
        String param = symbolStack.pop();
        String methodName = symbolStack.pop();
        String className = symbolStack.pop();

        symbolOps.addMethodParameter(className, methodName, param);

        symbolStack.push(className);
        symbolStack.push(methodName);
    }

    public void lastTypeBool() {
        symbolOps.setLastType(SymbolType.Bool);
    }

    public void lastTypeInt() {
        symbolOps.setLastType(SymbolType.Int);
    }

    public void main() {

    }
}
