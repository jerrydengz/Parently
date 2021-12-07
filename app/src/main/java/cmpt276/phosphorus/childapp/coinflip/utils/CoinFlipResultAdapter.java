package cmpt276.phosphorus.childapp.coinflip.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cmpt276.phosphorus.childapp.R;
import cmpt276.phosphorus.childapp.model.child.Child;
import cmpt276.phosphorus.childapp.model.child.ChildManager;
import cmpt276.phosphorus.childapp.model.coin.CoinFlipResult;

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
        View view = convertView;

        if (view == null) {
            // https://stackoverflow.com/questions/7803771/call-to-getlayoutinflater-in-places-not-in-activity/18942760#18942760
            view = LayoutInflater.from(getContext()).inflate(R.layout.coin_flip_history_item, parent, false);
        }

        CoinFlipResult coinFlipResult = getItem(position);

        ImageView resultIcon = view.findViewById(R.id.coinFlipResultIcon);
        int resultImg = coinFlipResult.getDidWin() ? R.drawable.ic_baseline_check_circle_24 : R.drawable.ic_baseline_cancel_24;
        resultIcon.setImageResource(resultImg);

        TextView name = view.findViewById(R.id.nameFlipHistory);
        Child child = ChildManager.getInstance().getChild(coinFlipResult);
        name.setText(child.getName());

        // set child image
        ImageView childPortraitFlipHistory = view.findViewById(R.id.childPortraitFlipHistory);

        // https://github.com/bumptech/glide
        if (child.getChildPortraitPath() != null) {
            Glide.with(this.getContext())
                    .load(child.getChildPortraitPath())
                    .into(childPortraitFlipHistory);
        } else {
            childPortraitFlipHistory.setImageResource(R.drawable.child_portrait_default);
        }

        TextView dateTime = view.findViewById(R.id.flipDateTime);
        dateTime.setText(coinFlipResult.getFormattedTime());

        String choice = view.getResources().getString(R.string.flipChoiceLabel) + coinFlipResult.getPickedSide();
        TextView flipChoice = view.findViewById(R.id.coinFlipChoice);
        flipChoice.setText(choice);

        String result = view.getResources().getString(R.string.flipResultLabel) + coinFlipResult.getFlipResult();
        TextView flipResult = view.findViewById(R.id.flipResult);
        flipResult.setText(result);

        return view;
    }
}