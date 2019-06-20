package league.results;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import league.results.PointCriteria.Item;
import league.util.XmlNode;
import league.util.XmlNode.XmlException;

public class XmlInput {

	private XmlInput() {}

	private static final String DATA = "data";
	private static final String PLAYER = "player";
	private static final String NAME = "name";
	private static final String LAST_NAME = "lastname";
	private static final String MATCH = "match";
	private static final String ID = "id";
	private static final String FIRST = "first";
	private static final String SECOND = "second";
	private static final String RESULT = "result";
	private static final String LEAGUE = "league";
	private static final String POINTS = "points";
	private static final String MATCH_PLAYED = "match_played";
	private static final String GAME_WON = "game_won";
	private static final String MATCH_WON = "match_won";
	private static final String COUNT = "count";

	public static History read(String filename) throws IOException {
		try (InputStream input = new FileInputStream(filename)) {
			return read(input);
		}
	}

	public static History read(InputStream input) throws IOException {
		XmlNode node;
		try {
			node = XmlNode.parse(input);
		} catch (XmlException e) {
			throw new IOException(e);
		}

		XmlNode root = node.onlyChild(DATA);
		if (root == null)
			throw new IOException();

		Map<String, Player> byId = new LinkedHashMap<>();
		for (XmlNode playerNode : root.children(PLAYER)) {
			String name = extract(playerNode, NAME);
			String lastName = extract(playerNode, LAST_NAME);
			String id = extract(playerNode, ID);
			Player player = new Player(name, lastName);
			Player old = byId.putIfAbsent(id, player);
			if (old != null)
				throw new IOException(old + "-" + player);
		}

		IdRegister<PointCriteria> pointsById = new IdRegister<>();
		for (XmlNode pointsNode : root.children(POINTS)) {
			String id = extract(pointsNode, NAME);
			int matchWon = pointCount(pointsNode, MATCH_WON);
			int gameWon = pointCount(pointsNode, GAME_WON);
			int matchPlayed = pointCount(pointsNode, MATCH_PLAYED);
			PointCriteria points = PointCriteria.newBuilder()
					.set(Item.GAME_WON, gameWon)
					.set(Item.MATCH_WON, matchWon)
					.set(Item.MATCH_PLAYED, matchPlayed)
					.build();
			pointsById.register(id, points);
		}

		List<League> leagues = new ArrayList<>();

		for (XmlNode leagueNode : root.children(LEAGUE)) {
			String name = extract(leagueNode, NAME);
			String pointsId = extract(leagueNode, POINTS);
			PointCriteria points = pointsById.get(pointsId);

			League league = new League(name, points);
			Set<String> ids = new HashSet<>();
			for (XmlNode playerNode : leagueNode.children(PLAYER)) {
				String id = extract(playerNode, ID);
				Player player = byId.get(id);
				if (player == null)
					throw new IOException(id);
				ids.add(id);
				league.addPlayer(player);
			}

			Pattern pattern = Pattern.compile("(\\d+):(\\d+)");
			for (XmlNode matchNode : leagueNode.children(MATCH)) {
				String firstId = extract(matchNode, FIRST);
				String secondId = extract(matchNode, SECOND);

				if (!ids.contains(firstId))
					throw new IOException(firstId);
				if (!ids.contains(secondId))
					throw new IOException(secondId);
				if (firstId.equals(secondId))
					throw new IOException(firstId);

				String resultString = extract(matchNode, RESULT);

				Player first = byId.get(firstId);
				Player second = byId.get(secondId);

				Matcher matcher = pattern.matcher(resultString);
				if (!matcher.find())
					throw new IOException(resultString);
				int firstRes = value(matcher, 1);
				int secondRes = value(matcher, 2);

				if (firstRes < 0 || secondRes < 0 || firstRes == secondRes)
					throw new IOException(resultString);

				Result result = new Result(firstRes, secondRes);
				Match match = new Match(first, second, result);

				league.addMatch(match);
			}
			leagues.add(league);
		}

		return new History(byId.values(), leagues);
	}

	private static int pointCount(XmlNode parent, String child) {
		XmlNode node = parent.onlyChild(child);
		if (node == null)
			return 0;
		String countString = extract(node, COUNT);
		try {
			return Integer.parseInt(countString);
		} catch (NumberFormatException e) {
			throw new InputException(countString);
		}
	}

	private static int value(Matcher matcher, int group) {
		String value = matcher.group(group);
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			throw new InputException(value);
		}
	}

	private static String extract(XmlNode node, String attribute) {
		String value = node.attribute(attribute);
		if (value == null)
			throw new InputException(attribute);
		return value;
	}

	private static class IdRegister<T> {
		private final Map<String, T> map = new LinkedHashMap<>();

		void register(String id, T object) {
			if (object == null)
				throw new InputException(id);
			T old = map.putIfAbsent(id, object);
			if (old != null)
				throw new InputException(id + "," + old + "," + object);
		}

		T get(String id) {
			T value = map.get(id);
			if (value == null)
				throw new InputException(id);
			return value;
		}

		List<T> elements() {
			return new ArrayList<>(map.values());
		}

	}

	private static class InputException extends RuntimeException {

		private static final long serialVersionUID = 1L;

		InputException(Throwable t) {
			super(t);
		}

		InputException(String s) {
			super(s);
		}

	}

}
