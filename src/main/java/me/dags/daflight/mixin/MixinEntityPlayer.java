package me.dags.daflight.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

@Mixin(EntityPlayer.class)
public abstract class MixinEntityPlayer extends EntityLivingBase
{
    private static final String Entity = "Lnet/minecraft/entity/Entity;";

    public MixinEntityPlayer(World worldIn)
    {
        super(worldIn);
    }

    @Inject(method = "onUpdate()V", at = @At(value = "FIELD", target = Entity + "noClip:Z", ordinal = 0, shift = At.Shift.AFTER))
    public void onUpdate(CallbackInfo ci)
    {
        //noClip = true;
    }
}
