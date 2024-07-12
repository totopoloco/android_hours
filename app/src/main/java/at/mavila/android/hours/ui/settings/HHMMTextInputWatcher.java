package at.mavila.android.hours.ui.settings;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import com.google.android.material.textfield.TextInputEditText;
import java.util.regex.Pattern;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@RequiredArgsConstructor
public class HHMMTextInputWatcher implements TextWatcher, OnSettingsChangeListener {

  @NonNull
  private final SettingsViewModel settingsViewModel;
  @NonNull
  private final TextInputEditText editText;

  private String previousText;
  private int previousCursorPosition;
  private static final Pattern HHMM_PATTERN = Pattern.compile("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$");

  @Override
  public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    Log.d("HHMMTextInputWatcher",
        "beforeTextChanged: " + s.toString() + " start: " + start + " count: " + count + " after: " + after);
    this.previousText = s.toString();
    this.previousCursorPosition = this.editText.getSelectionStart();
  }

  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count) {
    //Not used
  }

  @Override
  public void afterTextChanged(Editable s) {
    final String input = s.toString();
    if (HHMM_PATTERN.matcher(input).matches() && !StringUtils.equals(this.previousText, input)) {
      this.onSettingsChanged(input);
      return;
    }
  }

  @Override
  public void onSettingsChanged(final String input) {
    this.settingsViewModel.updateSettings(input, SettingsField.MAXIMUM_HOUR_TO_WORK_IN_A_DAY);
  }
}
