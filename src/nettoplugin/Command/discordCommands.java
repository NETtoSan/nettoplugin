package nettoplugin.Command;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class discordCommands extends ListenerAdapter {
  public void onGuildMessageReceived (GuildMessageReceivedEvent event){
        String[] args = event.getMessage().getContentRaw().split(" ");
        if(args[0].equalsIgnoreCase("..")){
            return;
        }
        if(args[0].equalsIgnoreCase("..succ")){
          event.getChannel().sendTyping().queue();
          event.getChannel().sendMessage("no u").queue();

          return;
        }
    }
}
