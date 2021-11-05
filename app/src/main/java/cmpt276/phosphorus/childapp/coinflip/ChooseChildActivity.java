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

import java.util.List;
import java.util.stream.Collectors;

import cmpt276.phosphorus.childapp.R;
import cmpt276.phosphorus.childapp.model.Child;
import cmpt276.phosphorus.childapp.model.ChildManager;
import cmpt276.phosphorus.childapp.utils.Emoji;

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

    // todo gotta worry about duplicates
    private void updateChildrenList() {
        ListView listView = findViewById(R.id.listChildren);
        ChildManager childManager = ChildManager.getInstance();

        List<Child> children = childManager.getAllChildren();
        List<String> childrenNames = children.stream().map(child -> {
            String childName = child.getName();
            boolean didPickLast = child.equals(ChildManager.getInstance().getLastCoinChooserChild());
            return didPickLast ? (childName + " - " + Emoji.STAR.get()) : childName;
        }).collect(Collectors.toList());

        // Ref https://android--code.blogspot.com/2015/08/android-listview-text-size.html
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, childrenNames) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv = view.findViewById(android.R.id.text1);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
                tv.setPadding(50, 100, 0, 100);
                return view;
            }
        };

        listView.setOnItemClickListener((adapterView, view, position, l) -> {
            Child selectedChild = children.get(position);
            if (selectedChild.equals(ChildManager.getInstance().getLastCoinChooserChild())) {
                return; // todo Do we want to enforce them not choosing them again?
            }

            startActivity(ChooseSideActivity.makeIntent(this, selectedChild.getUUID()));
            finish(); // We don't want users coming back here
        });

        listView.setAdapter(arrayAdapter);
    }


}