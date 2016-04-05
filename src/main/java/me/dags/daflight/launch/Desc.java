package me.dags.daflight.launch;

/**
 * @author dags_ <dags@dags.me>
 */

public class Desc
{
    private Obf returnType;
    private Obf[] vars;

    public Desc(Obf returnType, Obf... vars)
    {
        this.returnType = returnType;
        this.vars = vars;
    }

    public Desc(String returnType, String returnTypeObf, Obf... vars)
    {
        this(new Obf(returnType, returnTypeObf), vars);
    }

    public String get(boolean obf)
    {
        StringBuilder sb = new StringBuilder().append("(");
        for (Obf var : vars) sb.append(var.getDesc(obf));
        return sb.append(")").append(returnType.getDesc(obf)).toString();
    }
}
