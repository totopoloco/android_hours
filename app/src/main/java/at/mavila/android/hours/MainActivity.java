package at.mavila.android.hours;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import at.mavila.android.hours.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

      //Call the super class onCreate to complete the creation of activity like
        super.onCreate(savedInstanceState);

      // Inflate the layout using the view binding and set the content view
        final ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(getToolbar(binding));


      // Set the floating action button to show a snack bar
        binding.appBarMain.fab.setOnClickListener(view -> getSnackbar(view).show());
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        this.mAppBarConfiguration = getAppBarConfiguration(binding.drawerLayout);
        createView(binding.navView);
    }

    private static Toolbar getToolbar(final ActivityMainBinding binding) {
        return binding.appBarMain.toolbar;
    }

  private static Snackbar getSnackbar(final View view) {
        return Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(R.id.fab);
    }

    private static AppBarConfiguration getAppBarConfiguration(DrawerLayout drawer) {
        return new AppBarConfiguration.Builder(
            R.id.nav_home, R.id.nav_gallery, R.id.nav_settings)
                .setOpenableLayout(drawer)
                .build();
    }

    private void createView(NavigationView navigationView) {
        NavController navController = getNavController();
      NavigationUI.setupActionBarWithNavController(this, navController, this.mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {

      if (NavigationUI.navigateUp(getNavController(), this.mAppBarConfiguration)) {
            return true;
        }

        return super.onSupportNavigateUp();
    }

    private NavController getNavController() {
        return Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
    }
}