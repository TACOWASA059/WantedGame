package com.github.TACOWASA059.wantedgame.listeners;

import com.github.TACOWASA059.wantedgame.WantedGame;
import com.github.TACOWASA059.wantedgame.utils.SetBoard;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListner implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        Block block=e.getBlock();
        for(SetBoard setBoard:WantedGame.plugin.setBoardList.values()){
            if(setBoard.blockHashSet.contains(block))e.setCancelled(true);
        }
    }
}
