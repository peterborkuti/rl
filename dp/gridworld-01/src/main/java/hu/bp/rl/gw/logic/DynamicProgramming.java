package hu.bp.rl.gw.logic;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * v: values
 * r: rewards: every move: -1, goal: State=0 or State=maxState-1
 * a: actions: up/down/left/right
 * pi(a|s): equal probabilities (every action is 25% prob
 */

public class DynamicProgramming {
	private final int maxState;
	private final int dimension;
	private final BigDecimal gamma;
	private final List<BigDecimal> stateValues;
	private final List<String> policy;

	private static final Direction[] DIRECTIONS = {
			new Direction("U", -1, 0),
			new Direction("D", +1, 0),
			new Direction("L", 0, -1),
			new Direction("R", 0, +1)
	};

	public DynamicProgramming(int dimension, BigDecimal gamma) {
		this.dimension = dimension;
		this.maxState = dimension * dimension; // word is an nxn rectangle
		this.gamma = gamma;
		Random r = new Random();
		stateValues = IntStream.range(0, maxState).mapToDouble(i -> r.nextDouble()).boxed().map(BigDecimal::new).collect(Collectors.toList());
		stateValues.set(0, BigDecimal.ZERO);
		stateValues.set(maxState-1, BigDecimal.ZERO);
		policy = getPolicy();
	}

	public List<String> getPolicy() {
		return
			IntStream.range(1, maxState - 1).parallel().mapToObj(s -> {
				return getDirectionStream()
					.map(direction -> getDirectionValue(direction, s))
				.max(Comparator.comparing(DirectionValue::getValue))
				.orElse(new DirectionValue("X", BigDecimal.ZERO)).sign;
			})
			.collect(Collectors.toList());
	}

	public static Stream<Direction> getDirectionStream() {
		return Arrays.stream(DIRECTIONS).parallel();
	}

	public DirectionValue getDirectionValue(Direction direction, Integer centerState) {
		int state = getLinearNeighbour(centerState, direction);
		return new DirectionValue(direction.sign, (centerState == state) ? new BigDecimal(-1000) : stateValues.get(state));
	}

	public BigDecimal improvePolicy() {
		BigDecimal max = BigDecimal.ZERO;
		for (int s = 1; s < maxState - 1; s++) {
			BigDecimal v = stateValues.get(s);
			List<Integer> sPrime = getNeighbours(s);

			BigDecimal vPrime = sPrime.parallelStream()
					.map(statePrime ->
							(new BigDecimal(0.25)).multiply(getReward(statePrime).add(gamma.multiply(stateValues.get(statePrime)))))
					.reduce(BigDecimal.ZERO, BigDecimal::add);
			BigDecimal delta = vPrime.add(v.negate()).abs();

			if (max.compareTo(delta) == -1) max = delta;

			stateValues.set(s, vPrime);
		}

		return max;
	}

	public String toString() {
		List<String> policy = new ArrayList<>();
		policy.add(" ");
		policy.addAll(getPolicy());
		policy.add(" ");
		List<String> policyCells = new ArrayList<>();
		List<String> valueCells = new ArrayList<>();
		for(int i = 0; i < maxState; i++) {
			policyCells.add(policy.get(i) + getLineEnding(i));
			valueCells.add(String.format("%04.2f", stateValues.get(i)) + getLineEnding(i));
		}
		return getTable(policyCells) + "\n\n" + getTable(valueCells);
	}

	private String getTable(List<String> cells) {
		return " | " + cells.stream().collect(Collectors.joining(" | "));
	}

	private String getLineEnding(int i) {
		return ((i+1) % dimension == 0) ? "\n" : "";
	}

	private BigDecimal getReward(int state) {
		return (state == 0 || state == maxState - 1) ? BigDecimal.ZERO : new BigDecimal(-1);
	}

	private List<Integer> getNeighbours(int s) {
		return getDirectionStream().map(d -> getLinearNeighbour(s, d)).collect(Collectors.toList());
	}

	private Integer getLinearNeighbour(final int s, final Direction dir) {
		int r = s / dimension;
		int c = s - r * dimension;

		int newR = r + dir.dr;
		int newC = c + dir.dc;

		if (newR >= 0 && newR < dimension && newC >= 0 && newC < dimension) {
			return newR * dimension + newC;
		}

		return s;
	}
}
