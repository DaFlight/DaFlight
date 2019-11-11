package me.dags.daflight.mixin;

import com.mojang.authlib.GameProfile;
import me.dags.daflight.DaFlight;
import me.dags.daflight.MCHooks;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;

/**
 * @author dags <dags@dags.me>
 */
@Mixin(ClientPlayerEntity.class)
public class MixinEntityPlayerSP extends AbstractClientPlayerEntity {

    public MixinEntityPlayerSP(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Override
    public float getFovModifier() {
        return DaFlight.instance().movementHandler().disableFov() ? 1.0F : super.getFovModifier();
    }

    @Override
    public void preparePlayerToSpawn() {
        super.preparePlayerToSpawn();
        DaFlight.instance().setSinglePlayer(MCHooks.Game.isSinglePlayer());
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
