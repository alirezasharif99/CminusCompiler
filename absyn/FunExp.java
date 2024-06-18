package absyn;

public class FunExp extends Exp {
    public VarExp name;
    public Exp type;
    public Exp params;
    public ExpList compound;
    public int funaddr;

    public FunExp(int row, int col, Exp type, VarExp name, Exp params, ExpList compound) {
        this.row = row;
        this.col = col;
        this.name = name;
        this.type = type;
        this.params = params;
        this.compound = compound;
        this.funaddr = -1;
    }

    public void accept(AbsynVisitor visitor, int level, boolean isAddr) {
        visitor.visit(this, level, isAddr);
    }
}
