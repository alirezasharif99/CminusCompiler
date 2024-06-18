package absyn;

public interface AbsynVisitor {

  public void visit(ExpList exp, int level, boolean isAddr);

  public void visit(AssignExp exp, int level, boolean isAddr);

  public void visit(IfExp exp, int level, boolean isAddr);

  public void visit(IntExp exp, int level, boolean isAddr);

  public void visit(OpExp exp, int level, boolean isAddr);

  public void visit(RepeatExp exp, int level, boolean isAddr);

  public void visit(VarExp exp, int level, boolean isAddr);

  public void visit(TypeExp exp, int level, boolean isAddr);

  public void visit(FunExp exp, int level, boolean isAddr);

  public void visit(ParListExp exp, int level, boolean isAddr);

  public void visit(ParamExp exp, int level, boolean isAddr);

  public void visit(CompExp exp, int level, boolean isAddr);

  public void visit(ReturnExp exp, int level, boolean isAddr);

  public void visit(MathExp exp, int level, boolean isAddr);

  public void visit(CallExp exp, int level, boolean isAddr);

}
