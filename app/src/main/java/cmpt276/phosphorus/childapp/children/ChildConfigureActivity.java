package cmpt276.phosphorus.childapp.children;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;
import java.util.UUID;

import cmpt276.phosphorus.childapp.R;
import cmpt276.phosphorus.childapp.model.Child;
import cmpt276.phosphorus.childapp.model.ChildManager;
import cmpt276.phosphorus.childapp.model.DataManager;
import cmpt276.phosphorus.childapp.model.DataType;
import cmpt276.phosphorus.childapp.model.TaskManager;
import cmpt276.phosphorus.childapp.utils.Intents;

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

    private ChildManager childManager;
    private Child child;

    private static final boolean PERMISSION_ACCEPTED = true;
    private static final boolean PERMISSION_DENIED = false;
    public static final int PERMISSION_REQUEST_CODE = 100;
    public static final String INTENT_TYPE_FOR_GALLERY = "image/*";

    public static final int BINARY_BYTE_SIZE = 1024;
    public static final int BYTE_OFFSET_INTEGER = 0;

    private UUID childUUID;

    private ImageView childPortrait;
    private Uri photoURI;
    private String currentPhotoPath; // Way to retrieve photo from storage

    ActivityResultLauncher<Uri> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.TakePicture(),
            result -> {
                if (!result) {
                    return;
                }
                childPortrait.setImageURI(photoURI);
            });

    ActivityResultLauncher<String> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri == null) {
                    return;
                }
                saveImageFromGallery(uri);
                photoURI = uri;
                childPortrait.setImageURI(photoURI);
            });

    public static Intent makeIntentNewChild(Context context) {
        return makeIntent(context, null);
    }

    public static Intent makeIntent(Context context, Child childObj) {
        Intent intent = new Intent(context, ChildConfigureActivity.class);
        intent.putExtra(Intents.CHILD_UUID_TAG, (childObj != null ? childObj.getUUID().toString() : null));
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
                this.child.setChildPortraitPath(currentPhotoPath);
            } else {
                Child newChild = new Child(cleanedName);
                newChild.setChildPortraitPath(currentPhotoPath);
                newChild.setUuid(childUUID);
                this.childManager.addChild(newChild);
                TaskManager.getInstance()
                        .getAllTasks()
                        .forEach(task -> task.addChild(newChild));
            }

            DataManager.getInstance(this).saveData(DataType.CHILDREN);
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
                DataManager.getInstance(this).saveData(DataType.CHILDREN);
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
            if (cameraPermission) {
                dispatchTakePictureIntent();
            }
        });
    }

    private void createGalleryBtn() {
        Button button = findViewById(R.id.btnUseGallery);
        button.setOnClickListener(v -> {
            boolean galleryPermission = setUpUseGalleryPermissions();
            if (galleryPermission) {
                galleryLauncher.launch(INTENT_TYPE_FOR_GALLERY);
            }
        });
    }

    private void extractIntent() {
        Intent packageInfo = getIntent();
        String intentChildUUID = packageInfo.getStringExtra(Intents.CHILD_UUID_TAG);
        this.child = this.childManager.getChildByUUID(intentChildUUID);
    }

    private void loadValues() {
        TextView childTitleText = findViewById(R.id.configure_child_title);
        Button deleteBtn = findViewById(R.id.btnDelete);
        childPortrait = findViewById(R.id.imgChildPicture);
        childUUID = UUID.randomUUID();

        boolean isEditing = this.isEditingChild();

        if (isEditing) {
            // Update's the text input with the child's name
            EditText childNameEditText = findViewById(R.id.name_edit_text);
            childNameEditText.setText(this.child.getName());
            currentPhotoPath = this.child.getChildPortraitPath();
            childUUID = this.child.getUUID();

            // Dependency from https://github.com/bumptech/glide
            if(this.child.getChildPortraitPath() != null) {
                Glide.with(this)
                        .load(this.child.getChildPortraitPath())
                        .into(childPortrait);
            }
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

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this,
                        this.getPackageName() + ".provider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                cameraLauncher.launch(photoURI);
            }
        }
    }

    // https://stackoverflow.com/questions/45520599/creating-file-from-uri/45520771#45520771
    private void saveImageFromGallery(Uri uri) {
        // Takes image uri from gallery and copies it to app's internal storage
        File photoFile;
        try {
            photoFile = createImageFile();
            InputStream inputStream = getContentResolver().openInputStream(uri);
            OutputStream outputStream = new FileOutputStream(photoFile);
            byte[] buf = new byte[BINARY_BYTE_SIZE];
            int len;
            while ((len = inputStream.read(buf)) > BYTE_OFFSET_INTEGER){
                outputStream.write(buf, BYTE_OFFSET_INTEGER, len);
            }
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = childUUID.toString() + ".jpg";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = new File(storageDir, imageFileName);

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
}