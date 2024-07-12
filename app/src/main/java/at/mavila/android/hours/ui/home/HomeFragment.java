package at.mavila.android.hours.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import at.mavila.android.hours.databinding.FragmentHomeBinding;
import at.mavila.android.hours.ui.settings.SettingsRepository;
import at.mavila.android.hours.ui.settings.SettingsViewModel;
import at.mavila.android.hours.ui.settings.SettingsViewModelFactory;

public class HomeFragment extends Fragment {

  private FragmentHomeBinding binding;

  public View onCreateView(@NonNull final LayoutInflater layoutInflater,
                           final ViewGroup viewGroup,
                           final Bundle bundle) {
    HomeViewModel homeViewModel = getHomeViewModel();
    this.binding = FragmentHomeBinding.inflate(layoutInflater, viewGroup, false);
    initializeFields(homeViewModel);
    return this.binding.getRoot();
  }

  private void initializeFields(HomeViewModel homeViewModel) {
    binding.entryHour.setText(String.valueOf(homeViewModel.getEntryHour().getValue()));
    binding.entryHour.addTextChangedListener(new EntryHourTextWatcher(this.binding, homeViewModel, this));

    binding.entryLunchBreak.setText(String.valueOf(homeViewModel.getBreakTime().getValue()));
    binding.entryLunchBreak.addTextChangedListener(new EntryLunchBreakTextWatcher(this.binding, homeViewModel, this));

    binding.entryStartLunch.setText(String.valueOf(homeViewModel.getLunchHour().getValue()));
    binding.entryStartLunch.addTextChangedListener(new EntryStartLunchTextWatcher(this.binding, homeViewModel, this));

    binding.calculateButton.setOnClickListener(new ClickButtonListener(this.binding, getSettingsViewModel()));
  }

  private SettingsViewModel getSettingsViewModel() {
    return
        new ViewModelProvider(
            this,
            new SettingsViewModelFactory(new SettingsRepository(requireContext())))
            .get(SettingsViewModel.class);
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