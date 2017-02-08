package angel.restconnections.net;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import angel.restconnections.MainActivity;

/**
 * Created by angel on 2/8/2017.
 */

public class VolleyDispatcher {

    private VolleyDispatcher() {
    }

    private static VolleyDispatcher instance = new VolleyDispatcher();

    public static VolleyDispatcher getInstance() {
        return instance;
    }

    // application's main queue
    private RequestQueue requestQueue;

    // since multiple threads may be accessing this, let's make it synchronized
    public synchronized void addToQueue(MainActivity context, Request request) {
        if (requestQueue == null)
            requestQueue = Volley.newRequestQueue(context);

        requestQueue.add(request);
    }
}
