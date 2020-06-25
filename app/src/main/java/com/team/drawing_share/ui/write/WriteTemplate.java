package com.team.drawing_share.ui.write;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WriteTemplate {
    public String Title;
    public String Writer;
    public String Time;
    public String Image;


    WriteTemplate(String title, String writer,String storageref){

        Title = title;
        Writer = writer;

        long time = System.currentTimeMillis();
        SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-mm-dd_hh:mm:ss");
        Time = dayTime.format(new Date(time));
        Image = storageref.toString() + Time + "_" + Title + ".png";
    }

}
