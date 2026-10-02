package com.sighs.merrysnow.init;

import com.mojang.blaze3d.platform.InputConstants;

public class ClientUtils {
    public static float randomSunnySnow;

    public static boolean isKeyPressed(int keyCode) {
        if (keyCode == InputConstants.UNKNOWN.getValue()) return false;
        return InputConstants.isKeyDown(keyCode);
    }
}
