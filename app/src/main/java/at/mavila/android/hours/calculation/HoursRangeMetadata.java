package at.mavila.android.hours.calculation;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.With;

@Getter
@With
@Builder
@ToString
public class HoursRangeMetadata implements Serializable {
  private static final long serialVersionUID = 2520790584457236249L;
  private final HoursRangeDetail hoursRangeDetail;
  private final String duration;
  private final BigDecimal durationInHours;
}
