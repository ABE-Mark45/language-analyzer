package com.parser;

import java.util.ArrayList;
import java.util.HashMap;

class Context {
    public HashMap<String, Integer> variables = new HashMap<String, Integer>();
}


interface Statement {
    public abstract void execute(Context context) throws Exception;
}


class AssignmentStatement implements Statement {
    private String varName;
    private ArithmeticExpression arithmeticExpression;
    public AssignmentStatement(String varName, ArithmeticExpression arithmeticExpression) {
        this.varName = varName;
        this.arithmeticExpression = arithmeticExpression;
    }

    public void execute(Context context) throws Exception
    {
        context.variables.put(varName, arithmeticExpression.evaluate(context));
        System.out.println(varName + " = " + context.variables.get(varName));
    }
}

interface BooleanExpression {
    public boolean evaluate(Context context) throws Exception;
}

class IsEqualExpression implements BooleanExpression {
    private ArithmeticExpression arithmeticExpressionA;
    private ArithmeticExpression arithmeticExpressionB;

    public IsEqualExpression(ArithmeticExpression arithmeticExpressionA, ArithmeticExpression arithmeticExpressionB) {
        this.arithmeticExpressionA = arithmeticExpressionA;
        this.arithmeticExpressionB = arithmeticExpressionB;
    }


    @Override
    public boolean evaluate(Context context) throws Exception {
        return arithmeticExpressionA.evaluate(context) == arithmeticExpressionB.evaluate(context);
    }
}

class ANDExpression implements BooleanExpression {
    private BooleanExpression booleanExpressionA;
    private BooleanExpression booleanExpressionB;

    public ANDExpression(BooleanExpression booleanExpressionA, BooleanExpression booleanExpressionB) {
        this.booleanExpressionA = booleanExpressionA;
        this.booleanExpressionB = booleanExpressionB;
    }


    @Override
    public boolean evaluate(Context context) throws Exception {
        return booleanExpressionA.evaluate(context) & booleanExpressionB.evaluate(context);
    }
}


class ConstantBoolean implements BooleanExpression {
    private boolean val;

    public ConstantBoolean(boolean val) {
        this.val = val;
    }


    @Override
    public boolean evaluate(Context context) throws Exception {
        return val;
    }
}

class NotExpression implements BooleanExpression {
    private BooleanExpression booleanExpression;

    public NotExpression(BooleanExpression booleanExpression) {
        this.booleanExpression = booleanExpression;
    }


    @Override
    public boolean evaluate(Context context) throws Exception {
        return ! booleanExpression.evaluate(context);
    }
}



interface ArithmeticExpression {
    public int evaluate(Context context) throws Exception;
}

class NumberExpression implements ArithmeticExpression {
    private int number;

    public NumberExpression(int number) {
        this.number = number;
    }

    @Override
    public int evaluate(Context context) {
        return number;
    }
}

class VariableExpression implements ArithmeticExpression {
    private String identifier;

    public VariableExpression(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public int evaluate(Context context) throws Exception {
        if (!context.variables.containsKey(identifier))
            throw new Exception("Variable " + identifier + " is not declared in this scope");
        return context.variables.get(identifier);
    }
}

class BinaryArithmeticExpression implements ArithmeticExpression {
    private ArithmeticExpression arithmeticExpressionA;
    private ArithmeticExpression arithmeticExpressionB;
    private String operator;

    public BinaryArithmeticExpression(ArithmeticExpression arithmeticExpressionA, String operator, ArithmeticExpression arithmeticExpressionB) {
        this.arithmeticExpressionA = arithmeticExpressionA;
        this.arithmeticExpressionB = arithmeticExpressionB;
        this.operator = operator;
    }

    @Override
    public int evaluate(Context context) throws Exception {
        switch (operator)
        {
            case "+":
                return arithmeticExpressionA.evaluate(context) + arithmeticExpressionB.evaluate(context);
            case "-":
                int opA = arithmeticExpressionA.evaluate(context);
                int opB = arithmeticExpressionB.evaluate(context);

                if(opA < opB)
                    throw new Exception("negative numbers are not allowed");
                return opA - opB;
            default:
                throw new Exception("Undefined Operator");
        }
    }
}



class IfElseStatement implements Statement {
    private BooleanExpression expression;
    private Statement branchA, branchB;

    public IfElseStatement(BooleanExpression expression, Statement branchA, Statement branchB) {
        this.expression = expression;
        this.branchA = branchA;
        this.branchB = branchB;
    }

    @Override
    public void execute(Context context) throws Exception {
        System.out.println("Enter if statement");
        if(expression.evaluate(context)) {
            System.out.println("Execute branch A");
            branchA.execute(context);
        }
        else {
            System.out.println("Execute branch B");
            branchB.execute(context);
        }

        System.out.println("Exit if statement");
    }
}

class DoWhileStatement implements Statement {
    private BooleanExpression expression;
    private Statement action;

    public DoWhileStatement(BooleanExpression expression, Statement action) {
        this.expression = expression;
        this.action = action;
    }

    @Override
    public void execute(Context context) throws Exception {
        System.out.println("Enter while loop");
        while (expression.evaluate(context))
            action.execute(context);
        System.out.println("Exit while loops");
    }
}

class SkipStatement implements Statement {

    @Override
    public void execute(Context context) throws Exception {
        System.out.println("Skip Statement");
        return;
    }
}

class CodeBlock implements Statement {
    private ArrayList<Statement> statements = new ArrayList<Statement>();

    public void add(Statement s) {
        statements.add(s);
    }
    @Override
    public void execute(Context context) throws Exception {
        for(Statement s: statements)
            s.execute(context);
    }
}