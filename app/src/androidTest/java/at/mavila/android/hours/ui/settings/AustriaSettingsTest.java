package at.mavila.android.hours.ui.settings;

import static org.junit.Assert.assertEquals;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class AustriaSettingsTest {

  @Test
  public void testAustriaSettings() {
    AustriaSettings austriaSettings = new AustriaSettings();
    assertEquals(462, austriaSettings.getMinutesPerDayOfWork());
    assertEquals(240, austriaSettings.getMaximumMinutesInARow());
    assertEquals(30, austriaSettings.getMinutesOfBreakBetweenRanges());
    assertEquals(20, austriaSettings.getMaximumHourToWorkInADay());
  }

}