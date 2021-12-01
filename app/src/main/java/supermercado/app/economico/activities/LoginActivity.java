package supermercado.app.economico.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import supermercado.app.economico.R;
import supermercado.app.economico.api.Api;
import supermercado.app.economico.api.RequestHandler;
import supermercado.app.economico.models.Usuarios;
import supermercado.app.economico.utils.SharedPrefManager;

public class LoginActivity extends AppCompatActivity {

    private EditText mail, password;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mail = (EditText)findViewById(R.id.editTextMailLogin);
        password = (EditText)findViewById(R.id.editTextPasswordLogin);

        findViewById(R.id.buttonLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });
    }

    private void userLogin(){
        final String email = mail.getText().toString().trim();
        final String pass = password.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            mail.setError(getString(R.string.escribe_tu_correo_electronico_label));
            mail.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(pass)){
            password.setError(getString(R.string.escribe_tu_contraseña_label));
            password.requestFocus();
            return;
        }

        class UserLogin extends AsyncTask<Void, Void, String>{

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = progressDialog.show(LoginActivity.this, getString(R.string.comprobando_datos_label),
                        getString(R.string.espere_por_favor_label), false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                try {
                    JSONObject obj = new JSONObject(s);

                    if(!obj.getBoolean("error")){
                        Toast.makeText(LoginActivity.this, obj.getString("message"), Toast.LENGTH_LONG).show();

                        JSONObject userJSon = obj.getJSONObject("contenido");

                        Usuarios user = new Usuarios(
                                userJSon.getInt("id"),
                                userJSon.getString("usuario"),
                                userJSon.getString("password"),
                                userJSon.getString("role"),
                                userJSon.getString("mail")
                        );

                        SharedPrefManager.getmInstance(LoginActivity.this).userLogin(user);

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }else{
                        progressDialog.cancel();
                        Toast.makeText(LoginActivity.this, obj.getString("message"), Toast.LENGTH_LONG).show();
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler requestHandler = new RequestHandler();

                HashMap<String, String> params = new HashMap<>();
                params.put("mail", email);
                params.put("password", pass);

                return requestHandler.sendPostRequest(Api.URL_LOGIN_USUARIO, params);
            }
        }

        UserLogin ul = new UserLogin();
        ul.execute();
    }
}
