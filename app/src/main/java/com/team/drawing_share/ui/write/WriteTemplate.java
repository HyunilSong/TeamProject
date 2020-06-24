package com.team.drawing_share.ui.write;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WriteTemplate {
    public String Title;
    public String Writer;
    public String Time;

    WriteTemplate(String title, String writer){
        Title = title;
        Writer = writer;
        long time = System.currentTimeMillis();
        SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        Time = dayTime.format(new Date(time));
    }

}
