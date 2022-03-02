package dev.isxander.mcworldflip.mixins;

import net.minecraft.world.gen.feature.OrePlacedFeatures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(OrePlacedFeatures.class)
public class OrePlacedFeaturesMixin {
    @ModifyArg(
            method = "<clinit>",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/feature/OrePlacedFeatures;modifiersWithCount(ILnet/minecraft/world/gen/decorator/PlacementModifier;)Ljava/util/List;"),
            index = 0
    )
    private static int modifyCount(int count) {
        return count * 2;
    }

    @ModifyArg(
            method = "<clinit>",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/feature/OrePlacedFeatures;modifiersWithRarity(ILnet/minecraft/world/gen/decorator/PlacementModifier;)Ljava/util/List;"),
            index = 0
    )
    private static int modifyRarity(int rarity) {
        return rarity / 2;
    }
}
