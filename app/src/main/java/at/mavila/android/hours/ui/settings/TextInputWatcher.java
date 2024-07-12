package at.mavila.android.hours.ui.settings;

import android.text.Editable;
import android.text.TextWatcher;
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

  private boolean isUpdating = false;
  private String previousText = "";
  private int previousCaretPosition = 0;

  @Override
  public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    this.previousText = s.toString();
    this.previousCaretPosition = editText.getSelectionStart();
  }

  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count) {
    // No action needed here
  }

  /**
   * This method is called after the text has changed. It checks if the text has actually changed and
   * if so, it updates the settings.
   *
   * @param s The editable text
   */
  @Override
  public void afterTextChanged(final Editable s) {
    if (this.isUpdating) {
      return;
    }

    if (StringUtils.isBlank(this.previousText)) {
      this.isUpdating = false;
      return;
    }

    final String newValue = s.toString();
    if (valuesHaveChanged(newValue)) {
      this.isUpdating = true;
      settingsViewModel.updateSettings(newValue, this.settingsField);
      // Calculate expected new caret position based on text length change
      editText.setSelection(newValue.length());
      this.isUpdating = false;
      return;
    }
    editText.setSelection(previousCaretPosition);
  }

  private boolean valuesHaveChanged(String newValue) {
    return !StringUtils.equals(this.previousText, newValue);
  }


  @Override
  public void onSettingsChanged(String newValue) {
    this.settingsViewModel.updateSettings(newValue, this.settingsField);
  }
}