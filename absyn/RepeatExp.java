package absyn;

public class RepeatExp extends Exp {
  public Exp test;
  public ExpList exps;

  public RepeatExp(int row, int col, Exp test, ExpList exps) {
    this.row = row;
    this.col = col;
    this.exps = exps;
    this.test = test;
  }

  public void accept(AbsynVisitor visitor, int level, boolean isAddr) {
    visitor.visit(this, level, isAddr);
  }
}
