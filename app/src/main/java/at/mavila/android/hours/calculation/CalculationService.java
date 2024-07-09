package at.mavila.android.hours.calculation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CalculationService {

  private final int minutesPerDayOfWork;
  private final int maximumMinutesInARow;
  private final int minutesOfBreakBetweenRanges;
  private final int maximumHourToWorkInADay;

  public List<HoursRangeDetail> calculateRanges(final Integer minutesOfLunchBreak,
                                                final int entry,
                                                final int entryMinute,
                                                final LocalDateTime lunchBreakStart) {

    return RangesCalculator.rangeCalculator(

        LocalDateTime.of(LocalDate.now(), LocalTime.of(entry, entryMinute)),
        lunchBreakStart,
        this.minutesPerDayOfWork,
        TimeUtilitiesService.getMinutesOfLunchBreakParameter(minutesOfLunchBreak),
        this.maximumMinutesInARow,
        this.minutesOfBreakBetweenRanges,
        this.maximumHourToWorkInADay
    );

  }

  public HourRoot buildRoot(final List<HoursRangeDetail> ranges,
                            final long totalMinutes,
                            final long hours,
                            final long minutes,
                            final LocalTime lunchBreakStart) {

    return
        HourRoot
            .builder()
            .ranges(buildDetails(ranges))
            .totalHours(TimeUtilitiesService.convertFromMinutesToHours(totalMinutes))
            .totalHoursInHHMM(String.format(Locale.getDefault(), "%02d:%02d", hours, minutes))
            .expectedLunchTimeInHHMM(lunchBreakStart.toString())
            .extraComment("OK")
            .build();

  }

  private List<HoursRangeMetadata> buildDetails(final List<HoursRangeDetail> ranges) {
    return ranges.stream().map(range -> {
      final LocalDateTime start = range.getStart();
      final LocalDateTime end = range.getEnd();
      final String durationInHHMM = TimeUtilitiesService.getDurationFormatted(start, end);
      final BigDecimal duration =
          TimeUtilitiesService.convertFromMinutesToHours(TimeUtilitiesService.getDurationInMinutes(start, end));
      //------------------------
      return HoursRangeMetadata
          .builder()
          .hoursRangeDetail(range)
          .duration(durationInHHMM)
          .durationInHours(duration)
          .build();
    }).collect(Collectors.toList());
  }


}
