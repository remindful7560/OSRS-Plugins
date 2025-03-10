package net.remindful.specwallofshame.utils;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.events.ScreenshotTaken;
import net.runelite.client.ui.DrawManager;
import net.runelite.client.util.ImageUtil;

// Taken from https://github.com/neeerp/runelite-random-screenshot/blob/master/src/main/java/com/randomscreenshot/ScreenshotUtil.java
// under the BSD 2-Clause License

@Singleton
@Slf4j
public class ScreenshotUtil
{
	@Inject
	private DrawManager drawManager;
	@Inject
	private ScheduledExecutorService executor;
	@Inject
	private EventBus eventBus;
	@Inject
	private FileFactory fileFactory;

	public void takeScreenshot(String name)
	{
		Consumer<Image> imageCallback = (img) ->
		{
			// This callback is on the game thread, move to executor thread
			executor.submit(() -> saveScreenshot(img, name));
		};

		drawManager.requestNextFrameListener(imageCallback);
	}

	private void saveScreenshot(Image image, String name)
	{
		BufferedImage screenshot = ImageUtil.bufferedImageFromImage(image);

		File screenshotFile;
		try
		{
			screenshotFile = fileFactory.createScreenshotFile(name);
			ImageIO.write(screenshot, "PNG", screenshotFile);
		}
		catch (IOException ex)
		{
			log.error("error writing screenshot", ex);
			return;
		}

		ScreenshotTaken screenshotTaken = new ScreenshotTaken(
				screenshotFile,
				screenshot
		);
		eventBus.post(screenshotTaken);
	}
}
