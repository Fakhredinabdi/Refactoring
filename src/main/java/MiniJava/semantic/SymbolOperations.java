package MiniJava.semantic;

import MiniJava.semantic.symbol.Symbol;
import MiniJava.semantic.symbol.SymbolTable;
import MiniJava.semantic.symbol.SymbolType;

/**
 * A class that separates symbol table operations into distinct query and modifier methods.
 */
public class SymbolOperations {
    private final SymbolTable symbolTable;

    public SymbolOperations(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }

    /**
     * Query method: Check if a symbol exists
     * @param className class name
     * @param methodName method name
     * @param symbolName symbol name
     * @return true if symbol exists
     */
    public boolean hasSymbol(String className, String methodName, String symbolName) {
        try {
            symbolTable.get(className, methodName, symbolName);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Query method: Get symbol information without modifying state
     * @param className class name
     * @param methodName method name
     * @param symbolName symbol name
     * @return Symbol object if found, null otherwise
     */
    public Symbol getSymbol(String className, String methodName, String symbolName) {
        try {
            return symbolTable.get(className, methodName, symbolName);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Query method: Get method symbol without modifying state
     * @param className class name
     * @param methodName method name
     * @return Symbol object if found, null otherwise
     */
    public Symbol getMethodSymbol(String className, String methodName) {
        try {
            return symbolTable.get(className, methodName);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Query method: Get method address without modifying state
     * @param className class name
     * @param methodName method name
     * @return method address if found, -1 otherwise
     */
    public int getMethodAddress(String className, String methodName) {
        try {
            Symbol symbol = symbolTable.get(className, methodName);
            return symbol.address;
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * Modifier method: Add a new method to the symbol table
     * @param className class name
     * @param methodName method name
     * @param address method address
     */
    public void addMethod(String className, String methodName, int address) {
        symbolTable.addMethod(className, methodName, address);
    }

    /**
     * Modifier method: Add a new class to the symbol table
     * @param className class name
     */
    public void addClass(String className) {
        symbolTable.addClass(className);
    }

    /**
     * Modifier method: Add a new field to a class
     * @param className class name
     * @param fieldName field name
     */
    public void addField(String className, String fieldName) {
        symbolTable.addField(className, fieldName);
    }

    /**
     * Modifier method: Start a method call
     */
    public void startCall(String className, String methodName) {
        symbolTable.startCall(className, methodName);
    }

    /**
     * Query method: Get next parameter for a method
     */
    public Symbol getNextParam(String className, String methodName) {
        return symbolTable.getNextParam(className, methodName);
    }

    /**
     * Query method: Get method return type
     */
    public SymbolType getMethodReturnType(String className, String methodName) {
        return symbolTable.getMethodReturnType(className, methodName);
    }

    /**
     * Query method: Get method return address
     */
    public int getMethodReturnAddress(String className, String methodName) {
        return symbolTable.getMethodReturnAddress(className, methodName);
    }

    /**
     * Query method: Get method caller address
     */
    public int getMethodCallerAddress(String className, String methodName) {
        return symbolTable.getMethodCallerAddress(className, methodName);
    }

    /**
     * Modifier method: Set superclass for a class
     */
    public void setSuperClass(String className, String superClassName) {
        symbolTable.setSuperClass(className, superClassName);
    }

    /**
     * Modifier method: Add a local variable to a method
     */
    public void addMethodLocalVariable(String className, String methodName, String varName) {
        symbolTable.addMethodLocalVariable(className, methodName, varName);
    }

    /**
     * Modifier method: Add a parameter to a method
     */
    public void addMethodParameter(String className, String methodName, String paramName) {
        symbolTable.addMethodParameter(className, methodName, paramName);
    }

    /**
     * Modifier method: Set the last type used
     */
    public void setLastType(SymbolType type) {
        symbolTable.setLastType(type);
    }
} 