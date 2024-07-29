package at.mavila.android.hours.ui.settings;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import com.google.android.material.textfield.TextInputEditText;
import java.util.regex.Pattern;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HHMMTextInputWatcher implements TextWatcher {

  private static final String TIME_SEPARATOR = ":";
  @NonNull
  private final SettingsViewModel settingsViewModel;
  @NonNull
  private final TextInputEditText editText;

  private static final Pattern HHMM_PATTERN = Pattern.compile("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$");

  @Override
  public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    Log.d("HHMMTextInputWatcher",
        "beforeTextChanged: " + s.toString() + " start: " + start + " count: " + count + " after: " + after);
  }

  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count) {
    // Does nothing
  }

  @Override
  public void afterTextChanged(Editable s) {
    final String input = s.toString();

    if (!HHMM_PATTERN.matcher(input).matches()) {
      editText.setError("Invalid time format. Please use HH:MM"); // Clear the error if the input is valid
      return;
    }

    validateHHMM(input);

  }

  private void validateHHMM(String input) {
    final String[] parts = input.split(TIME_SEPARATOR);
    final int hours = Integer.parseInt(parts[0]);
    final int minutes = Integer.parseInt(parts[1]);

    final boolean isValid = (hours >= 0 && hours <= 23) && (minutes >= 0 && minutes <= 59);
    editText.setError(isValid ? null : "Time must be between 00:00 and 23:59.");
  }

}
