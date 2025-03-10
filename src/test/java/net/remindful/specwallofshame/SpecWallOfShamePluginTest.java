package net.remindful.specwallofshame;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class SpecWallOfShamePluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(SpecWallOfShamePlugin.class);
		RuneLite.main(args);
	}
}