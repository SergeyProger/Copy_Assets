package com.head.remvel;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
//Импортируемая библиотека для увеличения изображения
import uk.co.senab.photoview.PhotoViewAttacher;


public class MainActivity extends AppCompatActivity implements OptionsFragment.OnSelectedButtonListener, ReadkFragment.OnSelectedMenuRead  {

    private boolean mIsDynamic;

    public static PhotoViewAttacher photoViewAttacher;

    FragmentManager fragmentManager;
    OptionsFragment fragment1;
    ReadkFragment fragment2;
    FragmentTransaction ft;
    RenderDer renderDer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         fragmentManager = getSupportFragmentManager();
         ft = fragmentManager.beginTransaction();
         fragment2 = (ReadkFragment) fragmentManager.findFragmentById(R.id.read_layout);
        //проверка на существование второго фрагмента
         mIsDynamic = (fragment2 == null || !fragment2.isInLayout());

        //если второй нулл загружаем первый.
        if (mIsDynamic) {
            // Создаем и добавляем первый фрагмент
           fragment1 = new OptionsFragment();
            ft.add(R.id.container, fragment1, "fragment1");
            // Подтверждаем операцию
            ft.commit();
        }

    }

    public static void zoom(ImageView image) {
         photoViewAttacher = new PhotoViewAttacher(image);
         photoViewAttacher.update();
    }

    @Override
    public void onButtonSelected(int buttonIndex) {
            // Динамическое переключение на другой фрагмент
            ft = fragmentManager.beginTransaction();
            fragment2 = new ReadkFragment();
            // Подготавливаем аргументы
            Bundle args = new Bundle();
            args.putInt(ReadkFragment.BUTTON_INDEX, buttonIndex);
            fragment2.setArguments(args);
            ft.replace(R.id.container, fragment2, "fragment2");
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(null);
            ft.commit();
        photoViewAttacher.cleanup();

        }

    @Override
    public void onMenuSelected(int menuIndex) {
        ft = fragmentManager.beginTransaction();
        fragment1 = new OptionsFragment();
        ft.replace(R.id.container, fragment1, "fragment1");
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
        photoViewAttacher.cleanup();

    }

}

