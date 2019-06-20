package league.results;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import league.util.Util;

public class MatchTable {

	public static MatchTable from(League league) {
		Map<Player, Map<Player, Result>> results = new IdentityHashMap<>();

		for (Player player : league.players())
			results.put(player, new IdentityHashMap<>());

		for (Match match : league.matches()) {
			Result result = match.result();

			Player first = match.first();
			Player second = match.second();
			Map<Player, Result> map = results.get(first);
			if (map.containsKey(second))
				throw new IllegalArgumentException();
			map.put(second, result);
			results.get(second).put(first, result.reverse());
		}

		Map<Player, MatchRow> rows = new IdentityHashMap<>();

		for (Player player : league.players()) {
			Map<Player, Result> map = results.get(player);
			MatchRow row = new MatchRow(player, map);
			rows.put(player, row);
		}

		return new MatchTable(league.players(), rows);
	}

	private final List<Player> players;
	private final Map<Player, MatchRow> results;

	private MatchTable(List<Player> players, Map<Player, MatchRow> results) {
		this.players = Util.protect(players);
		this.results = results;
	}

	public List<Player> players() {
		return players;
	}

	public List<MatchRow> rows() {
		return Util.map(players, results::get);
	}

	public Result result(Player first, Player second) {
		MatchRow row = results.get(first);
		if (row == null)
			throw new IllegalArgumentException(String.valueOf(first));
		return row.result(second);
	}

	public static class MatchRow {
		private final Player player;
		private final Map<Player, Result> results;

		private MatchRow(Player player, Map<Player, Result> results) {
			this.player = player;
			this.results = results;
		}

		public Player player() {
			return player;
		}

		public Result result(Player other) {
			return results.get(other);
		}

	}

}
