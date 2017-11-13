package com.example.msuba.chatapp.Onboarding;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.msuba.chatapp.Adapters.UserCreds;
import com.example.msuba.chatapp.Main.Landing;
import com.example.msuba.chatapp.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    String BASE_URL = "https://chatapp-4f28b.firebaseio.com/";
    EditText Uname, Pass;
    String User, pass;
    Button Login, Reg;
    Firebase fb_db;
    UserCreds userCreds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(MainActivity.this);
        fb_db = new Firebase(BASE_URL);

        Uname = (EditText) findViewById(R.id.Uname);
        Pass = (EditText) findViewById(R.id.Pass);
        Login = (Button) findViewById(R.id.Login);
        Reg = (Button) findViewById(R.id.Register);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                User = Uname.getText().toString();
                pass = Pass.getText().toString();
                new MyTask2().execute();
            }
        });
        Reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User = Uname.getText().toString();
                pass = Pass.getText().toString();
                new MyTask1().execute();
            }
        });


    }

    private class MyTask1 extends AsyncTask<String, Integer, String>
    {

        @Override
        protected String doInBackground(String... strings) {
            userCreds = new UserCreds();
            userCreds.Uname = User;
            userCreds.Pass = pass;
            fb_db.child("UserCreds").child(User).setValue(userCreds);
            return null;
        }
    }
    private class MyTask2 extends AsyncTask<String, Integer, String>
    {

        @Override
        protected String doInBackground(String... strings) {

            BASE_URL = BASE_URL+"UserCreds/"+User+"/";
            Firebase fb_db2 = new Firebase(BASE_URL);
            fb_db2.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                     userCreds = dataSnapshot.getValue(UserCreds.class);
                    System.out.println("BOWWWWW"+userCreds.Uname+"____"+userCreds.Pass);
                    try{
                        if(User.equals(userCreds.Uname)&&pass.equals(userCreds.Pass))
                        {
                            SharedPreferences pref = getApplicationContext().getSharedPreferences("ChatAppPreff", 0); // 0 - for private mode
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("Uname",User);
                            editor.commit();

                            System.out.println("BOQQQQQQQ");
                            startActivity(new Intent(getApplicationContext(), Landing.class));
                        }
                    }catch (Exception e)
                    {
                        Toast.makeText(getApplicationContext(),"Invalid Credentials",Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
            return null;
        }
    }
}
