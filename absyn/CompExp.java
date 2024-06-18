package absyn;

public class CompExp extends Exp {

    public Exp first;
    public Exp second;

    public CompExp(int row, int col, Exp first, Exp second) {
        this.row = row;
        this.col = col;
        this.first = first;
        this.second = second;
        this.def = null;
        this.info = null;
    }

    public void accept(AbsynVisitor visitor, int level, boolean isAddr) {
        visitor.visit(this, level, isAddr);
    }
}
