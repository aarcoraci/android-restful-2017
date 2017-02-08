package angel.restconnections.net;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Observable;

import angel.restconnections.MainActivity;
import angel.restconnections.domain.Post;

/**
 * Created by angel on 2/8/2017.
 */

public class PostConnector extends Observable {

    private static final String ENDPOINT_GET_POSTS = "https://jsonplaceholder.typicode.com/posts";

    private static PostConnector instance = new PostConnector();

    public static PostConnector getInstance() {
        return instance;
    }

    private PostConnector() {
    }

    /***
     * Get posts from the web service
     * @param context
     */
    public void getPosts(final MainActivity context) {
        // let's not mess with the UI thread
        new Thread(new Runnable() {
            @Override
            public void run() {

                JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, ENDPOINT_GET_POSTS, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                if (response != null) {
                                    // this is what we are looking for !
                                    ArrayList<Post> result = new ArrayList<>();

                                    try {
                                        for (int i = 0; i < response.length(); i++) {
                                            JSONObject current = response.getJSONObject(i);
                                            Post post = new Post();
                                            post.id = current.getInt("id");
                                            post.userId = current.getInt("userId");
                                            post.body = current.getString("body");
                                            post.title = current.getString("title");
                                            result.add(post);
                                        }
                                    } catch (JSONException ex) {
                                        Log.d(PostConnector.class.getSimpleName(), ex.getMessage());
                                        setChanged();
                                        notifyObservers(null);
                                    }

                                    // all good! let's notify the results
                                    setChanged();
                                    notifyObservers(result);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                setChanged();
                                notifyObservers(null);
                            }
                        }
                );

                VolleyDispatcher.getInstance().addToQueue(context, jsonObjectRequest);
            }
        }).start();
    }

}
