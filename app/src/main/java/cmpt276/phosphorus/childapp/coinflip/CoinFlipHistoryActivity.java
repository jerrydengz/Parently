package cmpt276.phosphorus.childapp.coinflip;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import cmpt276.phosphorus.childapp.R;
import cmpt276.phosphorus.childapp.coinflip.utils.CoinFlipResultAdapter;
import cmpt276.phosphorus.childapp.model.Child;
import cmpt276.phosphorus.childapp.model.ChildManager;
import cmpt276.phosphorus.childapp.model.CoinFlipResult;

// ==============================================================================================
//
// Shows the sorted by date history of all coin flip resaults
//
// ==============================================================================================
public class CoinFlipHistoryActivity extends AppCompatActivity {

    public static Intent makeIntent(Context context) {
        return new Intent(context, CoinFlipHistoryActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_flip_history);

        this.setTitle(getString(R.string.flip_coin_history_action_title));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.makeList();
    }

    // If user select the top left back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    private List<CoinFlipResult> sortedResultsByTimes() {
        List<CoinFlipResult> all = ChildManager.getInstance(this).getAllChildren()
                .stream()
                .map(Child::getCoinFlipResults)
                .flatMap(List::stream)
                .collect(Collectors.toList());

        return all.stream().sorted(Comparator.comparing(CoinFlipResult::getTime).reversed()).collect(Collectors.toList());
    }

    private void makeList() {
        ListView listView = findViewById(R.id.listHistory);
        CoinFlipResultAdapter adapter = new CoinFlipResultAdapter(this, this.sortedResultsByTimes());
        listView.setAdapter(adapter);
    }

}