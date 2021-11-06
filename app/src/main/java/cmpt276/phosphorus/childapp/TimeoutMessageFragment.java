package cmpt276.phosphorus.childapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

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

        // Code from https://developer.android.com/reference/android/os/Vibrator
        Vibrator vibrator = (Vibrator) requireActivity()
                .getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(VibrationEffect.createOneShot(10000, 100));

        Button btn = view.findViewById(R.id.btnStopTimeout);
        btn.setOnClickListener(v -> {
            player.stop();
            vibrator.cancel();
            dismiss();
        });
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
    }
}