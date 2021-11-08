package cmpt276.phosphorus.childapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import java.util.UUID;

import cmpt276.phosphorus.childapp.model.Child;
import cmpt276.phosphorus.childapp.model.ChildManager;

public class ChildrenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_children);

        this.createBackBtn();

        Child dummy = new Child(UUID.randomUUID().toString());
        ChildManager childMan = ChildManager.getInstance(this);
        childMan.addChild(dummy);
        Log.d("asdfg", childMan.getAllChildren().toString());
        childMan.getFromFile();
        Log.d("asd", childMan.getAllChildren().toString());

    }

    private void createBackBtn(){
        Button button = findViewById(R.id.btnBackChildren);
        button.setOnClickListener(view -> finish());
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, ChildrenActivity.class);
    }

}