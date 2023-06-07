package uni.erick.markedex;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import uni.erick.markedex.extra.Extra;

public class RegistroTienda3 extends AppCompatActivity {

    //Objetos de Volley. envia solicitud en cola
    RequestQueue requestQueue;
    //Objeto de la clase con datos generales
    Extra extra = new Extra();

    CheckBox RA,CS,TC,CH,FV,ME,CM,FT;
    Button rtienda3;
    int i = 8;
    String [] categorias = new String[i];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_tienda3);
        findViewById();
        requestQueue = Volley.newRequestQueue(this);
        rtienda3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Obj = new Intent(RegistroTienda3.this,IngresoTienda.class);
                String idt = getIntent().getStringExtra("IDTIENDA");
                if(RA.isChecked() || CS.isChecked() || TC.isChecked()||CH.isChecked()||FV.isChecked()||ME.isChecked()||CM.isChecked()||FT.isChecked()){
                    if (RA.isChecked()){
                        String ra = "1";
                        nuevaListacategoria(idt,ra);
                    }
                    if(CS.isChecked()){
                        String ra = "2";
                        nuevaListacategoria(idt,ra);
                    }
                    if(TC.isChecked()){
                        String ra = "3";
                        nuevaListacategoria(idt,ra);
                    }
                    if (CH.isChecked()){
                        String ra = "4";
                        nuevaListacategoria(idt,ra);
                    }
                    if (FV.isChecked()){
                        String ra = "5";
                        nuevaListacategoria(idt,ra);
                    }
                    if (ME.isChecked()){
                        String ra = "6";
                        nuevaListacategoria(idt,ra);
                    }
                    if (CM.isChecked()){
                        String ra = "7";
                        nuevaListacategoria(idt,ra);
                    }
                    if (FT.isChecked()){
                        String ra = "8";
                        nuevaListacategoria(idt,ra);
                    }
                    startActivity(Obj);//en caso de no estar chequeado ninguno no pasa a otro activity
                }

            }
        });
    }

    private void nuevaListacategoria(final String id_tienda, final String id_categoria) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                //url del archivo.php:
                extra.url("crearListacategoria.php"),
                new Response.Listener<String>() { //verifica si se acepto la solicitud
                    @Override
                    public void onResponse(String response) {
                        //Encaso de que la BD responda
                        Toast.makeText(RegistroTienda3.this, "Tienda Registrada", Toast.LENGTH_SHORT).show();

                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegistroTienda3.this, "Ocurrió un Error en la BD", Toast.LENGTH_SHORT).show();

                    }
                }
        ){ //Esto es para darle decodif
            //formando HashMap (clave:valor) desde los parámetros
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                //nombre de variable en archivo php, parametro
                params.put("id_tienda", id_tienda);
                params.put("id_categoria", id_categoria);
                return params;
            }
        };
        requestQueue.add(stringRequest);


    }
    private void findViewById(){
        RA=findViewById(R.id.RA) ;
        CS = findViewById(R.id.CS);
        TC = findViewById(R.id.TC);
        CH= findViewById(R.id.CH);
        FV = findViewById(R.id.FV);
        ME = findViewById(R.id.ME);
        CM= findViewById(R.id.CT);
        FT=findViewById(R.id.FT);
        rtienda3=findViewById(R.id.ReTienda3);
    }
}