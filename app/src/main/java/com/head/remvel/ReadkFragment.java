package com.head.remvel;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class ReadkFragment extends Fragment implements View.OnClickListener {

    // Имя для аргумента
    public static final String BUTTON_INDEX = "button_index";
    // Значение по умолчанию
    private static final int BUTTON_INDEX_DEFAULT = -1;

    private final String PAGE_INDEX = "index";

    private int currentPage ; //номер страницы

    private Button next,previous;

    public static ImageView imageView;

    RenderDer renderDer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View readView = inflater.inflate(R.layout.read_layout, container, false);

        imageView = (ImageView)readView.findViewById(R.id.image);

        MainActivity.zoom(imageView);

        next = (Button)readView.findViewById( R.id.next);
        next.setOnClickListener(this);
        previous = (Button) readView.findViewById( R.id.previous);
        previous.setOnClickListener(this);

        if (savedInstanceState != null) {
            currentPage = savedInstanceState.getInt(PAGE_INDEX, 0);
        }else {
            // Получим индекс, если имеется
            Bundle args = getArguments();
            int buttonIndex = args != null ? args.getInt(BUTTON_INDEX,
                    BUTTON_INDEX_DEFAULT) : BUTTON_INDEX_DEFAULT;
            // Если индекс обнаружен, то используем его
            if (buttonIndex != BUTTON_INDEX_DEFAULT) {
                currentPage = buttonIndex;
            }
        }
        renderDer = new RenderDer(this.getContext());
        renderDer.openRenderer();
        renderDer.render(currentPage);

        return readView;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuIndex = item.getItemId();
        switch (menuIndex) {
            case R.id.action_main:
                // Handle fragment menu item
              OnSelectedMenuRead lisner = (OnSelectedMenuRead)getActivity();
                lisner.onMenuSelected(menuIndex);
                renderDer.closeRenderer();
                return true;
            default:
                // Not one of ours. Perform default menu processing
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        switch (viewId) {
            case R.id.next:
                currentPage++;
                renderDer.render(currentPage);
                break;
            case R.id.previous:
                currentPage--;
                renderDer.render(currentPage);
                break;
        }

    }

    public interface OnSelectedMenuRead {
        void onMenuSelected(int menuIndex);

    }

    public void onSaveInstanceState(Bundle saveInstanceState) {
        // Сохраняем номер страницы
        saveInstanceState.putInt(PAGE_INDEX,currentPage);
        // всегда вызывайте суперкласс для сохранения состояний видов
        super.onSaveInstanceState(saveInstanceState);
    }

}
