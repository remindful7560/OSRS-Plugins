package net.remindful.specwallofshame.utils;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.remindful.specwallofshame.SpecWallOfShameConfig;
import net.runelite.api.Client;
import static net.runelite.client.RuneLite.SCREENSHOT_DIR;
import net.runelite.client.config.RuneScapeProfileType;
import net.runelite.client.util.Text;

// Taken from https://github.com/neeerp/runelite-random-screenshot/blob/master/src/main/java/com/randomscreenshot/FileFactory.java
// with modifications, under the BSD 2-Clause License

@Singleton
@Slf4j
public class FileFactory
{
	private static final DateFormat TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
	@Inject
	private Client client;

	@Inject
	private SpecWallOfShameConfig config;

	public File createScreenshotFile(String name) throws IOException {
		File screenshotDirectory = createScreenshotDirectory();
		String dateStr = format(new Date());
		String fileName = name + " " + dateStr;

		File screenshotFile = new File(screenshotDirectory, fileName + ".png");

		// To make sure that screenshots don't get overwritten, check if file exists,
		// and if it does create file with same name and suffix.
		int i = 1;
		while (screenshotFile.exists())
		{
			screenshotFile = new File(screenshotDirectory, fileName + String.format("(%d)", i++) + ".png");
		}

		return screenshotFile;
	}

	/**
	 * Create a directory at `path` if one does not exist, and return the
	 * corresponding `File` object.
	 *
	 * If path is an empty string, the player profile directory is used instead.
	 */
	public File createScreenshotDirectory() throws IOException {
		File screenshotDirectory;
		screenshotDirectory = getPlayerScreenshotDirectory();

		if (!screenshotDirectory.mkdirs() && !screenshotDirectory.exists())
		{
			throw new IOException("Could not create screenshot directory at " + screenshotDirectory.getAbsolutePath());
		}

		return screenshotDirectory;
	}

	private File getPlayerScreenshotDirectory() {
		File directory;

		if (client.getLocalPlayer() != null && client.getLocalPlayer().getName() != null)
		{
			String playerDir = client.getLocalPlayer().getName();
			RuneScapeProfileType profileType = RuneScapeProfileType.getCurrent(client);
			if (profileType != RuneScapeProfileType.STANDARD)
			{
				playerDir += "-" + Text.titleCase(profileType);
			}
			playerDir += File.separator + "Spec Wall of Shame";

			directory = new File(SCREENSHOT_DIR, playerDir);
		}
		else
		{
			directory = SCREENSHOT_DIR;
		}

		return directory;

	}

	private static String format(Date date)
	{
		synchronized (TIME_FORMAT)
		{
			return TIME_FORMAT.format(date);
		}
	}
}