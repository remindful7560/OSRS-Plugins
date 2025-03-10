package net.remindful.specwallofshame;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.remindful.specwallofshame.utils.ScreenshotUtil;
import net.runelite.api.*;
import net.runelite.api.events.HitsplatApplied;
import net.runelite.api.events.PostClientTick;
import net.runelite.api.events.SoundEffectPlayed;
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

	@Inject
	private ScreenshotUtil screenShotUtil;

	private boolean zcb_spec_used_this_tick = false;
	private boolean ruby_proc_succeeded_this_tick = false;
	private Hitsplat last_player_hitsplat = null;

	@Override
	protected void startUp() throws Exception {

	}

	@Override
	protected void shutDown() throws Exception {

	}

	@Subscribe(priority = 2) // Raise priority in case sounds were muted by Annoyance Mute or similar
	public void onSoundEffectPlayed(SoundEffectPlayed soundEffectPlayed)
	{
		int soundId = soundEffectPlayed.getSoundId();
		if (soundId == SoundEffectID.godwars_armadyl_spec) {
			zcb_spec_used_this_tick = true;
		} else if (soundId == SoundEffectID.blood_sacrifice) {
			ruby_proc_succeeded_this_tick = true;
		}
	}

	@Subscribe
	public void onPostClientTick(PostClientTick event)
	{
		if (zcb_spec_used_this_tick && !ruby_proc_succeeded_this_tick) {
			log.warn("Attack missed");
		}
		
		if (zcb_spec_used_this_tick && ruby_proc_succeeded_this_tick && last_player_hitsplat.getAmount() < config.zcbSpecDamageThreshold()) {
			screenShotUtil.takeScreenshot("ZCB mistime");
		}

		zcb_spec_used_this_tick = false;
		ruby_proc_succeeded_this_tick = false;
	}

	@Subscribe
	public void onHitsplatApplied(HitsplatApplied hitsplatApplied) {
		Actor actor = hitsplatApplied.getActor();
		if (!(actor instanceof NPC)) {
			return;
		}

		Hitsplat hitsplat = hitsplatApplied.getHitsplat();
		if (!hitsplat.isMine()) {
			return;
		}

		last_player_hitsplat = hitsplat;
	}

	@Provides
	SpecWallOfShameConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(SpecWallOfShameConfig.class);
	}
}
