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

import queri.model.HttpAuthenticate;
public class RepliesFragment extends Fragment {

    private ListView comments;
    private TextView meta;
    private TextView Post;
    private ArrayList<HashMap<String, String>> commentInfo;
    private String TAG = RepliesFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View featuredReply = inflater.inflate(R.layout.fragment_replies,container,false);
        comments = (ListView) featuredReply.findViewById(R.id.list_reply);
        meta = (TextView) featuredReply.findViewById(R.id.textView2);
        Post = (TextView) featuredReply.findViewById(R.id.textView6);

        Post.setText(getArguments().getString("post"));
        meta.setText(getArguments().getString("meta"));
        return featuredReply;
    }
}
