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
           // if (intent.hasExtra("time"))
            try{
                Bundle bundle = intent.getExtras();
                if (bundle != null){
                    if (!String.valueOf(bundle.getInt("time")).trim().isEmpty())
                        time = bundle.getInt("time",0);

                    if (!String.valueOf(bundle.getInt("tipoTiempo")).trim().isEmpty())
                        tipoTiempo = bundle.getInt("tipoTiempo",0);

                    if (!String.valueOf(bundle.getInt("identificador")).trim().isEmpty())
                        identificador = bundle.getInt("identificador",0);

                    if (!String.valueOf(bundle.getInt("persona")).trim().isEmpty())
                        persona = Integer.parseInt(bundle.getString("persona"));
                }else{
                    time=0;
                    tipoTiempo=0;
                    identificador=0;
                    persona=0;
                }

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
                            if (time !=0){ alarm.setAlarm(getApplicationContext(), time,identificador,tipoTiempo,persona);}

                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }
                }).start();


            Toast.makeText(this, R.string.nuevaalarmacreada,Toast.LENGTH_LONG).show();
            }
            catch (Exception ex){

            }
            return START_STICKY;
        }

  /*  @Override
    public boolean stopService(Intent name) {

        return super.stopService(name);

    }*/

    @Override
        public void onStart(Intent intent, int startId)
        {
            if (time !=0){ alarm.setAlarm(getApplicationContext(), time,identificador,tipoTiempo,persona);}
        }

        @Override
        public IBinder onBind(Intent intent)
        {
            return null;
        }

    @Override
    public void onDestroy() {
        //super.onDestroy();
       // Intent nuevo = new Intent(this,MyService.class);
       // stopService(nuevo);
        sendBroadcast(new Intent("android.intent.action.reinicio"));
    }
}