package queri.controller;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.capstone.queri.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import queri.model.HttpAuthenticate;

public class RepliesFragmentArchived extends Fragment {
    public ListView comments;
    private TextView meta;
    private TextView Post;
    private ImageView img;
    public String id = "";
    private ArrayList<HashMap<String, String>> commentInfo;
    private String TAG = RepliesFragmentArchived.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View archivedReply = inflater.inflate(R.layout.fragment_replies_archived, container, false);
        comments = (ListView) archivedReply.findViewById(R.id.list_reply);
        meta = (TextView) archivedReply.findViewById(R.id.textView2);
        Post = (TextView) archivedReply.findViewById(R.id.textView6);

        Post.setText(getArguments().getString("post"));
        meta.setText(getArguments().getString("meta"));
        id = getArguments().getString("postId");
        return archivedReply;
    }
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
            Toast.makeText(getActivity(), "Showing Archived",
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpAuthenticate sh = new HttpAuthenticate();
            // Making a request to url and getting response
            String url = "https://us-central1-projectq-42a18.cloudfunctions.net/queri/posts/archived/" + id + "/comments";
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
}
