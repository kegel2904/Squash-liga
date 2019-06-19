package league.results;

public class Result {

	private final int first;
	private final int second;

	private static void check(int games) {
		if (games < 0)
			throw new IllegalArgumentException(String.valueOf(games));
	}

	Result(int first, int second) {
		check(first);
		check(second);

		if (first == second)
			throw new IllegalArgumentException(String.valueOf(first));

		this.first = first;
		this.second = second;

	}

	public int first() {
		return first;
	}

	public int second() {
		return second;
	}

	public Result reverse() {
		return new Result(second, first);
	}

	@Override
	public String toString() {
		return first + ":" + second;
	}

}
