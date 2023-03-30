package com.github.TACOWASA059.wantedgame.utils;

import com.github.TACOWASA059.wantedgame.WantedGame;
import com.github.TACOWASA059.wantedgame.mapRender.GetMap;
import com.github.TACOWASA059.wantedgame.mapRender.ImageDecoder;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

import static java.lang.Math.abs;
import static java.lang.Math.max;

public class SetBoard {
    public double coordinate_x;
    public double coordinate_y;
    public double coordinate_z;
    public double height;
    public double length;
    private World world;

    public double sys_x;
    public double sys_y;
    public double sys_z;
    private double dis;

    //背景ブロックの座標リスト
    public HashSet<Block> blockHashSet =new HashSet<>();

    //生成中のarmorstandリスト
    public HashMap<ArmorStand,Vector> armorStandList=new HashMap();
    //Wanted
    public ArmorStand wanted;

    private int taskid;
    private int level;
    public double Timer;
    private BossBar bossBar;

    public SetBoard(double coordinate_x, double coordinate_y, double coordinate_z, double height, double length, World world,Vector direction){
        this.coordinate_x=coordinate_x;
        this.coordinate_y=coordinate_y;
        this.coordinate_z=coordinate_z;
        this.height=height;
        this.length=length;
        this.world=world;
        init(direction);
    }
    //盤面の作成
    public void init(Vector direction){
        double x=direction.getX();
        double z=direction.getZ();
        if(abs(x)>= abs(z)){
            if(x!=0.0) dis=x/abs(x);
            else dis=1.0;
            for(int i=0;i<height;i++){
                for(int j=0;j<length;j++){
                    Location location=new Location(world,coordinate_x+dis,coordinate_y+i,coordinate_z+j);
                    sys_x=0.0;
                    sys_y=height;
                    sys_z=length;
                    location.getBlock().setType(Material.QUARTZ_BLOCK);
                    blockHashSet.add(location.getBlock());
                }
            }
        }
        else{
            if(z!=0.0) dis=z/abs(z);
            else dis=1.0;
            for(int i=0;i<height;i++){
                for(int j=0;j<length;j++){
                    Location location=new Location(world,coordinate_x+j,coordinate_y+i,coordinate_z+dis);
                    sys_z=0.0;
                    sys_y=height;
                    sys_x=length;
                    location.getBlock().setType(Material.QUARTZ_BLOCK);
                    blockHashSet.add(location.getBlock());
                }
            }
        }
    }
    //最初のカウントダウン用
    BukkitTask task;
    public void startGame(){
        Timer=20.0;
        level=0;
        final int[] count = {5};
        task = new BukkitRunnable() {
            @Override
            public void run() {
                count[0] = count[0] > 0 ? count[0] - 1 : 0;
                if (count[0] == 0) {
                    start();
                    if(task !=null) task.cancel();
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1, 1);
                    }
                    return;
                }
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.sendActionBar("あと" + count[0] + "秒で開始します。");
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1, 1);
                }
            }
        }.runTaskTimer(WantedGame.plugin, 0, 20L);

        /*
        Bukkit.getScheduler().runTaskLater(WantedGame.plugin,()->{
            remove_armorstand();
        },500L);
        */

    }
    public void endGame(){
        end();
        if(task!=null) task.cancel();
        if(bossBar!=null) bossBar.removeAll();
    }
    public void start(){
        level+=1;
        SummonArmorStand();
        taskid=Bukkit.getScheduler().scheduleSyncRepeatingTask(WantedGame.plugin,()->{
            reflection_boundary();
            if(bossBar!=null)bossBar.removeAll();
            Integer time=(int)Timer;
            bossBar=Bukkit.createBossBar("あと "+time+" 秒", BarColor.PURPLE,BarStyle.SOLID);
            bossBar.setProgress(Math.max(0.0,Math.min(Timer/20.0,1.0))); //進行度 0 ~ 1 の値 (0.5だと半分)
            for(Player player:Bukkit.getOnlinePlayers()){
                bossBar.addPlayer(player);
            }
            bossBar.setVisible(true);
            if(Timer<=0.0){
                end();
                for(Player player:Bukkit.getOnlinePlayers()){
                    player.playSound(player.getLocation(),Sound.BLOCK_ANVIL_USE,1,1);
                    player.sendTitle(ChatColor.BOLD+"level"+ChatColor.AQUA+level+ChatColor.GREEN+"で終了","",1,20,1);
                    player.sendMessage(ChatColor.BOLD+"level"+ChatColor.AQUA+level+ChatColor.GREEN+"で終了");
                }
                bossBar.removeAll();
            }
            Timer-=1.0/20.0;
        },0L,1L);
    }
    public void end(){
        remove_armorstand();
        Bukkit.getScheduler().cancelTask(taskid);
    }
    public void SummonArmorStand(){
        int max_num=Math.min(PlayerList.uuid_list.size(),level*5+10);
        List<String> uuid_list=new ArrayList<>(PlayerList.uuid_list);
        Collections.shuffle(uuid_list);
        for(int i=0;i<max_num;i++){
            String name=WantedGame.plugin.getCustomConfig().getString(uuid_list.get(i)+".MCID");
            ItemStack head=PlayerHead.head_list.get(uuid_list.get(i));
            Random random=new Random();
            Double random_x=random.nextDouble()*max(sys_x-1.0,0.0);
            Double random_y=random.nextDouble()*(sys_y-1.0)-1.5;
            Double random_z=random.nextDouble()*max(sys_z-1.0,0.0);

            Location location=new Location(world,random_x+coordinate_x,random_y+coordinate_y,random_z+coordinate_z);
            if(sys_x==0.0&&dis>=0){
                location.setYaw(90);
            }else if(sys_x==0.0&& dis<0){
                location.setYaw(270);
            }else if(sys_z==0.0&&dis>=0){
                location.setYaw(180);
            }else if(sys_z==0.0&& dis<0){
                location.setYaw(0);
            }
            Entity entity=world.spawnEntity(location, EntityType.ARMOR_STAND);

            Double velocity_x=(random.nextDouble()-0.5)/5.0;
            Double velocity_y=(random.nextDouble()-0.5)/5.0;
            Double velocity_z=(random.nextDouble()-0.5)/5.0;
            if(sys_x==0.0)velocity_x=0.0;
            if(sys_z==0.0)velocity_z=0.0;
            Vector vector=new Vector(velocity_x,velocity_y,velocity_z);
            ArmorStand armor=(ArmorStand) entity;
            armor.setHelmet(head);
            armor.setGravity(false);
            armor.setInvisible(true);
            armor.setCustomName(name);
            armorStandList.put(armor,vector);
            if(i==0)wanted=armor;
        }
        String base64=WantedGame.plugin.getCustomConfig().getString(uuid_list.get(0)+".skin");
        BufferedImage image=null;
        try {
            image= ImageDecoder.decodeImage(base64);
            image=ImageDecoder.extractFace(image);
            image=ImageDecoder.addtext(image,WantedGame.plugin.getCustomConfig().getString(uuid_list.get(0)+".MCID"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if(image!=null){
            ItemStack map=GetMap.output(image,world);
            for(Player player:Bukkit.getOnlinePlayers()){
                Inventory inventory=player.getInventory();
                inventory.setItem(0,map);
            }
        }

        for(Player player:Bukkit.getOnlinePlayers()){
            player.sendTitle(ChatColor.RED + "WANTED", ChatColor.AQUA + wanted.getName() + ChatColor.GREEN + "を探せ!",5 ,25,5);
        }

    }
    public void remove_armorstand(){
        for(ArmorStand armorStand:armorStandList.keySet()){
            armorStand.remove();
        }
        Bukkit.getScheduler().cancelTask(taskid);
        armorStandList=new HashMap<>();
    }
    public void periodic_boundary(){
        for(ArmorStand armorStand:armorStandList.keySet()){
            if(armorStand!=null){
                Location ref_location= new Location(world,coordinate_x,coordinate_y,coordinate_z);
                Location entity_loc=armorStand.getLocation().clone().add(armorStandList.get(armorStand)).subtract(ref_location);
                Double x=armorStand.getLocation().clone().add(armorStandList.get(armorStand)).getX();
                Double y=armorStand.getLocation().clone().add(armorStandList.get(armorStand)).getY();
                Double z=armorStand.getLocation().clone().add(armorStandList.get(armorStand)).getZ();
                if(entity_loc.getX()<0.0){
                    x+=sys_x;
                }
                else if(entity_loc.getX()>sys_x){
                    x-=sys_x;
                }
                if(entity_loc.getY()<-2.0){
                    y+=sys_y;
                }
                else if(entity_loc.getY()>sys_y-2.0){
                    y-=sys_y;
                }
                if(entity_loc.getZ()<0.0){
                    z+=sys_z;
                }
                else if(entity_loc.getZ()>sys_z){
                    z-=sys_z;
                }
                armorStand.teleport(new Location(world,x,y,z));
            }
        }
    }
    public void reflection_boundary(){
        for(ArmorStand armorStand:armorStandList.keySet()){
            if(armorStand!=null){
                Location ref_location= new Location(world,coordinate_x,coordinate_y,coordinate_z);
                Location entity_loc=armorStand.getLocation().clone().add(armorStandList.get(armorStand)).subtract(ref_location);
                Vector vector=armorStandList.get(armorStand);
                Double x=vector.getX();
                Double y=vector.getY();
                Double z=vector.getZ();
                if (sys_x != 0.0) {
                    if(entity_loc.getX()<0.0){
                        x=abs(x);
                    }
                    else if(entity_loc.getX()>sys_x){
                        x=-abs(x);
                    }
                }

                if(entity_loc.getY()<-2.0){
                    y=abs(y);
                }
                else if(entity_loc.getY()>sys_y-2.0){
                    y=-abs(y);
                }
                if (sys_z != 0.0) {
                    if(entity_loc.getZ()<0.0){
                        z=abs(z);
                    }
                    else if(entity_loc.getZ()>sys_z){
                        z=-abs(z);
                    }
                }
                armorStandList.put(armorStand,new Vector(x,y,z));
                armorStand.teleport(armorStand.getLocation().clone().add(new Vector(x,y,z)));
            }
        }
    }
}
