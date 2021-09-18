package nettoplugin.Command;

import arc.Core;
import arc.files.Fi;
import arc.Events;
import arc.util.Nullable;
import mindustry.*;
import mindustry.mod.*;
import mindustry.maps.*;
import mindustry.Vars;
import mindustry.game.Team;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import mindustry.maps.Map;
import mindustry.maps.Maps.*;
import mindustry.mod.Plugin;
import mindustry.maps.Map;
import mindustry.net.Administration;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import nettoplugin.Autos.util;
import nettoplugin.mindustryCommand;


import mindustry.core.GameState.*;
import mindustry.Vars.*;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.io.*;
import java.io.File;
import java.awt.image.BufferedImage;
import java.lang.reflect.Array;

import static mindustry.Vars.state;

public class test extends ListenerAdapter {
    private @Nullable Map nextMapOverride;
    private JSONObject dOb = mindustryCommand.alldata.getJSONObject("discord");;
    public Role crap;
    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        String[] args = event.getMessage().getContentRaw().split(" ");
        String[] msg = event.getMessage().getContentRaw().split(" ",2);
        String roleID = dOb.getString("gameOver_role_id");
        if(args[0].startsWith("..") && !event.getChannel().getName().contains("bot")){
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("This is not a valid channel!");
            eb.setDescription("Please use it in <#783149893622562838>.");
            eb.setColor(0x33FFEC);

            //event.getChannel().sendTyping().queue();
            //Disable first event.getChannel().sendMessage(eb.build()).queue();

            return;
        }
        if(args[0].equalsIgnoreCase("..players")) {
            if(state.isMenu()){
              EmbedBuilder eb = new EmbedBuilder();
              eb.setTitle("The server is down!").setDescription("The server needs to be running before you can use this command").setColor(0xFF3333);
              event.getChannel().sendTyping().queue();
              event.getChannel().sendMessage(eb.build()).queue();
              return;
            }
            StringBuilder lijst = new StringBuilder();
            StringBuilder admins = new StringBuilder();
            lijst.append("Players: total "+ Groups.player.size());
            for(Player user: Groups.player){
                Administration.PlayerInfo userinfo = user.getInfo();
                admins.append(userinfo.lastName+ " / "+userinfo.admin+"\n");
            }
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Joined players");
            eb.setColor(0x33FFEC);
            eb.setDescription("Currnet players joined NETtoTOWN");
            eb.addField("Players total", lijst.toString(),false);
            eb.addField("Administrators",admins.toString(),false);

            event.getChannel().sendTyping().queue();
            event.getChannel().sendMessage(eb.build()).queue();
            return;
        }
        if(args[0].equalsIgnoreCase("..test")){
            event.getChannel().sendTyping().queue();
            event.getChannel().sendMessage(event.getMember().getRoles().toString()).queue();
            return;
        }
        if(args[0].equalsIgnoreCase("..say")){
            if(state.isMenu()){
              EmbedBuilder eb = new EmbedBuilder();
              eb.setTitle("The server is down!").setDescription("The server needs to be running before you can use this command").setColor(0xFF3333);
              event.getChannel().sendTyping().queue();
              event.getChannel().sendMessage(eb.build()).queue();
              return;
            }
            if(args.length < 2){
              EmbedBuilder eb = new EmbedBuilder();
              eb.setTitle("Enter message");
              eb.setDescription("Usage: ..say <context>");
              eb.setColor(0xFF3333);
              event.getChannel().sendTyping().queue();
              event.getChannel().sendMessage(eb.build()).queue();
              return;
            }
            Call.sendMessage("[cyan]"+event.getAuthor().getName()+"@Discord >[]"+ msg[1].trim());
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Sent!").setDescription("You("+event.getAuthor().getName()+"): "+ msg[1].trim()).setColor(0x33FFEC);
            event.getChannel().sendTyping().queue();
            event.getChannel().sendMessage(eb.build()).queue();
            return;
        }
        if(args[0].equalsIgnoreCase("..help")){
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Help embed");
            eb.setDescription("This embed shows you all the commands where you can interact with NETtoTOWN");
            eb.setColor(0x33FFEC);
            eb.addField("Public commands","Prefix: *..*\n1. players\n2. test\n3. say\n4. help\n5. info\n6. maps\n7. map",false);
            eb.addField("Management commands","Prefix: *..*\n1. gameover\n2. announce\n3. list\n4. host\n5. stop",false);

            event.getChannel().sendTyping().queue();
            event.getChannel().sendMessage(eb.build()).queue();
            return;
        }
        if(args[0].equalsIgnoreCase("..info")){
            if(state.isMenu()){
              EmbedBuilder eb = new EmbedBuilder();
              eb.setTitle("The server is down!").setDescription("The server needs to be running before you can use this command").setColor(0xFF3333);
              event.getChannel().sendTyping().queue();
              event.getChannel().sendMessage(eb.build()).queue();
              return;
            }
            try{
                EmbedBuilder eb = new EmbedBuilder();
                eb.setTitle("Information");
                eb.setDescription("NETtoTOWN status");
                eb.addField("Current map",state.map.name(),false);
                eb.addField("Performance",Core.graphics.getFramesPerSecond()+"FPS / "+ Core.app.getJavaHeap()/1024/1024+" Megabytes",false);
                eb.setColor(0x33FFEC);
                eb.addField("Players",""+Groups.player.size(), false);

                event.getChannel().sendTyping().queue();
                event.getChannel().sendMessage(eb.build()).queue();
                return;
            }
            catch(Exception e){

            }
        }
        if(args[0].equalsIgnoreCase("..maps")){
            StringBuilder nmap = new StringBuilder();
            EmbedBuilder eb = new EmbedBuilder();
            int i = 1;
            Vars.maps.reload();
            eb.setTitle("Maps").setDescription("Maps stored to NETtoTOWN").setColor(0x33FFEC);
            for(Map m: Vars.maps.customMaps()){
                nmap.append("**"+i+")**"+m.name()+"("+m.width+"/"+m.height+")\n");

            }
            eb.addField("Total maps: "+i,nmap.toString(),false);
            event.getChannel().sendTyping().queue();
            event.getChannel().sendMessage(eb.build()).queue();
        }
        if(args[0].equalsIgnoreCase("..map")){
          if(args.length < 2){
            EmbedBuilder eb = new EmbedBuilder().setTitle("Map name required!").setDescription("..map <map name>").setColor(0xFF3333);
            event.getChannel().sendTyping().queue();
            event.getChannel().sendMessage(eb.build()).queue();
            return;
          }
          Map map = util.findMap(msg[1].trim());
          if(map == null){
            EmbedBuilder eb = new EmbedBuilder().setTitle("Invalid map!").setDescription("find map by executing `..maps`").setColor(0xFF3333);
            event.getChannel().sendTyping().queue();
            event.getChannel().sendMessage(eb.build()).queue();
            return;
          }

          Fi mapFile = map.file;

          EmbedBuilder eb = new EmbedBuilder().setTitle(map.name()).setDescription(map.description()).setAuthor(map.author()).setColor(0x33FFEC);

          try{
            map.image
          }
          catch(Exception e){
            event.getChannel().sendTyping().queue();
            event.getChannel().sendMessage(new EmbedBuilder().setTitle("Error parsing map").setDescription(""+e).setColor(0xFF3333).build()).queue();
            return;
          }
          event.getChannel().sendTyping().queue();
          event.getChannel().sendMessage(eb.build()).queue();
        }
        else if(args[0].startsWith("..")){
            if(event.getAuthor().isBot()) return;
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Unknown command!");
            eb.setDescription("Run **..help** to get a list of all commands!");
            eb.setColor(0xFF3333);
            //event.getChannel().sendTyping().queue();
            //event.getChannel().sendMessage(eb.build()).queue();
            return;
        }
    }

}
