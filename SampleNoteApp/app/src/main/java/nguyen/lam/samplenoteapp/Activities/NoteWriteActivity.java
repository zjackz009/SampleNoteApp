package nguyen.lam.samplenoteapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;

import nguyen.lam.samplenoteapp.Layouts.LinedEditText;
import nguyen.lam.samplenoteapp.R;
import nguyen.lam.samplenoteapp.Utilities.Constant;
import nguyen.lam.samplenoteapp.Utilities.FileUtil;
import nguyen.lam.samplenoteapp.Utilities.Utils;

public class NoteWriteActivity extends AppCompatActivity implements View.OnClickListener {

    private LinedEditText txtNote;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_write);
        this.context = this;

        String name = "";
        if (getIntent().hasExtra(Constant.INTENT_KEY.INTENT_KEY_NOTE_NAME)) {
            name = getIntent().getStringExtra(Constant.INTENT_KEY.INTENT_KEY_NOTE_NAME);
        }
        initView(name);
    }

    private void initView(String name) {

        ImageButton btnBack = (ImageButton) findViewById(R.id.btn_back);
        btnBack.setOnClickListener(this);

        ImageButton btnSave = (ImageButton) findViewById(R.id.btn_save);
        btnSave.setOnClickListener(this);
        txtNote = (LinedEditText) findViewById(R.id.txt_note_text);
        if (!TextUtils.isEmpty(name)) {
            String data = FileUtil.readFile(context, Utils.getDeviceId(context), name);
            if (!TextUtils.isEmpty(data)) {
                txtNote.setText(data);
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_save:
                String text = txtNote.getText().toString();
                if(!TextUtils.isEmpty(text)) {
                    String[] lines = Utils.splitString(text, Constant.SYMBOL_BREAK);
                    String firstLine = "";

                    for (String line : lines) {
                        if (!TextUtils.isEmpty(line)) {
                            firstLine = line;
                            break;
                        }
                    }
                    String fileName = firstLine + Constant.SYMBOL_ + System.currentTimeMillis();
                    FileUtil.writeFile(context, Utils.getDeviceId(context), fileName, text);
                    close(RESULT_CANCELED);
                }else {
                    finish();
                }

                break;
            default:
                break;
        }
    }

    private void close(int resultCode){
        Intent intent = getIntent();
        setResult(resultCode,intent);
        NoteWriteActivity.this.finish();
    }
}
