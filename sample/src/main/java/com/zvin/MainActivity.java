package com.zvin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.zvin.library.Debug;
import com.zvin.library.RoundShowImage;

public class MainActivity extends AppCompatActivity {
    private RoundShowImage mRoundShowImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(Debug.DEBUG_TAG, "on MainActivity onCreate");
        mRoundShowImg = (RoundShowImage)findViewById(R.id.round_show_img);
        mRoundShowImg.setRate(2);
    }
}
