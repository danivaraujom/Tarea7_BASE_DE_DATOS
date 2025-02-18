package gal.cifpacarballeira.unidad4_tarea7gestordeberes;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class HomeworkDAO {

    private final BaseDeDatos baseDeDatos;

    public HomeworkDAO(Context context) {
        this.baseDeDatos = new BaseDeDatos(context);
    }

    public long insertarTarea(Homework homework) {
        SQLiteDatabase db = baseDeDatos.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("subject", homework.getSubject());
        valores.put("description", homework.getDescription());
        valores.put("dueDate", homework.getDueDate());
        valores.put("isCompleted", homework.isCompleted() ? 1 : 0);

        long id = db.insert("homework", null, valores);
        db.close();
        return id;
    }

    public int actualizarTarea(Homework homework) {
        SQLiteDatabase db = baseDeDatos.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("subject", homework.getSubject());
        valores.put("description", homework.getDescription());
        valores.put("dueDate", homework.getDueDate());
        valores.put("isCompleted", homework.isCompleted() ? 1 : 0);

        int filas = db.update("homework", valores, "id = ?", new String[]{String.valueOf(homework.getId())});
        db.close();
        return filas;
    }

    public int eliminarTarea(int id) {
        SQLiteDatabase db = baseDeDatos.getWritableDatabase();
        int filas = db.delete("homework", "id = ?", new String[]{String.valueOf(id)});
        db.close();
        return filas;
    }

    public List<Homework> obtenerTodasLasTareas() {
        SQLiteDatabase db = baseDeDatos.getReadableDatabase();
        List<Homework> listaTareas = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM homework", null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range")
                Homework tarea = new Homework(
                        cursor.getInt(cursor.getColumnIndex("id")),
                        cursor.getString(cursor.getColumnIndex("subject")),
                        cursor.getString(cursor.getColumnIndex("description")),
                        cursor.getString(cursor.getColumnIndex("dueDate")),
                        cursor.getInt(cursor.getColumnIndex("isCompleted")) != 0
                );
                listaTareas.add(tarea);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return listaTareas;
    }

    @SuppressLint("Range")
    public Homework obtenerTareaPorId(int id) {
        SQLiteDatabase db = baseDeDatos.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM homework WHERE id = ?", new String[]{String.valueOf(id)});
        Homework tarea = null;

        if (cursor.moveToFirst()) {
            tarea = new Homework(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("subject")),
                    cursor.getString(cursor.getColumnIndex("description")),
                    cursor.getString(cursor.getColumnIndex("dueDate")),
                    cursor.getInt(cursor.getColumnIndex("isCompleted")) != 0
            );
        }

        cursor.close();
        db.close();
        return tarea;
    }
}
