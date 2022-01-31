package com.example.choose.registration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.choose.R;
import com.example.choose.retrofit.RetrofitUtils;
import com.example.choose.api.LoginController;
import com.example.choose.login.LoginActivity;
import com.google.android.material.tabs.TabLayout;
import com.rd.PageIndicatorView;

import java.util.ArrayList;
import java.util.List;

public class Registration extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    FirstStepFragment firstStepFragment;
    SecondStepFragment secondStepFragment;
    ThirdStepFragment thirdStepFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        PageIndicatorView pageIndicatorView = findViewById(R.id.pageIndicatorView);
        pageIndicatorView.setCount(3);
        pageIndicatorView.setSelection(0);

        firstStepFragment = new FirstStepFragment();
        secondStepFragment = new SecondStepFragment();
        thirdStepFragment = new ThirdStepFragment();

        Button btn = (Button)findViewById(R.id.next_one_btn);
        TextView txt = (TextView)findViewById(R.id.login_btn);

        LoginController controller = RetrofitUtils.getInstance().getRetrofit().create(LoginController.class);
        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Registration.this, LoginActivity.class));
                RetrofitUtils.getInstance().deleteAccountManager(AccountManager.get(Registration.this));
            }
        });

        btn.setOnClickListener(v -> {
            if(viewPager.getCurrentItem() == 0){
                if(!firstStepFragment.isComplete()) return;
                firstStepFragment.send(viewPager);
            }else if(viewPager.getCurrentItem() == 1){
                if(!secondStepFragment.isComplete()) return;
                secondStepFragment.send(viewPager, btn);
            }else if(viewPager.getCurrentItem() == 2){
                if(!thirdStepFragment.isComplete()) return;
                thirdStepFragment.send(viewPager,
                        controller,
                        firstStepFragment.getUsername(),
                        firstStepFragment.getPassword(),
                        Registration.this);
            }
        });

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        pageIndicatorView.setViewPager(viewPager);
        setupViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {/*empty*/}

            @Override
            public void onPageSelected(int position) {
                pageIndicatorView.setSelection(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {/*empty*/}
        });

        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        Registration.ViewPagerAdapter adapter = new Registration.ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(firstStepFragment, "Uno");
        adapter.addFragment(secondStepFragment, "Dos");
        adapter.addFragment(thirdStepFragment, "Tres");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}