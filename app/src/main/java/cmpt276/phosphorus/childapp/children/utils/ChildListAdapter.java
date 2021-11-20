package cmpt276.phosphorus.childapp.children.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.Nullable;

import java.util.List;

import cmpt276.phosphorus.childapp.R;
import cmpt276.phosphorus.childapp.model.Child;

// https://www.youtube.com/watch?v=WRANgDgM2Zg
public class ChildListAdapter extends ArrayAdapter<Child> {

    public ChildListAdapter(Context context, List<Child> children) {
        super(context, R.layout.child_profile, children);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View childView = convertView;

        if (childView == null) {
            // https://stackoverflow.com/questions/7803771/call-to-getlayoutinflater-in-places-not-in-activity/18942760#18942760
            childView = LayoutInflater.from(getContext()).inflate(R.layout.child_profile, parent, false);
        }

        Child childProfile = getItem(position);

        // Set the image
        ImageView childPortrait = childView.findViewById(R.id.childProfileIcon);

        // https://github.com/bumptech/glide
        if(childProfile.getChildPortraitPath() != null){
            Glide.with(this.getContext()).load(childProfile.getChildPortraitPath()).into(childPortrait);
        }else{
            childPortrait.setImageResource(R.drawable.child_profile_img);
        }

        // Set the name
        TextView childName = childView.findViewById(R.id.child_profile_name);
        childName.setText(childProfile.getName());
        childName.setTypeface(null, Typeface.BOLD);

        return childView;
    }

}