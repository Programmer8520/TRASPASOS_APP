package com.example.TRASPASOS_APP.ui.salida;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class FragmentAdapterDatosRecuperados extends FragmentStateAdapter {
    public FragmentAdapterDatosRecuperados(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = null;
        switch (position)
        {
            case 0 :
                fragment=new datosRecuperadosFragment();

                break;
            case 1 :
                fragment=new continuarEscaneoFragment();
                break;

        }

        return fragment;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
