package at.mavila.android.hours.ui.home;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import androidx.fragment.app.Fragment;
import at.mavila.android.hours.R;
import at.mavila.android.hours.databinding.FragmentHomeBinding;
import org.apache.commons.lang3.StringUtils;

public class EntryHourTextWatcher implements TextWatcher {

  private final FragmentHomeBinding binding;
  private final HomeViewModel homeViewModel;
  private final Fragment fragment;

  public EntryHourTextWatcher(final FragmentHomeBinding binding, final HomeViewModel homeViewModel, final Fragment fragment) {
    this.binding = binding;
    this.homeViewModel = homeViewModel;
    this.fragment = fragment;
  }

  @Override
  public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    Log.d("HomeFragment", "beforeTextChanged");
  }

  @Override
  public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

    if (isInvalidInput(charSequence)) {
      return;
    }

    setText(charSequence);
  }

  @Override
  public void afterTextChanged(Editable s) {
    Log.d("HomeFragment", "afterTextChanged");
  }


  //HELPER METHODS
  private void setText(final CharSequence charSequence) {
    try {
      int value = Integer.parseInt(charSequence.toString());

      if (isValueNotInRange(value)) {
        setErrorMessage();
        return;
      }

      homeViewModel.getEntryHour().setValue(value);
    } catch (NumberFormatException e) {
      Log.e("HomeFragment", "Invalid number format", e);
    }
  }

  private boolean isValueNotInRange(int value) {
    return value < 0 || value > 23;
  }

  private void setErrorMessage() {
    binding.entryHour.setError(this.fragment.getString(R.string.invalid_value));
  }

  private static boolean isInvalidInput(CharSequence charSequence) {
    return StringUtils.isBlank(charSequence);
  }
  //END HELPER METHODS


}
