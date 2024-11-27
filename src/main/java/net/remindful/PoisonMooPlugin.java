package net.remindful;

import javax.inject.Inject;

import com.google.inject.Provides;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Constants;
import net.runelite.api.VarPlayer;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.Notifier;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import java.util.Random;

@Slf4j
@PluginDescriptor(
		name = "Poison Moo"
)
public class PoisonMooPlugin extends Plugin {

	@Inject
	private Client client;

	@Inject
	private ClientThread clientThread;

	@Inject
	private Notifier notifier;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private PoisonMooConfig config;

	static final Random rand = new Random();

	private int lastDamage = 0;

	@Provides
	PoisonMooConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(PoisonMooConfig.class);
	}

	@Override
	protected void startUp() throws Exception {
	}

	@Override
	protected void shutDown() throws Exception {
	}

	@Subscribe
	private void onVarbitChanged(VarbitChanged event)
	{
		if (event.getVarpId() == VarPlayer.POISON)
		{
			if (lastDamage == 0) {
				lastDamage = config.maxMooLength();
			}
			moo(lastDamage);
			lastDamage = nextDamage(event.getValue());
		}
	}

	public void moo(int damage) {
		var player = client.getLocalPlayer();

		var moo = new StringBuilder("Moo");

		switch (config.mooVariety()) {
			case Never:
				break;
			case Random:
				moo.append("o".repeat(rand.nextInt(config.maxMooLength())));
				break;
			case Damage:
				float frac = (float)damage / (float)MAX_DAMAGE;
				moo.append("o".repeat(Math.round(config.maxMooLength()*frac)));
				break;
		}

		switch (config.tilde()) {
			case Never:
				break;
			case Always:
				moo.append('~');
				break;
			case Random:
				if (rand.nextBoolean()) {
					moo.append('~');
				}
				break;
		}

		switch (config.exclaim()) {
			case Never:
				break;
			case Always:
				moo.append('!');
				break;
			case Random:
				if (rand.nextBoolean()) {
					moo.append('!');
				}
				break;
		}

		player.setOverheadText(moo.toString());
		player.setOverheadCycle(config.mooDuration() * 1000 / Constants.CLIENT_TICK_LENGTH);
	}


	private static final int VENOM_THRESHOLD = 1000000;
	private static final int MAX_DAMAGE = 20;
	private static int nextDamage(int poisonValue)
	{
		int damage;

		if (poisonValue >= VENOM_THRESHOLD)
		{
			//Venom Damage starts at 6, and increments in twos;
			//The VarPlayer increments in values of 1, however.
			poisonValue -= VENOM_THRESHOLD - 3;
			damage = poisonValue * 2;
			//Venom Damage caps at 20, but the VarPlayer keeps increasing
			if (damage > MAX_DAMAGE)
			{
				damage = MAX_DAMAGE;
			}
		}
		else
		{
			damage = (int) Math.ceil(poisonValue / 5.0f);
		}

		return damage;
	}
}
