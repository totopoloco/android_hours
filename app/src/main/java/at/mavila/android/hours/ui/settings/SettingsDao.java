package at.mavila.android.hours.ui.settings;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface SettingsDao {

  @Insert
  void insert(Settings settings);

  @Update
  void update(Settings settings);

  @Query("SELECT * FROM settings LIMIT 1")
  LiveData<Settings> getAllSettings();

  @Query("select count(*) from settings")
  int countSettings();

}
