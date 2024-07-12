package at.mavila.android.hours.ui.settings;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import com.google.android.material.textfield.TextInputEditText;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * This class is responsible for watching the text input and updating the settings.
 * It implements the TextWatcher interface and the OnSettingsChangeListener interface.
 * It is designed to be used with a TextInputEditText and reuses the SettingsViewModel.
 *
 * @author marcoavila
 */
@RequiredArgsConstructor
public class TextInputWatcher implements TextWatcher, OnSettingsChangeListener {

  @NonNull
  private final SettingsViewModel settingsViewModel;
  @NonNull
  private final SettingsField settingsField;
  @NonNull
  private final TextInputEditText editText;

  private String previousText;
  private int previousCursorPosition;

  @Override
  public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    Log.d("TextInputWatcher",
        "beforeTextChanged: " + s.toString() + " start: " + start + " count: " + count + " after: " + after);
    this.previousText = s.toString();
    this.previousCursorPosition = start;
  }

  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count) {

  }

  /**
   * This method is called after the text has changed. It checks if the text has actually changed and
   * if so, it updates the settings.
   *
   * @param s The editable text
   */
  @Override
  public void afterTextChanged(final Editable s) {

    // Get the new value
    final String newValue = s.toString();
    // Get the current text
    final String currentText = this.previousText;

    // Capture the current cursor position
    final int cursorPosition = this.previousCursorPosition;

    // Check if the text has actually changed
    if (hasTextActuallyChanged(currentText, newValue)) {
      // Update the settings
      onSettingsChanged(newValue);
      // Since we're setting the text anew, we need to restore the cursor position
      this.editText.setText(newValue);
      this.editText.setSelection(Math.min(cursorPosition, newValue.length()));
      return;
    }

    tryToRestoreCursorPosition(newValue);
  }

  /**
   * Tries to restore the cursor position after the text has been set anew.
   * It catches any exceptions that might occur.
   *
   * @param value The new value of the text
   */
  private void tryToRestoreCursorPosition(String value) {
    // If the text hasn't changed, try to restore the cursor position
    try {
      this.editText.setSelection(value.length());
    } catch (Exception e) {
      Log.e("TextInputWatcher", "Error setting cursor position", e);
    }
  }

  private static boolean hasTextActuallyChanged(String currentText, String newValue) {
    return isNotEqualsCurrentAndNew(currentText, newValue) && StringUtils.isNotBlank(currentText);
  }

  private static boolean isNotEqualsCurrentAndNew(String currentText, String newValue) {
    return !StringUtils.equals(currentText, newValue);
  }

  @Override
  public void onSettingsChanged(String newValue) {
    this.settingsViewModel.updateSettings(newValue, this.settingsField);
  }
}