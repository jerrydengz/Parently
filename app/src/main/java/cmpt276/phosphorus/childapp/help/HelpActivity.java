package cmpt276.phosphorus.childapp.help;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Objects;

import cmpt276.phosphorus.childapp.R;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        this.setTitle(getString(R.string.help_activity_title));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        this.setLinkFunctionality();
    }

    // If user select the top left back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, HelpActivity.class);
    }

    private void setLinkFunctionality() {
        // https://stackoverflow.com/questions/2734270/how-to-make-links-in-a-textview-clickable
        TextView coinImgLink = findViewById(R.id.coin_img_link);
        TextView timerImgLink = findViewById(R.id.timer_relax_bg_link);
        TextView appIconLink = findViewById(R.id.app_icon_link);
        TextView menuIconLink = findViewById(R.id.menu_icon_link);
        TextView appBackgroundLink = findViewById(R.id.app_bg_link);
        TextView victorySoundLink = findViewById(R.id.victory_sound_link);
        TextView defeatSoundLink = findViewById(R.id.defeat_sound_link);

        // Sets the hyperlink functionality to be clickable
        coinImgLink.setMovementMethod(LinkMovementMethod.getInstance());
        timerImgLink.setMovementMethod(LinkMovementMethod.getInstance());
        appIconLink.setMovementMethod(LinkMovementMethod.getInstance());
        menuIconLink.setMovementMethod(LinkMovementMethod.getInstance());
        appBackgroundLink.setMovementMethod(LinkMovementMethod.getInstance());
        victorySoundLink.setMovementMethod(LinkMovementMethod.getInstance());
        defeatSoundLink.setMovementMethod(LinkMovementMethod.getInstance());
    }
}