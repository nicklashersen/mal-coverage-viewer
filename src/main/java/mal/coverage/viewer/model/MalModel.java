package mal.coverage.viewer.model;

import java.util.Map;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class MalModel {
    public final Map<String, MalAssetDescription> classDescriptions;
    public final List<MalAsset> assets;

    public MalModel(Map<String,MalAssetDescription> descs,
                    List<MalAsset> assets) {
        classDescriptions = descs;
        this.assets = assets;
    }

    /**
     * Constructs a model from a MAL threat model JSON instance.
     * 
     * @param obj root json object node
     * @return A MalModel 
     */
    public static MalModel fromJSON(JSONObject obj) {
        // Mal class descriptions
        JSONArray jsonAssetClasses = obj.getJSONArray("assets");
        JSONObject[] assetClasses = new JSONObject[jsonAssetClasses.length()];

        for (int i = 0; i < jsonAssetClasses.length(); i++) {
            assetClasses[i] = jsonAssetClasses.getJSONObject(i);
        }

        Map<String, MalAssetDescription> classDescs = MalAssetDescription.fromJSON(assetClasses);

        // Mal object instances
        JSONArray jsonAssetInstances = obj.getJSONArray("model");
        List<MalAsset> assets = new ArrayList<>(jsonAssetInstances.length());

        for (int i = 0; i< jsonAssetInstances.length(); i++) {
            JSONObject object = jsonAssetInstances.getJSONObject(i);

            assets.add(MalAsset.fromJSON(object, classDescs));
        }

        return new MalModel(classDescs, assets);
    }
}