package com.github.TACOWASA059.wantedgame.commands;

import com.github.TACOWASA059.wantedgame.WantedGame;
import com.github.TACOWASA059.wantedgame.utils.PlayerList;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class WantedGameTabCompleter implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> list=new ArrayList<>();
        if(args.length==1){
            list.add("setLoc");
            list.add("setBoard");
            list.add("removeBoard");
            list.add("startGame");
            list.add("endGame");
            list.add("config");
            list.add("usage");
        }
        else if(args.length==2&&args[0].equalsIgnoreCase("config")){
            list.add("setHeight");
            list.add("setLength");
            list.add("show");
            list.add("save");
            list.add("getData");
            list.add("reload");
        }
        else if(args.length==3&&args[0].equalsIgnoreCase("config")&&args[1].equalsIgnoreCase("getData")){
            for(String uuid:PlayerList.uuid_list){
                String name=WantedGame.plugin.getCustomConfig().getString(uuid+".MCID");
                list.add(name);
            }
            list.add("all");
        }
        else if(args.length==2&&args[0].equalsIgnoreCase("startGame")){
            list.addAll(WantedGame.setBoardList.keySet());
        }
        else if(args.length==2&&args[0].equalsIgnoreCase("endGame")){
            list.addAll(WantedGame.setBoardList.keySet());
        }
        else if(args.length==2&&args[0].equalsIgnoreCase("removeBoard")){
            list.addAll(WantedGame.setBoardList.keySet());
        }
        return  list;
    }
}
