package com.github.TACOWASA059.wantedgame.utils;

import com.github.TACOWASA059.wantedgame.WantedGame;

import java.util.HashSet;
import java.util.Set;

public class PlayerList {
    public static Set<String> uuid_list=new HashSet<>();
    public PlayerList(){
        uuid_list=new HashSet<>();
        uuid_list=WantedGame.plugin.getCustomConfig().getKeys(false);
    }

}
