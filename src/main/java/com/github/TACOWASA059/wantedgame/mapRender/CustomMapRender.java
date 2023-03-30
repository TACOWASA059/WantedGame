package com.github.TACOWASA059.wantedgame.mapRender;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;

import java.awt.image.BufferedImage;

public class CustomMapRender extends MapRenderer {
    private BufferedImage img;

    public CustomMapRender(BufferedImage img) {
        this.img = img;
    }

    @Override
    public void render(@NotNull MapView mapView, @NotNull MapCanvas mapCanvas, @NotNull Player player) {
        //x_axis -left to right (0-127)
        //y_axis -top to bottom (0-127)
        mapCanvas.drawImage(0, 0, MapPalette.resizeImage(img));
    }
}