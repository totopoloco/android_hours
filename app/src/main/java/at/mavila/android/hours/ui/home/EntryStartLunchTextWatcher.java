package at.mavila.android.hours.ui.home;

import android.text.Editable;
import android.text.TextWatcher;
import at.mavila.android.hours.databinding.FragmentHomeBinding;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EntryStartLunchTextWatcher implements TextWatcher {

  private final FragmentHomeBinding binding;
  private final HomeViewModel homeViewModel;
  private final HomeFragment homeFragment;

  @Override
  public void beforeTextChanged(CharSequence s, int start, int count, int after) {

  }

  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count) {

  }

  @Override
  public void afterTextChanged(Editable s) {

  }
}
