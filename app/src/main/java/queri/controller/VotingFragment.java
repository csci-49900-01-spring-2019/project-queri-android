package queri.controller;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.capstone.queri.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import queri.model.HttpAuthenticate;

public class VotingFragment extends Fragment {

    private ListView posts;
    private ArrayList<HashMap<String, String>> postInfo;
    private String TAG = VotingFragment.class.getSimpleName();
    private EditText post;
    private CheckBox Anonymous;
    private ImageView img;
    private String Anon = "Anonymous";
    private ToggleButton toggle;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View voting = inflater.inflate(R.layout.fragment_voting, container, false);
        posts = (ListView) voting.findViewById(R.id.list);
        post = (EditText) voting.findViewById(R.id.editText);
        img = (ImageView) voting.findViewById(R.id.imageView3);
        Anonymous = (CheckBox) voting.findViewById(R.id.checkBox);
        img.setOnClickListener(clickednotif);
        return voting;
    }


    private ImageView.OnClickListener clickednotif = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String commentGiven = post.getText().toString();
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
        postInfo = new ArrayList<>();
        new GetPosts().execute();

    }

    private class GetPosts extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getActivity(),"Showing Voting",
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpAuthenticate sh = new HttpAuthenticate();
            // Making a request to url and getting response
            String url = "https://us-central1-projectq-42a18.cloudfunctions.net/queri/posts/voting";
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    Log.e(TAG, "Response from voting: " + jsonStr);
                    for (Iterator<String> it = jsonObj.keys(); it.hasNext(); ) {
                        String key = it.next();
                        JSONObject post = jsonObj.getJSONObject(key);
                        String postContent = post.getString("content");

                        JSONObject meta = post.getJSONObject("meta");
                        String numVotes = String.valueOf(meta.getInt("likes"));

                        String username = post.getString("username");

                        String totalmeta = username+"\t"+"\tNumber of Likes: "+numVotes;
                        HashMap<String, String> card = new HashMap<>();

                        // adding each child node to HashMap key => value
                        card.put("Post", postContent);
                        card.put("meta_data", totalmeta);

                        // adding contact to contact list
                        postInfo.add(card);
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
            ListAdapter adapter = new SimpleAdapter(getActivity(), postInfo,
                    R.layout.list_view_with_like, new String[]{ "Post","meta_data"},
                    new int[]{R.id.Post, R.id.meta_data});
            posts.setAdapter(adapter);
        }
    }
    private class PostComment extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... arg0) {
            HttpAuthenticate sh = new HttpAuthenticate();
            // Making a request to url and getting response
            String url = "https://us-central1-projectq-42a18.cloudfunctions.net/queri/posts/voting/new/";


            String commentGiven = post.getText().toString();

            while(commentGiven.isEmpty()){
                Toast.makeText(getActivity(),"Must have adequate input",
                        Toast.LENGTH_SHORT).show();
                commentGiven = post.getText().toString();
                post.getText().clear();
            }

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
            new GetPosts().execute();
        }
    }




}
