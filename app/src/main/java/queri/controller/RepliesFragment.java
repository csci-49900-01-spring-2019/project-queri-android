package queri.controller;

import android.support.v4.app.Fragment;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.capstone.queri.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import queri.model.HttpAuthenticate;
public class RepliesFragment extends Fragment {

    public ListView comments;
    private TextView meta;
    private TextView Post;
    private ImageView img;
    private EditText comment;
    private String username = "";
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private String Anon = "Anonymous";
    private CheckBox ifchecked;
    private ArrayList<HashMap<String, String>> commentInfo;
    private String TAG = RepliesFragment.class.getSimpleName();
    public String id = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View featuredReply = inflater.inflate(R.layout.fragment_replies, container, false);
        comments = (ListView) featuredReply.findViewById(R.id.list_reply);
        meta = (TextView) featuredReply.findViewById(R.id.textView2);
        Post = (TextView) featuredReply.findViewById(R.id.textView6);

        Post.setText(getArguments().getString("post"));
        meta.setText(getArguments().getString("meta"));

        img = (ImageView) featuredReply.findViewById(R.id.imageView3);
        comment = (EditText) featuredReply.findViewById(R.id.editText2);

        ifchecked = (CheckBox) featuredReply.findViewById(R.id.checkBox);
        img.setOnClickListener(clickednotif);
        id = getArguments().getString("postId");
        return featuredReply;
    }

    private ImageView.OnClickListener clickednotif = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String commentGiven = comment.getText().toString();
            if(commentGiven.isEmpty()){
                Toast.makeText(getActivity(),"Must have adequate input",
                        Toast.LENGTH_SHORT).show();
            }
            else
                new PostComment().execute();
        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        commentInfo = new ArrayList<>();
        new GetComments().execute();

    }

    private class GetComments extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getActivity(), "Showing Featured",
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpAuthenticate sh = new HttpAuthenticate();
            // Making a request to url and getting response
            String url = "https://us-central1-projectq-42a18.cloudfunctions.net/queri/posts/featured/" + id + "/comments";
            String jsonStr = sh.makeServiceCall(url);
            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    for (Iterator<String> it = jsonObj.keys(); it.hasNext(); ) {
                        String key = it.next();
                        JSONObject comment = jsonObj.getJSONObject(key);
                        String content = comment.getString("content");
                        String username = comment.getString("username");
                        HashMap<String, String> card = new HashMap<>();
                        card.put("content", content);
                        card.put("author", username);
                        commentInfo.add(card);
                    }

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ListAdapter adapter = new SimpleAdapter(getActivity(), commentInfo,
                    R.layout.list_view, new String[]{ "content","author"},
                    new int[]{R.id.Post, R.id.meta_data});
            comments.setAdapter(adapter);
               }
    }
    private class PostComment extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getActivity(), "Showing Featured",
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpAuthenticate sh = new HttpAuthenticate();
            // Making a request to url and getting response
            String url = "https://us-central1-projectq-42a18.cloudfunctions.net/queri/posts/featured/" + id + "/comments/new";

            String commentGiven = comment.getText().toString();
            String jsonStr = null;
            try {
                jsonStr = sh.outputServiceCall(url,Anon,commentGiven);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr == null) {
                Log.e(TAG, "Couldn't get json from server.");

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            new GetComments().execute();
        }
    }

}
