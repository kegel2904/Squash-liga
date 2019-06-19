package league.results;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class League {

	private final String name;
	private final List<Player> players;
	private final List<Match> matches;

	League(String name) {
		this.name = Objects.requireNonNull(name);
		this.players = new ArrayList<>();
		this.matches = new ArrayList<>();
	}

	public String name() {
		return name;
	}

	public List<Player> players() {
		return new ArrayList<>(players);
	}

	public List<Match> matches() {
		return new ArrayList<>(matches);
	}

	void addMatch(Match match) {
		checkPlayer(match.first());
		checkPlayer(match.second());
		matches.add(match);
	}

	void addPlayer(Player player) {
		if (!players.contains(player))
			players.add(player);
	}

	private void checkPlayer(Player player) {
		if (!players.contains(player))
			throw new IllegalArgumentException(player.name());
	}

	@Override
	public String toString() {
		return name;
	}

}
