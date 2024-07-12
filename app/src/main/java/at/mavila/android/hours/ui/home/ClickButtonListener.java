package at.mavila.android.hours.ui.home;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.constraintlayout.widget.Guideline;
import at.mavila.android.hours.R;
import at.mavila.android.hours.calculation.CalculationService;
import at.mavila.android.hours.calculation.HourRoot;
import at.mavila.android.hours.calculation.HoursRangeDetail;
import at.mavila.android.hours.calculation.HoursRangeMetadata;
import at.mavila.android.hours.calculation.TimeUtilitiesService;
import at.mavila.android.hours.databinding.FragmentHomeBinding;
import at.mavila.android.hours.ui.settings.SettingsViewModel;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

@AllArgsConstructor
public class ClickButtonListener implements View.OnClickListener {

  private final FragmentHomeBinding fragmentHomeBinding;
  private final SettingsViewModel settingsViewModel;


  private final static int MAXIMUM_MINUTES_IN_A_ROW;
  private final static int MINUTES_OF_BREAK_BETWEEN_RANGES;
  private final static int MAXIMUM_HOUR_TO_WORK_IN_A_DAY;
  /**
   * If true, the movement of the time will be in quarters, otherwise every 5 minutes.
   */
  private final static boolean MOVEMENT_IN_QUARTERS;

  //TODO: We need to get the following values from the settings of the application that will be implemented.
  static {
    MAXIMUM_MINUTES_IN_A_ROW = 240;
    MINUTES_OF_BREAK_BETWEEN_RANGES = 30;
    MAXIMUM_HOUR_TO_WORK_IN_A_DAY = 20;
    MOVEMENT_IN_QUARTERS = true;
  }


  @Override
  public void onClick(final View v) {

    this.settingsViewModel.getSettings().observeForever(settings -> {

      final Context context = v.getContext();
      final String entryHour = Objects.requireNonNull(this.fragmentHomeBinding.entryHour.getText()).toString();
      final String entryLunchBreak = Objects.requireNonNull(this.fragmentHomeBinding.entryLunchBreak.getText()).toString();
      final String entryStartLunch = Objects.requireNonNull(this.fragmentHomeBinding.entryStartLunch.getText()).toString();


      Log.d("HomeFragment", "Calculate button clicked. " +
                            "Entry hour: " + entryHour + ", " +
                            "Entry lunch break: " + entryLunchBreak + ", " +
                            "Entry start lunch: " + entryStartLunch);

      int minutesPerDayOfWork = settings.getMinutesPerDayOfWork();

      Log.d("HomeFragment", "Minutes per day of work: " + minutesPerDayOfWork);

      final CalculationService calculationService = new CalculationService(
          minutesPerDayOfWork,
          MAXIMUM_MINUTES_IN_A_ROW,
          MINUTES_OF_BREAK_BETWEEN_RANGES,
          MAXIMUM_HOUR_TO_WORK_IN_A_DAY
      );
      final LocalTime lunchStart = getLunchStart(entryStartLunch);

      final List<HoursRangeDetail> rangeDetails =
          getHoursRangeDetails(calculationService, entryLunchBreak, entryHour, lunchStart, MOVEMENT_IN_QUARTERS);

      final long totalMinutes = TimeUtilitiesService.getTotalMinutes(rangeDetails);
      final long hours = totalMinutes / 60;   // since both are ints, you get an int
      final long minutes = totalMinutes % 60;

      final HourRoot hourRoot = calculationService.buildRoot(rangeDetails, totalMinutes, hours, minutes, lunchStart);

      Log.d("HomeFragment", "HourRoot: " + hourRoot.toString());

      // Get the table layout
      TableLayout tableLayout = v.getRootView().findViewById(R.id.resultTable);
      // Clear the table
      tableLayout.removeAllViews();
      // If there is the key pad, hide it
      hideKeyPadIfPresent(v);
      //----------------------------------
      //Add headers to the table
      addHeadersToTable(context, tableLayout);
      // Add the rows to the table
      List<HoursRangeMetadata> ranges = hourRoot.getRanges();

      //Some defense programming (ranges should be empty at most).
      // Validate the ranges for null or empty
      if (CollectionUtils.isEmpty(ranges)) {
        return;
      }

      //Adjust the guideline percentage
      setGuidelinePercentage(v, ranges.size());

      //Populate the table with the results
      ranges.forEach(range -> addToRow(v, range, tableLayout));
      // ----------------------------------
      // End of table results

      // Display the summary in the text view for this purpose
      displaySummary(v, context, hourRoot);


    });


  }

  private static LocalTime getLunchStart(String entryStartLunch) {

    if (MOVEMENT_IN_QUARTERS) {
      return
          LocalTime.of(Integer.parseInt(entryStartLunch), TimeUtilitiesService.randomizeMinuteInHourInQuarters());
    }

    return
        LocalTime.of(Integer.parseInt(entryStartLunch), TimeUtilitiesService.randomizeMinuteInHourForEveryFiveMinutes());

  }

  private static void hideKeyPadIfPresent(View v) {
    Object systemService = v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

    if (!(systemService instanceof InputMethodManager)) {
      return;
    }

    if (Objects.isNull(v.getWindowToken())) {
      return;
    }

    ((InputMethodManager) systemService)
        .hideSoftInputFromWindow
            (
                v.getWindowToken(),
                InputMethodManager.RESULT_UNCHANGED_SHOWN
            );
  }

  private static void setGuidelinePercentage(View v, int rangesSize) {
    Guideline guideline = v.getRootView().findViewById(R.id.guideline);

    if (rangesSize > 2) {
      guideline.setGuidelinePercent(0.7f);
      return;
    }
    guideline.setGuidelinePercent(0.65f);
  }

  private static void displaySummary(View v, Context context, HourRoot hourRoot) {
    String hoursLabel = context.getResources().getString(R.string.Total_Hours);
    String hoursHHMMLabel = context.getResources().getString(R.string.Total_hours_in_hh_mm);
    String lunchTimeLabel = context.getResources().getString(R.string.Expected_lunch_time_in_hh_mm);

    // Format the summary text and display it
    TextView summaryTextView = v.getRootView().findViewById(R.id.summaryTextView);
    summaryTextView.setText(getSummaryText(hoursLabel, hourRoot, hoursHHMMLabel, lunchTimeLabel));
    summaryTextView.setVisibility(View.VISIBLE);
  }

  private static String getSummaryText(String hoursLabel, HourRoot hourRoot, String hoursHHMMLabel, String lunchTimeLabel) {
    return String.format(Locale.getDefault(),
        "%s: %.2f\n%s: %s\n%s: %s",
        hoursLabel, hourRoot.getTotalHours(),
        hoursHHMMLabel, hourRoot.getTotalHoursInHHMM(),
        lunchTimeLabel, hourRoot.getExpectedLunchTimeInHHMM());
  }

  private void addToRow(View v, HoursRangeMetadata range, TableLayout tableLayout) {
    final HoursRangeDetail hoursRangeDetail = range.getHoursRangeDetail();
    final String start = hoursRangeDetail.getStart().toLocalTime().toString();
    final String end = hoursRangeDetail.getEnd().toLocalTime().toString();

    addRowToTable(v.getContext(), tableLayout, start, end, range.getDuration(), range.getDurationInHours().toString());
  }

  private void addRowToTable(Context context, TableLayout table, String start, String end, String duration,
                             String durationInHours) {
    // Create a new table row.
    TableRow row = new TableRow(context);

    // Create a TextView for each column
    TextView startView = new TextView(context);
    startView.setText(start);
    setTextSize(startView);
    row.addView(startView);

    TextView endView = new TextView(context);
    endView.setText(end);
    setTextSize(endView);
    row.addView(endView);

    TextView durationView = new TextView(context);
    durationView.setText(duration);
    setTextSize(durationView);
    row.addView(durationView);

    TextView durationInHoursView = new TextView(context);
    durationInHoursView.setText(durationInHours);
    setTextSize(durationInHoursView);
    row.addView(durationInHoursView);

    // Add the TableRow to the TableLayout
    table.addView(row);
  }

  private static void setTextSize(final TextView textView) {
    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
  }

  private void addHeadersToTable(Context context, TableLayout table) {
    // Create a new table row.
    TableRow row = new TableRow(context);

    // Create a TextView for each column
    TextView startView = new TextView(context);
    startView.setText(R.string.Start);
    row.addView(startView);

    TextView endView = new TextView(context);
    endView.setText(R.string.End);
    row.addView(endView);

    TextView durationView = new TextView(context);
    durationView.setText(R.string.Duration);
    row.addView(durationView);

    TextView durationInHoursView = new TextView(context);
    durationInHoursView.setText(R.string.Duration_Hours);
    row.addView(durationInHoursView);

    // Add the TableRow to the TableLayout
    table.addView(row);
  }

  private static List<HoursRangeDetail> getHoursRangeDetails(final CalculationService calculationService,
                                                             final String entryLunchBreak,
                                                             final String entryHour,
                                                             final LocalTime lunchStart,
                                                             final boolean movementInQuarters /*TODO: Must come from properties*/) {


    return calculationService.calculateRanges(
        Integer.parseInt(entryLunchBreak),
        Integer.parseInt(entryHour),
        movementInQuarters ? TimeUtilitiesService.randomizeMinuteInHourInQuarters() :
            TimeUtilitiesService.randomizeMinuteInHourForEveryFiveMinutes(),
        LocalDateTime.of(LocalDate.now(), lunchStart));
  }
}