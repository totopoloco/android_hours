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
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import java.util.Objects;
import java.util.function.Function;

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
    configureFields(settingsViewModel);

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

  private void configureFields(final SettingsViewModel settingsViewModel) {
    //------------------------------------------------------
    minutesPerDay(settingsViewModel);
    //------------------------------------------------------
    minutesPerRow(settingsViewModel);
    //------------------------------------------------------
    breaks(settingsViewModel);
    //------------------------------------------------------
    final TextInputEditText maximumHour = this.binding.maximumHourOfTheDayToEndWorkInputEditText;
    settingsViewModel.getSettings().observe(getViewLifecycleOwner(), settings -> {
      if (Objects.isNull(settings)) {
        return;
      }
      maximumHour.setText(settings.getMaximumHourToWorkInADay());

    });
    maximumHour.addTextChangedListener(new HHMMTextInputWatcher(settingsViewModel, maximumHour));

    //------------------------------------------------------
    SwitchMaterial switchMovementInQuartersOption = this.binding.switchMovementInQuartersOption;
    settingsViewModel.getSettings().observe(getViewLifecycleOwner(), settings -> {
      if (Objects.isNull(settings)) {
        return;
      }
      switchMovementInQuartersOption.setChecked(settings.isMovementInQuarters());

    });
    //------------------------------------------------------

    final MaterialButton saveButton = this.binding.saveButton;
    settingsViewModel.getSettings().observe(getViewLifecycleOwner(), settings -> {
      if (Objects.isNull(settings)) {
        return;
      }
      saveButton.setOnClickListener(v -> {
        settingsViewModel.updateSettings(
            Objects.requireNonNull(this.binding.minutesPerDayOfWorkEditInputText.getText()).toString(),
            SettingsField.MINUTES_PER_DAY_OF_WORK);
        settingsViewModel.updateSettings(
            Objects.requireNonNull(this.binding.maximumMinutesInARowEditInputText.getText()).toString(),
            SettingsField.MAXIMUM_MINUTES_IN_A_ROW);
        settingsViewModel.updateSettings(
            Objects.requireNonNull(this.binding.minutesOfBreakBetweenRangesEditInputText.getText()).toString(),
            SettingsField.MINUTES_OF_BREAK_BETWEEN_RANGES);
        settingsViewModel.updateSettings(
            Objects.requireNonNull(this.binding.maximumHourOfTheDayToEndWorkInputEditText.getText()).toString(),
            SettingsField.MAXIMUM_HOUR_TO_WORK_IN_A_DAY
        );
        settingsViewModel.updateSettings(
            Boolean.toString(this.binding.switchMovementInQuartersOption.isChecked()),
            SettingsField.MOVEMENT_IN_QUARTERS
        );

        navigateToHomeFragment(); // Navigate to the home fragment

      });
    });

  }

  private void navigateToHomeFragment() {
    requireActivity().getSupportFragmentManager().popBackStack();
  }

  private void breaks(SettingsViewModel settingsViewModel) {
    final TextInputEditText minutesOfBreakBetweenRanges = binding.minutesOfBreakBetweenRangesEditInputText;
    settingsViewModel.getSettings().observe(getViewLifecycleOwner(), settings -> {
      if (Objects.isNull(settings)) {
        return;
      }
      minutesOfBreakBetweenRanges.setText(String.valueOf(settings.getMinutesOfBreakBetweenRanges()));

    });
    minutesOfBreakBetweenRanges.addTextChangedListener(
        getTextInputWatcher(minutesOfBreakBetweenRanges, s -> null)
    );
  }

  private void minutesPerRow(SettingsViewModel settingsViewModel) {
    final TextInputEditText hoursInARow = binding.maximumMinutesInARowEditInputText;
    settingsViewModel.getSettings().observe(getViewLifecycleOwner(), settings -> {
      if (Objects.isNull(settings)) {
        return;
      }
      hoursInARow.setText(String.valueOf(settings.getMaximumMinutesInARow()));

    });
    hoursInARow.addTextChangedListener(getTextInputWatcher(hoursInARow, s -> null));
  }

  private void minutesPerDay(SettingsViewModel settingsViewModel) {
    final TextInputEditText minutesPerDayOfWorkEditInputText = binding.minutesPerDayOfWorkEditInputText;
    settingsViewModel.getSettings().observe(getViewLifecycleOwner(), settings -> {
      if (Objects.isNull(settings)) {
        return;
      }
      minutesPerDayOfWorkEditInputText.setText(String.valueOf(settings.getMinutesPerDayOfWork()));

    });
    minutesPerDayOfWorkEditInputText
        .addTextChangedListener(getTextInputWatcher(minutesPerDayOfWorkEditInputText, new MinutesPerDayOfWorkFilter()));
  }

  private static TextInputWatcher getTextInputWatcher(
      final TextInputEditText minutesPerDayOfWorkEditInputText,
      final Function<String, String> filterFunction
  ) {
    return new TextInputWatcher(minutesPerDayOfWorkEditInputText, filterFunction);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }
}