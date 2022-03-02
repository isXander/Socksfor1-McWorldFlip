package dev.isxander.mcworldflip.mixins;

import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(DefaultBiomeFeatures.class)
public class DefaultBiomeFeaturesMixin {
    @ModifyArg(method = "addMonsters", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/SpawnSettings$SpawnEntry;<init>(Lnet/minecraft/entity/EntityType;III)V", ordinal = 6), index = 1)
    private static int modifyEndermanSpawnWeight(int weight) {
        return weight * 3;
    }
}
