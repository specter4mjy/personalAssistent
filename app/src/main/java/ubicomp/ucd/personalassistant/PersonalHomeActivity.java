package ubicomp.ucd.personalassistant;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.RemoteException;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.internal.telephony.ITelephony;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class PersonalHomeActivity extends Activity {

    public static final String SEND_LOCATION_TO_UI = "SEND LOCATION";
    public static LocationDB locDb;
    private UiUpdateReceiver myReceiver;
    // LogCat tag
    private static final String TAG = PersonalHomeActivity.class.getSimpleName();
    private String userLocation;
    // UI elements
    private Button btnShowLocation;
    private EventResultDataModel eventResult;
    private boolean userBusy;
    private final String CLASSHEAD = "COMP";
    private String userActivity;
    private HashMap<String,String> numberToName;
    BroadcastReceiver callBlocker;
    TelephonyManager telephonyManager;
    ITelephony telephonyService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_home);

        btnShowLocation = (Button) findViewById(R.id.btnShowLocation);

        // First we need to check availability of play services


        // Show location button click listener
        btnShowLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent startLocationService = new Intent(PersonalHomeActivity.this, LocationHandlerService.class);
                startService(startLocationService);
            }
        });

        // initialise location database
        locDb = new LocationDB();

        //Receiver to get location update from background service to perform required action

        myReceiver = new UiUpdateReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SEND_LOCATION_TO_UI);
        registerReceiver(myReceiver, intentFilter);

        eventResult = EventDataHelper.getEventData(this);
        TextView textView = (TextView) findViewById(R.id.tv_event);
        textView.setText(eventResult.getEventContent());
        Switch blockSwitch = (Switch) findViewById(R.id.blockSwitch);
        blockSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.i("specter", "switch on");
                    userActivity = "class";
                    blockCall();
                } else {
//                    stopBlock();
                }
            }
        });

        numberToName = new HashMap<>();
        numberToName.put("12345", "mum");
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void blockCall() {
        callBlocker = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                Class c = null;
                try {
                    c = Class.forName(telephonyManager.getClass().getName());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                Method m = null;
                try {
                    m = c.getDeclaredMethod("getITelephony");
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                m.setAccessible(true);
                try {
                    telephonyService = (ITelephony) m.invoke(telephonyManager);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                PhoneStateListener callBlockListener = new PhoneStateListener() {
                    @Override
                    public void onCallStateChanged(int state, String incomingNumber) {
                        if (state == TelephonyManager.CALL_STATE_RINGING) {
                            try {
                                Log.i("specter", "end call:" + incomingNumber);
                                telephonyService.endCall();
                                if (numberToName.get(incomingNumber) != null) {
                                    SmsManager smsManager = SmsManager.getDefault();
                                    switch (userActivity) {
                                        case "class":
                                            smsManager.sendTextMessage(incomingNumber,null,"Hi "+numberToName.get(incomingNumber)+", I'm having class. I'll call you later.",null,null);
                                            break;
                                    }
                                }
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                };
                telephonyManager.listen(callBlockListener, PhoneStateListener.LISTEN_CALL_STATE);
            }
        };
        IntentFilter filter = new IntentFilter("android.intent.action.PHONE_STATE");
        registerReceiver(callBlocker, filter);
        Log.i("specter", "register Receiver");

    }

    private void stopBlock() {
        unregisterReceiver(callBlocker);
        callBlocker = null;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public class UiUpdateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub

            Log.d(TAG, SEND_LOCATION_TO_UI);
            userLocation = intent.getStringExtra("location");
            if (eventResult.getEventLocation().toLowerCase().contains(userLocation.toLowerCase())) {
                userBusy = true;
                if (eventResult.getEventTitle().toLowerCase().contains(CLASSHEAD.toLowerCase())) {
                    userActivity = "class";
                }

            }

            Toast.makeText(PersonalHomeActivity.this, "Current location: " + userLocation,
                    Toast.LENGTH_LONG).show();

        }

    }
}