package at.mavila.android.hours.ui.settings;

/**
 * Interface for listening to changes in settings.
 *
 * @author marcoavila
 */
@FunctionalInterface
public interface OnSettingsChangeListener {
  /**
   * Called when a setting has changed.
   *
   * @param newValue the new value of the setting
   */
  void onSettingsChanged(final String newValue);
}
