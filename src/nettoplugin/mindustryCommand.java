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
import net.dv8tion.jda.api.entities.MessageEmbed;

import org.javacord.api.DiscordApi;
import nettoplugin.Command.test;
import nettoplugin.Command.serverControls;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.channel.Channel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.reaction.ReactionRemoveEvent;
import org.json.JSONObject;
import org.json.JSONTokener;
import static mindustry.Vars.*;
import javax.security.auth.login.LoginException;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class mindustryCommand extends Plugin{
    private final Long CDT = 300L;
    private final String FileNotFoundErrorMessage = "File not found!";
    public static JSONObject alldata;
    private JSONObject data;
    public static JDA jda;
    private static final Logger LOG = LoggerFactory.getLogger(mindustryCommand.class);

    //Automated message. Using javacord first!
    public DiscordApi api = null;
    public mindustryCommand(){
        System.out.println("Loading NETtoPLUGIN assets");
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

            System.out.println("Loaded NETtoPLUGIN assets");
            jda = JDABuilder.createDefault(alldata.getString("token")).build();
            jda.getPresence().setStatus(OnlineStatus.ONLINE);
            jda.getPresence().setActivity(Activity.listening("NETto's pain"));
            jda.addEventListener(new test());
            jda.addEventListener(new serverControls());
        }
        catch(Exception e){
            if(e.getMessage().contains("READY Packet")){
                System.out.println("\n[ERR!] nettoplugin: invalid token.\n");
            }
        }
        if(data.has("live_chat_channel_id")){
            Events.on(BuildSelectEvent.class , event ->{
                if(!event.breaking && event.builder != null && event.builder.buildPlan() != null && event.builder.buildPlan().block == Blocks.thoriumReactor && event.builder.isPlayer()){
                    //player is the unit controller
                    Player player = event.builder.getPlayer();
                    //send a message to everyone saying that this player has begun building a reactor
                    jda.getTextChannelById("740998890312171560").sendTyping().queue();
                    jda.getTextChannelById("740998890312171560").sendMessage(new net.dv8tion.jda.api.EmbedBuilder().setTitle("Alert!").setDescription(player.name + " has begun building a reactor at " + event.tile.x + ", " + event.tile.y).setColor(0xFF3333).build()).queue();
                }
            });
            Events.on(PlayerJoin.class,event->{
              jda.getTextChannelById("740998890312171560").sendMessage(new net.dv8tion.jda.api.EmbedBuilder().setTitle("Join event").setDescription(event.player.name+"Joined the server").build()).queue();
            });

            Events.on(EventType.PlayerChatEvent.class, event -> {
              jda.getTextChannelById("740998890312171560").sendTyping().queue();
              jda.getTextChannelById("740998890312171560").sendMessage("**"+event.player.name.replace("*","+")+"**: "+event.message).queue();
            });
            Events.on(ServerLoadEvent.class,event -> {
              jda.getTextChannelById("740998890312171560").sendTyping().queue();
              jda.getTextChannelById("740998890312171560").sendMessage(new net.dv8tion.jda.api.EmbedBuilder().setTitle("Server loaded!").setDescription("It will be hosted shortly").setColor(0x33FFEC).build()).queue();
              jda.getPresence().setActivity(Activity.listening("Server rumbling itself to death"));

            });
        }
    }
}
