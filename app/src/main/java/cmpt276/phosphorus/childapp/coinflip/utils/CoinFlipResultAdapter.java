package cmpt276.phosphorus.childapp.coinflip.utils;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cmpt276.phosphorus.childapp.R;
import cmpt276.phosphorus.childapp.model.Child;
import cmpt276.phosphorus.childapp.model.ChildManager;
import cmpt276.phosphorus.childapp.model.CoinFlipResult;

// ==============================================================================================
//
// Display all the coin flip results with a more spaced out look
//
// ==============================================================================================
public class CoinFlipResultAdapter extends ArrayAdapter<CoinFlipResult> {

    public CoinFlipResultAdapter(Context context, List<CoinFlipResult> results) {
        super(context, R.layout.coin_flip_history_item, results);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CoinFlipResult coinFlipResult = getItem(position);
        Child child = ChildManager.getInstance().getChild(coinFlipResult);

        View view = convertView;

        // Custom ListView
        if(view == null){
//            https://stackoverflow.com/questions/7803771/call-to-getlayoutinflater-in-places-not-in-activity/18942760#18942760
            view = LayoutInflater.from(getContext()).inflate(R.layout.coin_flip_history_item, parent, false);
        }

        ImageView resultIcon = view.findViewById(R.id.coin_flip_result_icon);
        if (coinFlipResult.getDidWin()) {
            resultIcon.setImageResource(R.drawable.ic_baseline_check_circle_24);
        } else {
            resultIcon.setImageResource(R.drawable.ic_baseline_cancel_24);
        }

        // set name
        TextView name = view.findViewById(R.id.name_flip_history);
        name.setText(child.getName());

        // set date
        TextView dateTime = view.findViewById(R.id.flip_date_time);
        dateTime.setText(coinFlipResult.getFormattedTime());

        // set coin side chosen text
        String choice = view.getResources().getString(R.string.flip_choice_label)
                + coinFlipResult.getPickedSide();
        TextView flipChoice = view.findViewById(R.id.coin_flip_choice);
        flipChoice.setText(choice);

        // set flip result text
        String result = view.getResources().getString(R.string.flip_result_label)
                + coinFlipResult.getFlipResult();
        TextView flipResult = view.findViewById(R.id.flip_result);
        flipResult.setText(result);

        return view;
    }

}