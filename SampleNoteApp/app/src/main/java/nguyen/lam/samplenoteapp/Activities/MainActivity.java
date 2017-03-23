package nguyen.lam.samplenoteapp.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioGroup;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.IOException;

import nguyen.lam.samplenoteapp.Adapters.NoteItemAdapter;
import nguyen.lam.samplenoteapp.Adapters.NoteItemSwipeHelper;
import nguyen.lam.samplenoteapp.Listeners.NoteItemListener;
import nguyen.lam.samplenoteapp.R;
import nguyen.lam.samplenoteapp.Services.UploadService;
import nguyen.lam.samplenoteapp.Utilities.Constant;
import nguyen.lam.samplenoteapp.Utilities.FileUtil;
import nguyen.lam.samplenoteapp.Utilities.PreferenceUtil;
import nguyen.lam.samplenoteapp.Utilities.Utils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int REQUEST_CODE_SUCCESS = 0x110;
    private static final int PERMISSION_REQUEST_CODE = 0x120;

    private ImageButton btnSync;
    private RecyclerView listNotes;
    private Context context;
    private FirebaseAuth mAuth;
    private NoteItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.context = this;
        mAuth = FirebaseAuth.getInstance();
        checkWritePermission();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(null == mAuth.getCurrentUser()) {
            mAuth.signInAnonymously()
                    .addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Log.d(TAG, "signInAnonymously:SUCCESS");
                        }
                    })
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Log.e(TAG, "signInAnonymously:FAILURE", exception);
                        }
                    });
        }

    }

    private void initView() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.btn_new_note);
        fab.setOnClickListener(this);

        btnSync = (ImageButton) findViewById(R.id.btn_sync);
        btnSync.setOnClickListener(this);

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.btn_switch);
        radioGroup.setOnCheckedChangeListener(onCheckedChangeListener);
        if (!PreferenceUtil.getInstance().getBoolean(Constant.PREFERENCE_KEY.PREFERENCE_KEY_AUTO_SYNC, false)) {
            radioGroup.check(R.id.btn_switch_manual);
        } else {
            radioGroup.check(R.id.btn_switch_auto);
            new Thread(new UploadService(FileUtil.getAllFile(context, Utils.getDeviceId(context)), context)).start();
        }

        listNotes = (RecyclerView)findViewById(R.id.list_note);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        listNotes.setLayoutManager(layoutManager);
        listNotes.setItemAnimator(new DefaultItemAnimator());

        adapter = new NoteItemAdapter(FileUtil.getAllFileName(context, Utils.getDeviceId(context)),noteItemListener);
        listNotes.setAdapter(adapter);

        ItemTouchHelper.Callback callback = new NoteItemSwipeHelper(adapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(listNotes);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_CODE_SUCCESS:
                if(resultCode ==RESULT_CANCELED) {
                    adapter.update(FileUtil.getAllFileName(context, Utils.getDeviceId(context)));
                }
                break;
            default:break;
        }
    }

    @Override
    public void onBackPressed() {
        if (PreferenceUtil.getInstance().getBoolean(Constant.PREFERENCE_KEY.PREFERENCE_KEY_AUTO_SYNC, false)) {
            new Thread(new UploadService(FileUtil.getAllFile(context, Utils.getDeviceId(context)), context)).start();
        }
        super.onBackPressed();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {
               Log.e(TAG,"Permission Grant");
            }
            break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_new_note:
                startNote("");
                break;
            case R.id.btn_sync:
                new Thread(new UploadService(FileUtil.getAllFile(context, Utils.getDeviceId(context)), context)).start();
                break;
            default:
                break;
        }
    }

    RadioGroup.OnCheckedChangeListener onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            int groupId = group.getId();
            switch (groupId) {
                case R.id.btn_switch:
                    switch (checkedId) {
                        case R.id.btn_switch_manual:
                            btnSync.setVisibility(View.VISIBLE);
                            PreferenceUtil.getInstance().putBoolean(Constant.PREFERENCE_KEY.PREFERENCE_KEY_AUTO_SYNC, false);
                            break;
                        case R.id.btn_switch_auto:
                            btnSync.setVisibility(View.GONE);
                            PreferenceUtil.getInstance().putBoolean(Constant.PREFERENCE_KEY.PREFERENCE_KEY_AUTO_SYNC, true);
                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    break;
            }
        }
    };

    NoteItemListener noteItemListener = new NoteItemListener() {
        @Override
        public void onItemClick(String name) {

            startNote(name);
        }

        @Override
        public void onItemRemove(String name) {
            File userFolder = new File(Environment.getExternalStorageDirectory().getPath()+Constant.ROOT_PATH+context.getString(R.string.app_name)+"/"+Utils.getDeviceId(context));
            if (!userFolder.exists()) {
                return;
            }
            File dateType = new File(userFolder.getPath() + "/" + name);

            if (!dateType.exists()) {
                return;
            }
            try {
                FileUtil.delete(dateType);
            } catch (IOException e) {
                Log.e(TAG,e.getMessage());
            }
        }
    };

    private void startNote(String name){
        if(checkWritePermission()) {
            Intent intent = new Intent(context, NoteWriteActivity.class);
            if (!TextUtils.isEmpty(name)) {
                intent.putExtra(Constant.INTENT_KEY.INTENT_KEY_NOTE_NAME, name);
            }
            startActivityForResult(intent, REQUEST_CODE_SUCCESS);
        }
    }

    private boolean checkWritePermission(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        } else {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                return false;
            } else {
                return true;
            }
        }
    }
}
