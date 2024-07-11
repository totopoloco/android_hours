package at.mavila.android.hours.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import at.mavila.android.hours.databinding.FragmentSettingsBinding;
import com.google.android.material.textfield.TextInputEditText;
import java.util.Objects;

/**
 * This fragment is responsible for displaying the settings view.
 *
 * @author marcoavila
 * @since 10.07.2024
 */
public class SettingsFragment extends Fragment {

  private FragmentSettingsBinding binding;

  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container,
                           Bundle savedInstanceState) {


    final SettingsViewModel settingsViewModel = getSettingsViewModel();
    this.binding = FragmentSettingsBinding.inflate(inflater, container, false);
    final View root = this.binding.getRoot();
    configureMinutesPerDayOfWork(settingsViewModel);

    return root;
  }

  private @NonNull SettingsViewModel getSettingsViewModel() {
    SettingsViewModel settingsViewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
      @NonNull
      @Override
      public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        if (SettingsViewModel.class.isAssignableFrom(modelClass)) {
          return getClass(modelClass);
        }

        throw new IllegalArgumentException("Unknown ViewModel class");

      }

      private <T extends ViewModel> @NonNull T getClass(@NonNull Class<T> modelClass) {
        return Objects.requireNonNull(modelClass.cast(new SettingsViewModel(new SettingsRepository(requireContext()))));
      }

    }).get(SettingsViewModel.class);
    settingsViewModel.observeSettingsAndUpdate(this);
    return settingsViewModel;
  }

  private void configureMinutesPerDayOfWork(final SettingsViewModel settingsViewModel) {
    final TextInputEditText minutesPerDayOfWorkEditInputText = binding.minutesPerDayOfWorkEditInputText;
    settingsViewModel.getSettings().observe(getViewLifecycleOwner(), settings -> {
      if (Objects.isNull(settings)) {
        return;
      }
      minutesPerDayOfWorkEditInputText.setText(String.valueOf(settings.getMinutesPerDayOfWork()));

    });
    minutesPerDayOfWorkEditInputText.addTextChangedListener(
        new TextInputWatcher(settingsViewModel, SettingsField.MINUTES_PER_DAY_OF_WORK));
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }
}