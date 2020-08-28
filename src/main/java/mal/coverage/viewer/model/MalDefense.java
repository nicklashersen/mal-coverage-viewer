package mal.coverage.viewer.model;

import java.util.Set;

public class MalDefense extends MalAbstractStep {
	public MalDefense(String name, int assetHash, int hash, Set<Integer> parents) {
		super(name, assetHash, hash, parents);
	}
}
