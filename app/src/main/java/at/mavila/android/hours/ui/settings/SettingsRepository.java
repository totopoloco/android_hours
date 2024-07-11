package at.mavila.android.hours.ui.settings;

import android.content.Context;
import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.room.Room;
import java.util.concurrent.Executors;

public class SettingsRepository {

  private final AppDataBase appDataBase;
  private final SettingsDao settingsDao;

  public SettingsRepository(final Context context) {
    this.appDataBase = Room.databaseBuilder(context, AppDataBase.class, "settings-database").build();
    this.settingsDao = this.appDataBase.settingsDao();
  }


  public LiveData<Settings> getSettings() {
    LiveData<Settings> allSettings = this.settingsDao.getAllSettings();
    Settings value = allSettings.getValue();
    Log.d("SettingsRepository", "getSettings: " + value);
    return allSettings;
  }

  public void insertSettings(final Settings settings) {
    Executors.newSingleThreadExecutor().execute(() -> this.settingsDao.insert(settings));
  }

  public void updateSettings(final Settings settings) {
    Executors.newSingleThreadExecutor().execute(() -> this.settingsDao.update(settings));
  }

  public void initializeDefaultSettingsIfNeeded() {
    Executors.newSingleThreadExecutor().execute(() -> {
      if (this.settingsDao.countSettings() == 0) {
        // No settings exist, insert default settings
        Settings defaultSettings = new Settings();
        defaultSettings.setMinutesPerDayOfWork(462);
        defaultSettings.setMaximumMinutesInARow(240);
        defaultSettings.setMinutesOfBreakBetweenRanges(30);
        defaultSettings.setMaximumHourToWorkInADay(20);
        defaultSettings.setMovementInQuarters(true);
        insertSettings(defaultSettings);
      }
    });
  }
}