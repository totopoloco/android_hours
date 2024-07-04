package at.mavila.android.hours.calculation;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.With;

@Getter
@With
@Builder
@ToString
public class HourRoot implements Serializable {

  private static final long serialVersionUID = -663242599071611204L;

  private final List<HoursRangeMetadata> ranges;
  private final BigDecimal totalHours;
  private final String totalHoursInHHMM;
  private final String expectedLunchTimeInHHMM;
  private final String extraComment;

}
