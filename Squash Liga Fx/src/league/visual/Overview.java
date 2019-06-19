package league.visual;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.HBox;
import league.results.History;
import league.results.League;
import league.results.Player;
import league.results.PointCriteria;
import league.results.Rankings;
import league.results.Rankings.Score;
import league.results.Rankings.Slot;
import league.visual.util.CustomListView;
import league.visual.util.CustomTabPane;
import league.visual.util.CustomTableView;
import league.visual.util.Stretch;

public class Overview {

	private final Node view;

	Overview(History history, PointCriteria criteria) {
		CustomTabPane tabs = new CustomTabPane();
		CustomListView<Player> players = CustomListView.newInstance(history.players());
		players.display(x -> x.name() + " " + x.lastName());
		tabs.tab("Players", players);
		LeaguesView leagues = new LeaguesView(history.leagues(), criteria);
		tabs.tab("Leagues", leagues.view);
		this.view = tabs;
	}

	Node view() {
		return view;
	}

	private static class LeaguesView {
		private final Node view;

		LeaguesView(List<League> leagues, PointCriteria criteria) {
			CustomListView<League> list = CustomListView.newInstance(leagues);
			list.display(League::name);

			HBox inner = new HBox();
			SplitPane pane = new SplitPane(list, inner);
			pane.setDividerPositions(0.2);

			list.selectedItem().addListener((x, o, n) -> {
				if (n == null)
					inner.getChildren().clear();
				else {
					LeagueView view = new LeagueView(n, criteria);
					inner.getChildren().setAll(view.view);
					Stretch.horizontal(view.view);
				}
			});

			this.view = pane;
		}

	}

	private static class LeagueView {
		private final Node view;

		private static String playerName(Player player) {
			return player.name() + " " + player.lastName();
		}

		LeagueView(League league, PointCriteria criteria) {
			Rankings rankings = Rankings.from(league, criteria);

			Map<Score, Integer> position = new HashMap<>();
			List<Score> scores = new ArrayList<>();
			for (Slot slot : rankings.slots())
				for (Score score : slot.scores()) {
					position.put(score, slot.position());
					scores.add(score);
				}

			CustomTableView<Score> rankingView = CustomTableView.newInstance(scores);
			rankingView.column("Position", position::get);
			rankingView.column("Player", x -> playerName(x.player()));
			rankingView.column("Matches", x -> x.matchesWon() + ":" + x.matchesLost());
			rankingView.column("Games", x -> x.gamesWon() + ":" + x.gamesLost());
			rankingView.column("Points", Score::points);
			rankingView.distribute(1, 3, 1, 1, 1);

			this.view = rankingView;
		}
	}

}
