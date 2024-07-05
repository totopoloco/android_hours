package at.mavila.android.hours.ui.home;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import at.mavila.android.hours.R;
import at.mavila.android.hours.calculation.CalculationService;
import at.mavila.android.hours.calculation.HourRoot;
import at.mavila.android.hours.calculation.HoursRangeDetail;
import at.mavila.android.hours.calculation.HoursRangeMetadata;
import at.mavila.android.hours.calculation.TimeUtilitiesService;
import at.mavila.android.hours.databinding.FragmentHomeBinding;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ClickButtonListener implements View.OnClickListener {

  private final FragmentHomeBinding fragmentHomeBinding;


  private final static int MAXIMUM_MINUTES_IN_A_ROW;
  private final static int MINUTES_OF_BREAK_BETWEEN_RANGES;
  private final static int MAXIMUM_HOUR_TO_WORK_IN_A_DAY;
  private final static int MINUTES_PER_DAY_OF_WORK;

  //TODO: We need to get the following values from the settings of the application that will be implemented.
  static {
    MINUTES_PER_DAY_OF_WORK = 462;
    MAXIMUM_MINUTES_IN_A_ROW = 240;
    MINUTES_OF_BREAK_BETWEEN_RANGES = 30;
    MAXIMUM_HOUR_TO_WORK_IN_A_DAY = 20;
  }


  @Override
  public void onClick(View v) {

    final Context context = v.getContext();

    String entryHour = Objects.requireNonNull(this.fragmentHomeBinding.entryHour.getText()).toString();
    String entryLunchBreak = Objects.requireNonNull(this.fragmentHomeBinding.entryLunchBreak.getText()).toString();
    String entryStartLunch = Objects.requireNonNull(this.fragmentHomeBinding.entryStartLunch.getText()).toString();

    Log.d("HomeFragment", "Calculate button clicked. " +
                          "Entry hour: " + entryHour + ", " +
                          "Entry lunch break: " + entryLunchBreak + ", " +
                          "Entry start lunch: " + entryStartLunch);

    final CalculationService calculationService = new CalculationService(
        MINUTES_PER_DAY_OF_WORK,
        MAXIMUM_MINUTES_IN_A_ROW,
        MINUTES_OF_BREAK_BETWEEN_RANGES,
        MAXIMUM_HOUR_TO_WORK_IN_A_DAY
    );

    final LocalTime lunchStart = LocalTime.of(Integer.parseInt(entryStartLunch), TimeUtilitiesService.randomizeMinuteInHour());
    final List<HoursRangeDetail> rangeDetails = getHoursRangeDetails(calculationService, entryLunchBreak, entryHour, lunchStart);

    final long totalMinutes = TimeUtilitiesService.getTotalMinutes(rangeDetails);
    final long hours = totalMinutes / 60;   // since both are ints, you get an int
    final long minutes = totalMinutes % 60;

    final HourRoot hourRoot = calculationService.buildRoot(rangeDetails, totalMinutes, hours, minutes, lunchStart);

    Log.d("HomeFragment", "HourRoot: " + hourRoot.toString());

    // Table results
    // ----------------------------------
    // Get the table layout
    TableLayout tableLayout = v.getRootView().findViewById(R.id.resultTable);
    // Clear the table
    tableLayout.removeAllViews();
    //Add headers to the table

    addHeadersToTable(context, tableLayout);
    // Add the rows to the table
    hourRoot.getRanges().forEach(range -> addToRow(v, range, tableLayout));
    // ----------------------------------
    // End of table results

    // Display the summary in the text view for this purpose
    displaySummary(v, context, hourRoot);
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
                                                             final LocalTime lunchStart) {
    return calculationService.calculateRanges(
        Integer.parseInt(entryLunchBreak),
        Integer.parseInt(entryHour),
        TimeUtilitiesService.randomizeMinuteInHour(),
        LocalDateTime.of(LocalDate.now(), lunchStart));
  }
}