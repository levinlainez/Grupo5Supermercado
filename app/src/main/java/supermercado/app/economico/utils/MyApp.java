package supermercado.app.economico.utils;

import android.app.Application;

import java.util.concurrent.atomic.AtomicInteger;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;
import supermercado.app.economico.models.Carrito;
import supermercado.app.economico.models.Deseos;

public class MyApp extends Application{
    public static AtomicInteger CarritoID = new AtomicInteger();
    public static AtomicInteger DeseoID = new AtomicInteger();

    @Override
    public void onCreate(){
     setUpRealmConfig();

     Realm realm = Realm.getDefaultInstance();
     CarritoID = getIdByTable(realm, Carrito.class);
     DeseoID = getIdByTable(realm, Deseos.class);
     realm.close();

     super.onCreate();
    }

    private void setUpRealmConfig(){
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(config);
    }

    private <T extends RealmObject> AtomicInteger getIdByTable(Realm realm, Class<T> anyClass){
        RealmResults<T> results = realm.where(anyClass).findAll();
        return (results.size() > 0) ? new AtomicInteger(results.max("id").intValue()) : new AtomicInteger();
    }
}
