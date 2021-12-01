package supermercado.app.economico.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import supermercado.app.economico.fragments.CategoriasFragment;
import supermercado.app.economico.fragments.UsuariosFragment;
import supermercado.app.economico.fragments.VentasFragment;

public class PagerAdministradorAdapter extends FragmentStatePagerAdapter{

    private int numberOfTabs;

    public PagerAdministradorAdapter(FragmentManager fm, int numberOfTabs) {
        super(fm);
        this.numberOfTabs = numberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return new UsuariosFragment();
            case 1:
                return new CategoriasFragment();
            case 2:
                return new VentasFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
