package MiniJava.codeGenerator;

import MiniJava.Log.Log;
import MiniJava.errorHandler.ErrorHandler;
import MiniJava.scanner.token.Token;
import MiniJava.semantic.symbol.Symbol;
import MiniJava.semantic.symbol.SymbolTable;
import MiniJava.semantic.symbol.SymbolType;

import java.util.Stack;
import java.util.Map;
import java.util.HashMap;

/**
 * Created by Alireza on 6/27/2015.
 */
public class CodeGenerator {
    private Memory memory;
    private Stack<Address> ss;
    private Stack<String> symbolStack;
    private Stack<String> callStack;
    private SymbolTable symbolTable;
    private final Map<Operation, OperationStrategy> operationStrategies;

    public CodeGenerator() {
        memory = new Memory();
        ss = new Stack<>();
        symbolStack = new Stack<>();
        callStack = new Stack<>();
        symbolTable = new SymbolTable(memory);

        // Initialize operation strategies
        operationStrategies = new HashMap<>();
        operationStrategies.put(Operation.ADD, new AddStrategy());
        operationStrategies.put(Operation.SUB, new SubStrategy());
        operationStrategies.put(Operation.MULT, new MultStrategy());
        operationStrategies.put(Operation.AND, new AndStrategy());
        operationStrategies.put(Operation.NOT, new NotStrategy());
        operationStrategies.put(Operation.EQ, new EqualStrategy());
        operationStrategies.put(Operation.LT, new LessThanStrategy());
    }

    public void printMemory() {
        memory.pintCodeBlock();
    }

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

    private void defMain() {
        // ss.pop();
        memory.add3AddressCode(ss.pop().num, Operation.JP,
                new Address(memory.getCurrentCodeBlockAddress(), varType.Address), null, null);
        String methodName = "main";
        String className = symbolStack.pop();

        symbolTable.addMethod(className, methodName, memory.getCurrentCodeBlockAddress());

        symbolStack.push(className);
        symbolStack.push(methodName);
    }

    // public void spid(Token next){
    // symbolStack.push(next.value);
    // }
    public void checkID() {
        symbolStack.pop();
        if (ss.peek().varType == varType.Non) {
            // TODO : error
        }
    }

    public void pid(Token next) {
        if (symbolStack.size() > 1) {
            String methodName = symbolStack.pop();
            String className = symbolStack.pop();
            try {

                Symbol s = symbolTable.get(className, methodName, next.value);
                varType t = varType.Int;
                switch (s.type) {
                case Bool:
                    t = varType.Bool;
                    break;
                case Int:
                    t = varType.Int;
                    break;
                }
                ss.push(new Address(s.address, t));

            } catch (Exception e) {
                ss.push(new Address(0, varType.Non));
            }
            symbolStack.push(className);
            symbolStack.push(methodName);
        } else {
            ss.push(new Address(0, varType.Non));
        }
        symbolStack.push(next.value);
    }

    public void fpid() {
        ss.pop();
        ss.pop();

        Symbol s = symbolTable.get(symbolStack.pop(), symbolStack.pop());
        varType t = varType.Int;
        switch (s.type) {
        case Bool:
            t = varType.Bool;
            break;
        case Int:
            t = varType.Int;
            break;
        }
        ss.push(new Address(s.address, t));

    }

    public void kpid(Token next) {
        ss.push(symbolTable.get(next.value));
    }

    public void intpid(Token next) {
        ss.push(new Address(Integer.parseInt(next.value), varType.Int, TypeAddress.Imidiate));
    }

    public void startCall() {
        // TODO: method ok
        ss.pop();
        ss.pop();
        String methodName = symbolStack.pop();
        String className = symbolStack.pop();
        symbolTable.startCall(className, methodName);
        callStack.push(className);
        callStack.push(methodName);

        // symbolStack.push(methodName);
    }

    public void call() {
        // TODO: method ok
        String methodName = callStack.pop();
        String className = callStack.pop();
        try {
            symbolTable.getNextParam(className, methodName);
            ErrorHandler.printError("The few argument pass for method");
        } catch (IndexOutOfBoundsException e) {
        }
        varType t = varType.Int;
        switch (symbolTable.getMethodReturnType(className, methodName)) {
        case Int:
            t = varType.Int;
            break;
        case Bool:
            t = varType.Bool;
            break;
        }
        Address temp = new Address(memory.getTemp(), t);
        ss.push(temp);
        memory.add3AddressCode(Operation.ASSIGN, new Address(temp.num, varType.Address, TypeAddress.Imidiate),
                new Address(symbolTable.getMethodReturnAddress(className, methodName), varType.Address), null);
        memory.add3AddressCode(Operation.ASSIGN,
                new Address(memory.getCurrentCodeBlockAddress() + 2, varType.Address, TypeAddress.Imidiate),
                new Address(symbolTable.getMethodCallerAddress(className, methodName), varType.Address), null);
        memory.add3AddressCode(Operation.JP,
                new Address(symbolTable.getMethodAddress(className, methodName), varType.Address), null, null);

        // symbolStack.pop();
    }

    public void arg() {
        // TODO: method ok

        String methodName = callStack.pop();
        // String className = symbolStack.pop();
        try {
            Symbol s = symbolTable.getNextParam(callStack.peek(), methodName);
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
            memory.add3AddressCode(Operation.ASSIGN, param, new Address(s.address, t), null);

            // symbolStack.push(className);

        } catch (IndexOutOfBoundsException e) {
            ErrorHandler.printError("Too many arguments pass for method");
        }
        callStack.push(methodName);

    }

    public void assign() {
        Address s1 = ss.pop();
        Address s2 = ss.pop();
        // try {
        if (s1.varType != s2.varType) {
            ErrorHandler.printError("The type of operands in assign is different ");
        }
        // }catch (NullPointerException d)
        // {
        // d.printStackTrace();
        // }
        memory.add3AddressCode(Operation.ASSIGN, s1, s2, null);
    }

    public void add() {
        executeOperation(Operation.ADD);
    }

    public void sub() {
        executeOperation(Operation.SUB);
    }

    public void mult() {
        executeOperation(Operation.MULT);
    }

    public void label() {
        ss.push(new Address(memory.getCurrentCodeBlockAddress(), varType.Address));
    }

    public void save() {
        ss.push(new Address(memory.saveMemory(), varType.Address));
    }

    public void _while() {
        memory.add3AddressCode(ss.pop().num, Operation.JPF, ss.pop(),
                new Address(memory.getCurrentCodeBlockAddress() + 1, varType.Address), null);
        memory.add3AddressCode(Operation.JP, ss.pop(), null, null);
    }

    public void jpf_save() {
        Address save = new Address(memory.saveMemory(), varType.Address);
        memory.add3AddressCode(ss.pop().num, Operation.JPF, ss.pop(),
                new Address(memory.getCurrentCodeBlockAddress(), varType.Address), null);
        ss.push(save);
    }

    public void jpHere() {
        memory.add3AddressCode(ss.pop().num, Operation.JP,
                new Address(memory.getCurrentCodeBlockAddress(), varType.Address), null, null);
    }

    public void print() {
        memory.add3AddressCode(Operation.PRINT, ss.pop(), null, null);
    }

    public void equal() {
        executeOperation(Operation.EQ);
    }

    public void less_than() {
        executeOperation(Operation.LT);
    }

    public void and() {
        executeOperation(Operation.AND);
    }

    public void not() {
        executeOperation(Operation.NOT);
    }

    public void defClass() {
        ss.pop();
        symbolTable.addClass(symbolStack.peek());
    }

    public void defMethod() {
        ss.pop();
        String methodName = symbolStack.pop();
        String className = symbolStack.pop();

        symbolTable.addMethod(className, methodName, memory.getCurrentCodeBlockAddress());

        symbolStack.push(className);
        symbolStack.push(methodName);
    }

    public void popClass() {
        symbolStack.pop();
    }

    public void extend() {
        ss.pop();
        symbolTable.setSuperClass(symbolStack.pop(), symbolStack.peek());
    }

    public void defField() {
        ss.pop();
        symbolTable.addField(symbolStack.pop(), symbolStack.peek());
    }

    public void defVar() {
        ss.pop();

        String var = symbolStack.pop();
        String methodName = symbolStack.pop();
        String className = symbolStack.pop();

        symbolTable.addMethodLocalVariable(className, methodName, var);

        symbolStack.push(className);
        symbolStack.push(methodName);
    }

    public void methodReturn() {
        // TODO : call ok

        String methodName = symbolStack.pop();
        Address s = ss.pop();
        SymbolType t = symbolTable.getMethodReturnType(symbolStack.peek(), methodName);
        varType temp = varType.Int;
        switch (t) {
        case Int:
            break;
        case Bool:
            temp = varType.Bool;
        }
        if (s.varType != temp) {
            ErrorHandler.printError("The type of method and return address was not match");
        }
        memory.add3AddressCode(Operation.ASSIGN, s,
                new Address(symbolTable.getMethodReturnAddress(symbolStack.peek(), methodName), varType.Address,
                        TypeAddress.Indirect),
                null);
        memory.add3AddressCode(Operation.JP,
                new Address(symbolTable.getMethodCallerAddress(symbolStack.peek(), methodName), varType.Address), null,
                null);

        // symbolStack.pop();
    }

    public void defParam() {
        // TODO : call Ok
        ss.pop();
        String param = symbolStack.pop();
        String methodName = symbolStack.pop();
        String className = symbolStack.pop();

        symbolTable.addMethodParameter(className, methodName, param);

        symbolStack.push(className);
        symbolStack.push(methodName);
    }

    public void lastTypeBool() {
        symbolTable.setLastType(SymbolType.Bool);
    }

    public void lastTypeInt() {
        symbolTable.setLastType(SymbolType.Int);
    }

    public void main() {

    }

    private void executeOperation(Operation op) {
        Address s2 = ss.pop();
        Address s1 = ss.pop();
        try {
            Address result = operationStrategies.get(op).execute(memory, s1, s2);
            ss.push(result);
        } catch (RuntimeException e) {
            ErrorHandler.printError(e.getMessage());
            ss.push(new Address(0, varType.Non));
        }
    }
}
