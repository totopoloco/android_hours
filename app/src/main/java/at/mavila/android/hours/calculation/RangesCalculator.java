package at.mavila.android.hours.calculation;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public final class RangesCalculator {

  private RangesCalculator() {
    throw new UnsupportedOperationException("This class cannot be instantiated.");
  }

  /**
   * Calculates the ranges of hours for a worker.
   *
   * @param entry
   * @param lunchBreakStart
   * @param minutesPerDayOfWork
   * @param minutesOfLunchBreak
   * @param maximumMinutesInARow
   * @param minutesOfBreakBetweenRanges
   * @param maximumHourToWorkInADay
   * @return a list of {@link HoursRangeDetail}
   */
  public static List<HoursRangeDetail> rangeCalculator(final LocalDateTime entry,
                                                       final LocalDateTime lunchBreakStart,
                                                       final long minutesPerDayOfWork,
                                                       final long minutesOfLunchBreak,
                                                       final long maximumMinutesInARow,
                                                       final long minutesOfBreakBetweenRanges,
                                                       final LocalTime maximumHourToWorkInADay) {

    List<HoursRangeDetail> rangeDetails = new ArrayList<>();


    long minutesLeft = minutesPerDayOfWork;
    LocalDateTime start = entry;
    LocalDateTime endOfLunchBreak = lunchBreakStart.plusMinutes(minutesOfLunchBreak);
    LocalDateTime adjustedLunchBreakStart = lunchBreakStart;
    while (minutesLeft > 0) {

      LocalDateTime end = start.plusMinutes(Math.min(minutesLeft, maximumMinutesInARow));
      end = adjustEndIfNecessary(lunchBreakStart, end, start, endOfLunchBreak);

      long minutesWorked = ChronoUnit.MINUTES.between(start, end);
      minutesLeft -= minutesWorked;
      // Add the range to the list
      rangeDetails.add(HoursRangeDetail.builder().start(start).end(end).build());

      // Check if the worker has already worked for maximumMinutesInARow and adjust the lunch break start time if necessary
      if (minutesWorked >= maximumMinutesInARow && end.isBefore(adjustedLunchBreakStart)) {
        adjustedLunchBreakStart = end;
        endOfLunchBreak = adjustedLunchBreakStart.plusMinutes(minutesOfLunchBreak);
      }

      start = calculateNewStart(minutesOfBreakBetweenRanges, end, endOfLunchBreak);
    }

    if (rangeDetails
        .stream()
        .anyMatch(getHoursRangeDetailPredicate(maximumHourToWorkInADay))) {
      return Collections.emptyList();
    }

    return rangeDetails;
  }

  private static Predicate<HoursRangeDetail> getHoursRangeDetailPredicate(final LocalTime maximumHourToWorkInADay) {
    return range -> hasReachedMaximTimeToWorkForTheDay(maximumHourToWorkInADay, range);
  }

  private static boolean hasReachedMaximTimeToWorkForTheDay(final LocalTime maximumHourToWorkInADay, HoursRangeDetail range) {
    return range.getStart().toLocalTime().isAfter(maximumHourToWorkInADay);
  }


  /**
   * Adjusts the end time if necessary.
   * If the end time is after the lunch break start and the start time is before the end of the lunch break,
   * the end time is adjusted to the lunch break start.
   *
   * @param lunchBreakStart the start time of the lunch break
   * @param end             the end time of the range
   * @param start           the start time of the range
   * @param endOfLunchBreak the end time of the lunch break
   * @return the adjusted end time
   */
  private static LocalDateTime adjustEndIfNecessary(final LocalDateTime lunchBreakStart,
                                                    final LocalDateTime end,
                                                    final LocalDateTime start,
                                                    final LocalDateTime endOfLunchBreak) {
    if (end.isAfter(lunchBreakStart) && start.isBefore(endOfLunchBreak)) {
      return lunchBreakStart;
    }
    return end;
  }

  /**
   * Calculates the new start time for the next range.
   *
   * @param minutesOfBreakBetweenRanges the minutes of break between ranges
   * @param end                         the end time of the previous range
   * @param endOfLunchBreak             the end time of the lunch break
   * @return the new start time
   */
  private static LocalDateTime calculateNewStart(final long minutesOfBreakBetweenRanges,
                                                 final LocalDateTime end,
                                                 final LocalDateTime endOfLunchBreak) {
    LocalDateTime start = end.plusMinutes(minutesOfBreakBetweenRanges);
    if (start.isBefore(endOfLunchBreak)) {
      return endOfLunchBreak;
    }
    return start;
  }

}