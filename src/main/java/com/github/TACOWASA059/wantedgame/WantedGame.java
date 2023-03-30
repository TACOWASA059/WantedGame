package com.github.TACOWASA059.wantedgame;

import com.github.TACOWASA059.wantedgame.commands.WantedGameCommand;
import com.github.TACOWASA059.wantedgame.commands.WantedGameTabCompleter;
import com.github.TACOWASA059.wantedgame.listeners.BlockBreakListner;
import com.github.TACOWASA059.wantedgame.listeners.EntityDamageListener;
import com.github.TACOWASA059.wantedgame.utils.PlayerHead;
import com.github.TACOWASA059.wantedgame.utils.PlayerList;
import com.github.TACOWASA059.wantedgame.utils.SetBoard;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class WantedGame extends JavaPlugin {
    public static WantedGame plugin;
    public File customConfigFile;
    public static PlayerHead playerHead;


    private FileConfiguration customConfig;

    public static Map<String,SetBoard> setBoardList=new HashMap<>();

    public static PlayerList playerList;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin=this;
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        createCustomConfig();
        playerList=new PlayerList();
        playerHead=new PlayerHead(PlayerList.uuid_list);
        getServer().getPluginManager().registerEvents(new EntityDamageListener(),this);
        getServer().getPluginManager().registerEvents(new BlockBreakListner(),this);
        getCommand("wg").setExecutor(new WantedGameCommand());
        getCommand("wg").setTabCompleter(new WantedGameTabCompleter());

    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    //textureデータの保存
    private void createCustomConfig() {
        customConfigFile = new File(getDataFolder(), "UUID_list.yml");
        if (!customConfigFile.exists()) {
            customConfigFile.getParentFile().mkdirs();
            saveResource("UUID_list.yml", false);
        }

        customConfig = new YamlConfiguration();
        try {
            customConfig.load(customConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    public FileConfiguration getCustomConfig() {
        return customConfig;
    }
}
