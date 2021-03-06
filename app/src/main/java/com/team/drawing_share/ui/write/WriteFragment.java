package com.team.drawing_share.ui.write;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mukesh.DrawingView;
import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback;
import com.team.drawing_share.DataActivity;
import com.team.drawing_share.R;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WriteFragment extends Fragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener{
    private Button saveButton, penButton, eraserButton, penColorButton, backgroundColorButton,
            loadButton, clearButton;
    private DrawingView drawingView;
    private SeekBar penSizeSeekBar, eraserSizeSeekBar;
    private WriteViewModel writeViewModel;
    private FirebaseAuth firebaseAuth;

    private FirebaseDatabase database;
    private DatabaseReference ref;

    private FirebaseStorage storage;
    private StorageReference storageref;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        writeViewModel =
                ViewModelProviders.of(this).get(WriteViewModel.class);
        View root = inflater.inflate(R.layout.fragment_write, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        storage = FirebaseStorage.getInstance();

        initializeUI(root);
        setListeners();
        return root;
    }
    private void setListeners() {
        saveButton.setOnClickListener(this);
        penButton.setOnClickListener(this);
        eraserButton.setOnClickListener(this);
        penColorButton.setOnClickListener(this);
        backgroundColorButton.setOnClickListener(this);
        penSizeSeekBar.setOnSeekBarChangeListener(this);
        eraserSizeSeekBar.setOnSeekBarChangeListener(this);
        loadButton.setOnClickListener(this);
        clearButton.setOnClickListener(this);
    }
    private void initializeUI(View v) {
        drawingView = v.findViewById(R.id.scratch_pad);
        saveButton = v.findViewById(R.id.save_button);
        loadButton = v.findViewById(R.id.load_button);
        penButton = v.findViewById(R.id.pen_button);
        eraserButton = v.findViewById(R.id.eraser_button);
        penColorButton = v.findViewById(R.id.pen_color_button);
        backgroundColorButton = v.findViewById(R.id.background_color_button);
        penSizeSeekBar = v.findViewById(R.id.pen_size_seekbar);
        eraserSizeSeekBar = v.findViewById(R.id.eraser_size_seekbar);
        clearButton = v.findViewById(R.id.clear_button);
    }

    private String nextIdea(String prev){
        String next = "";

        return next;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save_button:
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("Saving File");
                alert.setMessage("Write your file name.");
                final EditText title_et = new EditText(getActivity());
                alert.setView(title_et);
                alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //realtime database writing process
                        DatabaseReference idearef = ref.child("idea");
                        String title = title_et.getText().toString();
                        String username = firebaseAuth.getCurrentUser().getDisplayName();
                        WriteTemplate idea = new WriteTemplate(title,username,storageref.toString());
                        idearef.push().setValue(idea);

                        //save imagefile(.png) to local file
                        System.out.println(getActivity().getExternalFilesDir(null));
                        drawingView.saveImage(getActivity().getExternalFilesDir(null).getPath(), idea.Time + "_" + title,
                                Bitmap.CompressFormat.PNG, 100);

                        //get imagefile and upload to firebase storage
                        String imgpath = getActivity().getExternalFilesDir(null).getPath()+"/"+idea.Time+"_"+title+".png";
                        Bitmap bm = BitmapFactory.decodeFile(imgpath);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();

                        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();
                        storageref = storage.getReference(idea.Time+"_"+title+".png");

                        UploadTask uploadTask = storageref.putBytes(data);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                                // ...
                            }
                        });



                    }
                });
                alert.setNegativeButton("Don`t save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alert.show();
                break;
            case R.id.load_button:
                drawingView.loadImage(BitmapFactory.decodeResource(getResources(), R.raw.test));
                Log.d("saveImage", "quality cannot better that 100");
                break;
            case R.id.pen_button:
                drawingView.initializePen();
                break;
            case R.id.eraser_button:
                drawingView.initializeEraser();
                break;
            case R.id.clear_button:
                drawingView.clear();
                break;
            case R.id.pen_color_button:
                final ColorPicker colorPicker = new ColorPicker(getActivity(), 100, 100, 100);
                colorPicker.setCallback(
                        new ColorPickerCallback() {
                            @Override public void onColorChosen(int color) {
                                drawingView.setPenColor(color);
                                colorPicker.dismiss();
                            }
                        });
                colorPicker.show();
                break;
            case R.id.background_color_button:
                final ColorPicker backgroundColorPicker = new ColorPicker(getActivity(), 100, 100, 100);
                backgroundColorPicker.setCallback(
                        new ColorPickerCallback() {
                            @Override public void onColorChosen(int color) {
                                drawingView.setBackgroundColor(color);
                                backgroundColorPicker.dismiss();
                            }
                        });
                backgroundColorPicker.show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {
        int seekBarId = seekBar.getId();
        if (seekBarId == R.id.pen_size_seekbar) {
            drawingView.setPenSize(i);
        } else if (seekBarId == R.id.eraser_size_seekbar) {
            drawingView.setEraserSize(i);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}