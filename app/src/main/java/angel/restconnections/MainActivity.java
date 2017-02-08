package angel.restconnections;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import angel.restconnections.domain.Post;
import angel.restconnections.net.PostConnector;

public class MainActivity extends AppCompatActivity implements Observer {

    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private Button getPostsButton;
    private ListView postsList;
    private ArrayList<String> postTitles = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getPostsButton = (Button) findViewById(R.id.get_posts_button);
        getPostsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PostConnector.getInstance().getPosts(MainActivity.this);
            }
        });

        // list related stuff
        postsList = (ListView) findViewById(R.id.posts_list);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, postTitles);
        postsList.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PostConnector.getInstance().addObserver(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        PostConnector.getInstance().deleteObserver(this);
    }

    @Override
    public void update(Observable observable, final Object o) {

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                // we are ready to update the ui
                if (o != null) {
                    postTitles.clear();
                    ArrayList<Post> posts = (ArrayList<Post>) o;
                    for(Post current : posts){
                        postTitles.add(current.title);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}
