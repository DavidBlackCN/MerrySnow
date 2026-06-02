package com.sighs.merrysnow.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.sighs.merrysnow.Config;
import com.sighs.merrysnow.compat.sereneseasons.SereneSeasonsCompat;
import com.sighs.merrysnow.init.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Biome.class)
public class ClientBiomeMixin {
    @Inject(method = "getPrecipitationAt", at = @At("HEAD"), cancellable = true)
    private void noWeather(BlockPos blockPos, int i, CallbackInfoReturnable<Biome.Precipitation> cir) {
        String id = Utils.getBiomeId(Minecraft.getInstance().level, (Biome) (Object) this);
        String enforce = Config.ENFORCE_SNOW_WEATHER.get();
        if (!"default".equals(enforce)) {
            cir.setReturnValue(Boolean.parseBoolean(enforce) ? Biome.Precipitation.SNOW : Biome.Precipitation.RAIN);
            return;
        }
        String result = Config.getWeatherModify(id);
        if ("none".equals(result)) {
            cir.setReturnValue(Biome.Precipitation.NONE);
        } else if ("snow".equals(result)) {
            cir.setReturnValue(Biome.Precipitation.SNOW);
        } else if ("rain".equals(result)) {
            cir.setReturnValue(Biome.Precipitation.RAIN);
        }
    }

    @WrapOperation(method = "getPrecipitationAt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/biome/Biome;coldEnoughToSnow(Lnet/minecraft/core/BlockPos;I)Z"))
    private boolean customSnowWeather(Biome biome, BlockPos blockPos, int i, Operation<Boolean> original) {
        String enforce = Config.ENFORCE_SNOW_WEATHER.get();

        // Non-default global enforce always wins (explicit user intent)
        if (!"default".equals(enforce)) {
            return Boolean.parseBoolean(enforce);
        }

        // Per-biome overrides always apply
        String id = Utils.getBiomeId(Minecraft.getInstance().level, biome);
        String result = Config.getWeatherModify(id);
        if (!result.equals("default")) {
            return result.equals("snow");
        }

        // Default with no override: defer to vanilla (Serene Seasons modifies temperature here)
        return original.call(biome, blockPos, i);
    }
}
