package com.example.spokenenglishappfortermpaper;

import static androidx.navigation.Navigation.findNavController;
import static androidx.navigation.ui.NavigationUI.setupActionBarWithNavController;
import static androidx.navigation.ui.NavigationUI.setupWithNavController;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.spokenenglishappfortermpaper.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    private NavController navController;

    private AppBarConfiguration appBarConfiguration;

    private SharedViewModel sharedViewModel;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        sharedViewModel.test.observe(this, test -> {
            // Perform an action with the latest item data.
        });

        mAuth = FirebaseAuth.getInstance();

        setSupportActionBar(binding.toolbar);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();

        DrawerLayout drawer = binding.main;
        NavigationView navigationView = binding.navView;
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_login, R.id.nav_custom, R.id.nav_account, R.id.nav_signup
                //, R.id.nav_text_exercise
        )
                .setOpenableLayout(drawer)
                .build();
        //NavController navController = Navigation.findNavController(this, R.id.fragmentContainerView);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        BottomNavigationView bottomNavigationView = binding.bottomNavigationView;
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            TextView userLogin = navigationView.getHeaderView(0).findViewById(R.id.user_login);
            userLogin.setText(currentUser.getEmail());
        }

        sharedViewModel.isCurrentUser.observe(this, isCurrentUser -> {
            FirebaseUser current = mAuth.getCurrentUser();
            if(current != null){
                TextView userLogin = navigationView.getHeaderView(0).findViewById(R.id.user_login);
                userLogin.setText(currentUser.getEmail());
            } else {
                TextView userLogin = navigationView.getHeaderView(0).findViewById(R.id.user_login);
                userLogin.setText("example@gmail.com");
            }
        });


    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.nav_menu, menu);
//        return true;
//    }

    @Override
    public boolean onSupportNavigateUp() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        NavController navController = navHostFragment.getNavController();
        Log.d("MyLog", "Navigation performed " + navController.toString());
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void onLogoutClick(View v){
        FirebaseAuth.getInstance().signOut();
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        navController.navigate(R.id.nav_home);
//    }
}