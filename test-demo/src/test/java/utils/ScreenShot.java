package utils;

import java.text.SimpleDateFormat;

public class ScreenShot {
    public static String getFileName(){
        SimpleDateFormat sim1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sim2 = new SimpleDateFormat("HH-mm-ss");
        String dirName = sim1.format(System.currentTimeMillis());
        String fileName = sim2.format(System.currentTimeMillis());
        return "./src/test/screenshots/" + dirName + "/" + fileName + ".png";
    }
}