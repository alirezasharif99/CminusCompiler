package absyn;

public class ParamExp extends Exp {
    public VarExp name;
    public Exp type;
    public int array;
    public ParamExp(int row, int col, Exp type, VarExp name) {
        this.row = row;
        this.col = col;
        this.name = name;
        this.type = type;
        this.def = type.def;
        this.info = null;
        this.array = name.array;
        if(name.array == 1){
            this.def = type.def+"[-1]";
        }else{
            this.def = type.def;
        }
    }

    public void accept(AbsynVisitor visitor, int level, boolean isAddr) {
        visitor.visit(this, level, isAddr);
    }
}
