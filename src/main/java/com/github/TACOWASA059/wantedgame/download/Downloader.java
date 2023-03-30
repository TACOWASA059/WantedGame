package com.github.TACOWASA059.wantedgame.download;

import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedSignedProperty;
import com.github.TACOWASA059.wantedgame.WantedGame;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.UUID;

public class Downloader {
    public static void getPlayerdata(Player player, String playerName) throws Exception {
        UUID uuid= getUUID(playerName);
        JsonArray property=getProperty(uuid);
        String value=property.get(0).getAsJsonObject().get("value").getAsString();
        String signature=property.get(0).getAsJsonObject().get("signature").getAsString();
        BufferedImage skin=getSkin(value);
        String base64=encodeImage(skin);
        WrappedGameProfile profile = new WrappedGameProfile(uuid, playerName);

        profile.getProperties().put("textures", new WrappedSignedProperty("textures", value,signature));
        WantedGame.plugin.getCustomConfig().set(uuid.toString()+".MCID",playerName);
        WantedGame.plugin.getCustomConfig().set(uuid.toString()+".value",value);
        WantedGame.plugin.getCustomConfig().set(uuid.toString()+".signature",signature);
        WantedGame.plugin.getCustomConfig().set(uuid.toString()+".skin",base64);
        player.sendMessage(ChatColor.GREEN+WantedGame.plugin.getCustomConfig().getString(uuid.toString()+".MCID"));
        WantedGame.plugin.getCustomConfig().save(WantedGame.plugin.customConfigFile);
    }
    //UUIDを取得する
    public static UUID getUUID(String playerName) throws IOException {
        String uuidUrl = "https://api.mojang.com/users/profiles/minecraft/" + playerName;
        URL url = new URL(uuidUrl);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
            Gson gson=new Gson();
            JsonObject obj = gson.fromJson(reader, JsonObject.class);
            String str=insertDashUUID(obj.get("id").getAsString());
            return UUID.fromString(str);
        }
    }
    //UUIDの編集
    public static String insertDashUUID(String uuid) {
        return uuid.substring(0, 8) + "-" + uuid.substring(8, 12) + "-" + uuid.substring(12, 16) + "-" + uuid.substring(16, 20) + "-" + uuid.substring(20);
    }

    //Textureの取得
    private static JsonArray getProperty(UUID uuid)throws Exception{
        String url = "https://sessionserver.mojang.com/session/minecraft/profile/" + uuid+ "?unsigned=false";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String line;
        StringBuilder response = new StringBuilder();
        while ((line = in.readLine()) != null) {
            response.append(line);
        }
        in.close();
        JsonObject jsonObject = new JsonParser().parse(response.toString()).getAsJsonObject();
        JsonArray properties = jsonObject.getAsJsonArray("properties");
        System.out.println(properties);
        return properties;
    }
    private static BufferedImage getSkin(String texturevalue){
        String skinData=texturevalue;
        skinData = skinData.replaceFirst("data:image/png;base64,", "");
        // Decode the skin data and save it as a BufferedImage
        JSONObject json_meta=new JSONObject(new String(Base64.getDecoder().decode(skinData)));
        String skin_url = json_meta.getJSONObject("textures").getJSONObject("SKIN").getString("url");
        BufferedImage skin = null;
        try {
            skin = ImageIO.read(new URL(skin_url));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return skin;
    }
    public static String encodeImage(BufferedImage image) {
        String encodedImage = null;
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ImageIO.write(image, "png", stream);
            byte[] bytes = stream.toByteArray();
            encodedImage = Base64.getEncoder().encodeToString(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return encodedImage;
    }
}
