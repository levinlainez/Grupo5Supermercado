package supermercado.app.economico.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import supermercado.app.economico.fragments.DeseosFragment;
import supermercado.app.economico.fragments.MisComprasFragment;
import supermercado.app.economico.fragments.PerfilFragment;

public class PagerPerfilAdapter extends FragmentStatePagerAdapter {

    private int numberOfTabs;

    public PagerPerfilAdapter(FragmentManager fm, int numberOfTabs) {
        super(fm);
        this.numberOfTabs = numberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return new PerfilFragment();
            case 1:
                return new MisComprasFragment();
            case 2:
                return new DeseosFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
