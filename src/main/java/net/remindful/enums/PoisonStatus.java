package net.remindful.enums;

public enum PoisonStatus {
	Cured,
	Poisoned,
	Antipoisoned;

	public static PoisonStatus fromValue(int value) {
		if (value < 0) {
			return Antipoisoned;
		}
		if (value > 0) {
			return Poisoned;
		}

		return Cured;
	}
}
