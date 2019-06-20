package league.results;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import league.util.Util;

public class History {

	public static final History empty = new History(Collections.emptyList(), Collections.emptyList());

	private final List<Player> players;
	private final List<League> leagues;

	History(Collection<Player> players, Collection<League> leagues) {
		for (League league : leagues)
			if (!players.containsAll(league.players()))
				throw new IllegalArgumentException();
		this.players = Util.protect(players);
		this.leagues = Util.protect(leagues);
	}

	public List<Player> players() {
		return players;
	}

	public List<League> leagues() {
		return leagues;
	}

}
