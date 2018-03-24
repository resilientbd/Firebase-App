package firebaseapp.faisal.com.myapplication;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.Manifest;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private Button loginBtn;
    private FirebaseAuth mAuth;
    private String TAG="firebaseCheck";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email=findViewById(R.id.lemailinput);
        password=findViewById(R.id.lpasswordinput);
        loginBtn=findViewById(R.id.btnlogin);
        mAuth = FirebaseAuth.getInstance();

        Toolbar toolbar=findViewById(R.id.toolbar2);
        toolbar.setLogo(R.mipmap.ic_launcher);
       setSupportActionBar(toolbar);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuItem item=findViewById(R.id.user);
//        item.setTitle("My User");
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.mymenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        item=findViewById(R.id.user);
//        item.setTitle("My User");
        switch (item.getItemId())
        {
            case R.id.settings:Toast.makeText(getBaseContext(),"Settings clicked!",Toast.LENGTH_LONG).show();
            break;
            case R.id.tools:Toast.makeText(getBaseContext(),"Tools clicked!",Toast.LENGTH_LONG).show();
                break;
            case R.id.user:Toast.makeText(getBaseContext(),"User clicked!",Toast.LENGTH_LONG).show();
                break;
        }
        return false;
    }

    public void txtClick(View view) {
        Intent intent=new Intent(MainActivity.this,Registration.class);
        startActivity(intent);
    }

    public void loginAction(View view) {

        String semail=email.getText().toString();
        String spassword=password.getText().toString();

        login(semail,spassword);

    }

    public void login(final String email, String password)
    {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {



                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
//                            Toast.makeText(getBaseContext(), "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void updateUI(FirebaseUser user) {

        if(user!=null){
        Toast.makeText(getBaseContext(),"Login Successfull",Toast.LENGTH_LONG).show();
            Intent intent=new Intent(MainActivity.this,StudentActivity.class);
            intent.putExtra("username",user.getEmail());
            startActivity(intent);
        }
        else
        {
            Toast.makeText(getBaseContext(),"Login Failed",Toast.LENGTH_LONG).show();
        }
    }

    public void clearAll()
    {

    }

}
