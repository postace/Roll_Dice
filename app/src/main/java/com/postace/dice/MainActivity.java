package com.postace.dice;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    // represent dice color
    private static final String RED_DICE = "red_die";
    private static final String WHITE_DICE = "white_die";

    private int[] diceResult;
    private String mCurrentDiceColor;
    private int mCurrentDiceValue;
    private ImageButton mDiceButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDiceButton = (ImageButton) findViewById(R.id.dice_image_button);

        if (savedInstanceState != null) {
            diceResult = savedInstanceState.getIntArray("dices");
            mCurrentDiceValue = savedInstanceState.getInt("diceValue");
            mCurrentDiceColor = savedInstanceState.getString("diceColor");
            // also, we need update result text table
            refreshUI();
        } else {
            mCurrentDiceValue = 1;
            mCurrentDiceColor = WHITE_DICE;
            diceResult = new int[6];
        }

        SwitchCompat diceSwitch = (SwitchCompat) findViewById(R.id.dice_color_switch);
        diceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    mCurrentDiceColor = RED_DICE;
                    updateDiceUI();
                } else {
                    mCurrentDiceColor = WHITE_DICE;
                    updateDiceUI();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_reset:
                resetResultUI();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // Saving dice roll when configuration changed
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray("dices", diceResult);
        outState.putInt("diceValue", mCurrentDiceValue);
        outState.putString("diceColor", mCurrentDiceColor);
    }

    // ************* image button click callbacks ************* //
    public void onDiceClicked(View v) {
        // random a dice value
        mCurrentDiceValue = randomNum();
        diceResult[mCurrentDiceValue - 1]++;
        updateDiceUI();
        updateResultUI(mCurrentDiceValue);
    }

    // ************* private helper method ************* //

    // random number from 1 to 6
    private int randomNum() {
        Random random = new Random();
        return random.nextInt(6) + 1;
    }

    private void updateDiceUI() {
        String diceImgName = mCurrentDiceColor + "_" + mCurrentDiceValue;
        // update dice image UI
        int resId = getResources().getIdentifier(diceImgName, "drawable", getPackageName());
        mDiceButton.setImageResource(resId);
    }

    private void updateResultUI(int diceValue) {
        String textIdString = "die_" + diceValue;

        Resources res = getResources();
        int textViewId = res.getIdentifier(textIdString, "id", getPackageName());

        TextView textResult = (TextView) findViewById(textViewId);
        textResult.setText(String.valueOf(diceResult[diceValue - 1]));
    }

    private void resetResultUI() {
        // reset result value
        Arrays.fill(diceResult, 0);

        // reset result in UI
        for (int i = 1; i <= 6; i++) {
            String idString = "die_" + i;
            int resId = getResources().getIdentifier(idString, "id", getPackageName());
            TextView tv = (TextView) findViewById(resId);
            tv.setText("0");
        }
    }

    // refresh UI after configuration change
    private void refreshUI() {
        // update result text
        for (int i = 1; i <= 6; i++) {
            String idString = "die_" + i;
            int resId = getResources().getIdentifier(idString, "id", getPackageName());
            TextView tv = (TextView) findViewById(resId);
            tv.setText(String.valueOf(diceResult[i-1]));
        }
    }
}
