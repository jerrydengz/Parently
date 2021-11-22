package cmpt276.phosphorus.childapp.children;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
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
import cmpt276.phosphorus.childapp.model.child.Child;
import cmpt276.phosphorus.childapp.model.child.ChildManager;
import cmpt276.phosphorus.childapp.model.data.DataManager;
import cmpt276.phosphorus.childapp.model.data.DataType;
import cmpt276.phosphorus.childapp.model.task.TaskManager;
import cmpt276.phosphorus.childapp.children.utils.PermissionsEnumHelper;
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
    public static final String FILE_SUFFIX = ".jpg";

    private ImageView childPortrait;
    private File storageDir;
    private Uri photoURI;
    private String currentPhotoPath; // Way to retrieve photo from storage
    private String tempPhotoPath;
    private boolean toSaveTempFile;

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

    // Handling the case if user uploads an image file but ends up not wanting to save it
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!toSaveTempFile && tempPhotoPath != null) {
                deleteImageFile(tempPhotoPath);
        }
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

            final int MAX_CHAR_LENGTH = 15;
            if (cleanedName.length() >= MAX_CHAR_LENGTH) {
                this.showDialogAlert(R.string.dialog_title_name_too_large, R.string.dialog_msg_name_too_large);
                return;
            }

            if (isDuplicateChildName(cleanedName) && !(isEditingChild() && cleanedName.equals(child.getName()))) {
                this.showDialogAlert(R.string.dialog_title_dupe_name, R.string.dialog_msg_dupe_name);
                return;
            }

            if (isEditingChild()) {
                if(tempPhotoPath != null && currentPhotoPath != null) {
                    deleteImageFile(currentPhotoPath);
                }
                setThePhotoPath();
                this.child.setName(cleanedName);
                this.child.setChildPortraitPath(currentPhotoPath);
            } else {
                setThePhotoPath();
                Child newChild = new Child(cleanedName);
                newChild.setChildPortraitPath(currentPhotoPath);
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
                TaskManager.getInstance()
                        .getAllTasks()
                        .forEach(task -> task.removeChild(child.getUUID()));
                this.childManager.removeChild(this.child);

                DataManager.getInstance(this).saveData(DataType.CHILDREN);
                if(currentPhotoPath != null) {
                    deleteImageFile(currentPhotoPath);
                }
                finish();
            });
            dialogWarning.setNegativeButton(getResources().getString(R.string.dialog_negative), null);
            dialogWarning.show();
        });
    }

    private void createCameraBtn() {
        Button button = findViewById(R.id.btnUseCamera);
        button.setOnClickListener(v -> {
            boolean cameraPermission = setUpAPermission(PermissionsEnumHelper.CAMERA);
            if (cameraPermission) {
                dispatchTakePictureIntent();
            }
        });
    }

    private void createGalleryBtn() {
        Button button = findViewById(R.id.btnUseGallery);
        button.setOnClickListener(v -> {
            boolean galleryPermission = setUpAPermission(PermissionsEnumHelper.READ_EXTERNAL_STORAGE);
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
        storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if (isEditingChild()) {
            // Update's the text input with the child's name
            EditText childNameEditText = findViewById(R.id.name_edit_text);
            childNameEditText.setText(this.child.getName());
            currentPhotoPath = this.child.getChildPortraitPath();

            // Dependency from https://github.com/bumptech/glide
            if(this.child.getChildPortraitPath() != null) {
                Glide.with(this)
                        .load(this.child.getChildPortraitPath())
                        .into(childPortrait);
            }
        }

        childTitleText.setText(isEditingChild() ? this.child.getName() : getString(R.string.add_child_title));
        childTitleText.setTypeface(null, Typeface.BOLD);
        childTitleText.setTextColor(getResources().getColor(R.color.black, null));
        deleteBtn.setVisibility(isEditingChild() ? View.VISIBLE : View.INVISIBLE);
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

    private boolean setUpAPermission(PermissionsEnumHelper permissionType) {
        String type;
        if (permissionType == PermissionsEnumHelper.CAMERA) {
            type = Manifest.permission.CAMERA;
        } else {
            type = Manifest.permission.READ_EXTERNAL_STORAGE;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return requestAPermission(PermissionsEnumHelper.MANAGE_EXTERNAL_STORAGE, type);
        } else {
            return requestAPermission(PermissionsEnumHelper.WRITE_EXTERNAL_STORAGE, type);
        }
    }

    private boolean requestAPermission(PermissionsEnumHelper storageType, String permissionType) {
        String storage;
        if (storageType == PermissionsEnumHelper.MANAGE_EXTERNAL_STORAGE) {
            /*
            Note that we have already checked for proper Build.VERSION.SDK_INT in setUpAPermission,
            so just ignore this warning as it's already been accounted for and would be redundant
             */
            storage = Manifest.permission.MANAGE_EXTERNAL_STORAGE;
        } else {
            storage = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        }
        if ((ContextCompat.checkSelfPermission(this, permissionType) != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(this, storage) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[] {permissionType, storage}, PERMISSION_REQUEST_CODE);
        } else {
            // Permission was already granted
            return PERMISSION_ACCEPTED;
        }
        return PERMISSION_DENIED;
    }

    @SuppressLint("QueryPermissionsNeeded")
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

        /*
            Originally: String imageFileName = childUUID.toString() + ".jpg";
            Possible fix for overwriting/changing a child's image
            is to just make every file name different, tbh file being child's uuid name doesn't matter
            (the files still get deleted properly dw).
            Remove this comment on final clean up of iteration 2
         */
        String suffix = UUID.randomUUID().toString();
        String imageFileName = suffix + FILE_SUFFIX;

        File image = new File(storageDir, imageFileName);

        // Save a file: path for use with ACTION_VIEW intents
        // tempPhotoPath not set as current in case of changing picture
        tempPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void setThePhotoPath() {
        if (tempPhotoPath != null) {
            toSaveTempFile = true;
            currentPhotoPath = tempPhotoPath;
        }
    }

    //https://stackoverflow.com/questions/24659704/how-do-i-delete-files-programmatically-on-android
    private void deleteImageFile(String path) {
        File fileToDelete = new File(path);
        if (fileToDelete.exists()) {
            // Just note on clean up that this is a boolean
            if (fileToDelete.delete()) {
                System.out.println("File Deleted: " + path);
            } else {
                System.out.println("File not Deleted: " + path);
            }
        }
    }
}