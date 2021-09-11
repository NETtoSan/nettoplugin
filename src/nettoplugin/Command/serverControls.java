package nettoplugin.Command;
import net.dv8tion.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
public class serverControls extends ListenerAdapter{
  public void onGuildMessageReceived(GuildMessageReceivedEvent event){
    String[] args = event.getMessage().getContentRaw().split(" ");
    if(args[0].equalsIgnoreCase("..announce")){
      if(args.length < 2){
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Message required").setDescription("Usage: ..announce <context>").setColor(0xFF3333);
        event.getChannel().sendTyping().queue();
        event.getChannel().sendMessage(eb.build()).queue();
      }
    }
  }
}
