package nguyen.lam.samplenoteapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
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

import java.io.File;
import java.io.IOException;

import nguyen.lam.samplenoteapp.Adapters.NoteItemAdapter;
import nguyen.lam.samplenoteapp.Adapters.NoteItemSwipeHelper;
import nguyen.lam.samplenoteapp.Listeners.NoteItemListener;
import nguyen.lam.samplenoteapp.R;
import nguyen.lam.samplenoteapp.Utilities.Constant;
import nguyen.lam.samplenoteapp.Utilities.FileUtil;
import nguyen.lam.samplenoteapp.Utilities.PreferenceUtil;
import nguyen.lam.samplenoteapp.Utilities.Utils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int REQUEST_CODE_SUCCESS = 0x110;

    private ImageButton btnSync;
    private RecyclerView listNotes;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.context = this;
        initView();
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
        }

        listNotes = (RecyclerView)findViewById(R.id.list_note);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        listNotes.setLayoutManager(layoutManager);
        listNotes.setItemAnimator(new DefaultItemAnimator());

        NoteItemAdapter adapter = new NoteItemAdapter(FileUtil.getAllFileName(context, Utils.getDeviceId(context)),noteItemListener);
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
                    NoteItemAdapter adapter = new NoteItemAdapter(FileUtil.getAllFileName(context, Utils.getDeviceId(context)), noteItemListener);
                    listNotes.setAdapter(adapter);
                }
                break;
            default:break;
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
            File userFolder = context.getDir(Utils.getDeviceId(context), Context.MODE_PRIVATE);
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
        Intent intent = new Intent( context, NoteWriteActivity.class);
        if(!TextUtils.isEmpty(name)) {
            intent.putExtra(Constant.INTENT_KEY.INTENT_KEY_NOTE_NAME, name);
        }
        startActivityForResult(intent, REQUEST_CODE_SUCCESS);
    }
}
