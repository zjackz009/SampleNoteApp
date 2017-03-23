package nguyen.lam.samplenoteapp.Utilities;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import nguyen.lam.samplenoteapp.R;

public class FileUtil {

    private static final String TAG = FileUtil.class.getSimpleName();

    public synchronized static void writeFile(Context context, String parentPath, String fileName, String data) {
        File parentFolder = new File(Environment.getExternalStorageDirectory().getPath()+Constant.ROOT_PATH+context.getString(R.string.app_name)+"/"+parentPath);
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
        File parentFolder = new File(Environment.getExternalStorageDirectory().getPath()+Constant.ROOT_PATH+context.getString(R.string.app_name)+"/"+parentPath);
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

        File userFolder = new File(Environment.getExternalStorageDirectory().getPath()+Constant.ROOT_PATH+context.getString(R.string.app_name)+"/"+parentPath);
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

    public static ArrayList<File> getAllFile(Context context,String parentPath){

        File userFolder =  new File(Environment.getExternalStorageDirectory().getPath()+Constant.ROOT_PATH+context.getString(R.string.app_name)+"/"+parentPath);
        if (!userFolder.exists()) {
            return null;
        }

        File[] listOfFiles = userFolder.listFiles();

        return new ArrayList<File>(Arrays.asList(listOfFiles));
    }
}
