package cmpt276.phosphorus.childapp.breathe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import cmpt276.phosphorus.childapp.R;

public class BreatheActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breathe);
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, BreatheActivity.class);
    }
}