package com.github.TACOWASA059.wantedgame.mapRender;

import com.github.TACOWASA059.wantedgame.mapRender.CustomMapRender;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;

import java.awt.image.BufferedImage;

public class GetMap {
    public static ItemStack output(BufferedImage img, World world){
        ItemStack mapitem=new ItemStack(Material.FILLED_MAP,1);
        MapMeta mapMeta=(MapMeta)mapitem.getItemMeta();
        MapView mapview= Bukkit.createMap(world);

        //canvasなどの設定
        CustomMapRender mapRender=new CustomMapRender(img);
        mapview.getRenderers().clear();
        mapview.addRenderer(mapRender);

        mapMeta.setMapView(mapview);
        mapitem.setItemMeta(mapMeta);
        return mapitem;
    }
}