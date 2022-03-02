package dev.isxander.mcworldflip;

import me.andrew.gravitychanger.accessor.RotatableEntityAccessor;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.mixin.object.builder.SpawnRestrictionAccessor;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.SubtitleS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleFadeS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Direction;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;

public class MainEntrypoint implements ModInitializer {
    private final int TICK_INTERVAL = 1200;

    @Override
    public void onInitialize() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            Direction direction = Direction.values()[wrapIndex(server.getTicks() % Integer.MAX_VALUE / TICK_INTERVAL, Direction.values().length)];

            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                if (getGravity(player) != direction) {
                    setGravity(player, direction);
                    sendTitle(player, new LiteralText("GRAVITY CHANGE").formatted(Formatting.RED), new LiteralText(direction.asString().toUpperCase()).formatted(Formatting.RED), 40, 100, 40);
                } else if (TICK_INTERVAL - (server.getTicks() % Integer.MAX_VALUE % TICK_INTERVAL) == TICK_INTERVAL / 6) {
                    sendTitle(player, new LiteralText("CHANGE INCOMING").formatted(Formatting.LIGHT_PURPLE), new LiteralText("GRAVITY WILL CHANGE IN " + (TICK_INTERVAL / 6) / 20 + " SECONDS").formatted(Formatting.LIGHT_PURPLE), 40, 100, 40);
                }
            }
        });

        BiomeModifications.addSpawn((selection) -> selection.getBiome().getCategory() != Biome.Category.NETHER && selection.getBiome().getCategory() != Biome.Category.THEEND && selection.getBiome().getCategory() != Biome.Category.NONE, SpawnGroup.MONSTER, EntityType.BLAZE, 100, 1, 3);

        ServerPlayerEvents.AFTER_RESPAWN.register((o, n, alive) -> playerStartup(n));
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> playerStartup(handler.getPlayer()));
    }

    private void playerStartup(ServerPlayerEntity player) {
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false));
    }

    private void setGravity(PlayerEntity player, Direction direction) {
        RotatableEntityAccessor accessor = (RotatableEntityAccessor) player;
        accessor.gravitychanger$setGravityDirection(direction, false);
    }

    private Direction getGravity(PlayerEntity player) {
        RotatableEntityAccessor accessor = (RotatableEntityAccessor) player;
        return accessor.gravitychanger$getGravityDirection();
    }

    private void sendTitle(ServerPlayerEntity player, Text title, Text subtitle, int fadeIn, int stay, int fadeOut) {
        player.networkHandler.sendPacket(new TitleS2CPacket(title));
        player.networkHandler.sendPacket(new SubtitleS2CPacket(subtitle));
        player.networkHandler.sendPacket(new TitleFadeS2CPacket(fadeIn, stay, fadeOut));
    }

    private int wrapIndex(int index, int size) {
        return (index + size) % size;
    }
}
