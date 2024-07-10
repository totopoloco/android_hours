package at.mavila.android.hours.ui.settings;

import lombok.Getter;

/**
 * This class represents the settings of a country.
 */
@Getter
public abstract class CountrySettings {

  private final int minutesPerDayOfWork;
  private final int maximumMinutesInARow;
  private final int minutesOfBreakBetweenRanges;
  private final int maximumHourToWorkInADay; //in HH format

  protected CountrySettings(final int minutesPerDayOfWork,
                            final int maximumMinutesInARow,
                            final int minutesOfBreakBetweenRanges,
                            final int maximumHourToWorkInADay) {
    this.minutesPerDayOfWork = minutesPerDayOfWork;
    this.maximumMinutesInARow = maximumMinutesInARow;
    this.minutesOfBreakBetweenRanges = minutesOfBreakBetweenRanges;
    this.maximumHourToWorkInADay = maximumHourToWorkInADay;
  }
}