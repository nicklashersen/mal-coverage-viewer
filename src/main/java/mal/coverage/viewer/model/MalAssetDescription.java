package mal.coverage.viewer.model;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class MalAssetDescription {
    public final String className;
    public final String[] attackSteps;
    public final String[] defense;

    public MalAssetDescription(String className, String[] attack, String[] defense) {
        this.className = className;
        this.attackSteps = attack;
        this.defense = defense;
    }

    public static MalAssetDescription fromJSON(JSONObject obj) {
        String className = obj.getString("className");
        JSONArray attackSteps = obj.getJSONArray("attack");

        String[] attack = new String[attackSteps.length()];
        for (int i = 0; i < attackSteps.length(); i++) {
            attack[i] = attackSteps.getString(i);
        }


        JSONArray defenseJSON = obj.getJSONArray("defnese");
        String[] defense = new String[defenseJSON.length()];
        for (int i = 0; i < defenseJSON.length(); i++) {
            defense[i] = defenseJSON.getString(i);
        }

        return new MalAssetDescription(className, attack, defense);
    }

    /**
     * Return a map of class names and descriptions.
     * 
     * @param objs JSON object to construct the map from
     */
    public static Map<String, MalAssetDescription> fromJSON(JSONObject[] objs) {
        HashMap<String, MalAssetDescription> res = new HashMap<>(objs.length);

        for (JSONObject obj : objs) {
            MalAssetDescription desc = fromJSON(obj);
            res.put(desc.className, desc);
        }
        
        return res;
    }
}