package com.example.nikh.firebaseauth;

import android.content.Intent;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.PrivateKey;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;

    private TextView textViewUserEmail;
    private Button buttonLogout;

    private DatabaseReference Mydatabase;
    private DatabaseReference Message_Ref;
    private DatabaseReference Tag_Mode;
    private DatabaseReference Tag_Status;
    private DatabaseReference Tag_Reset;


    private EditText editTextName, editTextPurpose;
    private Button buttonsave;

    private TextView textViewMessage;
    private Button buttonTrainingStart;
    private Button buttonTestingStart;
    private Button buttonTestingStop;
    private Button buttonTestingReset;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_profile );

        //initializing firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();

        //if the user is not logged in
        //that means current user will return null
        if (firebaseAuth.getCurrentUser() == null) {
            //closing this activity
            finish();
            //starting new activity
            startActivity( new Intent( getApplicationContext(), ProfileActivity.class ) );
        }

        Mydatabase = FirebaseDatabase.getInstance().getReference();

        //editTextName = (EditText) findViewById(R.id.editTextName);
        //editTextPurpose =(EditText) findViewById(R.id.editTextPurpose);
        // buttonsave =(Button) findViewById(R.id.buttonSave);

        Message_Ref = Mydatabase.child( "Message" );

        Tag_Mode = Mydatabase.child( "Mode" );
        Tag_Status = Mydatabase.child( "Status" );
        Tag_Reset = Mydatabase.child( "Reset" );

        textViewMessage = (TextView) findViewById( R.id.textViewMessage );
        buttonTrainingStart = (Button) findViewById( R.id.buttonTraining );
        buttonTestingStart = (Button) findViewById( R.id.buttonTesting1 );
        buttonTestingStop = (Button) findViewById( R.id.buttonTesting2 );
        buttonTestingReset = (Button) findViewById( R.id.buttonReset );


        FirebaseUser user = firebaseAuth.getCurrentUser();

        //initializing views
        textViewUserEmail = (TextView) findViewById( R.id.textViewUserEmail );
        buttonLogout = (Button) findViewById( R.id.buttonLogout );


        //displaying logged in user name
        textViewUserEmail.setText( "Welcome  " + user.getEmail() );


        //adding listner to button
        buttonLogout.setOnClickListener( this );
        // buttonsave.setOnClickListener(this);


        buttonTrainingStart.setOnClickListener( this );
        buttonTestingStart.setOnClickListener( this );
        buttonTestingStop.setOnClickListener( this );
        buttonTestingReset.setOnClickListener( this );


    }

    private void saveUserInformation() {
        String name = editTextName.getText().toString().trim();
        String purpose = editTextPurpose.getText().toString().trim();

        UserInformation userInformation = new UserInformation( name, purpose );

        FirebaseUser user = firebaseAuth.getCurrentUser();

        Mydatabase.child( user.getUid() ).setValue( userInformation );


        Toast.makeText( this, "Information saved...", Toast.LENGTH_LONG ).show();

    }

    @Override
    protected void onStart() {
        super.onStart();

        Tag_Status.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String text = dataSnapshot.getValue( String.class );
                /*if (text == "Train_Start") {
                    textViewMessage.setText( "Training Started" );
                }
                if (text == "Train_Stop") {
                    textViewMessage.setText( "Training Completed" );
                }
                if (text == "Track_Start") {
                    textViewMessage.setText( "Tracking Started" );
                }
                if (text == "Track_Stop") {
                    textViewMessage.setText( "Tracking Completed" );
                }
                if (text == "Track_Reset") {
                    textViewMessage.setText( "Tracking Reset" );
                }*/

                textViewMessage.setText( text );

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        } );

        buttonTrainingStart.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Message_Ref.setValue( "Training Now" );
                Tag_Mode.setValue( "Training" );
                Tag_Status.setValue( "Training Started" );
            }
        } );


        buttonTestingStart.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Tag_Status.child( "value" ).addListenerForSingleValueEvent

                        ( new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String value = dataSnapshot.getValue( String.class );

                                if (value != "Training Started") {
                                    //Message_Ref.setValue( "Testing Started" );
                                    Tag_Mode.setValue( "Testing" );
                                    Tag_Status.setValue( "Tracking Started" );


                                }
                                else {
                                    buttonTestingStart.setEnabled( false );
                                };

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        } );
            }


        } );

        buttonTestingStop.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    Tag_Status.child( "value" ).addListenerForSingleValueEvent
                            ( new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String value = dataSnapshot.getValue( String.class );
                                    if (value != "Training Started") {
                                        //Message_Ref.setValue( "Testing Stopped" );

                                        Tag_Mode.setValue( "Testing" );
                                        Tag_Status.setValue( "Tracking Completed" );
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            } );
                }
            }
        } );

        buttonTestingReset.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Tag_Status.child( "value" ).addListenerForSingleValueEvent
                        ( new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String value = dataSnapshot.getValue( String.class );
                                if (value != "Training Started") {
                                    //Message_Ref.setValue( "Testing Stopped" );
                                    Tag_Mode.setValue( "Testing" );
                                    Tag_Status.setValue( "Tracking Reset" );
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        } );
            }

            ;

        } );
        /**
         * Called when a view has been clicked.
         *
         * @param view The view that was clicked.
         */


        ;
    }

    @Override
    public void onClick(View view) {

        /*if (view == buttonTrainingStart) {

            Tag_Mode.setValue( "Training On" );

        }

        if (view == buttonTestingStart) {

            Tag_Status.setValue( "Testing Started1" );


        }

        if (view == buttonTestingStop) {

            Tag_Status.setValue( "Testing Stopped1" );
        }*/


        if (view == buttonLogout) {
            firebaseAuth.signOut();
            finish();
            startActivity( new Intent( this, UserLogin.class ) );
        }
    }

            /*if (view ==buttonsave){
                saveUserInformation();*/

}