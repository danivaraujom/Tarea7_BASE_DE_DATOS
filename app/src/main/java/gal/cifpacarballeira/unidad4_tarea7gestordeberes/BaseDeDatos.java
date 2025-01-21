package gal.cifpacarballeira.unidad4_tarea7gestordeberes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class BaseDeDatos extends SQLiteOpenHelper {
    private static final String nombreBD= "BaseDeDatos";
    private static final int versionBD=1;

    public BaseDeDatos(@Nullable Context context) {
        super(context, nombreBD, null, versionBD);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE= "CREATE TABLE homework ("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                "subject TEXT,"+
                "description TEXT, "+
                "dueDate TEXT,"+
                "isCompleted INTEGER)";
        sqLiteDatabase.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP table if exists homework");
        onCreate(sqLiteDatabase);

    }
}
