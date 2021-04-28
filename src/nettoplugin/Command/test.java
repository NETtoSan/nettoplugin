package nettoplugin.Command;

import arc.Core;
import arc.Events;
import arc.util.Nullable;
import mindustry.*;
import mindustry.Vars;
import mindustry.game.EventType.*;
import mindustry.game.Team;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import mindustry.maps.Map;
import mindustry.maps.Maps.*;
import mindustry.maps.*;
import mindustry.net.Administration;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import nettoplugin.mindustryCommand;
import mindustry.core.GameState.*;
import mindustry.Vars.*;
import org.json.JSONObject;

import java.lang.reflect.Array;

import static mindustry.Vars.state;

public class test extends ListenerAdapter {
    private @Nullable Map nextMapOverride;
    private JSONObject dOb = mindustryCommand.alldata.getJSONObject("discord");;
    public Role crap;
    public Maps maps = new Maps();
    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        String[] args = event.getMessage().getContentRaw().split(" ");
        String roleID = dOb.getString("gameOver_role_id");
        if(args[0].startsWith("..") && !event.getChannel().getName().contains("bot")){
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("This is not a valid channel!");
            eb.setDescription("Please use it in <#783149893622562838>.");
            eb.setColor(0x33FFEC);

            event.getChannel().sendTyping().queue();
            event.getChannel().sendMessage(eb.build()).queue();

            return;
        }
        if(args[0].equalsIgnoreCase("..players")) {
            if(state.isMenu()){
                event.getChannel().sendTyping().queue();
                event.getChannel().sendMessage("**The server is down!** Server needs to be running before you can use this command\nTo view left players. NETto!_OS should have them all. Its been stored on the disk").queue();
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
            eb.setDescription("This is currently under testing. Significant stuffs coming in very soon!\nTo view left players. NETto!_OS should have them all. Its been stored on the disk");
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
        if(args[0].equalsIgnoreCase("..gameover")){
            Role validity = event.getGuild().getRoleById(roleID);
            if(validity != null){
                int identity = event.getMember().getRoles().indexOf(validity);
                System.out.println(identity);
                if(identity <= 0){
                    event.getChannel().sendTyping().queue();
                    event.getChannel().sendMessage("**No permission!**. Only NETtoTOWN managers can execute this command.").queue();
                    return;
                }
                crap = event.getMember().getRoles().get(identity);
                if(crap != null) {
                    if(state.isMenu()){
                        event.getChannel().sendTyping().queue();
                        event.getChannel().sendMessage("**The server is down!** Server needs to be running before you can use this command").queue();
                    }
                    else{
                        try{
                            event.getChannel().sendTyping().queue();
                            event.getChannel().sendMessage("Executing!").queue();
                            Events.fire(new GameOverEvent(Team.crux));

                        }
                        catch(Exception e){
                            event.getChannel().sendTyping().queue();
                            event.getChannel().sendMessage("There was an error INSIDE a gameover event!").queue();
                        }
                    }
                }
                else{
                    event.getChannel().sendTyping().queue();
                    event.getChannel().sendMessage("**No permission!**. Only NETtoTOWN managers can execute this command.").queue();
                }
            }
            return;
        }
        if(args[0].equalsIgnoreCase("..say")){
            String[] msg = event.getMessage().getContentRaw().split(" ",2);
            if(state.isMenu()){
                event.getChannel().sendTyping().queue();
                event.getChannel().sendMessage("**The server is down!** Server needs to be running before you can use this command").queue();
                return;
            }
            Call.sendMessage("[cyan]"+event.getAuthor().getName()+"@Discord >[]"+ msg[1].trim());
            event.getChannel().sendTyping().queue();
            event.getChannel().sendMessage("Sent!").queue();
            return;
        }
        if(args[0].equalsIgnoreCase("..help")){
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Help embed");
            eb.setDescription("This embed shows you all the commands where you can interact with NETtoTOWN");
            eb.setColor(0x33FFEC);
            eb.addField("Public commands","Prefix: *..*\n1. players\n2. test\n3. say\n4. help",false);
            eb.addField("Management commands","Prefix: *..*\n1. gameover",false);

            event.getChannel().sendTyping().queue();
            event.getChannel().sendMessage(eb.build()).queue();
            return;
        }
        if(args[0].equalsIgnoreCase("..host")){

        }
        if(args[0].equalsIgnoreCase("..info")){
            if(state.isMenu()){
                event.getChannel().sendTyping().queue();
                event.getChannel().sendMessage("**The server is down!** Server needs to be running before you can use this command").queue();
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
        else if(args[0].startsWith("..")){
            if(event.getAuthor().isBot()) return;
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Unknown command!");
            eb.setDescription("Run **..help** to get a list of all commands!");
            eb.setColor(0xFF3333);
            event.getChannel().sendTyping().queue();
            event.getChannel().sendMessage(eb.build()).queue();
            return;
        }
    }

}
