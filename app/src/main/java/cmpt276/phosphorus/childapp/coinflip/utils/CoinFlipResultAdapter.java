package cmpt276.phosphorus.childapp.coinflip.utils;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import cmpt276.phosphorus.childapp.model.Child;
import cmpt276.phosphorus.childapp.model.ChildManager;
import cmpt276.phosphorus.childapp.model.CoinFlipResult;

// ==============================================================================================
//
// Display all the coin flip resaults with a more spaced out look
//
// ==============================================================================================
public class CoinFlipResultAdapter extends ArrayAdapter<CoinFlipResult> {

    public CoinFlipResultAdapter(Context context, List<CoinFlipResult> results) {
        super(context, android.R.layout.simple_list_item_1, results);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CoinFlipResult coinFlipResult = getItem(position);
        Child child = ChildManager.getInstance(this.getContext()).getChild(coinFlipResult);

        // Ref https://android--code.blogspot.com/2015/08/android-listview-text-size.html
        View view = super.getView(position, convertView, parent);
        TextView tv = view.findViewById(android.R.id.text1);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
        tv.setPadding(50, 100, 0, 100);

        // todo string.xml
        tv.setText(child.getName() + ", " + coinFlipResult.getFlipResult() + ", " + coinFlipResult.getFormattedTime());

        if (coinFlipResult.getDidWin()) {
            tv.setBackgroundColor(Color.GREEN);
        }

        return view;
    }

}