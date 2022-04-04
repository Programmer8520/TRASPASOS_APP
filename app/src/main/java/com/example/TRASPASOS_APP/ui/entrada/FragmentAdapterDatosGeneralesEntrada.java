package com.example.TRASPASOS_APP.ui.entrada;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class FragmentAdapterDatosGeneralesEntrada extends FragmentStateAdapter{

    public FragmentAdapterDatosGeneralesEntrada(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = null;
        switch (position)
        {
            //aqui pondremos los nombres de los fragments que utilizaremos
            case 0 :
                fragment=new documentoFragment();
                break;
            case 1 :
                fragment=new verificacionFragment(); //detalleFragment
                break;
            case 2 :
                fragment=new personalEntradaFragment();
                break;
        }

        return fragment;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
