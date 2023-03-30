package com.github.TACOWASA059.wantedgame.listeners;

import com.github.TACOWASA059.wantedgame.WantedGame;
import com.github.TACOWASA059.wantedgame.utils.SetBoard;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageListener implements Listener{
    @EventHandler
    public void onEntityDamageEvent(EntityDamageEvent e){
        Entity entity=e.getEntity();
        if(entity.getType()!= EntityType.ARMOR_STAND){
            return;
        }
        for(SetBoard setBoard: WantedGame.plugin.setBoardList.values()){
            if(setBoard.wanted==(ArmorStand) entity){
                setBoard.end();
                setBoard.remove_armorstand();
                for(Player player: Bukkit.getOnlinePlayers()){
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP,1,1);
                    Bukkit.getScheduler().runTaskLater(WantedGame.plugin,()->{
                        setBoard.Timer=Math.min(40.0,setBoard.Timer+5.0);
                        setBoard.start();
                    },20L);
                }
            }
            else{
                if(setBoard.armorStandList.containsKey(entity)){
                    setBoard.Timer=Math.max(0.0,setBoard.Timer-1);
                    for(Player player:Bukkit.getOnlinePlayers()){
                        player.playSound(player.getLocation(),Sound.ENTITY_BOAT_PADDLE_WATER,1,1);
                    }
                }
            }
        }
    }
}
