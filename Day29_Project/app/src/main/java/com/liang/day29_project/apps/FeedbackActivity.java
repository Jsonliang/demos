package com.liang.day29_project.apps;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.liang.day29_project.R;

public class FeedbackActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_title,et_content ;
    private Button btn_commit,btn_cancel ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_feedback);

        initView();
    }

    private void initView() {
        et_title = (EditText) findViewById(R.id.feedb_editText);
        et_content= (EditText) findViewById(R.id.feedb_edit_content);

        btn_commit = (Button) findViewById(R.id.feedb_commit);
        btn_cancel = (Button) findViewById(R.id.feedb_cancel);

        btn_commit.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.feedb_commit:

            break;
            case  R.id.feedb_cancel:

            break;
        }
    }
}
