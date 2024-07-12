package at.mavila.android.hours.ui.settings;

import android.util.Log;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.Objects;

public class SettingsViewModel extends ViewModel {

  private final MutableLiveData<String> mText;
  private final SettingsRepository settingsRepository;
  private final MutableLiveData<Settings> mutableLiveData = new MutableLiveData<>();

  public SettingsViewModel(final SettingsRepository settingsRepository) {
    this.mText = new MutableLiveData<>();
    this.mText.setValue("This is settings fragment");
    this.settingsRepository = settingsRepository;
  }

  public LiveData<String> getText() {
    return this.mText;
  }

  public LiveData<Settings> getSettings() {
    return this.settingsRepository.getSettings();
  }

  public void observeSettingsAndUpdate(final LifecycleOwner lifecycleOwner) {

    this.settingsRepository.getSettings().observe(lifecycleOwner, settings -> {
      if (Objects.isNull(settings)) {
        return;
      }
      this.mutableLiveData.setValue(settings);
    });

  }

  public void updateSettings(final String newValue, final SettingsField settingsField) {

    try {
      Settings settings = this.mutableLiveData.getValue();
      if (Objects.isNull(settings)) {
        Log.w("SettingsViewModel", "Settings are null");
        return;
      }
      final int parsedInt = getParsedInt(newValue);
      switch (settingsField) {
        case MINUTES_PER_DAY_OF_WORK:
          minutesPerDayOfWork(parsedInt, settings);
          break;
        case MAXIMUM_MINUTES_IN_A_ROW:
          settings.setMaximumMinutesInARow(parsedInt);
          break;
        case MINUTES_OF_BREAK_BETWEEN_RANGES:
          breakBetweenRanges(parsedInt, settings);
          break;
        case MAXIMUM_HOUR_TO_WORK_IN_A_DAY:
          settings.setMaximumHourToWorkInADay(newValue);
          break;
        case MOVEMENT_IN_QUARTERS:
          settings.setMovementInQuarters(Boolean.parseBoolean(newValue));
          break;
      }
      this.settingsRepository.updateSettings(settings);
    } catch (Exception exception) {
      Log.w("SettingsViewModel", "Error updating settings", exception);
    }
  }

  private static void breakBetweenRanges(int parsedInt, Settings settings) {
    if (parsedInt < 0) {
      settings.setMinutesOfBreakBetweenRanges(0);
      return;
    }
    if (parsedInt > 60) {
      settings.setMinutesOfBreakBetweenRanges(60);
      return;
    }
    settings.setMinutesOfBreakBetweenRanges(parsedInt);
  }

  private static void minutesPerDayOfWork(int parsedInt, Settings settings) {
    if (parsedInt < 1) {
      settings.setMinutesPerDayOfWork(1);
      return;
    }
    if (parsedInt > 1440) {
      settings.setMinutesPerDayOfWork(1440);
      return;
    }
    settings.setMinutesPerDayOfWork(parsedInt);
  }

  private static int getParsedInt(String newValue) {
    try {
      return Integer.parseInt(newValue);
    } catch (Exception exception) {
      return 0;
    }
  }

}