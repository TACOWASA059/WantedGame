package com.github.TACOWASA059.wantedgame.utils;

import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedSignedProperty;
import com.github.TACOWASA059.wantedgame.WantedGame;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Field;
import java.util.*;

public class PlayerHead {
    public static Map<String, ItemStack> head_list=new HashMap<>();
    public PlayerHead(Set<String > list){
        for(String uuid:list){
            addPlayerhead(uuid);
        }
    }
    public Boolean addPlayerhead(String uuid){
        ItemStack head = null;
        try {
            head = getPlayerHead(uuid);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        head_list.put(uuid,head);
        return true;
    }
    //プレイヤーheadを取得
    public static ItemStack getPlayerHead(String uuid) throws Exception {
        UUID uuID=UUID.fromString(uuid);
        String MCID=null;
        String value=null;
        String signature=null;
        try{
            MCID=WantedGame.plugin.getCustomConfig().getString(uuid+"."+"MCID");
            value=WantedGame.plugin.getCustomConfig().getString(uuid+"."+"value");
            signature=WantedGame.plugin.getCustomConfig().getString(uuid+"."+"signature");
        }catch (NullPointerException e){
            System.out.println(uuid);
        }

        WrappedGameProfile profile = new WrappedGameProfile(uuID, MCID);
        profile.getProperties().put("textures", new WrappedSignedProperty("textures", value,signature));
        ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
        ItemMeta meta = head.getItemMeta();
        meta.setDisplayName(MCID);
        Field profileField;
        try {
            profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, profile.getHandle());
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e1) {
            e1.printStackTrace();
        }
        head.setItemMeta(meta);
        return head;
    }

}
