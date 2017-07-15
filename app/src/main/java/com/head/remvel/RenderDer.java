package com.head.remvel;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.head.copyassetslibrary.CopyFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static com.head.remvel.ReadkFragment.imageView;

/**
 * Created by Сергей on 02.05.2017.
 */

public class RenderDer extends MainActivity{

    private Context context;


    private  final int REQUEST_WRITE_STORAGE = 112;
    private  boolean READ_FILE_GRANTED = false;

    private final String DIR_SD = "/RemVel/";
    //название файла
    private final String NAMEFILE = "dm.pdf";
    //File Descriptor for rendered Pdf file
    private ParcelFileDescriptor mFileDescriptor;
    //For rendering a PDF document
    private PdfRenderer mPdfRenderer;
    //For opening current page, render it, and close the page
    private PdfRenderer.Page mCurrentPage;

    private static boolean copyTrue = false;

    public static void setCopyTrue(boolean copyTrue) {
        RenderDer.copyTrue = copyTrue;
    }

    RenderDer(Context context){
        this.context = context;
    }


    public void openRenderer() {
        int hasReadContactPermission = ContextCompat.checkSelfPermission(this.context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        // если устройство до API 23, устанавливаем разрешение
        if(hasReadContactPermission == PackageManager.PERMISSION_GRANTED){
            READ_FILE_GRANTED = true;
        }
        else{
            // вызываем диалоговое окно для установки разрешений
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE, },REQUEST_WRITE_STORAGE);
        }
        // если разрешение установлено, загружаем контакты
        if (READ_FILE_GRANTED) {
            try {
                File sdPath = Environment.getExternalStorageDirectory();
                // добавляем свой каталог к пути
                sdPath = new File(sdPath.getAbsolutePath() + "/" + DIR_SD);
                File file = new File(sdPath, NAMEFILE);
                mFileDescriptor = ParcelFileDescriptor.open(file,
                        ParcelFileDescriptor.MODE_READ_ONLY);
                mPdfRenderer = new PdfRenderer(mFileDescriptor);
            } catch (FileNotFoundException e) {
                new CopyFile(DIR_SD, NAMEFILE, context);
             //   if (copyTrue == true)
                    openRenderer();
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public  void closeRenderer() {

        try {
            if (mCurrentPage != null)
                mCurrentPage.close();
            if (mPdfRenderer != null)
                mPdfRenderer.close();
            if (mFileDescriptor != null)
                mFileDescriptor.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    //метод для прорисовки
    public void render(int index) {
        if (mPdfRenderer == null || mPdfRenderer.getPageCount() <= index
                || index < 0) {
            return;
        }
        // For closing the current page before opening another one.
        try {
            if (mCurrentPage != null) {
                mCurrentPage.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Open page with specified index
        mCurrentPage = mPdfRenderer.openPage(index);
        //добавив по 500 к размерам прорисовки получаем возможность при увеличении видеть чёткую картинку.

        Bitmap bitmap = Bitmap.createBitmap(mCurrentPage.getWidth()+500,mCurrentPage.getHeight()+500, Bitmap.Config.ARGB_8888);
        // PdfRenderer класс который позволяет открывать pdf  документ

        //проверка на минемальную
        if (index < 8) {//если пользователь нажал страницу <0
            index = 8;//возвращаем на первую страницу
        }//и максимальную страницу документа
        else if (index > mPdfRenderer.getPageCount ( )) {//если пользователь нажал страницу > max
            index = mPdfRenderer.getPageCount ( ) - 1;//возвращаем на предыдущюю страницу
        }
        //предоставляем для показа нашу страницу
        mCurrentPage.render ( bitmap,null,null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY );
        imageView.setImageBitmap ( bitmap );//Устанавливает точечный рисунок как содержание этого ImageView.
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_WRITE_STORAGE && grantResults.length == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                READ_FILE_GRANTED = true;
            }
            else {
                Toast toast = Toast.makeText(context.getApplicationContext(),
                        "Приложение не сможет работать без копирования книг в память телефона. \n" +
                                " Включите доступ в настройках. ", Toast.LENGTH_LONG);
                toast.show();
            }
            onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

}
