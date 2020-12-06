package com.obstacle.desktop;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class AssetPacker {
    private static final boolean DRAW_DEBUG_OUTLINE = false;
    private static final String RAW_ASSET_PATH = "desktop/assets-raw";

    private static final String ASSET_PATH = "android/assets";

    public static void main(String[] args) {
        TexturePacker.Settings settings = new TexturePacker.Settings();
        settings.debug = DRAW_DEBUG_OUTLINE;
//        settings.atlasExtension = ".pack";
        settings.pot = false;

        TexturePacker.process(settings,
                RAW_ASSET_PATH + "/gameplay",
                ASSET_PATH + "/gameplay",
                "gameplay");
    }
}
