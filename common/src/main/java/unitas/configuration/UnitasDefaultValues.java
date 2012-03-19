package unitas.configuration;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.configuration.Configuration;

/**
 *
 * @author tram
 */
public class UnitasDefaultValues {
    
     private static Configuration config = UnitasConfiguration.getConfiguration();
     
     public static List<String> getStorageElements() {
        
        List<Object> list = config.getList("unitas.egi.string.list.se");
        List<String> strings = new ArrayList<String>();
        for (Object item : list){
            strings.add((String) item);
        }
        
        return strings;
    }
}
