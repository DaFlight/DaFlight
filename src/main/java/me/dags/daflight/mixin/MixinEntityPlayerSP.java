package me.dags.daflight.mixin;

import com.mojang.authlib.GameProfile;
import me.dags.daflight.DaFlight;
import me.dags.daflight.MCHooks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

/**
 * @author dags <dags@dags.me>
 */
@Mixin(EntityPlayerSP.class)
public class MixinEntityPlayerSP extends AbstractClientPlayer {

    public MixinEntityPlayerSP(World worldIn) {
        super(worldIn, new GameProfile(null, null));
    }

    @Override
    public float getFovModifier() {
        return DaFlight.instance().movementHandler().disableFov() ? 1.0F : super.getFovModifier();
    }

    @Override
    public void preparePlayerToSpawn() {
        super.preparePlayerToSpawn();
        DaFlight.instance().setSinglePlayer(Minecraft.getInstance().isSingleplayer());
        DaFlight.instance().setServerName(serverName());
        DaFlight.instance().updateConfig();
        DaFlight.instance().movementHandler().reset();
        DaFlight.instance().messageHandler().registerChannels();
        DaFlight.instance().messageHandler().connect();
    }

    @Override
    protected float getJumpUpwardsMotion() {
        return DaFlight.instance().movementHandler().jump(super.getJumpUpwardsMotion());
    }

    private static String serverName() {
        if (!DaFlight.instance().isSinglePlayer()) {
            ServerData currentServer = MCHooks.Game.getInstance().getCurrentServerData();
            return currentServer != null ? currentServer.serverIP.replace(":", "-").replace("-25565", "") : "";
        }
        return "";
    }
}
