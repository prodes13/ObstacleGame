package com.obstacle.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class AssetDescriptors {

    public static final AssetDescriptor<BitmapFont> FONT1 = new AssetDescriptor<BitmapFont>(AssetPaths.UI_FONT1, BitmapFont.class);
    public static final AssetDescriptor<BitmapFont> FONT2 = new AssetDescriptor<BitmapFont>(AssetPaths.UI_FONT2, BitmapFont.class);

    public static final AssetDescriptor<TextureAtlas> GAME_PLAY = new AssetDescriptor<TextureAtlas>(AssetPaths.GAME_PLAY, TextureAtlas.class);

    private AssetDescriptors() {

    }
}
