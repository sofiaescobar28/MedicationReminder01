package sv.edu.catolica.medicationreminder;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.os.SystemClock;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.content.Context.NOTIFICATION_SERVICE;
import static sv.edu.catolica.medicationreminder.R.drawable.botiquin;
import static sv.edu.catolica.medicationreminder.R.drawable.medicamento;

public class Alarm extends BroadcastReceiver
{
    private NotificationManager notificationManager;
    private PendingIntent pendingIntent;

    Notification notification;
   //ManejadorBD admin;
    //SQLiteDatabase db;

    int MINUTOS=1,HORAS=2,DIAS=3, SEMANAS=4, MESES=5;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent)
    {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "tag");
        wl.acquire();
//Notificacion còdigo
        String NOTIFICATION_CHANNEL_ID = context.getString(R.string.app_name);
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent mIntent = new Intent(context, Historial.class);


        Resources res = context.getResources();
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);




        final int persona_id = intent.getExtras().getInt("persona_id");
      final int  NOTIFICATION_ID =intent.getExtras().getInt("identificador",-1);

      insertarRegistro(NOTIFICATION_ID,persona_id,context);

       // Historial historiL = new Historial();
       // historiL.insertarRegistro(NOTIFICATION_ID,persona_id);

      String id_dos = String.valueOf(NOTIFICATION_ID);
        String persona_dos = String.valueOf(persona_id);
        mIntent.putExtra("RE_COD",id_dos);
        mIntent.putExtra("PER_COD",persona_dos);
        mIntent.putExtra("notificacion",true);

      String titulo = intent.getExtras().getString("titulo");
        String medicamento = intent.getExtras().getString("medicamento");
        //int  NOTIFICATION_ID=7;



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //final int NOTIFY_ID = 0; // ID of notification
            String id = NOTIFICATION_CHANNEL_ID; // default_channel_id
            String title = NOTIFICATION_CHANNEL_ID; // Default Channel
            PendingIntent pendingIntent;
            NotificationCompat.Builder builder;
            NotificationManager notifManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (notifManager == null) {
                notifManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            }
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notifManager.getNotificationChannel(id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(id, title, importance);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notifManager.createNotificationChannel(mChannel);
            }

            builder = new NotificationCompat.Builder(context, id);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(context, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentTitle(titulo).setCategory(Notification.CATEGORY_SERVICE)
                    .setSmallIcon(R.drawable.medicamento)   // required
                    .setContentText(medicamento)
                    .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.botiquin))//yo le cambie
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setSound(soundUri)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(medicamento))
                    .setContentIntent(pendingIntent)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            Notification notification = builder.build();
            notifManager.notify(NOTIFICATION_ID, notification);


        } else {
            pendingIntent = PendingIntent.getActivity(context, 1, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            notification = new NotificationCompat.Builder(context)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.medicamento)
                    .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.medicamento))
                    .setSound(soundUri)
                    .setAutoCancel(true)
                    .setContentTitle(titulo).setCategory(Notification.CATEGORY_SERVICE)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(medicamento))
                    .setContentText(medicamento).build();

            notificationManager.notify(NOTIFICATION_ID, notification);
        }





        // Put here YOUR code.
        Toast.makeText(context, context.getText(R.string.alarmacreada), Toast.LENGTH_LONG).show(); // For example

        wl.release();
    }

    public  ArrayList<ENotificacion> buscarInformacionNotificacion(int recordatorio_id,Context ctx){
        ManejadorBD admin;
        SQLiteDatabase db;
        admin=new ManejadorBD(ctx,"MEDICATIONREMINDER",null,1);
    admin=new ManejadorBD(ctx,"MEDICATIONREMINDER",null,1);
    db = admin.getWritableDatabase();
    Cursor fila = db.rawQuery(" SELECT r.RE_COD ,r.RE_TITULO, m2.MED_NOMBRE,m.MEDXRED_DOSIFICACION,m.RE_DOSIS, p.PER_NOMBRE,p.PER_COD " +
                    " FROM RECORDATORIO r " +
                    " INNER JOIN PERSONA p ON r.PER_COD = p.PER_COD"+
                    " INNER JOIN MEDXRE m ON r.RE_COD = m.RE_COD" +
                    " INNER JOIN MEDICAMENTO m2 ON m.MED_COD = m2.MED_COD " +
                    " WHERE r.RE_COD = " + recordatorio_id,
            null);

    ArrayList<ENotificacion> informacion = new ArrayList<ENotificacion>();
    while (fila.moveToNext()){

        ENotificacion _noti = new ENotificacion();
        _noti.recordatorio_id=fila.getInt(0);
        _noti.titulo = fila.getString(1);
        _noti.Medicamento = fila.getString(2);
        _noti.Dosificacion = fila.getString(3);
        _noti.Dosis_Cantidad = fila.getString(4);
        _noti.persona=fila.getString(5);
        _noti.persona_id=fila.getInt(6);

        informacion.add(_noti);


    }
    db.close();
    return informacion;


}
    public void setAlarm(Context context, int Time, int identificador,int tipoTiempo,int person)
    {
        ArrayList<ENotificacion> noti = buscarInformacionNotificacion(identificador,context);
        String titulo="",medicamento=""+context.getText(R.string.medicamento2);
        for (int j=0;j<=noti.size()-1;j++  ) {

            if (j==0)
             titulo= titulo + noti.get(j).titulo + " - "+noti.get(j).persona;

             if (j==noti.size()-1)
            medicamento= medicamento + noti.get(j).Medicamento+"\n "+context.getText(R.string.dosificacion2)+noti.get(j).Dosificacion+"\n "+context.getText(R.string.dosis2)+noti.get(j).Dosis_Cantidad+"\n"+context.getText(R.string.presionapara_gestionar);
            else  medicamento= medicamento + noti.get(j).Medicamento+"\n "+context.getText(R.string.dosificacion2)+noti.get(j).Dosificacion+"\n "+context.getText(R.string.dosis2)+noti.get(j).Dosis_Cantidad+"\n"+context.getText(R.string.medicamento2);


        }
        if (tipoTiempo == MINUTOS){
            AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            Intent i = new Intent(context, Alarm.class);
            i.putExtra("identificador",identificador);
            i.putExtra("titulo",titulo);
            i.putExtra("medicamento",medicamento);
            i.putExtra("persona_id",person);
            PendingIntent pi = PendingIntent.getBroadcast(context, identificador, i, PendingIntent.FLAG_CANCEL_CURRENT);
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000*60*Time, pi); // Millisec * Second * Minute


        }else if (tipoTiempo == HORAS){
        AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, Alarm.class);
        i.putExtra("identificador",identificador);
            i.putExtra("titulo",titulo);
            i.putExtra("medicamento",medicamento);
            i.putExtra("persona_id",person);
        PendingIntent pi = PendingIntent.getBroadcast(context, identificador, i, 0);
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), AlarmManager.INTERVAL_HOUR*Time, pi); // Millisec * Second * Minute
         }else if (tipoTiempo == DIAS){
            AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            Intent i = new Intent(context, Alarm.class);
            i.putExtra("identificador",identificador);
            i.putExtra("titulo",titulo);
            i.putExtra("medicamento",medicamento);
            i.putExtra("persona_id",person);
            PendingIntent pi = PendingIntent.getBroadcast(context, identificador, i, 0);
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), AlarmManager.INTERVAL_DAY*Time, pi); // Millisec * Second * Minute
        }else if (tipoTiempo == SEMANAS){
            AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            Intent i = new Intent(context, Alarm.class);
            i.putExtra("identificador",identificador);
            i.putExtra("titulo",titulo);
            i.putExtra("medicamento",medicamento);
            i.putExtra("persona_id",person);
            PendingIntent pi = PendingIntent.getBroadcast(context, identificador, i, 0);
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), AlarmManager.INTERVAL_DAY*7*Time, pi); // Millisec * Second * Minute
        }

    }
    public ArrayList<ECantidadMed> obtenerCantidadMedicamentos(int recordatorio,Context ctx){
        ManejadorBD admin;
        SQLiteDatabase db;
        admin=new ManejadorBD(ctx,"MEDICATIONREMINDER",null,1);
        db = admin.getWritableDatabase();
        Cursor fila = db.rawQuery("SELECT m.MED_NOMBRE " +
                        " FROM MEDXRE m2" +
                        " INNER JOIN MEDICAMENTO m ON m2.MED_COD = m.MED_COD " +
                        " WHERE m2.RE_COD = "+recordatorio
                ,null);
        ArrayList<ECantidadMed> medicamentos=new ArrayList<ECantidadMed>();
        while (fila.moveToNext()){
            ECantidadMed _med = new ECantidadMed();
            _med.medicamento=fila.getString(0);
            medicamentos.add(_med);
        }
        db.close();
        return medicamentos;
    }

    public void insertarRegistro(int recordatorio,int persona,Context ctx)
                {
                    ManejadorBD admin;
                    SQLiteDatabase db;
                    admin=new ManejadorBD(ctx,"MEDICATIONREMINDER",null,1);
        ArrayList<ECantidadMed> medicamentos =obtenerCantidadMedicamentos(recordatorio,ctx);
        for (ECantidadMed med: medicamentos) {

            ContentValues registro = new ContentValues();
            Date Hoy = new Date();
            String fecha;

            fecha = new SimpleDateFormat("HH:mm dd/MM/yyyy").format(Hoy);

            registro.put("H_COD",ultimoID_Historial(ctx));
            registro.put("MEDXRED_COD",obtenerMedxRed_COD(med.medicamento,recordatorio,ctx));
            registro.put("H_FECHA", fecha);
            registro.put("H_ESTADO", 2);
            registro.put("H_COMENTARIO", med.medicamento);

            db = admin.getWritableDatabase();
            int valor = (int) db.insert("HISTORIAL", null, registro);

            db.close();
        }

    }
    public int ultimoID_Historial(Context ctx){
        ManejadorBD admin;
        SQLiteDatabase db;
        admin=new ManejadorBD(ctx,"MEDICATIONREMINDER",null,1);
        db = admin.getWritableDatabase();
        int num=-1;
        Cursor fila = db.rawQuery("SELECT H_COD FROM HISTORIAL" +
                " ORDER BY H_COD DESC"+
                " LIMIT 1;",null);
        if (fila.moveToFirst()){
            num=fila.getInt(0);
            num++;
        }else   {
            num = 1;
        }
        db.close();
        return num;
    }
    public int obtenerMedxRed_COD(String medicamento, int recordatorio,Context ctx){
        ManejadorBD admin;
        SQLiteDatabase db;
        admin=new ManejadorBD(ctx,"MEDICATIONREMINDER",null,1);
        db = admin.getWritableDatabase();

        int num=-1;
        Cursor fila = db.rawQuery("SELECT m.MEDXRED_COD FROM MEDXRE m " +
                " INNER JOIN RECORDATORIO r ON m.RE_COD = r.RE_COD " +
                " INNER JOIN MEDICAMENTO m2 ON m.MED_COD = m2.MED_COD " +
                " WHERE r.RE_COD = "+recordatorio+" AND m2.MED_NOMBRE ='"+medicamento+"'",null);
        if (fila.moveToFirst()){
            num=fila.getInt(0);

        }
        db.close();
        return num;
    }

    public void cancelAlarm(Context context, int identificador)
    {
        Intent intent = new Intent(context, Alarm.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, identificador, intent, PendingIntent.FLAG_NO_CREATE);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if(sender != null) {
            alarmManager.cancel(sender);
            Toast.makeText(context, context.getText(R.string.alarmademedicamentoscancela),Toast.LENGTH_LONG).show();
        }

    }
}