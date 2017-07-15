package com.head.copyassetslibrary;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;


/*
 * Created by Сергей on 02.05.2017.
 * Класс CopyFile для копирования файла с ассет папки на память устройства
 * наследуеться от абстракного класса Thread для создания потока в котором
 * будет происходить копирование файла. Процес вложен в новый поток что б
 * не тормозить приложение во время выполнения копирования.
 */

public  class CopyFile extends Thread {
    //Переменная класса  Context  который нужно получить от вызывающего класса
        Context context;
    //конструктор класса
    public CopyFile(String nameDir, String nameFile, Context context) {
       super("Поток копирования");
       this.context = context;//получаем контекст
        // получаем путь к SD
        File sdPath = Environment.getExternalStorageDirectory();
        // добавляем свой каталог к пути
        sdPath = new File(sdPath.getAbsolutePath() + nameDir);
        // создаем каталог
        sdPath.mkdirs();
        //обьявляем локальные переменнык класов необходимых для копирования
        AssetManager assetManager = context.getAssets();//AssetManager класс для рабты с ресурсами папки assets
        InputStream in = null;  //Базовый класс InputStream представляет классы, которые получают данные из различных источников
        OutputStream out = null;//Класс OutputStream - это абстрактный класс, определяющий потоковый байтовый вывод
        String newFileName = null;//сюда запишем название файла
        try {
            start();//запускаем новый поток  Thread
            in = assetManager.open( nameFile);//открываем
            newFileName = sdPath + "/" +  nameFile;//записуем название
            out = new FileOutputStream(newFileName);//определяем потоковый байтовый вывод

            byte[] buffer = new byte[1024];
            int read;
            //цыкл побайтово копирует данны из потока
            while ((read = in.read(buffer)) != -1) {//метод read позволяет считать данные в массив байтов и затем производить с ним манипуляции
                out.write(buffer, 0, read);//с помощью метода write строка записывается в файл.
            }
            //Закрываем потоки ввода и вывода
            in.close();
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

}
