import absyn.*;

/*
  Created by: Fei Song
  Modified by: Alireza Sharif & Pedram Mirmiran
  File Name: ShowTreeVisitor.java

 *  The ShowTreeVisitor class implements the AbsynVisitor interface and is responsible for 
 * traversing and displaying the abstract syntax tree (AST) of the C- language. 
 * It provides a visit method for each type of node in the AST, allowing for a recursive 
 * walk-through of the tree and printing out a structured representation of the program's syntax.

*/

public class ShowTreeVisitor implements AbsynVisitor {

  final static int SPACES = 4;

  private void indent(int level) {  // Handles indentation for each level of the tree by printing the appropriate number of spaces.
    for (int i = 0; i < level * SPACES; i++)
      System.out.print(" ");
  }

  public void visit(ExpList expList, int level, boolean isAddr) {// Visits an ExpList node and handles errors.
    while (expList != null) {
      expList.head.accept(this, level, isAddr);
      expList = expList.tail;
    }
  }

  public void visit(AssignExp exp, int level, boolean isAddr) { // Visits an AssignExp node and handles errors
    indent(level);
    System.out.println("AssignExp:");
    level++;
    exp.type.accept(this, level, isAddr);
    exp.name.accept(this, level, isAddr);
    if (exp.num != null)
      exp.num.accept(this, level, isAddr);
  }

  public void visit(IfExp exp, int level, boolean isAddr) { // Visits an IfExp node and handles errors.
    indent(level);
    System.out.println("IfExp:");
    level++;
    if (exp.test != null) {
      exp.test.accept(this, level, isAddr);
    } else {
      indent(level);
      System.out.println("Error test case not found");
    }
    exp.thenpart.accept(this, level, isAddr);
    if (exp.elsepart != null) {
      level--;
      indent(level);
      System.out.println("ElseExp:");
      level++;
      exp.elsepart.accept(this, level, isAddr);
    }

  }

  public void visit(IntExp exp, int level, boolean isAddr) { // Visits an IntExp node and handles errors.
    indent(level);
    if (exp.value == null)
      System.out.println("IntExp: Error value not found");
    else
      System.out.println("IntExp: " + exp.value);
  }

  public void visit(OpExp exp, int level, boolean isAddr) { // Visits an OpExp node and handles errors.
    indent(level);
    System.out.print("OpExp:");
    switch (exp.op) {
    case OpExp.PLUS:
      System.out.println(" + ");
      break;
    case OpExp.MINUS:
      System.out.println(" - ");
      break;
    case OpExp.TIMES:
      System.out.println(" * ");
      break;
    case OpExp.OVER:
      System.out.println(" / ");
      break;
    case OpExp.EQ:
      System.out.println(" == ");
      break;
    case OpExp.LT:
      System.out.println(" < ");
      break;
    case OpExp.GT:
      System.out.println(" > ");
      break;
    case OpExp.LE:
      System.out.println(" <= ");
      break;
    case OpExp.GE:
      System.out.println(" >= ");
      break;
    case OpExp.NEQ:
      System.out.println(" != ");
      break;
    case OpExp.ERROR:
      System.out.println(" Unrecognized symbol found ");
      break;
    default:
      System.out.println("Unrecognized operator at line " + exp.row + " and column " + exp.col);
    }
    level++;
  }

  public void visit(RepeatExp exp, int level, boolean isAddr) { // Visits a RepeatExp node and handles errors.
    indent(level);
    System.out.println("RepeatExp:");
    level++;
    if (exp.test != null) {
      exp.test.accept(this, level, isAddr);
    } else {
      indent(level);
      System.out.println("Error test case not found");
    }
    if (exp.exps != null)
      exp.exps.accept(this, level, isAddr);
  }

  public void visit(VarExp exp, int level, boolean isAddr) {  // Visits a VarExp node and handles errors.
    indent(level);
    if (exp.name == null) {
      System.out.println("VarExp: Error name not found");
    } else {
      System.out.println("VarExp: " + exp.name);
    }
    if (exp.exprs != null)
      exp.exprs.accept(this, level, isAddr);
    if(exp.array==1){
      indent(level);
      System.out.println("IntExp: -1");
    }
  }

  public void visit(TypeExp exp, int level, boolean isAddr) { // Visits a TypeExp node and handles errors.
    indent(level);
    System.out.print("TypeExp: ");
    switch (exp.type) {
    case TypeExp.INT:
      System.out.println("INT");
      break;
    case TypeExp.VOID:
      System.out.println("VOID");
      break;
    default:
      System.out.println("Error unrecognized type");
    }
  }

  public void visit(FunExp exp, int level, boolean isAddr) { // Visits a FunExp node and handles errors.
    indent(level);
    System.out.println("FunExp: ");
    level++;
    exp.type.accept(this, level, isAddr);
    exp.name.accept(this, level, isAddr);
    if (exp.params != null) {
      indent(level);
      System.out.println("ParamsExp: ");
      level++;
      exp.params.accept(this, level, isAddr);
    }
    level++;
    if (exp.compound != null) {
      level--;
      indent(level);
      System.out.println("CompoundExp: ");
      level++;
      exp.compound.accept(this, level, isAddr);
    }
  }

  public void visit(ParListExp exp, int level, boolean isAddr) { // Visits a ParListExp node and handles errors.
    indent(level);
    System.out.println("ParListExp: ");
    level++;
    exp.paramlist.accept(this, level, isAddr);
    exp.param.accept(this, level, isAddr);
  }

  public void visit(ParamExp exp, int level, boolean isAddr) { // Visits a ParamExp node and handles errors.
    indent(level);
    System.out.println("ParamExp: ");
    level++;
    exp.type.accept(this, level, isAddr);
    exp.name.accept(this, level, isAddr);
  }

  public void visit(CompExp exp, int level, boolean isAddr) { // Visits a CompExp node and handles errors.
    indent(level);
    if (exp.first == null && exp.second == null) {
      System.out.println("Expression: Error missing values");
    } else {
      System.out.println("Expression: ");
    }
    level++;
    if (exp.first != null)
      exp.first.accept(this, level, isAddr);
    if (exp.second != null)
      exp.second.accept(this, level, isAddr);
  }

  public void visit(ReturnExp exp, int level, boolean isAddr) { // Visits a ReturnExp node and handles errors.
    indent(level);
    System.out.println("ReturnExp: ");
    level++;
    exp.exps.accept(this, level, isAddr);
  }

  public void visit(MathExp exp, int level, boolean isAddr) { // Visits a MathExp node and handles errors.
    indent(level);
    switch (exp.type) {
    case MathExp.SIMPLE:
      System.out.println("SimpleExp: ");
      break;
    case MathExp.ADDITIVE:
      System.out.println("AdditiveExp: ");
      break;
    case MathExp.TERM:
      System.out.println("TermExp: ");
      break;
    default:
      System.out.println("Unrecognized statement at line " + exp.row + " and column " + exp.col);
    }
    level++;
    exp.lhs.accept(this, level, isAddr);
    exp.op.accept(this, level, isAddr);
    exp.rhs.accept(this, level, isAddr);
  }

  public void visit(CallExp exp, int level, boolean isAddr) {  // Visits a CallExp node and handles errors
    indent(level);
    System.out.println("CallExp: ");
    level++;
    exp.name.accept(this, level, isAddr);
    if (exp.args != null)
      exp.args.accept(this, level, isAddr);
  }

}
