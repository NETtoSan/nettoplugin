package nettoplugin;

import arc.*;
import mindustry.content.Blocks;
import mindustry.core.GameState;
import mindustry.game.EventType;
import net.dv8tion.*;
import org.javacord.*;
import arc.Core;
import mindustry.mod.Plugin;
import mindustry.net.Administration.PlayerAction;
import mindustry.game.EventType.*;
import mindustry.gen.Player;
import mindustry.core.GameState.*;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.ChannelType;
import org.javacord.api.DiscordApi;
import nettoplugin.Command.test;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.channel.Channel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.event.message.reaction.ReactionRemoveEvent;
import org.json.JSONObject;
import org.json.JSONTokener;
import static mindustry.Vars.*;
import javax.security.auth.login.LoginException;
import java.util.List;
import java.util.Optional;

public class mindustryCommand extends Plugin{
    private final Long CDT = 300L;
    private final String FileNotFoundErrorMessage = "File not found!";
    public static JSONObject alldata;
    private JSONObject data;
    public static JDA jda;

    //Automated message. Using javacord first!
    public DiscordApi api = null;
    public mindustryCommand(){
        System.out.println("It loaded!");
        try{
            String pureJSON = Core.settings.getDataDirectory().child("mods/settings.json").readString();
            alldata = new JSONObject(new JSONTokener(pureJSON));
            if(!alldata.has("in-game")) {
                System.out.println("[ERR!] nettoplugin: settings.json has an invalid format!\n");
                return;
            }
            else{
                data = alldata.getJSONObject("in-game");
            }
        }
        catch(Exception e){
            if(e.getMessage().contains(FileNotFoundErrorMessage)){
                System.out.println("[ERR!] nettoplugin: settings.json file is missing");
                return;
            }
            else{
                System.out.println("[ERR!] nettoplugin: Init error");
                return ;
            }
        }
        try{
            System.out.println("It loaded! and made through here!");
            jda = JDABuilder.createDefault(alldata.getString("token")).build();
            jda.getPresence().setStatus(OnlineStatus.ONLINE);
            jda.getPresence().setActivity(Activity.listening("absolutely nothing"));
            jda.addEventListener(new test());
            try{
                api = new DiscordApiBuilder().setToken(alldata.getString("token")).login().join();
            }
            catch(Exception e){
                System.out.println("[ERROR!] javacord: Cannot login!");
            }
        }
        catch(Exception e){
            if(e.getMessage().contains("READY Packet")){
                System.out.println("\n[ERR!] nettoplugin: invalid token.\n");
            }
        }

        if(data.has("live_chat_channel_id")){
            TextChannel tc = this.getTextChannel(data.getString("live_chat_channel_id"));
            if(tc != null){
                Events.on(EventType.PlayerChatEvent.class, event->{
                    tc.sendMessage("**" + event.player.name.replace("*","+")+ "**: " + event.message);
                });
                Events.on(ServerLoadEvent.class,event->{
                    if(state.isMenu()){
                        tc.sendMessage("**STATUS** Server is closed!");
                    }
                    if(state.isPaused()){
                        tc.sendMessage("**STATUS** Server is opened!");
                    }
                });
            }
        }
    }
    @Override
    public void init(){
        TextChannel tc = this.getTextChannel(data.getString("live_chat_channel_id"));
        Events.on(BuildSelectEvent.class, event->{
            if(!event.breaking && event.builder.buildPlan() != null && event.builder.buildPlan().block == Blocks.thoriumReactor && event.builder.isPlayer()){
                Player player = event.builder.getPlayer();
                tc.sendMessage("**ALERT!** "+ player.name+ " started building a reactor at " + event.tile.x + " , " + event.tile.y+"!");
            }
            if(event.breaking){
                
            }
        });
        Events.on(PlayerAction.class, event->{
            if(event.block.placeableOn){

            }
        });
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
}