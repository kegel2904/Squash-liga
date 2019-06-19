package league.results;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import league.results.PointCriteria.Item;
import league.util.Util;

public class Rankings {

	private List<Slot> slots;

	private static class Counter {

		private final Player player;

		private int points;
		private int matchesWon;
		private int matchesLost;
		private int gamesWon;
		private int gamesLost;

		Counter(Player player) {
			this.player = player;
		}

		void add(int value) {
			points += value;
		}

		void won() {
			matchesWon++;
		}

		void lost() {
			matchesLost++;
		}

		void won(int games) {
			gamesWon += games;
		}

		void lost(int games) {
			gamesLost += games;
		}

		Score score() {
			return new Score(player, matchesWon, matchesLost, gamesWon, gamesLost, points);
		}

	}

	public static Rankings from(League league, PointCriteria criteria) {
		Map<Player, Counter> byPlayer = new HashMap<>();

		for (Player player : league.players())
			byPlayer.put(player, new Counter(player));

		int matchPlayed = criteria.points(Item.MATCH_PLAYED);
		int gameWon = criteria.points(Item.GAME_WON);
		int matchWon = criteria.points(Item.MATCH_WON);

		for (Match match : league.matches()) {
			Result result = match.result();

			Counter first = byPlayer.get(match.first());
			Counter second = byPlayer.get(match.second());

			first.add(matchPlayed);
			first.add(result.first() * gameWon);

			second.add(matchPlayed);
			second.add(result.second() * gameWon);

			boolean firstWon = result.first() > result.second();
			Counter winner = firstWon ? first : second;
			Counter loser = firstWon ? second : first;
			winner.add(matchWon);

			winner.won();
			loser.lost();

			first.won(result.first());
			second.lost(result.first());

			first.lost(result.second());
			second.won(result.second());

		}

		List<Score> scores = Util.map(byPlayer.values(), Counter::score);
		Map<Integer, List<Score>> byPoints = Util.groupBy(scores, Score::points);
		Map<Integer, List<Score>> sorted = new TreeMap<>(Comparator.reverseOrder());
		sorted.putAll(byPoints);

		List<Slot> slots = new ArrayList<>();

		int nextPosition = 1;
		for (List<Score> batch : sorted.values()) {
			Slot slot = new Slot(nextPosition, batch);
			slots.add(slot);

			nextPosition += batch.size();
		}

		return new Rankings(slots);

	}

	private Rankings(List<Slot> slots) {
		this.slots = Util.protect(slots);
	}

	public List<Slot> slots() {
		return slots;
	}

	public static class Slot {
		private final int position;
		private final List<Score> scores;

		Slot(int position, List<Score> scores) {
			this.position = position;
			this.scores = Util.protect(scores);
		}

		public int position() {
			return position;
		}

		public List<Score> scores() {
			return scores;
		}
	}

	public static class Score {
		private final Player player;
		private final int matchesWon;
		private final int matchesLost;
		private final int gamesWon;
		private final int gamesLost;
		private final int points;

		private Score(Player player, int matchesWon, int matchesLost, int gamesWon, int gamesLost, int points) {
			this.player = player;
			this.matchesWon = matchesWon;
			this.matchesLost = matchesLost;
			this.gamesWon = gamesWon;
			this.gamesLost = gamesLost;
			this.points = points;
		}

		public Player player() {
			return player;
		}

		public int matchesWon() {
			return matchesWon;
		}

		public int matchesLost() {
			return matchesLost;
		}

		public int gamesWon() {
			return gamesWon;
		}

		public int gamesLost() {
			return gamesLost;
		}

		public int points() {
			return points;
		}

	}
}
