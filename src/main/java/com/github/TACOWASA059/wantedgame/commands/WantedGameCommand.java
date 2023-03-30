package com.github.TACOWASA059.wantedgame.commands;

import com.github.TACOWASA059.wantedgame.WantedGame;
import com.github.TACOWASA059.wantedgame.download.Downloader;
import com.github.TACOWASA059.wantedgame.utils.PlayerList;
import com.github.TACOWASA059.wantedgame.utils.SetBoard;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class WantedGameCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player){
            Player player=(Player) sender;
            if(!player.isOp()){
                player.sendMessage(ChatColor.RED+"このコマンドを実行する権限がありません");
                return true;
            }
            if(args.length==3&&args[0].equalsIgnoreCase("config")&&args[1].equalsIgnoreCase("setHeight")){
                Double height=null;
                try{
                    height=Double.parseDouble(args[2]);
                }catch (NumberFormatException e){
                    player.sendMessage(ChatColor.RED+"数値が間違っています");
                    player.sendMessage(ChatColor.GREEN+"/wg config setHeight <height>");
                    return true;
                }
                WantedGame.plugin.getConfig().set("height",height);
                player.sendMessage(ChatColor.GREEN+"高さが"+WantedGame.plugin.getConfig().get("height")+"に設定されました");
                return true;
            }
            else if(args.length==3&&args[0].equalsIgnoreCase("config")&&args[1].equalsIgnoreCase("setLength")){
                Double length=null;
                try{
                    length=Double.parseDouble(args[2]);
                }catch (NumberFormatException e){
                    player.sendMessage(ChatColor.RED+"数値が間違っています");
                    player.sendMessage(ChatColor.GREEN+"/wg config setLength <height>");
                    return true;
                }
                WantedGame.plugin.getConfig().set("length",length);
                player.sendMessage(ChatColor.GREEN+"長さが"+WantedGame.plugin.getConfig().get("length")+"に設定されました");
                return true;
            }
            else if(args.length==1&&args[0].equalsIgnoreCase("setLoc")){
                Location location=player.getLocation();
                WantedGame.plugin.getConfig().set("coordinate.x",location.getX());
                WantedGame.plugin.getConfig().set("coordinate.y",location.getY());
                WantedGame.plugin.getConfig().set("coordinate.z",location.getZ());
                player.sendMessage(ChatColor.GREEN+"基準座標が"+String.format("%.1f", WantedGame.plugin.getConfig().get("coordinate.x"))+" "+String.format("%.1f", WantedGame.plugin.getConfig().get("coordinate.y"))+" "+String.format("%.1f", WantedGame.plugin.getConfig().get("coordinate.z"))+"に設定されました");
                return true;
            }
            else if(args.length==4&& args[0].equalsIgnoreCase("setLoc")){
                Double x=null,y=null,z=null;
                try{
                    x=Double.parseDouble(args[1]);
                    y=Double.parseDouble(args[2]);
                    z=Double.parseDouble(args[3]);
                }catch (NumberFormatException e){
                    player.sendMessage(ChatColor.RED+"数値が間違っています");
                    player.sendMessage(ChatColor.GREEN+"/wg setLoc x y z");
                    return true;
                }
                WantedGame.plugin.getConfig().set("coordinate.x",x);
                WantedGame.plugin.getConfig().set("coordinate.y",y);
                WantedGame.plugin.getConfig().set("coordinate.z",z);
                player.sendMessage(ChatColor.GREEN+"基準座標が"+String.format("%.1f", WantedGame.plugin.getConfig().get("coordinate.x"))+" "+String.format("%.1f", WantedGame.plugin.getConfig().get("coordinate.y"))+" "+String.format("%.1f", WantedGame.plugin.getConfig().get("coordinate.z"))+"に設定されました");
                return true;
            }
            else if(args.length==2 && args[0].equalsIgnoreCase("config")&&args[1].equalsIgnoreCase("show")){
                player.sendMessage(ChatColor.AQUA+"基準座標"+ChatColor.GREEN+" : "+ChatColor.GREEN+String.format("%.1f", WantedGame.plugin.getConfig().get("coordinate.x"))+" "+String.format("%.1f", WantedGame.plugin.getConfig().get("coordinate.y"))+" "+String.format("%.1f", WantedGame.plugin.getConfig().get("coordinate.z")));
                player.sendMessage(ChatColor.AQUA+"長さ(横幅)"+ChatColor.GREEN+" : "+WantedGame.plugin.getConfig().get("length"));
                player.sendMessage(ChatColor.AQUA+"高さ(縦幅)"+ChatColor.GREEN+" : "+WantedGame.plugin.getConfig().get("height"));
                return true;
            }
            else if(args.length==2 && args[0].equalsIgnoreCase("config")&&args[1].equalsIgnoreCase("save")){
                WantedGame.plugin.saveConfig();
                player.sendMessage(ChatColor.GREEN+"コンフィグを保存しました。");
                return true;
            }
            else if(args.length==2&&args[0].equalsIgnoreCase("setBoard")){
                if(WantedGame.plugin.setBoardList.containsKey(args[1])){
                    player.sendMessage(ChatColor.RED+"そのボード名は既に使われています");
                    return true;
                }
                SetBoard setBoard=new SetBoard(WantedGame.plugin.getConfig().getDouble("coordinate.x"),
                        WantedGame.plugin.getConfig().getDouble("coordinate.y"),
                        WantedGame.plugin.getConfig().getDouble("coordinate.z"),
                        WantedGame.plugin.getConfig().getDouble("height"),
                        WantedGame.plugin.getConfig().getDouble("length"),
                        player.getWorld(),player.getLocation().getDirection());
                WantedGame.plugin.setBoardList.put(args[1],setBoard);
                player.sendMessage(ChatColor.GREEN+"ボード"+ChatColor.AQUA+args[1]+ChatColor.GREEN+"が設定されました。");
                return true;
            }
            else if(args.length==2&& args[0].equalsIgnoreCase("removeBoard")){
                SetBoard board=WantedGame.plugin.setBoardList.get(args[1]);
                for(Block block:board.blockHashSet){
                    block.setType(Material.AIR);
                }
                board.endGame();
                if(!WantedGame.plugin.setBoardList.containsKey(args[1])){
                    player.sendMessage(ChatColor.RED+"そのボードは登録されていません");
                }
                WantedGame.plugin.setBoardList.remove(args[1]);
                player.sendMessage(ChatColor.GREEN+"ボード"+ChatColor.AQUA+args[1]+ChatColor.GREEN+"は正常に削除されました。");
                return true;
            }
            else if(args.length==2&&args[0].equalsIgnoreCase("startGame")){
                String name=args[1];
                if(!WantedGame.plugin.setBoardList.containsKey(name)){
                    player.sendMessage(ChatColor.RED+"そのボードは登録されていません");
                    return true;
                }
                WantedGame.plugin.setBoardList.get(name).startGame();
                player.sendMessage(ChatColor.GREEN+"ゲームが開始されました");
                return true;
            }
            else if(args.length==2&&args[0].equalsIgnoreCase("endGame")){
                String name=args[1];
                if(!WantedGame.plugin.setBoardList.containsKey(name)){
                    player.sendMessage(ChatColor.RED+"そのボードは登録されていません");
                    return true;
                }
                WantedGame.plugin.setBoardList.get(name).endGame();
                player.sendMessage(ChatColor.GREEN+"ゲームを終了しました");
                return true;
            }
            else if(args.length==1&&args[0].equalsIgnoreCase("usage")){
                player.sendMessage(ChatColor.LIGHT_PURPLE+"--------------------------");
                player.sendMessage(ChatColor.AQUA+"/wg setLoc"+ChatColor.GREEN+" : 盤面の基準位置を設定(左下か右下)");
                player.sendMessage(ChatColor.AQUA+"/wg setLoc x y z"+ChatColor.GREEN+" : 盤面の基準位置を設定(左下か右下)");
                player.sendMessage(ChatColor.AQUA+"/wg setBoard <board_name>"+ChatColor.GREEN+" : コンフィグを元にボードを設定");
                player.sendMessage(ChatColor.AQUA+"/wg startGame <board_name>"+ChatColor.GREEN+" : ゲーム開始");
                player.sendMessage(ChatColor.AQUA+"/wg endGame <board_name>"+ChatColor.GREEN+" : ゲーム終了");
                player.sendMessage(ChatColor.AQUA+"/wg removeBoard <board_name>"+ChatColor.GREEN+" : ボードの削除");
                player.sendMessage(ChatColor.LIGHT_PURPLE+"--------------------------");
                player.sendMessage(ChatColor.AQUA+"/wg config setHeight <height>"+ChatColor.GREEN+" : 盤面の高さを設定");
                player.sendMessage(ChatColor.AQUA+"/wg config setLength <length>"+ChatColor.GREEN+" : 盤面の長さ(横)を設定");
                player.sendMessage(ChatColor.AQUA+"/wg config show"+ChatColor.GREEN+" : コンフィグを表示");
                player.sendMessage(ChatColor.AQUA+"/wg config save"+ChatColor.GREEN+" : コンフィグを保存");
                player.sendMessage(ChatColor.AQUA+"/wg config getData <all/MCID>"+ChatColor.GREEN+" : プレイヤー名のスキンデータを取得");
                player.sendMessage(ChatColor.AQUA+"/wg config reload"+ChatColor.GREEN+" : コンフィグをリロード");
                player.sendMessage(ChatColor.LIGHT_PURPLE+"--------------------------");
                return true;
            }
            else if(args.length==3&&args[0].equalsIgnoreCase("config")&&args[1].equalsIgnoreCase("getData")){
                if(args[2].equalsIgnoreCase("all")){
                    for(String uuid: PlayerList.uuid_list){
                        String mcid=WantedGame.plugin.getCustomConfig().getString(uuid+".MCID");
                        try {
                            Downloader.getPlayerdata(player,mcid);
                        } catch (Exception e) {
                            player.sendMessage(ChatColor.RED+mcid+"のデータを取得できませんでした。");
                            throw new RuntimeException(e);
                        }
                    }
                    player.sendMessage(ChatColor.GREEN+"取得が完了しました。");

                    return true;
                }
                else{
                    try {
                        Downloader.getPlayerdata(player,args[2]);
                    } catch (Exception e) {
                        player.sendMessage(ChatColor.RED+args[2]+"のデータを取得できませんでした。");
                        throw new RuntimeException(e);
                    }
                    player.sendMessage(ChatColor.GREEN+"取得が完了しました。");
                    return true;
                }
            }
            else if(args.length==2&& args[0].equalsIgnoreCase("config")&&args[1].equalsIgnoreCase("reload")){
                WantedGame.plugin.reloadConfig();
                WantedGame.playerList=new PlayerList();
                player.sendMessage(ChatColor.GREEN+"現在コンフィグには"+ChatColor.AQUA+WantedGame.playerList.uuid_list.size()+ChatColor.GREEN+"人が登録されています。");
                player.sendMessage(ChatColor.GREEN+"リロードが完了しました。");
                return true;
            }
            player.sendMessage(ChatColor.RED+"コマンドが間違っています。");
            player.sendMessage("/wg usage で使用法を確認してください。");
        }
        return true;
    }
}
