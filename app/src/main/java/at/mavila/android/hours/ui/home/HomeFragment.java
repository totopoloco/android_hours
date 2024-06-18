package at.mavila.android.hours.ui.home;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import at.mavila.android.hours.R;
import at.mavila.android.hours.databinding.FragmentHomeBinding;
import org.apache.commons.lang3.StringUtils;

public class HomeFragment extends Fragment {

  private FragmentHomeBinding binding;

  public View onCreateView(@NonNull final LayoutInflater layoutInflater,
                           final ViewGroup viewGroup,
                           final Bundle bundle) {

    HomeViewModel homeViewModel = getHomeViewModel();
    this.binding = FragmentHomeBinding.inflate(layoutInflater, viewGroup, false);
    View root = this.binding.getRoot();
    binding.entryHour.setText(String.valueOf(homeViewModel.getEntryHour().getValue()));

    binding.entryHour.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        Log.d("HomeFragment", "beforeTextChanged");
      }

      @Override
      public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

        if (StringUtils.isBlank(charSequence)) {
          return;
        }

        try {
          int value = Integer.parseInt(charSequence.toString());

          if (value < 0 || value > 24) {
            binding.entryHour.setError(getString(R.string.invalid_value));
          }

          homeViewModel.getEntryHour().setValue(value);
        } catch (NumberFormatException e) {
          Log.e("HomeFragment", "Invalid number format", e);
        }
      }

      @Override
      public void afterTextChanged(Editable s) {
        Log.d("HomeFragment", "afterTextChanged");
      }
    });


    return root;
  }


  private HomeViewModel getHomeViewModel() {
    return new ViewModelProvider(this).get(HomeViewModel.class);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    this.binding = null;
  }
}