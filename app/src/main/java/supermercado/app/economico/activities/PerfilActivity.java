package supermercado.app.economico.activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import supermercado.app.economico.R;
import supermercado.app.economico.adapters.PagerPerfilAdapter;
import supermercado.app.economico.utils.SharedPrefManager;

public class PerfilActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

            setToolbar();

            TabLayout tabLayout = (TabLayout)findViewById(R.id.tabLayoutPerfil);
            tabLayout.addTab(tabLayout.newTab().setText("Perfil"));
            tabLayout.addTab(tabLayout.newTab().setIcon(R.mipmap.ic_ventas_on));
            tabLayout.addTab(tabLayout.newTab().setText("Deseos"));
            tabLayout.setTabGravity(tabLayout.GRAVITY_FILL);

            final ViewPager viewPager = (ViewPager)findViewById(R.id.viewPagerPerfil);
            PagerAdapter adapter = new PagerPerfilAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

            viewPager.setAdapter(adapter);
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    int position = tab.getPosition();
                    viewPager.setCurrentItem(position);
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        }

        private void setToolbar(){
            Toolbar myToolbar = (Toolbar)findViewById(R.id.toolbarPerfil);
            setSupportActionBar(myToolbar);
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_menu_perfil, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logOutActionBarPerfil:
                SharedPrefManager.getmInstance(PerfilActivity.this).logOut();
            case R.id.verProductoActionBarPerfil:
                startActivity(new Intent(PerfilActivity.this, CarritoActivity.class));
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
