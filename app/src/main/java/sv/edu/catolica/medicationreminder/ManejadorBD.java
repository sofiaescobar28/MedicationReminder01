package sv.edu.catolica.medicationreminder;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ManejadorBD extends SQLiteOpenHelper {

    public ManejadorBD(@Nullable Context context,
                       @Nullable String name,
                       @Nullable SQLiteDatabase.CursorFactory factory,
                       int version) {
        super(context, name, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE PERSONA " +
                "(PER_COD int primary key," +
                "PER_NOMBRE text)");
        db.execSQL("CREATE TABLE RECORDATORIO " +
                "(RE_COD int primary key," +
                "PER_COD int," +
                "RE_TITULO text," +
                "RE_F_INICIO text," +
                "RE_INTERVALO_MDH text," +
                "RE_INTERVALO_VALOR int," +
                "RE_F_FINAL text," +
                "RE_ESTADO text," +
                "foreign key (PER_COD) references PERSONA(PER_COD))");

        db.execSQL("CREATE TABLE MEDICAMENTO " +
                "(MED_COD int primary key," +
                "MED_NOMBRE text," +
                "MED_TIPO text)" );
        db.execSQL("CREATE TABLE MEDXRE" +
                "(MEDXRED_COD int primary key," +
                "RE_COD int," +
                "MED_COD int," +
                "MEDXRED_DOSIFICACION text," +
                "RE_DOSIS text," +
                "foreign key (RE_COD) references RECORDATORIO(RE_COD)," +
                "foreign key (MED_COD) references MEDICAMENTO(MED_COD))");
        db.execSQL("CREATE TABLE HISTORIAL " +
                "(H_COD int primary key," +
                "MEDXRED_COD int," +
                "H_FECHA text," +
                "H_ESTADO text," +
                "H_COMENTARIO text," +
                "foreign key (MEDXRED_COD) references MEDXRE(MEDXRED_COD))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int v1, int v2) {

    }
}
