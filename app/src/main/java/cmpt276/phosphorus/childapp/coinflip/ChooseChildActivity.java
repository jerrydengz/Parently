package cmpt276.phosphorus.childapp.coinflip;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.List;

import cmpt276.phosphorus.childapp.R;

public class ChooseChildActivity extends AppCompatActivity {

    public static Intent makeIntent(Context context) {
        return new Intent(context, ChooseChildActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flip_coin_choose_child);

        this.updateChildrenList();
    }

    private void updateChildrenList() {
        ListView listView = findViewById(R.id.listChildren);
        // todo use modal
        List<String> list = Arrays.asList("Josh", "Jerry", "Jack", "Jason", "Kevin", "Bill", "Henry", "George");

        // Ref https://android--code.blogspot.com/2015/08/android-listview-text-size.html
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
                tv.setPadding(50, 100, 0, 100);
                return view;
            }
        };


        listView.setOnItemClickListener((adapterView, view, position, l) -> {
            startActivity(ChooseSideActivity.makeIntent(this, list.get(position)));
            finish(); // We don't want users coming back here
        });

        listView.setAdapter(arrayAdapter);
    }


}