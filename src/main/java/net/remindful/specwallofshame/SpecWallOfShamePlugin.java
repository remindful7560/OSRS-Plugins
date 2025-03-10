package net.remindful.specwallofshame;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.HitsplatApplied;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@Slf4j
@PluginDescriptor(
	name = "Spec Wall of Shame"
)
public class SpecWallOfShamePlugin extends Plugin  {
	@Inject
	private Client client;

	@Inject
	private SpecWallOfShameConfig config;

	@Override
	protected void startUp() throws Exception {
		log.info("Spec Wall of Shame started!");
	}

	@Override
	protected void shutDown() throws Exception {
		log.info("Spec Wall of Shame stopped!");
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged) {
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN) {
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Spec Wall of Shame says " + config.greeting(), null);
		}
	}

	@Provides
	SpecWallOfShameConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(SpecWallOfShameConfig.class);
	}
}
