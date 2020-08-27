package mal.coverage.viewer.model;

import java.util.Set;

public class MalDefense {
	public final String name;
	public final int assetHash;
	public final int hash;
	public final Set<Integer> parents;

	public MalDefense(String name, int assetHash, int hash, Set<Integer> parents) {
		this.name = name;
		this.hash = hash;
		this.assetHash = assetHash;
		this.parents = parents;
	}
}
