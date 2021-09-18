package nettoplugin.Autos;
import mindustry.maps.Map;
import mindustry.Vars;
import mindustry.core.GameState.*;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import nettoplugin.mindustryCommand;
import org.json.JSONObject;

public class util{
  private static JSONObject dOb = mindustryCommand.alldata.getJSONObject("discord");
  public static Map findMap(String query){
    Map map = null;
    try{
      map = Vars.maps.customMaps().get(Integer.parseInt(query));
    }
    catch(Exception e){
      for(Map m: Vars.maps.customMaps()){
        if(m.name().replaceAll(" ", "").toLowerCase().contains(query.toLowerCase().replaceAll(" ", ""))){
        map = m;
        break;
        }
      }
    }
    return map;
  }
  public static Role checkUser(GuildMessageReceivedEvent event){
    String roleID = dOb.getString("gameOver_role_id");
    Role role = null;
    Role validity = event.getGuild().getRoleById(roleID);
    if(validity != null){
      int identity = event.getMember().getRoles().indexOf(validity);
      if(identity <= 0){
        role = null;
      }
      else{
        Role addiperm = event.getMember().getRoles().get(identity);
        if(addiperm != null){
          role = addiperm;
        }
        else{
          role = null;
        }
      }
    }

    return role;
  }
}
