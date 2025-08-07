package net.remindful;

import net.remindful.enums.PoisonStatus;

import static net.remindful.enums.PoisonStatus.Cured;
import static net.remindful.enums.PoisonStatus.Poisoned;

public class PoisonState {
	private int currentValue = 0;
	private int nextValue;

	public PoisonState(int nextValue) {
		this.nextValue = nextValue;
	}

	public void setNextValue(int nextValue) {
		this.currentValue = this.nextValue;
		this.nextValue = nextValue;
	}

	public PoisonStatus nextStatus() {
		return PoisonStatus.fromValue(this.nextValue);
	}

	public PoisonStatus currentStatus() {
		return PoisonStatus.fromValue(this.currentValue);
	}

	public boolean poisonCured() {
		return this.currentStatus() == Poisoned && this.nextStatus() == Cured;
	}

	public int damage() {
		if (this.nextStatus() != Poisoned) {
			return 0;
		}
		return nextDamage(this.currentValue);
	}

	public static final int MAX_DURATION = 18;
	public float remainingFraction() {
		return (float) Math.abs(this.nextValue) / (float) MAX_DURATION;
	}

	// Pulled from the Poison plugin
	private static final int VENOM_THRESHOLD = 1000000;
	public static final int MAX_DAMAGE = 20;
	private static int nextDamage(int poisonValue) {
		int damage;

		if (poisonValue >= VENOM_THRESHOLD) {
			//Venom Damage starts at 6, and increments in twos;
			//The VarPlayer increments in values of 1, however.
			poisonValue -= VENOM_THRESHOLD - 3;
			damage = poisonValue * 2;
			//Venom Damage caps at 20, but the VarPlayer keeps increasing
			if (damage > MAX_DAMAGE) {
				damage = MAX_DAMAGE;
			}
		} else {
			damage = (int) Math.ceil(poisonValue / 5.0f);
		}

		return damage;
	}
}