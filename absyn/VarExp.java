package absyn;

public class VarExp extends Exp {
  public String name;
  public Exp exprs;
  public int SimpleDec;
  public int ArrayDec;
  public int FunctionDec;
  public int array;

  public VarExp(int row, int col, String name, Exp exprs, int array) {
    this.row = row;
    this.col = col;
    this.name = name;
    this.exprs = exprs;
    this.def = null;
    this.info = name;
    this.array = array;
  }

  public void accept(AbsynVisitor visitor, int level, boolean isAddr) {
    visitor.visit(this, level, isAddr);
  }
}
