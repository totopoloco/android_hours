package at.mavila.android.hours.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import at.mavila.android.hours.databinding.FragmentSettingsBinding;

/**
 * This fragment is responsible for displaying the settings view.
 *
 * @author marcoavila
 * @since 10.07.2024
 */
public class SettingsFragment extends Fragment {

  private FragmentSettingsBinding binding;

  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container,
                           Bundle savedInstanceState) {

    final SettingsViewModel settingsViewModel =
        new ViewModelProvider(this).get(SettingsViewModel.class);

    binding = FragmentSettingsBinding.inflate(inflater, container, false);
    final View root = binding.getRoot();

//    final TextView textView = binding.textSettings;
//    settingsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
    return root;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }
}