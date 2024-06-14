package com.dudu.wearlauncher;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.dudu.wearlauncher.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
