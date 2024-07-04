package at.mavila.android.hours.calculation;

import static org.junit.Assert.assertEquals;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class HoursRangeDetailTest {

  @Test
  public void testAndUsageOfBuilder() {
    HoursRangeDetail hoursRangeDetail =
        HoursRangeDetail
            .builder()
            .start(LocalDateTime.now())
            .end(LocalDateTime.now().minusHours(10))
            .build();

    assertEquals(10, hoursRangeDetail.getStart().until(hoursRangeDetail.getEnd(), ChronoUnit.HOURS));
  }

}