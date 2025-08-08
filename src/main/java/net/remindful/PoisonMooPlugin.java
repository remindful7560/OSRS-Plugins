package net.remindful;

import javax.inject.Inject;

import com.google.inject.Provides;

import lombok.extern.slf4j.Slf4j;
import net.remindful.enums.config.CureTicksChoice;
import net.remindful.enums.PoisonStatus;
import net.runelite.api.Client;
import net.runelite.api.Constants;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.gameval.VarPlayerID;
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

	static final int COW_ATMOSPHERIC = 3044;

	static final Random rand = new Random();

	private final PoisonState poisonState = new PoisonState(0);

	@Provides
	PoisonMooConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(PoisonMooConfig.class);
	}

	@Override
	protected void startUp() {
	}

	@Override
	protected void shutDown() {
	}

	@Subscribe
	private void onVarbitChanged(VarbitChanged event) {
		if (event.getVarpId() == VarPlayerID.POISON) {
			this.poisonState.setNextValue(event.getValue());
			moo(this.poisonState);
		}
	}

	private void moo(PoisonState poisonState) {
		if (poisonState.nextStatus() == PoisonStatus.Antipoisoned && !config.mooOnAntipoisonTicks()) {
			return;
		}
		if (poisonState.nextStatus() == PoisonStatus.Cured) {
			if (config.mooOnCureTicks() == CureTicksChoice.Never) {
				return;
			}
			if (!poisonState.poisonCured() && config.mooOnCureTicks() == CureTicksChoice.Poison_Cure) {
				return;
			}
		}

		var moo = new StringBuilder("Moo");

		switch (config.mooVariety()) {
			case Never:
				break;
			case Random:
				moo.append("o".repeat(rand.nextInt(config.maxMooLength())));
				break;
			case Damage:
				moo.append("o".repeat(Math.round(config.maxMooLength()*poisonState.damageFraction())));
				break;
			case Remaining:
				moo.append("o".repeat(Math.round(config.maxMooLength()*poisonState.remainingFraction())));
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

		var player = client.getLocalPlayer();
		player.setOverheadText(moo.toString());
		player.setOverheadCycle(config.mooDuration() * 1000 / Constants.CLIENT_TICK_LENGTH);

		if (config.playSoundEffectOnMoo()) {
			client.playSoundEffect(COW_ATMOSPHERIC);
		}
	}
}
