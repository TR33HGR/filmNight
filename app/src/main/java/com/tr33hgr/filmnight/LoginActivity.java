package com.tr33hgr.filmnight;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN = 365;
    private FirebaseAuth mAuth;

    FirebaseUser currentUser = null;

    SignInButton signInButton;
    LinearLayout userInfoLayout;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        context = this;

        setContentView(R.layout.activity_login);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signInButton = findViewById(R.id.login_signinButton_butt);
        signInButton.setOnClickListener(this);
        signInButton.setVisibility(View.INVISIBLE);

        userInfoLayout = findViewById(R.id.login_userInfo_layout);
        userInfoLayout.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();


        currentUser = mAuth.getCurrentUser();
        updateUI();
    }

    private void updateUI() {
        if(currentUser != null){
            signInButton.setVisibility(View.GONE);
            queryUser();
            Log.d("SIGNED IN", "Successful signin");
        }else{
            signInButton.setVisibility(View.VISIBLE);
        }
    }

    static DocumentReference docRef;

    private void queryUser() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        docRef = db.collection("users").document(currentUser.getUid());

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        userInfoLayout.setVisibility(View.GONE);
                        getUserInfo();
                    } else {
                        userInfoLayout.setVisibility(View.VISIBLE);
                        setUserInfo();
                    }
                } else {
                    Log.d("DATABASE ERROR", "database task unsuccessful" + task.getException());
                }
            }
        });
    }

    boolean gender;
    Button submit;
    RadioButton male;
    RadioButton female;

    private void setUserInfo() {
        ImageButton userProfilePic = findViewById(R.id.login_userPic_img);
        final EditText userName = findViewById(R.id.login_userName_txt);
        final EditText userEmail = findViewById(R.id.login_userEmail_txt);
        final EditText userDOB = findViewById(R.id.login_userDOB_txt);
        final EditText userHouseNum = findViewById(R.id.login_houseNum_txt);
        final EditText userPostCode = findViewById(R.id.login_postCode_txt);
        RadioGroup userGender = findViewById(R.id.login_gender_radGroup);
        male = findViewById(R.id.login_male_rad);
        female = findViewById(R.id.login_female_rad);
        submit = findViewById(R.id.login_submit_butt);

        //set default profile pic image is error receiving from url
        RequestOptions options = new RequestOptions().error((R.mipmap.ic_launcher));
        //get profile pic from url
        Glide.with(userProfilePic.getContext()).load(currentUser.getPhotoUrl()).apply(options).into(userProfilePic);

        userName.setText(currentUser.getDisplayName());
        userEmail.setText(currentUser.getEmail());

        userGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(male.isChecked()){
                    gender = GlobalVars.MALE;
                }else if(female.isChecked()){
                    gender = GlobalVars.FEMALE;
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalVars.user = new User(context,
                        userName.getText().toString(),
                        userEmail.getText().toString(),
                        userDOB.getText().toString(),
                        userHouseNum.getText().toString(),
                        userPostCode.getText().toString(),
                        gender
                );

                GlobalVars.user.getLocationFetcher().getRequestQueue().addRequestFinishedListener(new RequestQueue.RequestFinishedListener() {
                    @Override
                    public void onRequestFinished(Request request) {
                        double[] latLong = GlobalVars.user.getLocationFetcher().getLatLong();
                        GlobalVars.user.setAddressLoc(new GeoPoint(latLong[GlobalVars.LAT], latLong[GlobalVars.LONG]));

                        docRef.set(GlobalVars.user);

                        Log.d("SET", GlobalVars.user.getName());

                        beginApp();
                    }
                });



            }
        });


    }

    private void beginApp() {
        Log.d("BEGIN", "Start app");
        startActivity(new Intent(this, RootActivity.class));
    }

    private void getUserInfo() {
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                GlobalVars.user = documentSnapshot.toObject(User.class);

                Log.d("GET", GlobalVars.user.getEmail());

                beginApp();
            }
        });


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_signinButton_butt:
                signIn();
                break;
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            firebaseAuthWithGoogle(account);
        } catch (ApiException e) {

            Log.w("SIGNIN FAILED", "signInResult:failed code=" + e.getStatusCode());
            updateUI();
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d("FIREBASE AUTH", "firebaseAuthWithGoogle:" + account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("FIREBASE SUCCESS", "signInWithCredential:success");
                            currentUser = mAuth.getCurrentUser();
                            updateUI();
                        } else {
                            Log.w("FIREBASE FAIL", "signInWithCredential:failure", task.getException());
                            Snackbar.make(findViewById(R.id.login_container_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI();
                        }
                    }
                });
    }
}
