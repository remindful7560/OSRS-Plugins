package net.remindful;

import net.remindful.enums.config.CureTicksChoice;
import net.remindful.enums.config.ExclaimChoice;
import net.remindful.enums.config.MooVarietyChoice;
import net.remindful.enums.config.TildeChoice;
import net.runelite.client.config.*;

@ConfigGroup(net.remindful.PoisonMooConfig.GROUP)
public interface PoisonMooConfig extends Config {
	String GROUP = "poisonmoo";

	@ConfigItem(
			keyName = "mooOnAntipoisonTicks",
			name = "Moo on antipoison ticks",
			description = "Antiposion ticks are poison ticks with a negative value. Disabling this is arguably non-canon (Mod Ash comment pending)",
			position = 100
	)
	default boolean mooOnAntipoisonTicks() {
		return true;
	}

	@ConfigItem(
			keyName = "mooOnCureTicks",
			name = "Moo on cure ticks",
			description = "Cure ticks occur when a poison tick happens with a value of zero. Either because a poison/antipoison status ran out naturally, or because it was removed",
			position = 200
	)
	default CureTicksChoice mooOnCureTicks() {
		return CureTicksChoice.Always;
	}

	@ConfigItem(
			keyName = "playSoundEffectOnMoo",
			name = "Play sound effect on moo",
			description = "Play the cow mooing sound (uses in game sound effects volume). For true authenticity",
			position = 300
	)
	default boolean playSoundEffectOnMoo() {
		return false;
	}

	@Range(
			max = 18
	)
	@ConfigItem(
			keyName = "mooDuration",
			name = "Moo duration",
			description = "How long the overhead remains",
			position = 400
	)
	@Units(Units.SECONDS)
	default int mooDuration() {
		return 2;
	}

	@ConfigItem(
			keyName = "mooVariety",
			name = "Moo variety",
			description = "Moo in ways not known to OSRS before now",
			position = 500
	)
	default MooVarietyChoice mooVariety() {
		return MooVarietyChoice.Never;
	}

	@Range(
			max = 20
	)
	@ConfigItem(
			keyName = "maxMooLength",
			name = "Max extra length",
			description = "The maximum additional extra 'o's that will be added, if moo variety is on",
			position = 600
	)
	default int maxMooLength() {
		return 5;
	}

	@ConfigItem(
			keyName = "tilde",
			name = "~",
			description = "If you sort of like it",
			position = 700
	)
	default TildeChoice tilde() {
		return TildeChoice.Never;
	}

	@ConfigItem(
			keyName = "exclaim",
			name = "!",
			description = "If you want the world to know",
			position = 800
	)
	default ExclaimChoice exclaim() {
		return ExclaimChoice.Never;
	}
}

