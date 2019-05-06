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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.capstone.queri.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import queri.model.HttpAuthenticate;

public class VotingFragment extends Fragment {

    private ListView posts;
    private ArrayList<HashMap<String, String>> postInfo;
    private String TAG = VotingFragment.class.getSimpleName();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View voting = inflater.inflate(R.layout.fragment_voting,container,false);
        posts = (ListView) voting.findViewById(R.id.list);
        return voting;
    }
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
            String url = "https://us-central1-projectq-42a18.cloudfunctions.net/queri/posts/";
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray featuredPosts = jsonObj.getJSONArray("voting");
                    Log.e(TAG, "Response from voting: " + featuredPosts);
                    // looping through All posts
                    for (int i = 0; i < featuredPosts.length(); i++) {
                        JSONObject post = featuredPosts.getJSONObject(i);
                        String postContent = post.getString("content");

                        // Getting Post meta data
                        JSONObject meta = post.getJSONObject("meta");
                        String numVotes = String.valueOf(meta.getInt("votes"));

                        String username = post.getString("username");

                        String totalmeta = username+"\t"+"\tNumber of Likes: "+numVotes;

                        // tmp hash map for single post
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


}