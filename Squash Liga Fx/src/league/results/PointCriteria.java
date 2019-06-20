package league.results;

import java.util.EnumMap;
import java.util.Map;

class PointCriteria {

	public enum Item {
		MATCH_WON,
		MATCH_PLAYED,
		GAME_WON
	}

	static Builder newBuilder() {
		return new Builder();
	}

	static class Builder {

		private final Map<Item, Integer> points;

		private Builder() {
			this.points = new EnumMap<>(Item.class);
		}

		Builder set(Item item, int count) {
			points.put(item, count);
			return this;
		}

		PointCriteria build() {
			return new PointCriteria(points);
		}

	}

	private final Map<Item, Integer> points;

	private PointCriteria(Map<Item, Integer> points) {
		Map<Item, Integer> helper = new EnumMap<>(Item.class);
		helper.putAll(points);
		this.points = helper;
	}

	int points(Item item) {
		return points.getOrDefault(item, 0);
	}

}
