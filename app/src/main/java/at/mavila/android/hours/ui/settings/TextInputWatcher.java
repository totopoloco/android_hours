package at.mavila.android.hours.ui.settings;

import android.text.Editable;
import android.text.TextWatcher;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TextInputWatcher implements TextWatcher, OnSettingsChangeListener {

  private final SettingsViewModel settingsViewModel;
  private final SettingsField settingsField;

  @Override
  public void beforeTextChanged(CharSequence s, int start, int count, int after) {

  }

  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count) {

  }

  @Override
  public void afterTextChanged(final Editable s) {
    onSettingsChanged(s.toString());
  }

  @Override
  public void onSettingsChanged(String newValue) {
    this.settingsViewModel.updateSettings(newValue, this.settingsField);
  }
}
