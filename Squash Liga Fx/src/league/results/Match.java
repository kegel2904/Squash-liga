package league.results;

import java.util.Objects;

public class Match {

	private final Player first;
	private final Player second;

	private final Result result;

	Match(Player first, Player second, Result result) {
		if (first == second)
			throw new IllegalArgumentException(first.name());

		this.first = Objects.requireNonNull(first);
		this.second = Objects.requireNonNull(second);
		this.result = Objects.requireNonNull(result);
	}

	public Player first() {
		return first;
	}

	public Player second() {
		return second;
	}

	public Result result() {
		return result;
	}

	@Override
	public String toString() {
		return first + " - " + second + " " + result;
	}

}
