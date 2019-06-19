package league.results;

import java.util.Objects;

public class Player {

	private final String name;
	private final String lastName;

	Player(String name, String lastName) {
		this.name = Objects.requireNonNull(name);
		this.lastName = Objects.requireNonNull(lastName);
	}

	public String name() {
		return name;
	}

	public String lastName() {
		return lastName;
	}

	@Override
	public String toString() {
		return name + " " + lastName;
	}

}
