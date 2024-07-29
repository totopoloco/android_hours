package at.mavila.android.hours.ui.settings;

import at.mavila.android.hours.databinding.FragmentSettingsBinding;
import java.util.Objects;
import java.util.function.Function;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MinutesInRangeOfTheDayFilter implements Function<String, String> {

  private final FragmentSettingsBinding binding;
  private final String message;

  @Override
  public String apply(String s) {
    try {
      final int current = Integer.parseInt(s);
      final int range = Integer.parseInt(Objects.requireNonNull(binding.minutesPerDayOfWorkEditInputText.getText()).toString());
      return current > range ? message : null;
    } catch (NumberFormatException e) {
      return "Invalid number format.";
    }
  }
}
