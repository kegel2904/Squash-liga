package league.visual;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import league.results.History;
import league.results.League;
import league.results.MatchTable;
import league.results.MatchTable.MatchRow;
import league.results.Player;
import league.results.Rankings;
import league.results.Rankings.Score;
import league.results.Rankings.Slot;
import league.results.Result;
import league.visual.util.CustomListView;
import league.visual.util.CustomTabPane;
import league.visual.util.CustomTableView;
import league.visual.util.Stretch;

public class Overview {

	private final Node view;

	Overview(History history) {
		CustomTabPane tabs = new CustomTabPane();
		CustomListView<Player> players = CustomListView.newInstance(history.players());
		players.display(x -> x.name() + " " + x.lastName());
		tabs.tab("Players", players);
		LeaguesView leagues = new LeaguesView(history.leagues());
		tabs.tab("Leagues", leagues.view);
		this.view = tabs;
	}

	Node view() {
		return view;
	}

	private static class LeaguesView {
		private final Node view;

		LeaguesView(List<League> leagues) {
			CustomListView<League> list = CustomListView.newInstance(leagues);
			list.display(League::name);

			HBox inner = new HBox();
			SplitPane pane = new SplitPane(list, inner);
			pane.setDividerPositions(0.2);

			list.selectedItem().addListener((x, o, n) -> {
				if (n == null)
					inner.getChildren().clear();
				else {
					ResultsView results = new ResultsView(n);
					LeagueView rankings = new LeagueView(n);
					VBox box = new VBox(results.view, rankings.view);
					inner.getChildren().setAll(box);
					Stretch.vertical(rankings.view);
					Stretch.horizontal(box);
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

		LeagueView(League league) {
			Rankings rankings = Rankings.from(league);

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

	private static class ResultsView {
		private final Node view;

		private static String playerName(Player player) {
			return player.name().charAt(0) + ". " + player.lastName();
		}

		private static String result(Result result) {
			return result.first() + ":" + result.second();
		}

		ResultsView(League league) {
			MatchTable matches = MatchTable.from(league);

			CustomTableView<MatchRow> rowsView = CustomTableView.newInstance(matches.rows());
			rowsView.column((String) null, x -> playerName(x.player()));

			for (Player player : matches.players())
				rowsView.column(player.lastName(), x -> matches.result(x.player(), player),
						ResultsView::result);
			rowsView.distribute();

			this.view = rowsView;
		}
	}

}
