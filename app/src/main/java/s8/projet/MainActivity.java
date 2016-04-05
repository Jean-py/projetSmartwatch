package s8.projet;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends WearableActivity {

    private static final SimpleDateFormat AMBIENT_DATE_FORMAT =
            new SimpleDateFormat("HH:mm", Locale.US);

    private BoxInsetLayout mContainerView;
    private TextView mTextView;
    private TextView mClockView;
    private Button buttonYes;
    private Button buttonNo;
    private TextView areUOk;
    private Button buttonTapingGesture1;
    private Button buttonTapingGesture2;

    private boolean finger1 = false;
    private boolean finger2 = false;
    private boolean fingerOnBeat = false;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setAmbientEnabled();

        mContainerView = (BoxInsetLayout) findViewById(R.id.container);
        mTextView = (TextView) findViewById(R.id.text);

        buttonYes = (Button) findViewById(R.id.buttonYes);
        buttonNo = (Button) findViewById(R.id.buttonNo);
        areUOk = (TextView) findViewById(R.id.areUOK);
        buttonTapingGesture1 = (Button) findViewById(R.id.buttonTapping1);
        buttonTapingGesture2 = (Button) findViewById(R.id.buttonTapping2);


        //listener Bouton tappingGesture
        buttonTapingGesture1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                callBackTapping1();
                return false;
            }

        });
        buttonTapingGesture2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                callBackTapping1();
                return false;
            }

        });


        //listener bouton No
        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callbackButtonNo();
            }
        });

        //listener bouton No
        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbackButtonYes();
            }
        });
    }

    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        updateDisplay();
    }

    @Override
    public void onUpdateAmbient() {
        super.onUpdateAmbient();
        updateDisplay();
    }

    @Override
    public void onExitAmbient() {
        updateDisplay();
        super.onExitAmbient();
    }

    private void updateDisplay() {
        if (isAmbient()) {
            mContainerView.setBackgroundColor(getResources().getColor(android.R.color.holo_purple));
            mTextView.setTextColor(getResources().getColor(android.R.color.holo_purple));
            mClockView.setVisibility(View.VISIBLE);
            mClockView.setText(AMBIENT_DATE_FORMAT.format(new Date()));
        } else {
            mContainerView.setBackground(null);
            mTextView.setTextColor(getResources().getColor(android.R.color.holo_purple));
            mClockView.setVisibility(View.GONE);
        }
    }


    public void callbackButtonNo() {
        areUOk.setText("No");
        areUOk.setBackgroundColor(Color.BLUE);
    }

    public void callbackButtonYes() {
        areUOk.setText("yes");
        areUOk.setBackgroundColor(Color.RED);
    }

    public void callBackTapping1() {
        finger1 = true;
        if (finger2) {
            buttonNo.setVisibility(View.VISIBLE);
            buttonYes.setVisibility(View.VISIBLE);
        }
        Log.d("click on taping", "taping");
    }

    public void callBackTapping2() {
        finger2 = true;
        if (finger1) {
            buttonNo.setVisibility(View.VISIBLE);
            buttonYes.setVisibility(View.VISIBLE);
        }

        Log.d("click on taping", "taping");
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int xPos, yPos;
        int index = MotionEventCompat.getActionIndex(event);

        if (event.getPointerCount() > 1) {
            //Log.d("MainActivity", "Multitouch event");
            finger2 = finger1 = true;
            // The coordinates of the current screen contact, relative to
            // the responding View or Activity.
            xPos = (int) MotionEventCompat.getX(event, index);
            yPos = (int) MotionEventCompat.getY(event, index);

        } else {
            // Single touch event
            //Log.d("MainActivity", "Single touch event");
            xPos = (int) MotionEventCompat.getX(event, index);
            yPos = (int) MotionEventCompat.getY(event, index);
            finger1 = true;
            finger2 = false;
        }
        return super.dispatchTouchEvent(event);
    }
}
