package league.results;

import java.util.EnumMap;
import java.util.Map;

public class PointCriteria {

	public enum Item {
		MATCH_WON,
		MATCH_PLAYED,
		GAME_WON
	}

	public static Builder newBuilder() {
		return new Builder();
	}

	public static class Builder {

		private final Map<Item, Integer> points;

		private Builder() {
			this.points = new EnumMap<>(Item.class);
		}

		public Builder set(Item item, int count) {
			points.put(item, count);
			return this;
		}

		public PointCriteria build() {
			return new PointCriteria(points);
		}

	}

	private final Map<Item, Integer> points;

	private PointCriteria(Map<Item, Integer> points) {
		Map<Item, Integer> helper = new EnumMap<>(Item.class);
		helper.putAll(points);
		this.points = helper;
	}

	public int points(Item item) {
		return points.getOrDefault(item, 0);
	}

}
