package ubicomp.ucd.personalassistant;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class PersonalHomeActivity extends Activity {

    public static final String SEND_LOCATION_TO_UI = "SEND LOCATION";
    public static LocationDB locDb;
    private UiUpdateReceiver myReceiver;
    // LogCat tag
    private static final String TAG = PersonalHomeActivity.class.getSimpleName();

    // UI elements
    private Button btnShowLocation;

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
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

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
            String location = intent.getStringExtra("location");

            Toast.makeText(PersonalHomeActivity.this, "Current location: " + location,
                    Toast.LENGTH_LONG).show();

        }

    }
}