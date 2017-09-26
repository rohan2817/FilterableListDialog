package me.rohanpeshkar.filterablelistdialogsample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import me.rohanpeshkar.filterablelistdialog.FilterableListDialog;

/**
 * Created by rohan on 9/22/17.
 */

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initViews();
    }

    private void initViews() {
        Button btnClick = (Button) findViewById(R.id.btn_click);
        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickTest();
            }
        });
    }

    void onClickTest() {
        FilterableListDialog.create(this, getItems(),
                new FilterableListDialog.OnListItemSelectedListener() {
                    @Override
                    public void onItemSelected(String item) {
                        showToast("Selected item : " + item);
                    }
                }).show();
    }

    private ArrayList<String> getItems() {
        ArrayList<String> items = new ArrayList<>();
        items.addAll(Arrays.asList(getResources().getStringArray(R.array.states_array)));
        return items;
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
