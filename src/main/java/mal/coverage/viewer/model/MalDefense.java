package mal.coverage.viewer.model;

public class MalDefense {
	public final String name;
	public final int assetHash;
	public final int hash;

	public MalDefense(String name, int assetHash, int hash) {
		this.name = name;
		this.hash = hash;
		this.assetHash = assetHash;
	}
}
