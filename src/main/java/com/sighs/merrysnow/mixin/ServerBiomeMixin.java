package com.sighs.merrysnow.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.sighs.merrysnow.Config;
import com.sighs.merrysnow.compat.sereneseasons.SereneSeasonsCompat;
import com.sighs.merrysnow.init.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Biome.class)
public class ServerBiomeMixin {
    @Unique
    LevelReader levelReader;

    @Inject(method = "shouldSnow", at = @At("HEAD"))
    private void store(LevelReader _levelReader, BlockPos p_47521_, CallbackInfoReturnable<Boolean> cir) {
        this.levelReader = _levelReader;
    }

    @Inject(method = "shouldFreeze(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;Z)Z", at = @At("HEAD"))
    private void store(LevelReader _levelReader, BlockPos p_47482_, boolean p_47483_, CallbackInfoReturnable<Boolean> cir) {
        this.levelReader = _levelReader;
    }


    @WrapOperation(
            method = "shouldSnow",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/biome/Biome;getPrecipitationAt(Lnet/minecraft/core/BlockPos;I)Lnet/minecraft/world/level/biome/Biome$Precipitation;"
            )
    )
    private Biome.Precipitation snow(Biome biome, BlockPos blockPos, int seaLevel, Operation<Biome.Precipitation> original) {
        String enforceCover = Config.ENFORCE_SNOW_COVER.get();

        // Non-default global override always wins (explicit user intent)
        if (!"default".equals(enforceCover)) {
            return Boolean.parseBoolean(enforceCover) ? Biome.Precipitation.SNOW : Biome.Precipitation.RAIN;
        }

        // Per-biome overrides always apply
        String id = Utils.getBiomeId(levelReader, biome);
        String result = Config.getSnowModify(id);
        if (!result.equals("default")) {
            return Boolean.parseBoolean(result) ? Biome.Precipitation.SNOW : Biome.Precipitation.RAIN;
        }

        // Default with no override: defer to vanilla (Serene Seasons modifies temperature here)
        return original.call(biome, blockPos, seaLevel);
    }

    @WrapOperation(method = "shouldFreeze(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;Z)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/biome/Biome;warmEnoughToRain(Lnet/minecraft/core/BlockPos;I)Z"))
    private boolean freeze(Biome biome, BlockPos blockPos, int i, Operation<Boolean> original) {
        String enforceFreeze = Config.ENFORCE_ICE_FREEZE.get();

        // Non-default global override always wins (explicit user intent)
        if (!"default".equals(enforceFreeze)) {
            return !Boolean.parseBoolean(enforceFreeze);
        }

        // Per-biome overrides always apply
        String id = Utils.getBiomeId(levelReader, biome);
        String result = Config.getFreezeModify(id);
        if (!result.equals("default")) {
            return !Boolean.parseBoolean(result);
        }

        // Default with no override: defer to vanilla (Serene Seasons modifies temperature here)
        return original.call(biome, blockPos, i);
    }
}
