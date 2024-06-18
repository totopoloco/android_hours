package at.mavila.android.hours.ui.home;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import lombok.Getter;

@Getter
public class HomeViewModel extends ViewModel {
  private final MutableLiveData<Integer> entryHour = new MutableLiveData<>(9);
  private final MutableLiveData<Integer> lunchHour = new MutableLiveData<>(13);
  private final MutableLiveData<Integer> breakTime = new MutableLiveData<>(30);
}