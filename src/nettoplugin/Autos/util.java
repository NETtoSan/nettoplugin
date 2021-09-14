package nettoplugin.Autos;
import mindustry.maps.Map;
import mindustry.Vars;
public class util{
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
}
