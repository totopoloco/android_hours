package at.mavila.android.hours.ui.settings;

import static org.junit.Assert.assertEquals;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class SettingsTest {

  @Test
  public void testAustriaSettings() {

    Settings settings = new Settings();
    settings.setMinutesPerDayOfWork(462);
    settings.setMaximumMinutesInARow(240);
    settings.setMinutesOfBreakBetweenRanges(30);
    settings.setMaximumHourToWorkInADay(20);

    assertEquals(462, settings.getMinutesPerDayOfWork());
    assertEquals(240, settings.getMaximumMinutesInARow());
    assertEquals(30, settings.getMinutesOfBreakBetweenRanges());
    assertEquals(20, settings.getMaximumHourToWorkInADay());

    settings.setId(1);
    assertEquals(1, settings.getId());

  }

}