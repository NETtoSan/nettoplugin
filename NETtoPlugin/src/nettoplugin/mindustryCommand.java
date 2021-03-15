package nettoplugin;


//javacord

import arc.util.*;
import arc.Core;
import arc.Events;

import mindustry.game.EventType;

import mindustry.gen.*;
import mindustry.content.*;
import mindustry.mod.*;
import mindustry.mod.Plugin;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.channel.Channel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.permission.Role;

//json
import org.json.JSONObject;
import org.json.JSONTokener;

import java.lang.Thread;
import java.util.HashMap;
import java.util.Optional;


public class mindustryCommand extends Plugin{

    private final Long CDT = 300L;
    private final String FileNotFoundErrorMessage = "File not found! config\\mods\\settings.json";
    private JSONObject alldata;
    private JSONObject data;
    private DiscordApi api = null;
    private HashMap<Long,String> cooldowns = new HashMap<Long,String>();

    public mindustryCommand(){
        Log.info("Loaded nettoplugin constructor");

        try{
            String pureJSON = Core.settings.getDataDirectory().child("mods/settings.json").readString();
            alldata = new JSONObject(new JSONTokener(pureJSON));
            if(!alldata.has("in-game")){
                System.out.println("[ERR!] nettoplugin: settings.json has an invalit format!\n");
                return;
            } else {
                data = alldata.getJSONObject("in-game");
            }
        } catch (Exception e){
            if(e.getMessage().contains(FileNotFoundErrorMessage)){
                System.out.println("[ERR!] nettoplugin: settings.json file is missing.\nBot cant start!");
                return;
            } else {
                System.out.println("[ERR!] nettoplugin: Init error");
                return;
            }
        }
        try{
            api = new DiscordApiBuilder().setToken(alldata.getString("token")).login().join();
        }catch(Exception e){
            if(e.getMessage().contains("READY Packet")){
                System.out.println("\n[ERR!] nettoplugin: invalid token.\n");
            } else {
                e.printStackTrace();
            }
        }

        Log.info("This has run");
        BotThread bt = new BotThread(api,Thread.currentThread(),alldata.getJSONObject("discord"));
        bt.setDaemon(false);
        bt.start();

        if(data.has("live_chat_channel_id")){
            TextChannel tc = this.getTextChannel(data.getString("live_chat_channel_id"));
            if(tc != null){
                Events.on(EventType.PlayerChatEvent.class, event->{
                   tc.sendMessage("**" + event.player.name.replace("*","+")+ "**: " + event.message);
                });
            }
        }
    }
    @Override
    public void init(){
        Events.on(EventType.BuildSelectEvent.class, event ->{
            if(!event.breaking && event.builder != null && event.builder.buildPlan() != null && event.builder.buildPlan().block == Blocks.microProcessor && event.builder.isPlayer()){
                Player player = event.builder.getPlayer();

                Call.sendMessage("[scarlet]ALERT![] " + player.name + " has begun building a microprocessor at " + event.tile.x + ", " + event.tile.y);

                //send to discord
                TextChannel tc = this.getTextChannel(data.getString("live_chat_channel_id"));
                if(tc != null){
                    tc.sendMessage("**[ALERT!]** "+ player.name + " ("+player.uuid()+") has begun building a microprocessor at " + event.tile.x + ", " + event.tile.y);
                }
            }
        });
    }
    @Override
    public void registerServerCommands(CommandHandler handler){
    }
    @Override
    public void registerClientCommands(CommandHandler handler){
        if(api != null){
            handler.<Player>register("d" , "<text...>","Sends a message to moderators", (args,player)->{
                if(args[0] == "null"){
                    player.sendMessage("[scarlet]Please enter a context!");
                }
                if(!data.has("dchannel_id")){
                    player.sendMessage("[scarlet]This command is disabled!");
                }
                else {
                    TextChannel tc = this.getTextChannel(data.getString("dchannel_id"));
                    if(tc == null) {
                        player.sendMessage("[scarlet]This command has no message configuration!");
                        return;
                    }
                    tc.sendMessage(player.name + " *@Mindustry* :"+ args[0]);
                    player.sendMessage("Sent!");
                }
            });
        } else {
            Log.err("[ERR!] API null!");
        }
    }
    public TextChannel getTextChannel(String id){
        Optional<Channel> dc = ((Optional<Channel>)this.api.getChannelById(id));
        if(!dc.isPresent()){
            System.out.println("[ERR!] nettoplugin: channel not found");
            return null;
        }
        Optional<TextChannel> dtc = dc.get().asTextChannel();
        if(!dtc.isPresent()){
            System.out.println("[ERR!] nettoplugin: textchannel not found!");
            return null;
        }
        return dtc.get();
    }
    public Role getRole(String id){
        Optional<Role> r1 = this.api.getRoleById(id);
        if(!r1.isPresent()){
            System.out.println("[ERR!] nettoplugin: adminrole not found!");
            return null;
        }
        return r1.get();
    }
}