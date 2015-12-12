package me.dags.daflight.launch;

/**
 * @author dags_ <dags@dags.me>
 */

public class Obf
{
    private final String name;
    private final String obfName;

    public Obf(String name, String obfName)
    {
        this.name = name;
        this.obfName = obfName;
    }

    public Obf(String name)
    {
        this(name, name);
    }

    public String get(boolean obf)
    {
        return obf ? obfName : name;
    }

    public String getDesc(boolean obf)
    {
        if (name.length() == 1 && obfName.length() == 1)
        {
            char c = name.charAt(0);
            if (c == 'V' || c == 'I' || c == 'F' || c == 'Z' || c == 'D' || c == 'J')
            {
                return name;
            }
        }
        return "L" + get(obf) + ";";
    }

    public static final Obf BOOLEAN = new Obf("Z");
    public static final Obf DOUBLE = new Obf("D");
    public static final Obf FLOAT = new Obf("F");
    public static final Obf INT = new Obf("I");
    public static final Obf LONG = new Obf("J");
    public static final Obf VOID = new Obf("V");

    public static final Obf EntityPlayerSP = new Obf("net/minecraft/client/entity/EntityPlayerSP", "bew");
    public static final Obf Minecraft = new Obf("net/minecraft/client/Minecraft", "ave");
    public static final Obf NetHandlerPlayClient = new Obf("net/minecraft/client/network/NetHandlerPlayClient", "bcy");
    public static final Obf PlayerControllerMP = new Obf("net/minecraft/client/multiplayer/PlayerControllerMP","bda");
    public static final Obf StatFileWriter = new Obf("net/minecraft/stats/StatFileWriter", "nb");
    public static final Obf World = new Obf("net/minecraft/world/World", "adm");

    public static final Obf getPlayerMethod = new Obf("func_178892_a", "a");
    public static final Desc getPlayerDesc = new Desc(EntityPlayerSP, World, StatFileWriter);
    public static final Desc initPlayerDesc = new Desc(VOID, Minecraft, World, NetHandlerPlayClient, StatFileWriter);
}
