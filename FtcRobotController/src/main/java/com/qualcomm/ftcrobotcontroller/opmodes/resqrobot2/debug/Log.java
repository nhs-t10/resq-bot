package com.qualcomm.ftcrobotcontroller.opmodes.resqrobot2.debug;

import android.os.Environment;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;

/**
 * Created by William Patsios on 1/15/2016.
 */
public class Log {
    private static final String FILE_NAME = "robot_log.txt";

    private File log;

    public Log() {
        log = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), FILE_NAME);
    }

    public void writeLine(String text) {
        try {
            FileWriter writer = new FileWriter(log);
            writer.append(text);
            writer.flush();
            writer.close();
        } catch(IOException io) {
            io.printStackTrace();
        }
    }
}
