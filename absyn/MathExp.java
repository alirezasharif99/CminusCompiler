package absyn;

public class MathExp extends Exp {
    public final static int SIMPLE = 0;
    public final static int ADDITIVE = 1;
    public final static int TERM = 2;

    public Exp rhs;
    public Exp lhs;
    public Exp op;
    public int type;

    public MathExp(int row, int col, Exp lhs, Exp op, Exp rhs, int type) {
        this.row = row;
        this.col = col;
        this.rhs = rhs;
        this.lhs = lhs;
        this.op = op;
        this.type = type;
        this.def = null;
        this.info = null;
    }

    public void accept(AbsynVisitor visitor, int level, boolean isAddr) {
        visitor.visit(this, level, isAddr);
    }
}
