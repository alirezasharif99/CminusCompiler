/*
  Created by: Alireza Sharif and Pedram Mirmiran
  File Name: SemanticAnalyzer.java

    Description: The SemanticAnalyzer class implements the AbsynVisitor interface for traversing and analyzing an abstract syntax tree (AST)
     of a program written in the C Minus language. It performs semantic checks such as variable and function declarations,
      type checking, and scope control. The class uses a symbol table (implemented as a HashMap) to keep track of identifiers
       and their corresponding types and scope levels. This allows the analyzer to detect and report errors like redeclaration of variables within the same scope,
        type mismatches in expressions and assignments, and incorrect function call arguments. Furthermore, the SemanticAnalyzer supports handling of arrays,
         including size declarations and index access. Error messages are generated for various semantic violations,
          enhancing the compiler's ability to guide programmers towards correct C Minus program syntax and semantics.
           This class plays a crucial role in ensuring that the generated code is semantically correct before proceeding to the code generation phase.

*/

import absyn.*;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Set;
import java.util.Iterator;

public class SemanticAnalyzer implements AbsynVisitor {

    HashMap<String, ArrayList<NodeType>> table;
    ArrayList<String> callArgs;
    int globalLevel = 0;
    String funcType = null;
    int returned = 0;

    public SemanticAnalyzer() {
        table = new HashMap<String, ArrayList<NodeType>>();
        callArgs = new ArrayList<String>();
    }

    public void printLevel(int level) {
        int inc = level;
        level = inc+1;
        Set<String> keys = table.keySet();
        Iterator<String> keyIterator = keys.iterator();
        while (keyIterator.hasNext()) {
            String key = keyIterator.next();
            ArrayList<NodeType> map = table.get(key);
            NodeType last = map.get(map.size() - 1);
            if (last.level == globalLevel) {
                indent(level);
                System.out.println(last.name + ": " + last.def);
            }
        }
        
        int dec = level;
        level = dec-1;
    }

    public void insert(NodeType node) {
        if (!table.containsKey(node.name)) {
            ArrayList<NodeType> map = new ArrayList<NodeType>();
            map.add(node);
            table.put(node.name, map);
        } else {
            ArrayList<NodeType> map = table.get(node.name);
            map.add(node);
            table.put(node.name, map);
        }
    }


    private NodeType lookup(String name) {
        if (!table.containsKey(name)) {
            return null;
        } else {
            ArrayList<NodeType> list = table.get(name);
            NodeType curr = list.get(list.size() - 1);
            return curr;
        }
    }

    private void deleteLevel(int level) {
        Set<String> keys = table.keySet();
        ArrayList<String> deleteKeys = new ArrayList<String>();
        Iterator<String> iter = keys.iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            ArrayList<NodeType> map = table.get(key);
            NodeType last = map.get(map.size() - 1);
            if (last.level == level) {
                map.remove(last);
                if (map.isEmpty()) {
                    deleteKeys.add(key);
                }
            }
        }
        keys.removeAll(deleteKeys);
    }

    private void indent(int level) {
        int i = 0;
        while (i < level * SPACES) {
            System.out.print(" ");
            i++;
        }
    }

    final static int SPACES = 4;

    public void visit(ExpList expList, int level, boolean isAddr) {
        while (expList != null) {
            expList.head.accept(this, level, isAddr);
            if (expList.head.def != null) {
                callArgs.add(expList.head.def);
            }
            expList = expList.tail;

        }
    }

    public void visit(AssignExp exp, int level, boolean isAddr) {
        exp.type.accept(this, level, isAddr);
        exp.name.accept(this, level, isAddr);
        if (exp.num != null) {
            if (exp.type.def.equals("VOID")) {
                System.err.println("Error: array type can't be void on line:" + (exp.row + 1) + " and column:" + exp.col);
            }
            exp.num.accept(this, level, isAddr);
        }
        NodeType test = lookup(exp.name.info);
        if (test != null && test.level == globalLevel) {
            System.err.println("Error: redefined variable " + exp.name.info + " at the same level on line:"
                    + (exp.row + 1) + " and column:" + exp.col);
        } else {
            if (exp.type.def.equals("VOID")) {
                System.err.println("Error: variables cannot be defined as VOID type on line:" + (exp.row + 1)
                        + " and column:" + exp.col);
                exp.type.def = "INT";
            }
            NodeType node;
            if (exp.num != null) {
                node = new NodeType(exp.name.info, exp.type.def + "[" + exp.num.info + "]", globalLevel, 0);
            } else {
                node = new NodeType(exp.name.info, exp.type.def, globalLevel, 0);
            }
            insert(node);
        }
    }

    public void visit(IfExp exp, int level, boolean isAddr) {
        if (exp.test != null) {
            exp.test.accept(this, level, isAddr);
            if (exp.test.def.equals("VOID")) {
                System.err.println("Error: If test can't be void on line:" + (exp.row + 1) + " and column:" + exp.col);
            } else {
                String temp[] = exp.test.def.split(" ");
                int i = 0;
                while (i < temp.length) {
                    NodeType n = lookup(temp[i]);
                    if (n != null && n.def.contains("VOID")) {
                        System.err.println("Error: Test cannot be void at line:" + (exp.row + 1) + " and column:" + exp.col);
                        break;
                    }
                    i++;
                }
            }
        }
        globalLevel++;
        level++;
        indent(level);
        System.out.println("Entering a new block");
        if (exp.thenpart != null)
            exp.thenpart.accept(this, level, isAddr);
        printLevel(level);
        deleteLevel(globalLevel);
        indent(level);
        System.out.println("Leaving a new block");
        if (exp.elsepart != null) {
            indent(level);
            System.out.println("Entering a new block");
            exp.elsepart.accept(this, level, isAddr);
            printLevel(level);
            indent(level);
            System.out.println("Leaving a new block");
            deleteLevel(globalLevel);
        }
        level--;
        globalLevel--;

    }

    public void visit(IntExp exp, int level, boolean isAddr) {
    }

    public void visit(OpExp exp, int level, boolean isAddr) {
        switch (exp.op) {
        case OpExp.PLUS:
            break;
        case OpExp.MINUS:
            break;
        case OpExp.TIMES:
            break;
        case OpExp.OVER:
            break;
        case OpExp.EQ:
            break;
        case OpExp.LT:
            break;
        case OpExp.GT:
            break;
        case OpExp.LE:
            break;
        case OpExp.GE:
            break;
        case OpExp.NEQ:
            break;
        case OpExp.ERROR:
            break;
        default:
            break;
        }
    }

    public void visit(RepeatExp exp, int level, boolean isAddr) {
        globalLevel++;

        if (exp.test != null) {
            exp.test.accept(this, level, isAddr);
            if (exp.test.def.equals("VOID")) {
                System.err.println("Error: While test can't be void on line:" + (exp.row + 1) + " and column:" + exp.col);
            } else {
                String temp[] = exp.test.def.split(" ");
                for (int i = 0; i < temp.length; i++) {
                    NodeType n = lookup(temp[i]);
                    if (n != null && n.def.contains("VOID")) {
                        System.err.println(
                                "Error: While test can't be void on line:" + (exp.row + 1) + " and column:" + exp.col);
                        break;
                    }
                }
            }
        }

        int inc = level;
        level = inc+1;

        if (exp.exps != null) {
            indent(level);
            System.out.println("Entering a new block");
            if (exp.exps != null)
                exp.exps.accept(this, level, isAddr);
            printLevel(level);
            indent(level);
            System.out.println("Leaving a new block");
            deleteLevel(globalLevel);
        }

        int dec = level;
        level = dec-1;
        globalLevel--;

    }

    public void visit(VarExp exp, int level, boolean isAddr) {
        if (exp.exprs != null) {
            exp.exprs.accept(this, level, isAddr);
        }
        NodeType test = lookup(exp.name);
        if (test != null) {
            if (exp.exprs != null) {
                try {
                    int temp = Integer.parseInt(exp.exprs.info);
                    exp.def = test.def + "[" + "INT" + "]";
                } catch (NumberFormatException e) {
                    NodeType param = lookup(exp.exprs.info);
                    if (param != null) {
                        if (param.def.contains("->")) {
                            exp.def = test.def + "[" + param.def.split(" ")[2] + "]";
                            if (param.def.split(" ")[2].equals("VOID")) {
                                System.err.println("Error: VOID type cannot be used to access array on line:"
                                        + (exp.row + 1) + " and column:" + exp.col);
                            }
                        } else {
                            exp.def = test.def + "[" + param.def + "]";
                        }
                    } else {
                        exp.def = "ERROR - undefined";
                    }
                }
            } else {
                exp.def = test.def;
            }
        } else {
            exp.def = "ERROR - undefined";
        }
    }

    public void visit(TypeExp exp, int level, boolean isAddr) {
    }

    public void visit(FunExp exp, int level, boolean isAddr) {
        exp.type.accept(this, level, isAddr);
        exp.name.accept(this, level, isAddr);

        NodeType test = lookup(exp.name.info);
        globalLevel++;
        level++;
        indent(level);
        System.out.println("Entering the scope for function " + exp.name.info + ":");
        if (exp.params != null) {
            exp.params.accept(this, level, isAddr);
        }

        if (test != null && test.level == 0) {
            System.err.println("Error: Function name already exists at the same level on line:" + (exp.row + 1)
                    + " and column:" + exp.col);
        } else {
            if (exp.params != null) {
                if (exp.params.def.equalsIgnoreCase("VOID")) {
                    exp.type.def = "(" + exp.params.def + ") -> " + exp.type.def;
                } else {
                    exp.type.def = "(" + exp.params.def.replaceAll("VOID", "INT") + ") -> " + exp.type.def;
                }
            } else {
                exp.type.def = "(" + ") -> " + exp.type.def;
            }
            NodeType node = new NodeType(exp.name.info, exp.type.def, globalLevel - 1, 0);
            insert(node);
        }
        funcType = exp.type.def;
        returned = 0;
        if (exp.compound != null) {
            exp.compound.accept(this, level, isAddr);
        }
        if (returned == 0) {
            if (funcType.split(" ").length > 2 && funcType.split(" ")[2].equals("INT")) {
                System.err.println("Error: Function declared with type INT but has no return on line:" + (exp.row + 1)
                        + " and column:" + exp.col);
            }
        }
        printLevel(level);
        indent(level);
        System.out.println("Leaving the function scope");
        deleteLevel(globalLevel);
        globalLevel--;
        level--;
        funcType = null;
    }

    public void visit(ParListExp exp, int level, boolean isAddr) {
        exp.paramlist.accept(this, level, isAddr);
        exp.param.accept(this, level, isAddr);
    }

    public void visit(ParamExp exp, int level, boolean isAddr) {
        exp.type.accept(this, level, isAddr);
        exp.name.accept(this, level, isAddr);
        NodeType test = lookup(exp.name.info);
        if (test != null && test.level == globalLevel) {
            System.err.println("Error: Variable " + exp.name.info + " already exist at the same level on line:"
                    + (exp.row + 1) + " and column:" + exp.col);
        } else {
            if (exp.type.def.equals("VOID")) {
                System.err.println("Error: variables cannot be defined as VOID type on line:" + (exp.row + 1)
                        + " and column:" + exp.col);
                exp.type.def = "INT";
            }
            if(exp.array == 1){
                exp.type.def = exp.type.def+"[-1]";
            }
            NodeType node = new NodeType(exp.name.info, exp.type.def, globalLevel, 0);
            insert(node);
        }

    }

    public void visit(CompExp exp, int level, boolean isAddr) {
        if (exp.first != null)
            exp.first.accept(this, level, isAddr);
        if (exp.second != null)
            exp.second.accept(this, level, isAddr);
        if (exp.first != null && exp.second != null) {
            if (!exp.first.def.contains("ERROR") && !exp.second.def.contains("ERROR")) {
                String first = exp.first.def;
                String second = exp.second.def;
                int array = 0;
                int array2 = 0;
                if (exp.first.def.contains("[")) {
                    array = 1;
                    String[] allFirst = exp.first.def.split("\\[");
                    first = allFirst[0];
                    if (allFirst.length > 2) {
                        array = 0;
                        if (exp.first.def.contains("ERROR")) {
                            exp.def = "ERROR";
                            return;
                        } else {
                            if (!first.equals(exp.first.def.split("\\[")[2].split("]")[0])) {
                                exp.def = "ERROR";
                                System.err.println("Error: VOID type cannot be used to access array on line:"
                                        + (exp.row + 1) + " and column:" + exp.col);
                                return;
                            }
                        }
                    }
                }
                if (exp.second.def.contains("[")) {
                    array2 = 1;
                    String[] allSecond = exp.second.def.split("\\[");
                    second = allSecond[0];
                    if (allSecond.length > 2) {
                        array2 = 0;
                    }
                }
                if (array != array2) {
                    System.err.println(
                            "Error: Invalid types for statement on line:" + (exp.row + 1) + " and column:" + exp.col);
                    exp.def = "ERROR";
                    return;
                } else if (array == 1) {
                    System.err.println("Error: Arrays cannot be used in assignments on line:" + (exp.row + 1)
                            + " and column:" + exp.col);
                    exp.def = "ERROR";
                    return;
                }

                if (first.equals(second)) {
                    exp.def = first;
                } else if (!first.equals(second)) {
                    exp.def = "ERROR";
                    System.err.println(
                            "Error: Invalid types for statement on line:" + (exp.row + 1) + " and column:" + exp.col);
                }
            }
            if (exp.first.def.equals("ERROR - undefined")) {
                System.err.println("Error: Variable not defined on line:" + (exp.row + 1) + " and column:" + exp.col);
            }
            if (exp.second.def.equals("ERROR - undefined")) {
                System.err.println("Error: Variable not defined on line:" + (exp.row + 1) + " and column:" + exp.col);
            }
        } else {
            exp.def = "ERROR";
        }
    }

    public void visit(ReturnExp exp, int level, boolean isAddr) {
        if (exp.exps != null) {
            exp.exps.accept(this, level, isAddr);
            if (!exp.exps.def.equals(funcType.split(" -> ")[1])) {
                System.err.println(
                        "Error: Function return type mismatch on line:" + (exp.row + 1) + " and column:" + exp.col);
            }
        } else {
            if (!funcType.split(" -> ")[1].equals("VOID")) {
                System.err.println(
                        "Error: Function return type mismatch on line:" + (exp.row + 1) + " and column:" + exp.col);
            }
        }
        returned++;
    }

    public void visit(MathExp exp, int level, boolean isAddr) {
        exp.lhs.accept(this, level, isAddr);
        exp.op.accept(this, level, isAddr);
        exp.rhs.accept(this, level, isAddr);

        if (!exp.lhs.def.contains("ERROR") && !exp.rhs.def.contains("ERROR")) {
            String left = exp.lhs.def;
            String right = exp.rhs.def;
            int array = 0;
            int array2 = 0;
            if (exp.lhs.def.contains("[")) {
                array = 1;
                String[] allFirst = exp.lhs.def.split("\\[");
                left = allFirst[0];
                if (allFirst.length > 2) {
                    array = 0;
                    if (exp.lhs.def.contains("ERROR")) {
                        exp.def = "ERROR";
                        return;
                    } else {
                        if (!left.equals(exp.lhs.def.split("\\[")[2].split("]")[0])) {
                            exp.def = "ERROR";
                            System.err.println("Error: VOID type cannot be used to access array on line:"
                                    + (exp.row + 1) + " and column:" + exp.col);
                            return;
                        }
                    }
                }
            }
            if (exp.rhs.def.contains("[")) {
                array2 = 1;
                String[] allSecond = exp.rhs.def.split("\\[");
                right = allSecond[0];
                if (allSecond.length > 2) {
                    array2 = 0;
                    if (exp.rhs.def.contains("ERROR")) {
                        exp.def = "ERROR";
                        return;
                    } else {
                        if (!right.equals(exp.rhs.def.split("\\[")[2].split("]")[0])) {
                            exp.def = "ERROR";
                            System.err.println("Error: VOID type cannot be used to access array on line:"
                                    + (exp.row + 1) + " and column:" + exp.col);
                            return;
                        }
                    }
                }
            }

            if (array != array2) {
                System.err
                        .println("Error: Invalid types for statement on line:" + (exp.row + 1) + " and column:" + exp.col);
                exp.def = "ERROR";
                return;
            }
            if (array == 1 || array2 == 1) {
                System.err
                        .println("Error: Invalid types for statement on line:" + (exp.row + 1) + " and column:" + exp.col);
                exp.def = "ERROR";
                return;
            }

            if (left.equals(right)) {
                exp.def = left;
            } else if (!left.equals(right)) {
                exp.def = "ERROR";
                System.err
                        .println("Error: Invalid types for equation on line:" + (exp.row + 1) + " and column:" + exp.col);
            }
        } else if (exp.lhs.def.equals("ERROR - undefined") || exp.rhs.def.equals("ERROR - undefined")) {
            System.err.println("Error: Variable not defined on line:" + (exp.row + 1) + " and column:" + exp.col);
            exp.def = "ERROR";
        } else {
            exp.def = "ERROR";
        }
    }

    public void visit(CallExp exp, int level, boolean isAddr) {
        callArgs.clear();
        String all = "";
        exp.name.accept(this, level, isAddr);
        if (exp.args != null) {
            exp.args.accept(this, level, isAddr);
            all = all.concat("(");
            Iterator<String> iterator = callArgs.iterator();
            while (iterator.hasNext()) {
                String s = iterator.next();
                String curr = s;
                if (s.contains("[")) {
                    if (s.split("\\[").length > 2) {
                        curr = s.split("\\[")[0];
                    }
                }
                all = all.concat(curr.replaceAll("\\n", "")).concat(", ");
            }
            
            all = all.substring(0, all.length() - 2).concat(")");
        }

        NodeType value = lookup(exp.name.name);

        if (all.contains("ERROR")) {
            System.err.println("Error: Variable not defined on line:" + (exp.row + 1) + " and column:" + exp.col);
            exp.def = "ERROR";
        }

        if (value != null) {
            String funcArgs = value.def.split(" -> ")[0];
            if (funcArgs.equalsIgnoreCase("(VOID)")) {
                funcArgs = "";
            }
            funcArgs=funcArgs.replaceAll("\\[(.*?)\\]", "[]");
            all= all.replaceAll("\\[(.*?)\\]", "[]");
            if (!all.equals(funcArgs)) {
                System.err.println("Error: Invalid function call on line:" + (exp.row + 1) + " and column:" + exp.col);
                exp.def = "ERROR";
            }
            exp.def = value.def.split(" -> ")[1];
        } else {
            exp.def = "ERROR";
            System.err.println("Error: Unknown function on line:" + (exp.row + 1) + " and column:" + exp.col);
        }
        callArgs.clear();
    }

}
