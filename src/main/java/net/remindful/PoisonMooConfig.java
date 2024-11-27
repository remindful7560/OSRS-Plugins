package net.remindful;

import net.remindful.enums.ExclaimChoice;
import net.remindful.enums.MooVarietyChoice;
import net.remindful.enums.TildeChoice;
import net.runelite.client.config.*;

@ConfigGroup(net.remindful.PoisonMooConfig.GROUP)
public interface PoisonMooConfig extends Config {
	String GROUP = "poisonmoo";

	@Range(
			min = 0,
			max = 18
	)
	@ConfigItem(
			keyName = "mooDuration",
			name = "Moo duration",
			description = "How long the overhead remains",
			position = 1
	)
	@Units(Units.SECONDS)
	default int mooDuration() {
		return 2;
	}

	@ConfigItem(
			keyName = "mooVariety",
			name = "Moo variety",
			description = "Moo in ways not known to OSRS before now",
			position = 2
	)
	default MooVarietyChoice mooVariety() {
		return MooVarietyChoice.Never;
	}

	@Range(
			min = 0,
			max = 20
	)
	@ConfigItem(
			keyName = "maxMooLength",
			name = "Max extra length",
			description = "The maximum additional extra 'o's that will be added, if moo variety is on",
			position = 3
	)
	default int maxMooLength() {
		return 5;
	}

	@ConfigItem(
			keyName = "tilde",
			name = "~",
			description = "If you sort of like it",
			position = 4
	)
	default TildeChoice tilde() {
		return TildeChoice.Never;
	}

	@ConfigItem(
			keyName = "exclaim",
			name = "!",
			description = "If you want the world to know",
			position = 5
	)
	default ExclaimChoice exclaim() {
		return ExclaimChoice.Never;
	}
}

