package nettoplugin.Command;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
public class serverControls extends ListenerAdapter{
  public void onGuildMessageReceived(GuildMessageReceivedEvent event){
    String[] args = event.getMessage().getContentRaw().split(" ");
    if(args[0].equalsIgnoreCase("..announce")){
      if(args.length < 2){
        return;
      }
    }
  }
}
