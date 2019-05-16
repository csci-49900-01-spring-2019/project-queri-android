package queri.controller;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.capstone.queri.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import queri.model.HttpAuthenticate;

public class FeaturedFragment extends Fragment{

    private ListView posts;
    private ArrayList<HashMap<String, String>> postInfo;
    private HashMap<String, String> reserveComments;
    private String TAG = FeaturedFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View featured = inflater.inflate(R.layout.fragment_featured,container,false);
        posts = (ListView) featured.findViewById(R.id.list);
        return featured;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        postInfo = new ArrayList<>();
        reserveComments = new HashMap<>();
        new GetPosts().execute();
        posts.setOnItemClickListener(clickedItem);

    }
    private ListView.OnItemClickListener clickedItem = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            TextView post = (TextView) view.findViewById(R.id.Post);
            TextView meta = (TextView) view.findViewById(R.id.meta_data);
            String post1 = post.getText().toString();
            String meta1 = meta.getText().toString();
            String postId = reserveComments.get(post1);
            Bundle bundle = new Bundle();
            bundle.putString("post",post1);
            bundle.putString("meta",meta1);
            bundle.putString("postId",postId);

//            Fragment reply = new RepliesFragment();
//            reply.setArguments(bundle);
            ((MainActivity) getActivity()).replies(bundle);
//            getChildFragmentManager().beginTransaction().replace(R.id.fragment_container_replies,reply).commit();
        }
    };
    private class GetPosts extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getActivity(),"Showing Featured",
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpAuthenticate sh = new HttpAuthenticate();
            // Making a request to url and getting response
            String url = "https://us-central1-projectq-42a18.cloudfunctions.net/queri/posts/featured";
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    int counter = 0;
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    Log.e(TAG, "Response from featured: " + jsonStr);
                    for (Iterator<String> it = jsonObj.keys(); it.hasNext(); ) {
                        String key = it.next();
                        JSONObject post = jsonObj.getJSONObject(key);
                        String postContent = post.getString("content");

                        JSONObject meta = post.getJSONObject("meta");
                        String numComments = String.valueOf(meta.getInt("comments"));
                        String days_left = String.valueOf(meta.getInt("days_remaining"));
                        String numVotes = String.valueOf(meta.getInt("likes"));

                        String username = post.getString("username");

                        String totalmeta = username + "\t" + "\tNumber of Likes: " + numVotes;
                        HashMap<String, String> card = new HashMap<>();

                        // adding each child node to HashMap key => value
                        card.put("Post", postContent);
                        card.put("meta_data", totalmeta);

                        // adding contact to contact list
                        reserveComments.put(postContent,String.valueOf(counter));
                        postInfo.add(card);
                        ++counter;
                    }
                }catch (final JSONException e) {
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
                    R.layout.list_view, new String[]{ "Post","meta_data"},
                    new int[]{R.id.Post, R.id.meta_data});
            posts.setAdapter(adapter);
        }
    }
//    @Override
//    public void onDestroyView()
//    {
//        FragmentManager mFragmentMgr= getFragmentManager();
//        FragmentTransaction mTransaction = mFragmentMgr.beginTransaction();
//        Fragment childFragment =mFragmentMgr.findFragmentById(R.id.fragment_container_replies);
//        mTransaction.remove(childFragment);
//        mTransaction.commit();
//        super.onDestroyView();
//    }



}
