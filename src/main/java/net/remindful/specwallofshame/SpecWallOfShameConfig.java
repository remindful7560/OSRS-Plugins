package net.remindful.specwallofshame;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("specwallofshame")
public interface SpecWallOfShameConfig extends Config
{
	@ConfigItem(
			keyName = "zcbSpecDamageThreshold",
			name = "ZCB spec ruby bolt damage threshold",
			description = "How low of a hit the player needs to make when using a ZCB with ruby bolts to count as a failure",
			position = 0
	)
	default int zcbSpecDamageThreshold()
	{
		return 28;
	}
}
