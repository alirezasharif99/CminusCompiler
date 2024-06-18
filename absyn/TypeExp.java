package absyn;

public class TypeExp extends Exp {
    public final static int INT = 0;
    public final static int VOID = 1;
    public final static int ERROR = 2;
    public Object name;
    public int type;

    public TypeExp(int row, int col, Object name, int type) {
        this.row = row;
        this.col = col;
        this.name = name;
        this.type = type;
        if (type == 0) {
            this.def = "INT";
        } else {
            this.def = "VOID";
        }
    }

    public void accept(AbsynVisitor visitor, int level, boolean isAddr) {
        visitor.visit(this, level, isAddr);
    }
}
