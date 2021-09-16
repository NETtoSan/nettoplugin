package nettoplugin.Autos;
import mindustry.maps.Map;
import mindustry.Vars;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import nettoplugin.mindustryCommand;
import org.json.JSONObject;

public class util{
  private JSONObject dOb = mindustryCommand.alldata.getJSONObject("discord");;
  private String roleID = dOb.getString("gameOver_role_id");
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
    //I will complete this later.
    Role role = null;
    return role;
  }
}
