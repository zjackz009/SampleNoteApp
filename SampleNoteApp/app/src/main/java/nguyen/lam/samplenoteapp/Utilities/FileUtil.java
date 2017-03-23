package nguyen.lam.samplenoteapp.Utilities;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FileUtil {

    private static final String TAG = FileUtil.class.getSimpleName();

    public synchronized static void writeFile(Context context, String parentPath, String fileName, String data) {
        File parentFolder = context.getDir(parentPath, Context.MODE_PRIVATE);
        if (!parentFolder.exists()) {
            parentFolder.mkdirs();
        }

        File dateType = new File(parentFolder.getPath() + "/" + fileName);

        try {
            if (!dateType.exists()) {

                FileOutputStream fileout = new FileOutputStream(dateType);
                fileout.write(data.getBytes());
                fileout.close();

            } else {
                delete(dateType);
                writeFile(context, parentPath, fileName, data);
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public static String readFile(Context context, String parentPath, String fileName) {
        File parentFolder = context.getDir(parentPath, Context.MODE_PRIVATE);
        if (!parentFolder.exists()) {
            return "";
        }
        File dateType = new File(parentFolder.getPath() + "/" + fileName);

        if (!dateType.exists()) {
            return "";
        }
        try {
            FileInputStream fi = new FileInputStream(dateType);
            InputStreamReader reader = new InputStreamReader(fi);
            BufferedReader bufferedReader = new BufferedReader(reader);

            StringBuilder textBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                line = line+"\n";
                textBuilder.append(line);
            }
            return textBuilder.toString();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            return "";
        }
    }

    public static boolean delete(File file) throws IOException {

        if (file.exists()) {
            if (file.isFile()) {
                return file.delete();

            } else if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File item : files) {
                    delete(item);
                }
                return file.delete();
            }
        }
        return false;
    }

    public static ArrayList<String> getAllFileName(Context context,String parentPath){
        ArrayList<String> listFile = new ArrayList<>();

        File userFolder = context.getDir(parentPath, Context.MODE_PRIVATE);
        if (!userFolder.exists()) {
            return null;
        }

        File[] listOfFiles = userFolder.listFiles();

        for (File file : listOfFiles) {
            if (file.isFile()) {
                listFile.add(file.getName());
            }
        }

        return listFile;
    }
}
