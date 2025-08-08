package net.remindful;

import net.remindful.enums.PoisonStatus;

import static net.remindful.enums.PoisonStatus.*;

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

	private static float clamp(float fraction, float min, float max) {
		if (fraction < min) {
			return min;
		}
		if (fraction > max) {
			return max;
		}

		return fraction;
	}

	public float damageFraction() {
		return clamp((float) this.damage() / (float) MAX_DAMAGE, 0.0f, 1.0f);
	}

	public static final int MAX_ANTIVENOM_DURATION = 21;
	public static final int MAX_ANTIPOISON_DURATION = MAX_ANTIVENOM_DURATION + 38;
	public static final int MAX_POISON_DURATION = 100; // This is an assumption based off of damage values
	public float remainingFraction() {
		var statusRemaining =  remaining(this.nextValue);
		var fraction = 0f;

		if (this.nextStatus() == Antipoisoned) {
			if (statusRemaining > MAX_ANTIPOISON_DURATION) {
				fraction = (float) (statusRemaining - MAX_ANTIPOISON_DURATION) / (float) MAX_ANTIVENOM_DURATION;
			} else {
				fraction = (float) statusRemaining / (float) MAX_ANTIPOISON_DURATION;
			}
		} else if (this.nextStatus() == Poisoned) {
			fraction = (float) statusRemaining / (float) MAX_POISON_DURATION;
		}


		return clamp(fraction, 0.0f, 1.0f);
	}

	private static int remaining(int poisonValue) {
		int remaining = poisonValue;

		if (poisonValue >= VENOM_THRESHOLD) {
			remaining = poisonValue - VENOM_THRESHOLD - 3;
		}

		return Math.abs(remaining);
	}

	// Pulled from the Poison plugin
	private static final int VENOM_THRESHOLD = 1_000_000;
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