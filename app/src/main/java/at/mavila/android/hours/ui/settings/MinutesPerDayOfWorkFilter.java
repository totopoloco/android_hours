package at.mavila.android.hours.ui.settings;

import java.util.function.Function;

public class MinutesPerDayOfWorkFilter implements Function<String, String> {

  @Override
  public String apply(String input) {

    try {
      return isNotInRange(Integer.parseInt(input)) ?
          "Minutes per day of work must be between 0 and 480."
          : null;
    } catch (NumberFormatException e) {
      return "Invalid number format.";
    }

  }

  private static boolean isNotInRange(int minutes) {
    return minutes < 0 || minutes > 480;
  }
}
