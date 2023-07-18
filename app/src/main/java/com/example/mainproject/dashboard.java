package com.example.mainproject;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class dashboard extends AppCompatActivity {
    FloatingActionButton addVideo;
    RecyclerView recview;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    Uri videoUri;

    private PlayerView playerView;
    private SimpleExoPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.appBarColor)));
        this.setTitle("Like And Comment");

        // Initialize Firebase Storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
       // Create a reference to your video file
        StorageReference videoRef = storage.getReference().child("video");


        addVideo=(FloatingActionButton)findViewById(R.id.addVideo);
        addVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Comment.class));

            }
        });

        recview=(RecyclerView) findViewById(R.id.recView);
        recview.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<filemodel> options=
                new FirebaseRecyclerOptions.Builder<filemodel>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("video"),filemodel.class).build();

        FirebaseRecyclerAdapter<filemodel,myviewholder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<filemodel, myviewholder>(options) {
            @Override
            protected void onBindViewHolder(@androidx.annotation.NonNull myviewholder holder, int position, @androidx.annotation.NonNull filemodel model) {
            holder.prepareExoPlayer(getApplication(),model.getTitle(),model.getVurl());
            }

            @androidx.annotation.NonNull
            @Override
            public myviewholder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow,parent,false);
                return new myviewholder(view);
            }
        };



        // Retrieve the download URL for the video
        videoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Use the video URL to play the video
                String videoUrl = uri.toString();
                // Play the video using a video player library or your own implementation
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle any errors that occur while retrieving the download URL
                Toast.makeText(dashboard.this, "Failed to get video URL", Toast.LENGTH_SHORT).show();
            }
        });

        firebaseRecyclerAdapter.startListening();
        recview.setAdapter(firebaseRecyclerAdapter);

    }
}