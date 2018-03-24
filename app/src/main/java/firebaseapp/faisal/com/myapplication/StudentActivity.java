package firebaseapp.faisal.com.myapplication;

import android.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentActivity extends AppCompatActivity {
private EditText inputname;
private EditText inputdep;
private EditText inputcontact;
private Button okbtn;
private ListView listView;
private TextView displayview;
private TextView idview;
ProgressBar progressBar;
private CustomAdapter adapter;
private List<Student> studentList;
private boolean isUpdate;
RelativeLayout layout;
    FirebaseFirestore db;
    String TAG="firebasedbcheck";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        inputname=findViewById(R.id.name);
        inputdep=findViewById(R.id.dep);
        inputcontact=findViewById(R.id.contact);
        okbtn=findViewById(R.id.btnok);
        listView=findViewById(R.id.listview);
        Toolbar toolbar=findViewById(R.id.toolbar2);
        toolbar.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(toolbar);
        displayview=findViewById(R.id.userview);
        String retriveString =getIntent().getStringExtra("username");
        displayview.setText(retriveString);
        layout=findViewById(R.id.studentroot);
        idview=findViewById(R.id.textView);
        //initiate progress bar
        progressBar = new ProgressBar(StudentActivity.this,null,android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100,100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        layout.addView(progressBar,params);
        progressBar.setVisibility(View.GONE);
        // Access a Cloud Firestore instance from your Activity

         db = FirebaseFirestore.getInstance();
         studentList=new ArrayList<Student>();
        adapter=new CustomAdapter(studentList,getBaseContext());
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String document=studentList.get(position).getId();
                Log.d(TAG,"Id: "+document);
                deleteData(document);
                return false;
            }
        });
//         adapter=new CustomAdapter()
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               idview.setText(studentList.get(position).getId());
               inputname.setText(studentList.get(position).getName());
               inputcontact.setText(studentList.get(position).getContact());
               inputdep.setText(studentList.get(position).getDepartment());

               requestCallPermission(true,inputcontact.getText().toString());
            }
        });

        requestCallPermission(false,null);
        realtimeUpdate();
    }
    private void requestCallPermission(boolean flag,String number)
    {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.CALL_PHONE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.CALL_PHONE},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            if(flag==true)
            {
                Intent intent=new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:"+number));
                startActivity(intent);
            }
        }
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
            case R.id.settings:
                Toast.makeText(getBaseContext(),"Settings clicked!",Toast.LENGTH_LONG).show();
                break;
            case R.id.tools:Toast.makeText(getBaseContext(),"Tools clicked!",Toast.LENGTH_LONG).show();
                break;
            case R.id.user:Toast.makeText(getBaseContext(),"User clicked!",Toast.LENGTH_LONG).show();
                break;
        }
        return false;
    }
    public void insertData(Student student)
    {
        // Create a new user with a first and last name
        Map<String, Object> tbl_student = new HashMap<>();
        tbl_student.put("name", student.getName());
        tbl_student.put("department", student.getDepartment());
        tbl_student.put("contact", student.getContact());

// Add a new document with a generated ID
        db.collection("student_info")
                .add(tbl_student)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        //resume user interaction
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        progressBar.setVisibility(View.GONE);

                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        //refreshDataView();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //resume user interaction
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        progressBar.setVisibility(View.GONE);
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    public void btnSave(View view) {

        String name=inputname.getText().toString();
        String dep=inputdep.getText().toString();
        String contact=inputcontact.getText().toString();
        insertData(new Student(null,name,dep,contact));
        //stop user interaction
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        progressBar.setVisibility(View.VISIBLE);

    }
    public void refreshDataView()
    {
        studentList.removeAll(studentList);

        db.collection("student_info")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                              //  Log.d(TAG, document.getId() + " => " + document.getData());
                            String id=""+document.getId();
                            String name=""+document.getString("name");
                            String dep=""+document.getString("department");
                            String contact=""+document.getString("contact");

                            Student st=new Student(id,name,dep,contact);
                            addData(st);
                            }
                            Log.d(TAG,""+studentList.size());

                            adapter.notifyDataSetChanged();
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }

    public void addData(Student student)
    {
        studentList.add(student);
    }
    public void realtimeUpdate() {
        final CollectionReference docRef = db.collection("student_info");
        docRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot snapshot, FirebaseFirestoreException e) {
                if(e!=null)
                {

                    Log.w(TAG, "Listen failed.", e);
                    return;
                }
                for (DocumentChange documentChange : snapshot.getDocumentChanges())
                {
                    Object name =  documentChange.getDocument().getData().get("name");
                    Log.d(TAG, "Last Added name- "+name);
                }
                if (snapshot != null) {
                //   List<DocumentSnapshot> documentList= snapshot.getDocuments();
//                   for(DocumentSnapshot document:documentList)
//                   {
////                       try {
////                           Student student = document.toObject(Student.class);
////                           Log.d(TAG, "Current data: " + student.toString());
////
////                       }catch (Exception ex)
////                       {
////                           Log.d(TAG, "Object Convertion Error: "+ex);
////                          // refreshDataView();
////                       }
//
//                   }
                    refreshDataView();
                } else {
                    Log.d(TAG, "Current data: null");
                }

            }


//            @Override
//            public void onEvent(CollectionReference collectionReference, FirebaseFirestoreException e) {
//
//            }
//        });
        });
    }
    public void deleteData(String doc)
    {
        db.collection("student_info").document(doc)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Error deleting: !"+e);
                    }
                });
    }

    public void updateClick(View view) {
        String id=idview.getText().toString();
        String name=inputname.getText().toString();
        String dep=inputdep.getText().toString();
        String contact=inputcontact.getText().toString();
//        insertData(new Student(null,name,dep,contact));
        updateDoc("student_info",id,dep,contact,name);
        //stop user interaction
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        progressBar.setVisibility(View.VISIBLE);
    }
    public void updateDoc(String tblname, String id, final String dep, final String contact, final String name)
    {
        final DocumentReference sfDocRef = db.collection(tblname).document(id);

        db.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
               // DocumentSnapshot snapshot = transaction.get(sfDocRef);

                transaction.update(sfDocRef, "name", name);
                transaction.update(sfDocRef, "department", dep);
                transaction.update(sfDocRef, "contact", contact);

                // Success
                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Transaction success!");
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                progressBar.setVisibility(View.GONE);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Transaction failure.", e);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }
}
