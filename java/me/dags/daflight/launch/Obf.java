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

    public static final me.dags.daflight.launch.Obf BOOLEAN = new me.dags.daflight.launch.Obf("Z");
    public static final me.dags.daflight.launch.Obf DOUBLE = new me.dags.daflight.launch.Obf("D");
    public static final me.dags.daflight.launch.Obf FLOAT = new me.dags.daflight.launch.Obf("F");
    public static final me.dags.daflight.launch.Obf INT = new me.dags.daflight.launch.Obf("I");
    public static final me.dags.daflight.launch.Obf LONG = new me.dags.daflight.launch.Obf("J");
    public static final me.dags.daflight.launch.Obf VOID = new me.dags.daflight.launch.Obf("V");

    public static final me.dags.daflight.launch.Obf EntityPlayerSP = new me.dags.daflight.launch.Obf("net/minecraft/client/entity/EntityPlayerSP", "bew");
    public static final me.dags.daflight.launch.Obf Minecraft = new me.dags.daflight.launch.Obf("net/minecraft/client/Minecraft", "ave");
    public static final me.dags.daflight.launch.Obf NetHandlerPlayClient = new me.dags.daflight.launch.Obf("net/minecraft/client/network/NetHandlerPlayClient", "bcy");
    public static final me.dags.daflight.launch.Obf PlayerControllerMP = new me.dags.daflight.launch.Obf("net/minecraft/client/multiplayer/PlayerControllerMP","bda");
    public static final me.dags.daflight.launch.Obf StatFileWriter = new me.dags.daflight.launch.Obf("net/minecraft/stats/StatFileWriter", "nb");
    public static final me.dags.daflight.launch.Obf World = new me.dags.daflight.launch.Obf("net/minecraft/world/World", "adm");

    public static final me.dags.daflight.launch.Obf getPlayerMethod = new me.dags.daflight.launch.Obf("func_178892_a", "a");
    public static final me.dags.daflight.launch.Desc getPlayerDesc = new me.dags.daflight.launch.Desc(EntityPlayerSP, World, StatFileWriter);
    public static final me.dags.daflight.launch.Desc initPlayerDesc = new Desc(VOID, Minecraft, World, NetHandlerPlayClient, StatFileWriter);
}
