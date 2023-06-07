package uni.erick.markedex;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import uni.erick.markedex.extra.Extra;

public class IngresoTienda extends AppCompatActivity {

    LinearLayout registrarTienda;
    EditText txtUsuario, txtPass;
    Button btnIngresar;

    RequestQueue requestQueue;
    Extra extra = new Extra();

    //El -1 por defecto significa que no ha llegado nada
    String idRecibido = "-1";
    boolean recibido = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingreso_tienda);

        /*txtUsuario.setText("");
        txtPass.setText("");*/

        requestQueue = Volley.newRequestQueue(this);
        //Relaciona las variables con los elementos en el xml
        findViewById();

        borrarDatosEscritos();

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usuario = txtUsuario.getText().toString();
                String contrasena = txtPass.getText().toString();
                validarusuario(usuario,contrasena);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(!recibido){
                            //Si aún no ha llegado nada de BD, repetir
                            handler.postDelayed(this, 500);
                        }else{
                            //Si el id ha cambiado, entonces el Usuario y pass son correctos
                            if (!idRecibido.equals("-1")) {
                                Intent irAmdCatalogo = new Intent(IngresoTienda.this, AdmCatalogo.class);
                                irAmdCatalogo.putExtra("idTienda", idRecibido);
                                startActivity(irAmdCatalogo);
                            }else{
                                Toast.makeText(IngresoTienda.this, "Usuario o Contraseña Incorrectos", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                },500);
            }
        });

        registrarTienda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(IngresoTienda.this, RegistroTienda.class);
                startActivity(a);
            }
        });
    }

    private void borrarDatosEscritos() {
        //Elimnar contenido de los EditTExt al iniciar activity
        txtUsuario.setText("");
        txtPass.setText("");
    }

    private void findViewById(){
        registrarTienda = findViewById(R.id.lyIngresoTienda_registrarTienda);
        txtUsuario = findViewById(R.id.txtNuevoArt_nombre);
        txtPass = findViewById(R.id.txtIngresoTienda_pass);
        btnIngresar = findViewById(R.id.btnIngresoTienda_ingresar);
    }

    private void validarusuario (final String usuario, final String pass){
        idRecibido = "-1";
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                extra.url("validarUsuario.php?usuario=" + usuario + "&" + "pass=" + pass),
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        int size = response.length();
                        //Si llegó al menos un elemento en el array
                        if(size>0){
                            try {
                                JSONArray id = new JSONArray(response.get(0).toString());
                                idRecibido = id.getString(0);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        recibido = true;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(IngresoTienda.this, "Ocurrió un error en la BD", Toast.LENGTH_SHORT).show();
                    }
                }

        );
        requestQueue.add(request);
    }
}