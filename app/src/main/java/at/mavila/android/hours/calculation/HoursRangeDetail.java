package at.mavila.android.hours.calculation;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.With;

@Getter
@With
@Builder
@ToString
public class HoursRangeDetail implements Serializable {
  private static final long serialVersionUID = 8486317548303540969L;
  private final LocalDateTime start;
  private final LocalDateTime end;
}
