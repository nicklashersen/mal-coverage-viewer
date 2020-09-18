package mal.coverage.viewer.model.coverage;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

/**
 * Window for displaying coverage data.
 */
public class CoverageData implements Iterable<CoverageData.Entry> {
	private Map<String, Entry> coverageResults = new HashMap<>();
	public Map<Integer, Double> compromised;
	public String selectionName;

	/**
	 * Add a new coverage mapping name -> result.
	 *
	 * @param name        coverage model name
	 * @param nMax        maximum number of compromised states
	 * @param compromised states
	 */
	public void add(String name, int nMax, int nCompromised) {
		coverageResults.put(name, new Entry(name, nMax, nCompromised));
	}

	/**
	 * Add a new coverage mapping name -> result.
	 *
	 * @param name        coverage model name
	 * @param nMax        maximum number of compromised states
	 * @param compromised states
	 */
	public void add(String name, BigInteger nMax, BigInteger nCompromised) {
		coverageResults.put(name, new Entry(name, nMax, nCompromised));
	}

	/**
	 * Returns coverage data associated with the specified name. If no such mapping
	 * has been registered an empty Optional is returned.
	 *
	 * @return Optional containing the result.
	 */
	public Optional<Entry> get(String name) {
		return Optional.ofNullable(coverageResults.get(name));
	}

	/**
	 * Returns an iterator over all registered coverage data.
	 *
	 * @return An iterator over all registered data
	 */
	@Override
	public Iterator<Entry> iterator() {
		return coverageResults.values().iterator();
	}

	public static class Entry {
		public String name;
		public BigInteger nMax;
		public BigInteger nCompromised;

		public Entry(String name, int nMax, int nCompromised) {
			this(name, BigInteger.valueOf(nMax), BigInteger.valueOf(nCompromised));
		}

		public Entry(String name, BigInteger nMax, BigInteger nCompromised) {
			this.name = name;
			this.nMax = nMax;
			this.nCompromised = nCompromised;
		}
	}
}
