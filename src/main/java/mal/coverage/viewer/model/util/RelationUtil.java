package mal.coverage.viewer.model.util;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import mal.coverage.viewer.model.MalAbstractStep;
import mal.coverage.viewer.model.MalModel;

public class RelationUtil {
	public static Set<Integer> getChildrenOf(MalAbstractStep step, MalModel mdl) {
		if (step.children.isEmpty()) {
			step.children = Stream.concat(mdl.attackSteps.values().stream(), mdl.defenses.values().stream())
				.filter(s -> s.parents.contains(step.hash))
				.map(s -> s.hash)
				.collect(Collectors.toSet());
		}

		return step.children;
	}
}
