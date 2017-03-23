package nguyen.lam.samplenoteapp.Services;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;

import nguyen.lam.samplenoteapp.Utilities.Utils;


public class UploadService implements Runnable {

    private ArrayList<File> listPath;

    private StorageReference mStorageRef;
    private Context context;

    public UploadService(ArrayList<File> listPath, Context context){
        this.listPath =  listPath;
        this.context = context;
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public void run() {
        UploadTask uploadTask;
        for (File file : listPath) {
            Uri fileUri = Uri.fromFile(file);
            StorageReference  noteStorageReference = mStorageRef.child(""+ Utils.getDeviceId(context) +"/"+ fileUri.getLastPathSegment());
            uploadTask = noteStorageReference.putFile(fileUri);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    Log.e("Upload","LinkDownload:: "+downloadUrl.toString());
                }
            });
        }
    }
}
