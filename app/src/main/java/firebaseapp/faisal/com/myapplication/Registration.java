package firebaseapp.faisal.com.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by ussl-01 on 3/19/2018.
 */

public class Registration extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private Button registerbtn;
    private FirebaseAuth mAuth;
    private String TAG="firebaseCheck";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        email=findViewById(R.id.remailinput);
        password=findViewById(R.id.rpasswordinput);
        registerbtn=findViewById(R.id.btnregistration);
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

    public void btnRegisterClick(View view) {
        String semail=email.getText().toString();
        String spassword=password.getText().toString();
        createUser(semail,spassword);
        Intent intent=new Intent(Registration.this,MainActivity.class);
        startActivity(intent);
        //firebase..

    }
    public void createUser(final String email, String password)
    {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            //FirebaseUser user = mAuth.getCurrentUser();
                            // updateUI(user);
                            clearAll();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getBaseContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //  updateUI(null);
                        }

                        // ...
                    }
                });
    }
    public void clearAll()
    {
        email.setText("");
        password.setText("");
        Toast.makeText(getBaseContext(), "Registration Successfull",
                Toast.LENGTH_SHORT).show();
    }
}
