package dev.isxander.mcworldflip.mixins;

import net.minecraft.entity.player.HungerManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(HungerManager.class)
public class HungerManagerMixin {
    @Shadow private float foodSaturationLevel;

    @ModifyArg(method = "update", at = @At(value = "INVOKE", target = "Ljava/lang/Math;max(FF)F"), index = 0)
    private float modifySaturation(float saturation) {
        return foodSaturationLevel - 0.5f;
    }
}
