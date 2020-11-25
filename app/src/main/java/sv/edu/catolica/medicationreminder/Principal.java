package sv.edu.catolica.medicationreminder;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

public class Principal extends AppCompatActivity {
    private ImageView ivPersona, ivRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        ivPersona = findViewById(R.id.ivPersona);
        ivRecord = findViewById(R.id.ivRecor);





    }


    //Ir a Personas
    public void persona(View v){
        finish();
        Intent perso = new Intent(Principal.this, Personas.class);
        startActivity(perso);
    }

    //Ir a Recordatorios
    public void medicamento(View v){
        finish();
        Intent recor = new Intent(Principal.this, Medicamento.class);
        startActivity(recor);
    }

    //Método para salir y detener la aplicación
    public void Salir(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getText(R.string.pregunta_salir)).setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish(); System.exit(0);
            }
        }).setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    //Al presionar Atrás
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            Salir();
        }
        return super.onKeyDown(keyCode, event);
    }
}