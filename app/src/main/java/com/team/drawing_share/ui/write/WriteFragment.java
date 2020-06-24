package com.team.drawing_share.ui.write;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.mukesh.DrawingView;
import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback;
import com.team.drawing_share.R;

public class WriteFragment extends Fragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener{
    private Button saveButton, penButton, eraserButton, penColorButton, backgroundColorButton,
            loadButton, clearButton;
    private DrawingView drawingView;
    private SeekBar penSizeSeekBar, eraserSizeSeekBar;
    private WriteViewModel writeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        writeViewModel =
                ViewModelProviders.of(this).get(WriteViewModel.class);
        View root = inflater.inflate(R.layout.fragment_write, container, false);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save_button:
                drawingView.saveImage(Environment.getExternalStorageDirectory().toString(), "test",
                        Bitmap.CompressFormat.PNG, 100);
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