package at.mavila.android.hours.ui.home;

import android.util.Log;
import android.view.View;
import at.mavila.android.hours.databinding.FragmentHomeBinding;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ClickButtonListener implements View.OnClickListener {

  private final FragmentHomeBinding fragmentHomeBinding;

  @Override
  public void onClick(View v) {
    String entryHour = this.fragmentHomeBinding.entryHour.getText().toString();
    String entryLunchBreak = this.fragmentHomeBinding.entryLunchBreak.getText().toString();
    String entryStartLunch = this.fragmentHomeBinding.entryStartLunch.getText().toString();

    Log.d("HomeFragment", "Calculate button clicked. " +
                          "Entry hour: " + entryHour + ", " +
                          "Entry lunch break: " + entryLunchBreak + ", " +
                          "Entry start lunch: " + entryStartLunch);
    // Use the values for whatever processing is needed
  }
}
