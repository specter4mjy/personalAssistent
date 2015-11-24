package ubicomp.ucd.personalassistant;

import android.location.Location;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by mukesh on 21/11/2015.
 */
public class LocationDB {
    private static HashMap<double[], String> loc_db = new HashMap<double[], String>();

    private String[] address = {"csi", "library", "hospital", "science north", "science center",
            "science south", "arts history", "arts cafe", "first cafe", "home"};

    private double[][]lat_lng = {{53.3092613,-6.2238843},{53.3068237, -6.2230313},
                                  {53.3096924, -6.221709},{53.3089761, -6.2243054},
                                  {53.3082629, -6.2244287},{53.3078109, -6.2243912},
                                  {53.3059438, -6.2225539},{53.3062435, -6.2218163},
                                  {53.3054165, -6.2206254},{53.3530657, -6.2610841}};
    private static final double THRESHOLD_DIST = 1000;//10 meter


    LocationDB(){
        createDB();
    }

    private void createDB(){
        int len =lat_lng.length;
        for(int i=0;i<len;i++){
            loc_db.put(lat_lng[i],address[i]);
        }
    }

    String searchDb(double cur_lat, double cur_lon) {
        float[] results = new float[1];
        for(int i=0; i<lat_lng.length;i++) {
            Location.distanceBetween(lat_lng[i][0], lat_lng[i][1], cur_lat, cur_lon, results);
            Log.d("Mukesh", "results[0] = "+ i + " "+results[0]);
            if (results[0] < THRESHOLD_DIST) {
                return address[i];
            }
        }
        return null;
        }

}