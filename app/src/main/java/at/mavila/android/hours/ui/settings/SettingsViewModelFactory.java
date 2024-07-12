package at.mavila.android.hours.ui.settings;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import java.util.Objects;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SettingsViewModelFactory implements ViewModelProvider.Factory {

  private final SettingsRepository settingsRepository;

  @NonNull
  @Override
  public <T extends ViewModel> T create(@NonNull final Class<T> modelClass) {
    if (SettingsViewModel.class.isAssignableFrom(modelClass)) {
      return Objects.requireNonNull(modelClass.cast(new SettingsViewModel(this.settingsRepository)));
    }
    throw new IllegalArgumentException("Unknown ViewModel class");
  }
}
