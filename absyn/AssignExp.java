package absyn;

public class AssignExp extends Exp {
  public VarExp name;
  public Exp type;
  public IntExp num;
  public int nestLevel; // 0 for global, 1 for local
  public int offset;

  public AssignExp(int row, int col, VarExp name, IntExp num, Exp type) {
    this.row = row;
    this.col = col;
    this.name = name;
    this.num = num;
    this.type = type;
  }

  public void accept(AbsynVisitor visitor, int level, boolean isAddr) {
    visitor.visit(this, level, isAddr);
  }
}
