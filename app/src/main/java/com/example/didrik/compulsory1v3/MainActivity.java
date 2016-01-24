package com.example.didrik.compulsory1v3;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * The class to be considered as the entry point of the application.
 * @author dids92
 */
public class MainActivity extends AppCompatActivity {

    private static final int TAKE_PHOTO = 0;
    private static final int PICK_IMAGE = 1;

    private static final int REQUEST_CAMERA_RW = 10;

    private static final String CAMERA = "Take Photo";
    private static final String GALLERY = "Choose from Gallery";
    private static final String CANCEL = "Cancel";

    private String [] options = { CAMERA, GALLERY, CANCEL };

    /**
     * Gives information to a camera application where to
     * store a captured image.
     */
    private String pathToCapturedPhoto;

    /**
     * Database operations
     */
    private ApplicationDatabase myDB;

    /**
     * Renders the view on a mobile device and adds drawable
     * resources (if they dont exist in the database).
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Resources resources = getResources();
        Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                resources.getResourcePackageName(R.drawable.img) + "/" +
                resources.getResourceTypeName(R.drawable.img) + "/" +
                resources.getResourceEntryName(R.drawable.img));

        myDB = new ApplicationDatabase(this, null, null, 1);
        if(!myDB.exists("Didrik"))
            myDB.addPerson(new Person("Didrik", uri.toString()));
    }

    /**
     * Pressing either the "Names", "Images" or "Learn" button, will start
     * either "NamesActivity", "ImagesActivity" or "LearnActivity",
     * respectively. The "Add Person" button will also invoke invoke
     * this method, but will use existing activities before resuming to
     * MainActivity.
     * @param view The item which has been clicked.
     */
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.names :
                startActivity(new Intent(this, NamesActivity.class));
                break;
            case R.id.images :
                startActivity(new Intent(this, ImagesActivity.class));
                break;
            case R.id.learn :
                ArrayList<Person> list = myDB.fetchAll();
                if(list.size() == 0)
                    Toast.makeText(getApplicationContext(), "Nothing to learn.", Toast.LENGTH_SHORT).show();
                else
                    startActivity(new Intent(this, LearnActivity.class));
                break;
            case R.id.addPerson :
                addMember();
                break;
            default :
                Toast.makeText(getApplicationContext(), "Something wrong happened", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * Adds a person to the database. If the input field contains a null-reference,
     * an empty string or is already contained in the database as a primary key,
     * the user will be given suitable information. The activity will otherwise
     * proceed with asking the user for an image which shall be associated to the
     * value entered in the inputfield.
     */
    private void addMember() {
        EditText editText = (EditText) findViewById(R.id.personName);
        String name = editText.getText().toString();
        if(name == null || name.equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter a name", Toast.LENGTH_SHORT).show();
        } else if(myDB.exists(name)) {
            Toast.makeText(getApplicationContext(), name + " already exists...", Toast.LENGTH_SHORT).show();
        } else {
            showImageOptions();
        }
    }

    /**
     * Prompts options to the user, where he/she wants to capture an image
     * with a camera application, choose an image from the image gallery,
     * or simply cancel.
     */
    public void showImageOptions() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Add a photo:");
        alertDialog.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which < 0 || which >= options.length) return;
                else {
                    switch (options[which]) {
                        case CAMERA:
                            //Versions at or above API level 23 (Marshmallow) require runtime permissions
                            //Relevant permissions include CAMERA, READ_EXTERNAL and WRITE_EXTERNAL.
                            //READ_EXTERNAL is implicitly underlying WRITE_EXTERNAL.
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                                        && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                    initiateCamera();
                                } else {
                                    //An explaining text will be given the user if necessary.
                                    if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                                        Toast.makeText(MainActivity.this, "Camera permission is needed to use the camera",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                    requestPermissions(new String[]{Manifest.permission.CAMERA,
                                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_RW);
                                }
                            } else {
                                initiateCamera();
                            }

                            break;
                        case GALLERY:
                            Intent galleryIntent;
                            //Versions above API level 19 (KitKat)
                            if(Build.VERSION.SDK_INT < 19) {
                                galleryIntent = new Intent();
                                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                                galleryIntent.setType("image/*");
                            } else {
                                galleryIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                                galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
                                galleryIntent.setType("image/*");
                            }
                            startActivityForResult(galleryIntent, PICK_IMAGE);
                            break;
                        default:
                            break;
                    }
                }
            }
        });
        alertDialog.show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String [] permissions, int [] results) {
        if(requestCode == REQUEST_CAMERA_RW) {
            if(results.length == 2
                    && results[0] == PackageManager.PERMISSION_GRANTED
                    && results[1] == PackageManager.PERMISSION_GRANTED) {
                initiateCamera();
            } else {
                Toast.makeText(MainActivity.this, "Permission was not granted", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, results);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_OK
                || (requestCode != TAKE_PHOTO && requestCode != PICK_IMAGE))
            return;
        //String path;
        Uri selectedImage;
        if(requestCode == TAKE_PHOTO) {
            File file = new File(pathToCapturedPhoto);
            try {
                ExifInterface exif = new ExifInterface(file.getPath());
                RotateUtil.rotate(exif, file);
            } catch(Exception e) { throw new Error(e); }

            selectedImage = Uri.fromFile(file);
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            values.put(MediaStore.MediaColumns.DATA, pathToCapturedPhoto);
            getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            //path = selectedImage.getPath();
        } else {
            selectedImage = data.getData();
           /* if(Build.VERSION.SDK_INT < 11)
                path = RealPathUtil.getRealPathFromURI_BelowAPI11(this, selectedImage);
            else if(Build.VERSION.SDK_INT < 19)
                path = RealPathUtil.getRealPathFromURI_API11to18(this, selectedImage);
            else
                path = RealPathUtil.getRealPathFromURI_API19(this, selectedImage);
                */
        }

        EditText editText = (EditText) findViewById(R.id.personName);
        String name = editText.getText().toString();

        myDB.addPerson(new Person(name, selectedImage.toString()));
    }

    private void initiateCamera() {
        Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (photoIntent.resolveActivity(getPackageManager()) != null) {
            File file = null;
            //TODO Implement throws IOException on createImageFile() !
            try {
                file = createImageFile();
            } catch (Exception e) {
                Log.i("MAINACTIVITY", "Create file failed");
                throw new Error(e);
            }
            if (file != null) {
                photoIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(file));
                startActivityForResult(photoIntent, TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() {
        File storageDirectory = null;
        File image = null;
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = "JPEG_" + timeStamp + "_";
            storageDirectory = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES
            );
            image = File.createTempFile(
                    fileName,
                    ".jpg",
                    storageDirectory
            );
            pathToCapturedPhoto = /*"file:" +*/ image.getAbsolutePath();
        } catch(Exception e) {
            Log.i("MAINACTIVITY", "Failed to create file..");
            throw new Error(e); }
        return image;
    }
}