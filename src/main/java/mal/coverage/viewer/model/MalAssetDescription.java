package mal.coverage.viewer.model;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class MalAssetDescription {
    public final String className;
    public final MalAttackStep[] attackSteps;
    public final String[] defense;

    public MalAssetDescription(String className, MalAttackStep[] attack, String[] defense) {
        this.className = className;
        this.attackSteps = attack;
        this.defense = defense;
    }

    public static MalAssetDescription fromJSON(JSONObject obj) {
        String className = obj.getString("className");
        JSONArray attackSteps = obj.getJSONArray("attack");

        MalAttackStep[] attack = new MalAttackStep[attackSteps.length()];
        for (int i = 0; i < attackSteps.length(); i++) {
            JSONObject jsonAttackStep = attackSteps.getJSONObject(i);

            attack[i] = new MalAttackStep(jsonAttackStep.getString("name"), jsonAttackStep.getString("type"));
        }

        JSONArray defenseJSON = obj.getJSONArray("defense");
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
