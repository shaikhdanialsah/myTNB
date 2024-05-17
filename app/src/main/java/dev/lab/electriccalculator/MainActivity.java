package dev.lab.electriccalculator;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity{
    CardView card2,card3;
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        card2=findViewById(R.id.card2);
        card3=findViewById(R.id.buttonGithub);

        card2.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,ElectricityCalculator.class);
            startActivity(intent);

        });

        card3.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,InstructionPage.class);
            startActivity(intent);
        });

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

         drawerLayout = findViewById(R.id.my_drawer_layout);
         actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
         drawerLayout.addDrawerListener(actionBarDrawerToggle);
         actionBarDrawerToggle.syncState();
         getSupportActionBar().setDisplayHomeAsUpEnabled(true);

         //Navigation view
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();

                if (itemId == R.id.menuAbout) {
                    Intent intent = new Intent(MainActivity.this, AboutPage.class);
                    startActivity(intent);
                    return true; // Indicates that the event has been handled
                }
                else if(itemId == R.id.menuShare)
                {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Check out my awesome app!");
                    intent.putExtra(Intent.EXTRA_TEXT, "Hey there! I've developed this cool app. Check it out!");
                    intent.putExtra(Intent.EXTRA_TEXT, "https://github.com/shaikhdanialsah/ICT602");
                    startActivity(Intent.createChooser(intent, "Share via"));
                    return true;
                }
                return false;
            }
        });

    }

    //For drawable navigation menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // If the item is handled by the Action Bar Drawer Toggle, return true.
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    //When user click on back button
    public void onBackPressed() {
        // Create a confirmation dialog
        new AlertDialog.Builder(this)
                .setTitle("Exit App")
                .setMessage("Are you sure you want to exit this app?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();

    }
}