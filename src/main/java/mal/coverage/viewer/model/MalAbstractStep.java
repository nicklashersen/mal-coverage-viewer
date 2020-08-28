package mal.coverage.viewer.model;

import java.util.Set;

public abstract class MalAbstractStep {
	public final String name;
	public final int assetHash;
	public final int hash;
	public final Set<Integer> parents;

	public MalAbstractStep(String name, int assetHash, int hash, Set<Integer> parents) {
		this.parents = parents;
		this.name = name;
		this.hash = hash;
		this.assetHash = assetHash;
	}
}
