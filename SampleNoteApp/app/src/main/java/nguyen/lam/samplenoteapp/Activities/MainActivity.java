package nguyen.lam.samplenoteapp.Activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioGroup;

import nguyen.lam.samplenoteapp.R;
import nguyen.lam.samplenoteapp.Utilities.Constant;
import nguyen.lam.samplenoteapp.Utilities.PreferenceUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton btnSync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.btn_new_note);
        fab.setOnClickListener(this);

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.btn_switch);
        radioGroup.setOnCheckedChangeListener(onCheckedChangeListener);
        if (PreferenceUtil.getInstance().getBoolean(Constant.PREFERENCE_KEY.PREFERENCE_KEY_AUTO_SYNC, false)) {
            radioGroup.check(R.id.btn_switch_manual);
        } else {
            radioGroup.check(R.id.btn_switch_auto);
        }

        btnSync = (ImageButton) findViewById(R.id.btn_sync);
        btnSync.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_new_note:
                Intent intent = new Intent(MainActivity.this, NoteWriteActivity.class);
                startActivity(intent);
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
}
