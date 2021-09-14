package nettoplugin.Command;
import net.dv8tion.*;
import mindustry.*;
import mindustry.gen.Call;
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
          String[] msg = event.getMessage().getContentRaw().split(" ",2);
          Call.announce(msg[1].trim());
          EmbedBuilder eb = new EmbedBuilder();
          eb.setTitle("Announced!").setDescription("You("+event.getAuthor().getName()+") announced: "+ msg[1].trim()).setColor(0x33FFEC);
          event.getChannel().sendTyping().queue();
          event.getChannel().sendMessage(eb.build()).queue();
          return;
        }
        else{
          EmbedBuilder nopermembed = new EmbedBuilder();
          nopermembed.setTitle("No permission!").setDescription("ONly NETtoTOWN managers can excute this command!").setColor(0xFF3333);
          event.getChannel().sendTyping().queue();
          event.getChannel().sendMessage(nopermembed.build()).queue();
        }
      }
    }
    if(args[0].equalsIgnoreCase("..list")){
        if(state.isMenu()){
          EmbedBuilder eb = new EmbedBuilder();
          eb.setTitle("The server is down!").setDescription("The server needs to be running before you can use this command").setColor(0xFF3333);
          event.getChannel().sendTyping().queue();
          event.getChannel().sendMessage(eb.build()).queue();
          return;
        }
      if(args.length < 2){
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Enter block name");
        eb.setDescription("Usage: ..list <block>");
        eb.setColor(0xFF3333);
        event.getChannel().sendTyping().queue();
        event.getChannel().sendMessage(eb.build()).queue();
        return;
      }
      event.getChannel().sendTyping().queue();
      event.getChannel().sendMessage("blame net this is not working").queue();
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
                  EmbedBuilder eb = new EmbedBuilder();
                  eb.setTitle("The server is down!").setDescription("The server needs to be running before you can use this command").setColor(0xFF3333);
                  event.getChannel().sendTyping().queue();
                  event.getChannel().sendMessage(eb.build()).queue();
                  return;
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
  }
}
