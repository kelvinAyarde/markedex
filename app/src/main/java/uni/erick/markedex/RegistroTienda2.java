package uni.erick.markedex;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class RegistroTienda2 extends AppCompatActivity {
    EditText nom,telf,des;
    Button reg;
    CheckBox deliveri,estacionamiento;
    String Delive = "0";
    String Esta = "0";

    //Al presionar Atrás, envía a la pantalla de inicio
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == event.KEYCODE_BACK){
            Intent irIngreso = new Intent(RegistroTienda2.this, IngresoTienda.class);
            startActivity(irIngreso);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_tienda2);
        findViewById();
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check();
                Intent Obj = new Intent(RegistroTienda2.this,MapaRegistroTienda.class);
                String usuario = getIntent().getStringExtra("USUARIO");
                String contraseña = getIntent().getStringExtra("CONTRASEÑA");
                Obj.putExtra("USUARIO",usuario);
                Obj.putExtra("CONTRASEÑA",contraseña);
                Obj.putExtra("NOMBRETI",nom.getText().toString());
                Obj.putExtra("TELEFONO",telf.getText().toString());
                Obj.putExtra("DESCRIPCION",des.getText().toString());
                Obj.putExtra("ESTACIONAMIENTO",Esta);
                Obj.putExtra("DELIVERY",Delive);
                startActivity(Obj);
            }
        });


    }
    private void check(){
        if(estacionamiento.isChecked()){
            Esta = "1";
        }
        if (deliveri.isChecked()){
            Delive = "1";
        }
    }
    private void findViewById(){
        //Relaciona las variables con los elementos del xml
        //Es mejor colocarlas aquí en lugar de que ocupen espacio en el onCreate
        nom = findViewById(R.id.NombreTi);
        telf = findViewById(R.id.TelefonoTi);
        des = findViewById(R.id.DesTi);
        reg = findViewById(R.id.ReTienda2);
        deliveri = findViewById(R.id.delivery);
        estacionamiento = findViewById(R.id.estacionamiento);
    }
}