package mal.coverage.viewer.model;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class MalModel {
    // MalAsset id -> asset
    public final Map<Integer, MalAsset> assets;
    public final String name;

    public MalModel(String name,
		    Map<Integer, MalAsset> assets) {

	this.assets = assets;
	this.name = name;
    }

    /**
     * Constructs a model from a MAL threat model JSON instance.
     * 
     * @param obj root json object node
     * @return A MalModel 
     */
    public static MalModel fromJSON(JSONObject obj) {
	// Mal class descriptions
	String name = obj.getString("testName");
	JSONArray jsonModel = obj.getJSONArray("model");

	Map<Integer, MalAsset> malAssets = new HashMap<>(jsonModel.length());

	for (int i = 0; i < jsonModel.length(); i++) {
	    JSONObject jsonMalAsset = jsonModel.getJSONObject(i);
	    MalAsset asset = MalAsset.fromJSON(jsonMalAsset);

	    malAssets.put(asset.hash, asset);
	}

	return new MalModel(name, malAssets);
    }
}
