package com.example.choose.create;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.evolve.backdroplibrary.BackdropContainer;
import com.example.choose.R;
import com.example.choose.home.HomeActivity;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;

public class ChooseType extends AppCompatActivity {

    private static BackdropContainer backdropContainer;
    private static boolean fragmentSelected;

    public static Context contextOfApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_type);
        contextOfApplication = getApplicationContext();

        fragmentSelected = false;

        BottomAppBar bar = findViewById(R.id.bottomAppBar);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setSelectedItemId(R.id.profile);

        MenuItem item1 = bottomNavigationView.getMenu().findItem(R.id.home);
        MenuItem item2 = bottomNavigationView.getMenu().findItem(R.id.communities);
        MenuItem item3 = bottomNavigationView.getMenu().findItem(R.id.discover);
        MenuItem item4 = bottomNavigationView.getMenu().findItem(R.id.profile);

        item4.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent i = new Intent(ChooseType.this, HomeActivity.class);
                i.putExtra("fragment", 0);
                startActivity(i);
                return false;
            }
        });

        item3.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent i = new Intent(ChooseType.this, HomeActivity.class);
                i.putExtra("fragment", 1);
                startActivity(i);
                return false;
            }
        });

        item2.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent i = new Intent(ChooseType.this, HomeActivity.class);
                i.putExtra("fragment", 2);
                startActivity(i);
                return false;
            }
        });

        item1.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent i = new Intent(ChooseType.this, HomeActivity.class);
                i.putExtra("fragment", 3);
                startActivity(i);
                return false;
            }
        });

        backdropContainer = findViewById(R.id.backdropContainer);
        backdropContainer.attachToolbar(findViewById(R.id.toolbar))
                .dropInterpolator(new LinearInterpolator())
                .dropHeight(0)
                .build();
        backdropContainer.showBackview();

        View back = backdropContainer.getChildAt(0);
        View front = backdropContainer.getChildAt(1);

        MaterialCardView materialCardView1 = back.findViewById(R.id.card1);
        MaterialCardView materialCardView2 = back.findViewById(R.id.card2);
        MaterialCardView materialCardView3 = back.findViewById(R.id.card3);
        MaterialCardView materialCardView4 = back.findViewById(R.id.card4);
        MaterialCardView materialCardView5 = back.findViewById(R.id.card5);

        materialCardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!fragmentSelected) {
                    backdropContainer.closeBackview();
                    displayView(0);
                    fragmentSelected = true;
                }
            }
        });

        materialCardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!fragmentSelected) {
                    backdropContainer.closeBackview();
                    displayView(1);
                    fragmentSelected = true;
                }
            }
        });

        materialCardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!fragmentSelected) {
                    backdropContainer.closeBackview();
                    displayView(2);
                    fragmentSelected = true;
                }
            }
        });

        materialCardView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!fragmentSelected) {
                    backdropContainer.closeBackview();
                    displayView(3);
                    fragmentSelected = true;
                }
            }
        });

        materialCardView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!fragmentSelected) {
                    backdropContainer.closeBackview();
                    displayView(4);
                    fragmentSelected = true;
                }
            }
        });
    }

    public static Context getContextOfApplication() {
        return contextOfApplication;
    }

    public static void close(){
        fragmentSelected = false;
        backdropContainer.showBackview();
    }

    public void showFragment(Fragment fragment, int position) {
        FragmentTransaction mTransaction = getSupportFragmentManager().beginTransaction();
        mTransaction.replace(R.id.main_container, fragment, fragment.getClass().getName());
        mTransaction.commit();
    }

    public void displayView(int position) {
        switch (position) {
            case 0:
                showFragment(new TextPostFragment(), position);
                break;
            case 1:
                showFragment(new ImagePostFragment(), position);
                break;
            case 2:
                showFragment(new VotingPostFragment(), position);
                break;
            case 3:
                showFragment(new PlayOffPostFragment(), position);
                break;
            case 4:
                showFragment(new PetitionPostFragment(), position);
                break;
        }
    }
}