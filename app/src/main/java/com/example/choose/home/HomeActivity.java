package com.example.choose.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.choose.create.ChooseType;
import com.example.choose.R;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeActivity extends AppCompatActivity {

    MenuItem item1;
    MenuItem item2;
    MenuItem item3;
    MenuItem item4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setSelectedItemId(R.id.profile);

        item1 = bottomNavigationView.getMenu().findItem(R.id.home);
        item2 = bottomNavigationView.getMenu().findItem(R.id.communities);
        item3 = bottomNavigationView.getMenu().findItem(R.id.discover);
        item4 = bottomNavigationView.getMenu().findItem(R.id.profile);

        MenuItem empty = bottomNavigationView.getMenu().findItem(R.id.itemEmpty);
        empty.setEnabled(false);

        FloatingActionButton btn2 = findViewById(R.id.fab_start);
        btn2.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, ChooseType.class)));

        Bundle extras = getIntent().getExtras();

        switchFragment(item4,0);
        switchFragment(item3,1);
        switchFragment(item2,2);
        switchFragment(item1,3);

        if(extras != null) {
            switch (extras.getInt("fragment")) {
                case 0:
                    displayView(0);
                    break;
                case 1:
                    displayView(1);
                    break;
                case 2:
                    displayView(2);
                    break;
                case 3:
                    displayView(3);
                    break;
            }
        }else{
            displayView(0);
        }
    }

    public void showFragment(Fragment fragment, int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_container, fragment, fragment.getClass().getName());
        transaction.commit();
    }

    public void displayView(int position) {
        switch (position) {
            case 0:
                showFragment(new PersonalPageFragment(), position);
                item4.setChecked(true);
                break;
            case 1:
                showFragment(new DiscoverFragment(), position);
                item3.setChecked(true);
                break;
            case 2:
                showFragment(new CommunitiesFragment(), position);
                item2.setChecked(true);
                break;
            case 3:
                showFragment(new FeedFragment(), position);
                item1.setChecked(true);
                break;
        }
    }

    public void switchFragment(MenuItem item ,int position) {
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                displayView(position);
                return false;
            }
        });
    }
}