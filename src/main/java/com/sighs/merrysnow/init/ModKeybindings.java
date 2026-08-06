package com.sighs.merrysnow.init;

import com.mojang.blaze3d.platform.InputConstants;
import com.sighs.merrysnow.MerrySnow;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public class ModKeybindings {
    public static final KeyMapping.Category MERRY_SNOW = KeyMapping.Category.register(Identifier.fromNamespaceAndPath(MerrySnow.MODID, "merry_snow"));
    public static final KeyMapping CONTROL_KEY = new KeyMapping("key.merrysnow.sunny_snow",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_M,
            MERRY_SNOW
    );

    public static void register() {
        KeyMappingHelper.registerKeyMapping(CONTROL_KEY);
    }
}
