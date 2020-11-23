package sv.edu.catolica.medicationreminder;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.sql.Time;

public class MyService extends Service {

int tipoTiempo, identificador,time,persona;






    public MyService() {

    }


        Alarm alarm = new Alarm();
        public void onCreate()
        {
            super.onCreate();


        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId)
        {
         time = intent.getExtras().getInt("time");
            tipoTiempo = intent.getExtras().getInt("tipoTiempo");
            identificador = intent.getExtras().getInt("identificador");
            persona = Integer.parseInt(intent.getExtras().getString("persona"));
            new Thread(new Runnable() {

                @Override
                public void run() {

                    // El servicio se finaliza a sí mismo cuando finaliza su
                    // trabajo.
                    try {
                        // Simulamos trabajo de 10 segundos.
                        Thread.sleep(10000);

                        // Instanciamos e inicializamos nuestro manager.
                        NotificationManager nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }).start();
            alarm.setAlarm(this, time,identificador,tipoTiempo,persona);
            Toast.makeText(this,"creado",Toast.LENGTH_LONG).show();
            return START_STICKY;
        }

        @Override
        public void onStart(Intent intent, int startId)
        {
            alarm.setAlarm(this, time,identificador,tipoTiempo,persona);
        }

        @Override
        public IBinder onBind(Intent intent)
        {
            return null;
        }

}