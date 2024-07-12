package at.mavila.android.hours.ui.settings;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SettingsViewModelFactory implements ViewModelProvider.Factory {

  private final SettingsRepository settingsRepository;

  @Override
  public <T extends ViewModel> T create(final Class<T> modelClass) {
    if (SettingsViewModel.class.isAssignableFrom(modelClass)) {
      return (T) new SettingsViewModel(this.settingsRepository);
    }
    throw new IllegalArgumentException("Unknown ViewModel class");
  }
}
