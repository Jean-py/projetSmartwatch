package s8.projet;

import android.graphics.Color;

import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
    private Button buttonTappingGesture;


    int xPosFinger1, yPosFinger1, xPosFinger2, yPosFinger2 = 0;
    int xButton;
    int yButton;
    Etat state;

    public enum Etat {
        INIT,FINGER1,FINGER2,TAPPING;
    }




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
        buttonTappingGesture = (Button)findViewById(R.id.buttonTaping);

        xButton = (int) buttonTappingGesture.getX();
        yButton = (int) buttonTappingGesture.getY();
        state = Etat.INIT;

        buttonTappingGesture.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch ( event.getAction() & MotionEvent.ACTION_MASK){

                    case (MotionEvent.ACTION_UP) :
                        callBackActionUp(event);
                        break;
                    case (MotionEvent.ACTION_POINTER_DOWN) :
                        if(stayOnTheButtonTappingGesture(event, 2)){
                            callBackActionPointerDown(event);
                        }

                        break;
                    case (MotionEvent.ACTION_POINTER_UP) :
                        callBackActionActionPointerUp(event);
                        break;

                    case (MotionEvent.ACTION_DOWN) :
                        callbackButtonActionDown(event);
                        break;

                }
                return true;
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


    public boolean stayOnTheButtonTappingGesture(MotionEvent event,int numberOfPointer ){

        boolean res = true;
        for( int i = 0 ; i < numberOfPointer ; i++) {
            System.out.println(" getX("+i+") : " + event.getX(i) + "   buttonTappingGesture.getX() : " +  buttonTappingGesture.getX() );
            System.out.println(" getY("+i+") : " + event.getY(i) + "   buttonTappingGesture.getY() : " +  buttonTappingGesture.getY() );
            res = res &&  event.getX(i) > buttonTappingGesture.getX()-100 ;


        }
        return res;
    }

    public void callbackButtonNo() {
        areUOk.setText("No");
        areUOk.setBackgroundColor(Color.BLUE);
        showButton();
    }

    public void callbackButtonYes() {
        areUOk.setText("yes");
        areUOk.setBackgroundColor(Color.RED);
        showButton();
    }


    //premier doigt sur l'écran
    public void callbackButtonActionDown(MotionEvent event) {

        System.out.println("Action Down");
        switch (state) {
            case INIT:
                state = Etat.FINGER1;
                break;
            case FINGER1:
                //impossible
                break;
            case FINGER2: //impossible
                break;
            case TAPPING://impossible
                break;
        }
    }
    //second doigt sur l'écran
    private void callBackActionPointerDown(MotionEvent event){
        System.out.println("Action pointer down");
        switch (state) {
            case INIT:
                break;
            case FINGER1:
                state = Etat.FINGER2;
                showButton();
                break;
            case FINGER2:
                //nothing
                break;
            case TAPPING:
                state = Etat.FINGER2;
                showButton();
                break;
        }
    }

    //premier doigt enlevé
    private  void callBackActionUp(MotionEvent event){
        System.out.println("Action up");
        switch (state){
            case INIT:
                //impossible
                break;
            case FINGER1:
                state = Etat.INIT;
                hideButton();
                break;
            case FINGER2:

                //TODO a voir: passe t'on en init ou rien du tout?
                state = Etat.INIT;
                hideButton();
                break;
            case TAPPING:
                state = Etat.INIT;
                hideButton();
                break;
        }
    }

    //on enleve le second doigt
    private  void callBackActionActionPointerUp(MotionEvent event){
        System.out.println("action Pointer up");
        switch (state){
            case INIT: //impossible
                break;
            case FINGER1: //impossible
                break;
            case FINGER2:
                state = Etat.TAPPING;
                showButton();
                break;
            case TAPPING: // impossible
                break;
        }
    }

    public void showButton(){
        buttonNo.setVisibility(View.VISIBLE);
        buttonYes.setVisibility(View.VISIBLE);
    }

    public void hideButton(){
        buttonNo.setVisibility(View.INVISIBLE);
        buttonYes.setVisibility(View.INVISIBLE);
    }



    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        int index = MotionEventCompat.getActionIndex(event);



        if (MotionEventCompat.getPointerCount(event) == 1) {
            // Single touch event
            xPosFinger1 = (int) MotionEventCompat.getX(event, 0);
            yPosFinger1 = (int) MotionEventCompat.getY(event, 0);
            // Log.d("MainActivity", "Single touch event   x1 : " + xPosFinger1 +  "  y1 : " + yPosFinger1+

        }else if (event.getPointerCount() > 1) {
           /* if(MotionEventCompat.getX(event,0) >= buttonTappingGesture.getX()  && MotionEventCompat.getX(event,1) >= buttonTappingGesture.getX() ) {
                finger2 =true;
                finger1 = true;
                doubleTouchHappend = true;
                // The coordinates of the current screen contact, relative to
                // the responding View or Activity.
                xPosFinger1 = (int) MotionEventCompat.getX(event, 0);
                yPosFinger1 = (int) MotionEventCompat.getY(event, 0);
                xPosFinger2 = (int) MotionEventCompat.getX(event, 1);
                yPosFinger2 = (int) MotionEventCompat.getY(event, 1);
                // Log.d("MainActivity", "Single touch event   x1 : " + xPosFinger1 +  "  y1 : " + yPosFinger1+
                // "  x2 : " + xPosFinger2 +  "  y2 : " + yPosFinger2);

                Log.d("premier if","1"+ "// finger1 : "+ finger1 + "  finger2 : " + finger2 + "  doubleTouchHappend : " + doubleTouchHappend);
            }*/
        }

        //checker les action UP
        if( MotionEventCompat.findPointerIndex(event, 0) == -1){
            /*
            finger1=false;
            finger2 = false;
            doubleTouchHappend = false;
            buttonNo.setVisibility(View.INVISIBLE);
            buttonYes.setVisibility(View.INVISIBLE);

            Log.d("dernier if", "4"+ "// finger1 : "+ finger1 + "  finger2 : " + finger2 + "  doubleTouchHappend : " + doubleTouchHappend);
            //buttonNo.setVisibility(View.INVISIBLE);
            //buttonYes.setVisibility(View.INVISIBLE);
            */
        }
        return super.dispatchTouchEvent(event);
    }
}
