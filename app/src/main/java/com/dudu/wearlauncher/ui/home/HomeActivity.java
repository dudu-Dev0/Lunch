package com.dudu.wearlauncher.ui.home;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import com.dudu.wearlauncher.R;
import com.dudu.wearlauncher.ui.ViewPagerFragmentAdapter;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    ViewPager homeViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        
        homeViewPager = findViewById(R.id.home_pager);
        List<Fragment> fragmentList = List.of();
        ViewPagerFragmentAdapter adapter = new ViewPagerFragmentAdapter(getSupportFragmentManager(),fragmentList);
        homeViewPager.setAdapter(adapter);
        
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
