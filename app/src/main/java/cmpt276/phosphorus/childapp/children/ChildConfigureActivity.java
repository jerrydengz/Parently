package cmpt276.phosphorus.childapp.children;

import static cmpt276.phosphorus.childapp.children.utils.BitmapOperations.*;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

import cmpt276.phosphorus.childapp.R;
import cmpt276.phosphorus.childapp.model.Child;
import cmpt276.phosphorus.childapp.model.ChildManager;

    /* Code assistance regarding Camera & Gallery from
        https://developer.android.com/training/basics/intents/result#separate
        https://developer.android.com/training/camera/photobasics#TaskPhotoView
        https://www.youtube.com/watch?v=qO3FFuBrT2E
        https://www.youtube.com/watch?v=HxlAktedIhM
    */

// ==============================================================================================
//
// Activity to handle both creating, and editing of children selected
// from the ChildrenActivity list
//
// ==============================================================================================
public class ChildConfigureActivity extends AppCompatActivity {

    private static final String CHILD_UUID_TAG = "ChildUUIDTag";

    private ChildManager childManager;
    private Child child;

    private static final boolean PERMISSION_ACCEPTED = true;
    private static final boolean PERMISSION_DENIED = false;
    public static final int PERMISSION_REQUEST_CODE = 100;
    public static final String INTENT_TYPE_FOR_GALLERY = "image/*";

    private Bitmap bitmapPortrait;
    private ImageView childPortrait;

    ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK &&
                        result.getData() != null) {
                    Bundle bundle = result.getData().getExtras();
                    // todo: refactor this to save this bitmap, crop image?
                    Bitmap originalBitmap = (Bitmap) bundle.get("data");
                    bitmapPortrait = scaleBitmap(originalBitmap);
                    childPortrait.setImageBitmap(bitmapPortrait);
                }
            }
    );

    ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK &&
                        result.getData() != null) {
                    try {
                        Bitmap originalBitmap = MediaStore.Images.Media.getBitmap(
                                getContentResolver(), result.getData().getData());
                        // todo: refactor this to save this bitmap, crop image?
                        bitmapPortrait = scaleBitmap(originalBitmap);
                        childPortrait.setImageBitmap(bitmapPortrait);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
    );

    public static Intent makeIntentNewChild(Context context) {
        return makeIntent(context, null);
    }

    public static Intent makeIntent(Context context, Child childObj) {
        Intent intent = new Intent(context, ChildConfigureActivity.class);
        intent.putExtra(CHILD_UUID_TAG, (childObj != null ? childObj.getUUID().toString() : null));
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_configure);
        this.childManager = ChildManager.getInstance();

        this.extractIntent(); // Gotta get intent info before we change the title
        int titleId = this.isEditingChild() ? R.string.child_configure_edit_title : R.string.child_configure_create_title;
        this.setTitle(getString(titleId));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        this.loadValues();
        this.createSaveBtn();
        this.createDeleteBtn();
        this.createCameraBtn();
        this.createGalleryBtn();
    }

    // If user select the top left back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    // https://youtu.be/y6StJRn-Y-A
    private void showDialogAlert(@StringRes int title, @StringRes int dec) {
        AlertDialog.Builder dialogWarning = new AlertDialog.Builder(this);
        dialogWarning.setTitle(getResources().getString(title));
        dialogWarning.setMessage(getResources().getString(dec));
        dialogWarning.setPositiveButton(getResources().getString(R.string.dialog_confirm), null);
        dialogWarning.show();
    }

    private void createSaveBtn() {
        EditText childNameEditText = findViewById(R.id.name_edit_text);
        Button button = findViewById(R.id.btnSave);

        button.setOnClickListener(view -> {
            String cleanedName = childNameEditText.getText().toString().trim(); // Removes spaces/white space

            // let user to continue to edit and change, till valid entry is entered, or exit
            if (cleanedName.isEmpty()) {
                this.showDialogAlert(R.string.dialog_title_invalid_name, R.string.dialog_msg_invalid_name);
                return;
            }

            if (cleanedName.length() >= 15) {
                this.showDialogAlert(R.string.dialog_title_name_too_large, R.string.dialog_msg_name_too_large);
                return;
            }

            if (isDuplicateChildName(cleanedName) && !(isEditingChild() && cleanedName.equals(child.getName()))) {
                this.showDialogAlert(R.string.dialog_title_dupe_name, R.string.dialog_msg_dupe_name);
                return;
            }

            if (isEditingChild()) {
                this.child.setName(cleanedName);
            } else {
                // todo: refactor this and model to include bitmap in initialization?
                this.childManager.addChild(new Child(cleanedName));
            }

            this.childManager.saveToFile();
            finish();
        });
    }

    private void createDeleteBtn() {
        Button button = findViewById(R.id.btnDelete);
        button.setOnClickListener(view -> {
            // https://youtu.be/y6StJRn-Y-A
            AlertDialog.Builder dialogWarning = new AlertDialog.Builder(this);
            dialogWarning.setTitle(getResources().getString(R.string.dialog_title_delete));
            dialogWarning.setMessage(getResources().getString(R.string.dialog_msg_delete));
            dialogWarning.setPositiveButton(getResources().getString(R.string.dialog_positive), (dialogInterface, i) -> {
                this.childManager.removeChild(this.child);
                this.childManager.saveToFile();
                finish();
            });
            dialogWarning.setNegativeButton(getResources().getString(R.string.dialog_negative), null);
            dialogWarning.show();
        });
    }

    private void createCameraBtn() {
        Button button = findViewById(R.id.btnUseCamera);
        button.setOnClickListener(v -> {
            boolean cameraPermission = setUpUseCameraPermissions();
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (cameraPermission) {
                if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                    cameraLauncher.launch(cameraIntent);
                }
            }
        });
    }

    private void createGalleryBtn() {
        Button button = findViewById(R.id.btnUseGallery);
        button.setOnClickListener(v -> {
            boolean galleryPermission = setUpUseGalleryPermissions();
            Intent galleryIntent = new Intent(Intent.ACTION_PICK);
            galleryIntent.setType(INTENT_TYPE_FOR_GALLERY);
            if (galleryPermission) {
                galleryLauncher.launch(galleryIntent);
            }
        });
    }

    private void extractIntent() {
        Intent packageInfo = getIntent();
        String intentChildUUID = packageInfo.getStringExtra(CHILD_UUID_TAG);
        if (intentChildUUID != null) {
            this.child = this.childManager.getChildByUUID(UUID.fromString(intentChildUUID));
        }
    }

    private void loadValues() {
        TextView childTitleText = findViewById(R.id.configure_child_title);
        Button deleteBtn = findViewById(R.id.btnDelete);
        childPortrait = findViewById(R.id.imgChildPicture);

        boolean isEditing = this.isEditingChild();

        if (isEditing) {
            // Update's the text input with the child's name
            EditText childNameEditText = findViewById(R.id.name_edit_text);
            childNameEditText.setText(this.child.getName());
        }

        childTitleText.setText(isEditing ? this.child.getName() : getString(R.string.add_child_title));
        childTitleText.setTypeface(null, Typeface.BOLD);
        childTitleText.setTextColor(getResources().getColor(R.color.black, null));
        deleteBtn.setVisibility(isEditing ? View.VISIBLE : View.INVISIBLE);
    }

    private boolean isDuplicateChildName(String childName) {
        return this.childManager.getAllChildren()
                .stream()
                .map(Child::getName)
                .anyMatch(childName::equals);
    }

    private boolean isEditingChild() {
        return this.child != null;
    }

    private boolean setUpUseCameraPermissions() {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {
                        Manifest.permission.CAMERA
                    },
                    PERMISSION_REQUEST_CODE);
        } else {
            // Permission was already granted
            return PERMISSION_ACCEPTED;
        }
        return PERMISSION_DENIED;
    }

    private boolean setUpUseGalleryPermissions() {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    },
                    PERMISSION_REQUEST_CODE);
        } else {
            // Permission was already granted
            return PERMISSION_ACCEPTED;
        }
        return PERMISSION_DENIED;
    }
}