package com.example.mainproject;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ReviewPage extends AppCompatActivity {

    private RatingBar ratingBar;
    private TextView textView;

    private TextView ratingValue, thankingText, averageRatingText, likesText, commentText;
    private Button submitRating, commentButton;
    private EditText commentEditText;
    private ImageButton likeButton;
    private boolean isLiked;

    private float userRating;
    private int likesCount = 0;
    private String comment = "";

    private DatabaseReference ratingReference;
    private DatabaseReference likesReference;
    private DatabaseReference userLikesReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_page);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.appBarColor)));
        getSupportActionBar().setTitle("Review");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ratingBar = findViewById(R.id.ratingId);
        textView = findViewById(R.id.textId);

        ratingBar = findViewById(R.id.ratingId);
        ratingValue = findViewById(R.id.ratingValueId);
        thankingText = findViewById(R.id.thankingId);
        averageRatingText = findViewById(R.id.averageRatingId);
        submitRating = findViewById(R.id.ratingSubmitId);

        likesText = findViewById(R.id.likesId);

        likeButton = findViewById(R.id.likeButton);

        // Initially, set the like button to the "unliked" state
        isLiked = false;
        likeButton.setImageResource(R.drawable.ic_unlike);
        commentText = findViewById(R.id.commentId);
        likeButton = findViewById(R.id.likeButton);
        commentButton = findViewById(R.id.commentButton);
        commentEditText = findViewById(R.id.commentEditText);

        ratingReference = FirebaseDatabase.getInstance().getReference("Rating");
        likesReference = FirebaseDatabase.getInstance().getReference("Likes");
        userLikesReference = FirebaseDatabase.getInstance().getReference("UserLikes");
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final String userId = firebaseUser.getUid();

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                userRating = rating;
                ratingValue.setText("Rating: " + userRating);
            }
        });

        submitRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RatingDetails ratingDetails = new RatingDetails(userId, userRating, likesCount, comment);

                ratingReference.child(userId).setValue(ratingDetails)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    thankingText.setText("Thank you for rating us");
                                } else {
                                    thankingText.setText("Error");
                                }
                            }
                        });
            }
        });



        // Calculate and display the average rating
        ratingReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                float totalRating = 0;
                int ratingCount = 0;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    RatingDetails ratingDetails = snapshot.getValue(RatingDetails.class);
                    if (ratingDetails != null) {
                        totalRating += ratingDetails.getRating();
                        ratingCount++;
                    }
                }

                if (ratingCount > 0) {
                    float averageRating = totalRating / ratingCount;
                    averageRatingText.setText("Average Rating: " + averageRating);
                } else {
                    averageRatingText.setText("Average Rating: N/A");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ReviewPage.this, "Error calculating average rating", Toast.LENGTH_SHORT).show();
            }
        });

        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLiked) {
                    // User has already liked, change to "unliked" state
                    likeButton.setImageResource(R.drawable.ic_unlike);
                    isLiked = false;
                    likesCount--;
                    updateLikes();
                    removeLike(userId);
                } else {
                    // User has not liked yet, change to "liked" state
                    likeButton.setImageResource(R.drawable.ic_like);
                    isLiked = true;
                    likesCount++;
                    updateLikes();
                    addLike(userId);
                }
            }
        });

        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comment = commentEditText.getText().toString().trim();
                updateComment();
            }
        });

        // Update the likes count from the database
        ratingReference.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                RatingDetails ratingDetails = dataSnapshot.getValue(RatingDetails.class);
                if (ratingDetails != null) {
                    likesCount = ratingDetails.getLikesCount();
                    isLiked = ratingDetails.isLiked();
                    updateLikes();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ReviewPage.this, "Error updating likes count", Toast.LENGTH_SHORT).show();
            }
        });

        // Listen for changes in the likes count for all users
        likesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int totalLikes = 0;
                StringBuilder commentStringBuilder = new StringBuilder();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String userId = snapshot.getKey();
                    int likes = snapshot.getValue(Integer.class);

                    totalLikes += likes;

                    RatingDetails ratingDetails = snapshot.child(userId).getValue(RatingDetails.class);
                    if (ratingDetails != null) {
                        String comment = ratingDetails.getComment();
                        if (comment != null) {
                            commentStringBuilder.append("User ID: ").append(userId).append(", Comment: ").append(comment).append("\n");
                        }
                    }
                }

               likesText.setText(totalLikes+" Likes" );
                commentText.setText(commentStringBuilder.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ReviewPage.this, "Error retrieving likes and comments", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateLikes() {
        likesText.setText(likesCount+" Likes" );
    }

    private void updateComment() {
        commentText.setText("Comment: " + comment);
    }

    private void addLike(final String userId) {
        userLikesReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Boolean liked = dataSnapshot.getValue(Boolean.class);
                if (liked == null || !liked) {
                    likesReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Integer likes = dataSnapshot.getValue(Integer.class);
                            if (likes == null) {
                                likes = 0;
                            }
                            likes++;
                            likesReference.child(userId).setValue(likes);
                            userLikesReference.child(userId).setValue(true);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(ReviewPage.this, "Error adding like", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ReviewPage.this, "Error adding like", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void removeLike(final String userId) {
        userLikesReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Boolean liked = dataSnapshot.getValue(Boolean.class);
                if (liked != null && liked) {
                    likesReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Integer likes = dataSnapshot.getValue(Integer.class);
                            if (likes == null || likes <= 0) {
                                likes = 0;
                            } else {
                                likes--;
                            }
                            likesReference.child(userId).setValue(likes);
                            userLikesReference.child(userId).setValue(false);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(ReviewPage.this, "Error removing like", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ReviewPage.this, "Error removing like", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
