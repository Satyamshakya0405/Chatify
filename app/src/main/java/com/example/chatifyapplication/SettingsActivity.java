package com.example.chatifyapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.net.MalformedURLException;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    FirebaseUser currUser;
    CircleImageView mimageView;
    TextView mStatus,mDisplayname;
    Button mStatusChange,mImageChange;
    private final int code=1;
    StorageReference mFirebaseStorage;
    String uid;
    String download_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        mimageView=findViewById(R.id.settings_image);
        mStatus=findViewById(R.id.settings_status);
        mDisplayname=findViewById(R.id.settings_displayname);
        mStatusChange=findViewById(R.id.settings_changestatus);
        mImageChange=findViewById(R.id.settings_changeimage);

            // change image
        mImageChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryintent=new Intent();
                galleryintent.setType("image/*");
                galleryintent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galleryintent,"SELECT IMAGE"),code);
//                CropImage.activity()
//                        .setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1,1)
//                        .start(SettingsActivity.this);
            }
        });

        // change status
        mStatusChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent statusIntent= new Intent(SettingsActivity.this,StatusChangeActivity.class);
                statusIntent.putExtra("status",mStatus.getText().toString());
                startActivity(statusIntent);
            }
        });


        // Firebase
        currUser= FirebaseAuth.getInstance().getCurrentUser();
         uid=currUser.getUid();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        mFirebaseStorage=FirebaseStorage.getInstance().getReference();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String name=snapshot.child("name").getValue().toString();

                String status=snapshot.child("status").getValue().toString();

                String image=snapshot.child("image").getValue().toString();

                String thumb_image=snapshot.child("thumb_image").getValue().toString();
                mDisplayname.setText(name);
                mStatus.setText(status);
                Picasso.get().load(image).into(mimageView);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == code && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            CropImage.activity(imageUri).setAspectRatio(1,1)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {


                Uri resultUri = result.getUri();
                final StorageReference filepath=mFirebaseStorage.child("profile_images").child(uid+".jpg");
                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                   if(task.isSuccessful())
                   {

                       Toast.makeText(SettingsActivity.this, "Added succesfully", Toast.LENGTH_SHORT).show();
//                      String download_uri=task.getResult().getStorage().getDownloadUrl().toString();
                         download_url=filepath.getDownloadUrl().toString();
                       filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                           @Override
                           public void onSuccess(Uri uri) {
                               download_url=uri.toString();
                               databaseReference.child("image").setValue(download_url).addOnCompleteListener(new OnCompleteListener<Void>() {
                                   @Override
                                   public void onComplete(@NonNull Task<Void> task) {
                                       if(task.isSuccessful())
                                       {

                                           Toast.makeText(SettingsActivity.this, "Added succesfully", Toast.LENGTH_SHORT).show();
                                       }
                                   }
                               });
                           }
                       });

//

                       }
                   else
                   {
                       Toast.makeText(SettingsActivity.this, " Error", Toast.LENGTH_SHORT).show();

                   }
                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
