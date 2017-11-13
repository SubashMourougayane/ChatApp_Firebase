package com.example.msuba.chatapp.Main;

import android.app.Activity;
import android.app.ListActivity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.msuba.chatapp.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

public class Landing extends Activity
{
    String BASE_URL = "https://chatapp-4f28b.firebaseio.com/UserCreds/";
    Firebase fb_db;
    ListView listView;
    ArrayList<String> userlist;
    private ArrayAdapter<String> listAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        listView = (ListView)findViewById(R.id.list);
        Firebase.setAndroidContext(getApplicationContext());
        fb_db = new Firebase(BASE_URL);
        new MyTask().execute();

    }
    private class MyTask extends AsyncTask<String, Integer, String>
    {

        @Override
        protected String doInBackground(String... strings) {
            fb_db.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("ChatAppPreff", 0); // 0 - for private mode
                    String User = pref.getString("Uname","");

                    userlist = new ArrayList<>();
                    System.out.println("BOWWW"+dataSnapshot.getKey());
                    for(DataSnapshot postSnapshot : dataSnapshot.getChildren())
                    {
                        if(!postSnapshot.getKey().equals(User))
                        {
                            System.out.println("ASDFASDFASDF"+postSnapshot.getKey());
                            userlist.add(postSnapshot.getKey());
                        }
                    }
                   listAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.list_items,R.id.textView,userlist);
                   listView.setAdapter(listAdapter);


                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

            return null;
        }
    }

}
