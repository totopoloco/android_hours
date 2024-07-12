package at.mavila.android.hours.ui.settings;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Settings {

  @PrimaryKey(autoGenerate = true)
  public int id;

  public int minutesPerDayOfWork;
  public int maximumMinutesInARow;
  public int minutesOfBreakBetweenRanges;
  public String maximumHourToWorkInADay; //in HH format
  public boolean movementInQuarters;

}