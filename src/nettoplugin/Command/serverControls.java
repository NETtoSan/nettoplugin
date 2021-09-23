package nettoplugin.Command;
import arc.Events;
import arc.Core;
import arc.files.Fi;
import arc.util.Timer.*;
import mindustry.gen.Call;
import mindustry.game.Team;
import mindustry.game.*;
import mindustry.io.*;
import mindustry.maps.*;
import mindustry.core.GameState.*;
import mindustry.game.EventType.*;
import nettoplugin.mindustryCommand;
import nettoplugin.Autos.util;

//Copied From Anuken
import mindustry.maps.Map;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.io.File;
import org.json.JSONObject;
import static mindustry.Vars.state;
import static mindustry.Vars.*;


public class serverControls extends ListenerAdapter{
  private Task lastTask;
  private Gamemode lastMode;
  private JSONObject dOb = mindustryCommand.alldata.getJSONObject("discord");;
  private String roleID = dOb.getString("gameOver_role_id");
  public Role crap;
  public void onGuildMessageReceived(GuildMessageReceivedEvent event){
    String[] args = event.getMessage().getContentRaw().split(" ");
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
      if(state.isMenu()){
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("The server is down!").setDescription("The server needs to be running before you can use this command").setColor(0xFF3333);
        event.getChannel().sendTyping().queue();
        event.getChannel().sendMessage(eb.build()).queue();
        return;
      }
      //Test function in Autos
      Role permission = util.checkUser(event);
      if(permission != null){
        try{
          event.getChannel().sendTyping().queue();
          event.getChannel().sendMessage("Executed!").queue();
          Events.fire(new GameOverEvent(Team.crux));

        }
        catch(Exception e){
          event.getChannel().sendTyping().queue();
          event.getChannel().sendMessage("There was an error INSIDE a gameover event!").queue();
        }
      }
      else{
        EmbedBuilder nopermembed = new EmbedBuilder();
        nopermembed.setTitle("No permission!").setDescription("ONly NETtoTOWN managers can excute this command!").setColor(0xFF3333);
        event.getChannel().sendTyping().queue();
        event.getChannel().sendMessage(nopermembed.build()).queue();
      }
      return;
    }
    if(args[0].equalsIgnoreCase("..stop")){
      if(state.isMenu()){
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("The server is down!").setDescription("The server needs to be running before you can use this command").setColor(0xFF3333);
        event.getChannel().sendTyping().queue();
        event.getChannel().sendMessage(eb.build()).queue();
        return;
      }
      Role permission = util.checkUser(event);
      if(permission != null){
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Stopped hosting!").setDescription("The server have stopped").setColor(0xFF3333);
        event.getChannel().sendTyping().queue();
        event.getChannel().sendMessage(eb.build()).queue();
        net.closeServer();
        if(lastTask != null) lastTask.cancel();
        state.set(State.menu);
        return;
      }
      else{
        EmbedBuilder nopermembed = new EmbedBuilder();
        nopermembed.setTitle("No permission!").setDescription("ONly NETtoTOWN managers can excute this command!").setColor(0xFF3333);
        event.getChannel().sendTyping().queue();
        event.getChannel().sendMessage(nopermembed.build()).queue();
      }
      return;
    }
    if(args[0].equalsIgnoreCase("..host")){
      if(state.is(State.playing)){
        EmbedBuilder eb = new EmbedBuilder().setTitle("Game is hosting!").setDescription("Use `..stop` to stop hosting").setColor(0xFF3333);
        event.getChannel().sendTyping().queue();
        event.getChannel().sendMessage(eb.build()).queue();
        return;
      }
      Role permission = util.checkUser(event);
      if(permission != null){
        if(lastTask != null) lastTask.cancel();
          Gamemode preset = Gamemode.survival;
          if(args.length > 1){
            try{
              preset = Gamemode.valueOf(args[1]);
            }
            catch(IllegalArgumentException e){
              return;
            }
          }

          Map result = maps.getShuffleMode().next(preset,state.map);
          logic.reset();
          lastMode = preset;
          Core.settings.put("lastServerMode",lastMode.name());
          try{
            world.loadMap(result,result.applyRules(lastMode));
            state.rules = result.applyRules(preset);
            logic.play();
            netServer.openServer();
            EmbedBuilder eb = new EmbedBuilder().setTitle("Hosted!").setDescription("Hosted map to be "+result.name()).setColor(0x33FFEC);
            event.getChannel().sendTyping().queue();
            event.getChannel().sendMessage(eb.build()).queue();
          }
          catch(MapException e){
            return;
          }
      }
      else{
        EmbedBuilder nopermembed = new EmbedBuilder();
        nopermembed.setTitle("No permission!").setDescription("ONly NETtoTOWN managers can excute this command!").setColor(0xFF3333);
        event.getChannel().sendTyping().queue();
        event.getChannel().sendMessage(nopermembed.build()).queue();
      }
      return;
    }
    if(args[0].equalsIgnoreCase("..rename")){
      if(state.isMenu()){
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("The server is down!").setDescription("The server needs to be running before you can use this command").setColor(0xFF3333);
        event.getChannel().sendTyping().queue();
        event.getChannel().sendMessage(eb.build()).queue();
        return;
      }
      util.checkUser(event);
    }
    if(args[0].equalsIgnoreCase("..admin")){
      if(state.isMenu()){
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("The server is down!").setDescription("The server needs to be running before you can use this command").setColor(0xFF3333);
        event.getChannel().sendTyping().queue();
        event.getChannel().sendMessage(eb.build()).queue();
        return;
      }
      Role permission = util.checkUser(event);
      if(permission != null){

      }
      else{
        EmbedBuilder nopermembed = new EmbedBuilder();
        nopermembed.setTitle("No permission!").setDescription("ONly NETtoTOWN managers can excute this command!").setColor(0xFF3333);
        event.getChannel().sendTyping().queue();
        event.getChannel().sendMessage(nopermembed.build()).queue();
      }
      return;
    }
    if(args[0].equalsIgnoreCase("..loadsave")){
      if(state.is(State.playing)){
        EmbedBuilder eb = new EmbedBuilder().setTitle("Game is hosting!").setDescription("Use `..stop` to stop hosting").setColor(0xFF3333);
        event.getChannel().sendTyping().queue();
        event.getChannel().sendMessage(eb.build()).queue();
        return;
      }
      Role permission = util.checkUser(event);
      if(permission != null){
        if(args.length < 2){
          EmbedBuilder eb = new EmbedBuilder();
          eb.setTitle("Enter savename");
          eb.setDescription("Usage: ..loadsave <savename>");
          eb.setColor(0xFF3333);
          event.getChannel().sendTyping().queue();
          event.getChannel().sendMessage(eb.build()).queue();
          return;
        }
        String[] msg = event.getMessage().getContentRaw().split(" ",2);
        Fi file = saveDirectory.child(msg[1].trim()+".msav");
        if(!SaveIO.isSaveValid(file)){
          EmbedBuilder eb = new EmbedBuilder().setTitle("No valid save data found!").setDescription("Try again").setColor(0xFF3333);
          event.getChannel().sendTyping().queue();
          event.getChannel().sendMessage(eb.build()).queue();
          return;
        }

        Core.app.post(() -> {
          try{
            SaveIO.load(file);
            state.rules.sector = null;
            EmbedBuilder eb = new EmbedBuilder().setTitle("Host!").setDescription("Loaded save `@`".replace("@",msg[1].trim())).setColor(0x33FFEC);
            event.getChannel().sendTyping().queue();
            event.getChannel().sendMessage(eb.build()).queue();
            state.set(State.playing);
            netServer.openServer();
          }
          catch(Throwable t){
            EmbedBuilder eb = new EmbedBuilder().setTitle("Failed to load save").setDescription("Outdated or corrupt file").setColor(0xFF3333);
            event.getChannel().sendTyping().queue();
            event.getChannel().sendMessage(eb.build()).queue();
          }
        });
      }
      else{
        EmbedBuilder nopermembed = new EmbedBuilder();
        nopermembed.setTitle("No permission!").setDescription("ONly NETtoTOWN managers can excute this command!").setColor(0xFF3333);
        event.getChannel().sendTyping().queue();
        event.getChannel().sendMessage(nopermembed.build()).queue();
      }
      return;
    }
  }
}
