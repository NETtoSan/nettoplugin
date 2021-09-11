package nettoplugin.Command;
import net.dv8tion.*;
import mindustry.*;
import mindustry.core.GameState.*;
import mindustry.game.EventType.*;
import nettoplugin.mindustryCommand;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.json.JSONObject;
import static mindustry.Vars.state;
public class serverControls extends ListenerAdapter{
  private JSONObject dOb = mindustryCommand.alldata.getJSONObject("discord");;
  public Role crap;
  public void onGuildMessageReceived(GuildMessageReceivedEvent event){
    String[] args = event.getMessage().getContentRaw().split(" ");
    String roleID = dOb.getString("gameOver_role_id");
    if(args[0].equalsIgnoreCase("..announce")){
      if(state.isMenu()){
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("The server is down!").setDescription("The server needs to be running before you can use this command").setColor(0xFF3333);
        event.getChannel().sendTyping().queue();
        event.getChannel().sendMessage(eb.build()).queue();
        return;
      }
      Role validity = event.getGuild().getRoleById(roleID);
      if(validity != null){
        int identity = event.getMember().getRoles().indexOf(validity);
        if(identity <= 0 ){
          EmbedBuilder nopermembed = new EmbedBuilder();
          nopermembed.setTitle("No permission!").setDescription("ONly NETtoTOWN managers can excute this command!").setColor(0xFF3333);
          event.getChannel().sendTyping().queue();
          event.getChannel().sendMessage(nopermembed.build()).queue();
          return;
        }
        Role addiPerm = event.getMember().getRoles().get(identity);
        if(addiPerm != null){
          if(args.length < 2){
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Message required").setDescription("Usage: ..announce <context>").setColor(0xFF3333);
            event.getChannel().sendTyping().queue();
            event.getChannel().sendMessage(eb.build()).queue();
            return;
          }
        }
        else{
          EmbedBuilder nopermembed = new EmbedBuilder();
          nopermembed.setTitle("No permission!").setDescription("ONly NETtoTOWN managers can excute this command!").setColor(0xFF3333);
          event.getChannel().sendTyping().queue();
          event.getChannel().sendMessage(nopermembed.build()).queue();
        }
      }
    }
  }
}
