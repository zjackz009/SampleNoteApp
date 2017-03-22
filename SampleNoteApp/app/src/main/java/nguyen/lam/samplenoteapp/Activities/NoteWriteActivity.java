package nguyen.lam.samplenoteapp.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import nguyen.lam.samplenoteapp.R;

public class NoteWriteActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_write);

        initView();
    }

    private void initView() {

        ImageButton btnBack = (ImageButton) findViewById(R.id.btn_back);
        btnBack.setOnClickListener(this);

        ImageButton btnSave = (ImageButton) findViewById(R.id.btn_save);
        btnSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.btn_save:
                break;
            default:
                break;
        }
    }
}
