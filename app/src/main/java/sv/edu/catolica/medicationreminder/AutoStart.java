package sv.edu.catolica.medicationreminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;

//import static androidx.core.content.ContextCompat.startForegroundService;

public class AutoStart extends BroadcastReceiver
{


    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
        {
            ArrayList<ERecordatorio> alarmas =traerRecordatorio(context);

            for (ERecordatorio r : alarmas){
                Intent service = new Intent(context,MyService.class);

                service.putExtra("time", r.RE_INTER_VALOR);
                service.putExtra("tipoTiempo", r.RE_INTERVALO_MDH);
                service.putExtra("identificador", r.RE_COD);
                service.putExtra("persona", String.valueOf(r.PER_COD));
                if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.O){
                    context.startForegroundService(service);
                }else{
                    context.startService(service);
                }

            }

        }
    }

    public ArrayList<ERecordatorio> traerRecordatorio(Context ctx){
        ManejadorBD admin;
        SQLiteDatabase db;
        admin=new ManejadorBD(ctx,"MEDICATIONREMINDER",null,1);
        db = admin.getWritableDatabase();
        Cursor fila = db.rawQuery("SELECT RE_COD,PER_COD, RE_INTERVALO_MDH, RE_INTERVALO_VALOR, RE_ESTADO FROM RECORDATORIO " +
                        "WHERE RE_ESTADO = 1"
                ,null);
        ArrayList<ERecordatorio> recordatorios = new ArrayList<ERecordatorio>();
        while (fila.moveToNext()){
            ERecordatorio _recor = new ERecordatorio();
            _recor.RE_COD=fila.getInt(0);
            _recor.PER_COD=fila.getInt(1);
            _recor.RE_INTERVALO_MDH=fila.getInt(2);
            _recor.RE_INTER_VALOR=fila.getInt(3);
            _recor.RE_ESTADO=fila.getInt(4);

            recordatorios.add(_recor);
        }
        db.close();
        return recordatorios;
    }
}