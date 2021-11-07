package cmpt276.phosphorus.childapp.timeout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import cmpt276.phosphorus.childapp.R;

public class TimeoutMessageFragment extends AppCompatDialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.timeout_finished_notification_layout, null);

        // Code from https://stackoverflow.com/questions/27473245/how-to-play-ringtone-sound-in-android-with-infinite-loop/27473353
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        MediaPlayer player = MediaPlayer.create(requireActivity()
                .getApplicationContext(),
                notification);
        player.setLooping(true);
        player.start();

        Button btn = view.findViewById(R.id.btnStopTimeout);
        btn.setOnClickListener(v -> {
            player.stop();
            dismiss();
        });
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
    }
}