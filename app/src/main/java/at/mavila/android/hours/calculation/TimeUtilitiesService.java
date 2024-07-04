package at.mavila.android.hours.calculation;

import static java.util.List.of;
import static java.util.Objects.nonNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public final class TimeUtilitiesService {

  private static final List<Integer> QUARTERS;
  private static final ThreadLocal<Random> RANDOM;

  static {
    QUARTERS = of(0, 15, 30, 45);
    RANDOM = ThreadLocal.withInitial(Random::new);
  }

  private TimeUtilitiesService() {
    throw new UnsupportedOperationException("This class cannot be instantiated.");
  }

  /**
   * Randomizes between 0 and 59 in quarters.
   *
   * @return the randomized minute in the hour, between 0 and 59
   */
  public static int randomizeMinuteInHour() {
    return QUARTERS.get(RANDOM.get().nextInt(QUARTERS.size()));
  }

  public static Integer getMinutesOfLunchBreakParameter(Integer minutesOfLunchBreak) {
    return nonNull(minutesOfLunchBreak) ? minutesOfLunchBreak : 30;
  }

  public static long getTotalMinutes(List<HoursRangeDetail> ranges) {
    return ranges.stream().mapToLong(range -> ChronoUnit.MINUTES.between(range.getStart(), range.getEnd())).sum();
  }

  /**
   * Calculates the duration in minutes between two LocalTime objects.
   *
   * @param start the start time
   * @param end   the end time
   * @return the duration in minutes
   */
  public static long getDurationInMinutes(LocalDateTime start, LocalDateTime end) {
    //Validate the input for null values
    if (Objects.isNull(start)) {
      throw new IllegalArgumentException("The start time must not be null.");
    }
    if (Objects.isNull(end)) {
      throw new IllegalArgumentException("The end time must not be null.");
    }
    return ChronoUnit.MINUTES.between(start, end);
  }

  /**
   * Formats the duration between two LocalTime objects in the format HH:mm.
   *
   * @param start the start time
   * @param end   the end time
   * @return the formatted duration
   */
  public static String getDurationFormatted(LocalDateTime start, LocalDateTime end) {
    long duration = getDurationInMinutes(start, end);
    long hours = duration / 60;
    long minutes = duration % 60;
    return String.format("%02d:%02d", hours, minutes);
  }

  /**
   * Converts minutes to hours.
   *
   * @param minutes the minutes to convert
   * @return the hours
   */
  public static BigDecimal convertFromMinutesToHours(long minutes) {
    return BigDecimal.valueOf(minutes).divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);
  }
}