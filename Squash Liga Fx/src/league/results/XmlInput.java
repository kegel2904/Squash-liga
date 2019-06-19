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

		List<League> leagues = new ArrayList<>();

		for (XmlNode leagueNode : root.children(LEAGUE)) {
			String name = extract(leagueNode, NAME);
			League league = new League(name);
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

	private static int value(Matcher matcher, int group) throws IOException {
		try {
			return Integer.parseInt(matcher.group(group));
		} catch (NumberFormatException e) {
			throw new IOException(e);
		}
	}

	private static String extract(XmlNode node, String attribute) throws IOException {
		String value = node.attribute(attribute);
		if (value == null)
			throw new IOException(attribute);
		return value;
	}

}
