package uni.erick.markedex;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button btnBuscar;
    LinearLayout ingresarComoTienda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById();

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent b = new Intent(MainActivity.this, UsuarioCategoria.class);
                startActivity(b);
            }
        });
        ingresarComoTienda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Crear Intent(esta class, class destino)
                Intent a = new Intent(MainActivity.this, IngresoTienda.class);
                //iniciar el otro activity
                startActivity(a);
            }
        });
    }


    private void findViewById(){
        ingresarComoTienda = findViewById(R.id.lyMain_ingComoTienda);
        btnBuscar = findViewById(R.id.btnMain_Buscar);
    }
}