package at.mavila.android.hours.ui.settings;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Settings.class}, version = 1, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {

  public abstract SettingsDao settingsDao();

}
