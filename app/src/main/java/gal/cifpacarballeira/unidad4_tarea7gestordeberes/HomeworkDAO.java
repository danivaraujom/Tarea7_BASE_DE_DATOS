package gal.cifpacarballeira.unidad4_tarea7gestordeberes;
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

    public long insertHomework(Homework homework) {
        SQLiteDatabase db = baseDeDatos.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("subject", homework.getSubject());
        values.put("description", homework.getDescription());
        values.put("dueDate", homework.getDueDate());
        values.put("isCompleted", homework.isCompleted() ? 1 : 0);

        long id = db.insert("homework", null, values);
        db.close();
        return id;
    }

    public int updateHomework(Homework homework) {
        SQLiteDatabase db = baseDeDatos.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("subject", homework.getSubject());
        values.put("description", homework.getDescription());
        values.put("dueDate", homework.getDueDate());
        values.put("isCompleted", homework.isCompleted() ? 1 : 0);

        int rows = db.update("homework", values, "id = ?", new String[]{String.valueOf(homework.getId())});
        db.close();
        return rows;
    }

    public int deleteHomework(int id) {
        SQLiteDatabase db = baseDeDatos.getWritableDatabase();
        int rows = db.delete("homework", "id = ?", new String[]{String.valueOf(id)});
        db.close();
        return rows;
    }

    public List<Homework> getAllHomework() {
        SQLiteDatabase db = baseDeDatos.getReadableDatabase();
        List<Homework> homeworkList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM homework", null);

        if (cursor.moveToFirst()) {
            do {
                Homework homework = new Homework(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("subject")),
                        cursor.getString(cursor.getColumnIndexOrThrow("description")),
                        cursor.getString(cursor.getColumnIndexOrThrow("dueDate")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("isCompleted")) != 0
                );
                homeworkList.add(homework);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return homeworkList;
    }

    public Homework getHomeworkById(int id) {
        SQLiteDatabase db = baseDeDatos.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM homework WHERE id = ?", new String[]{String.valueOf(id)});
        Homework homework = null;

        if (cursor.moveToFirst()) {
            homework = new Homework(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("subject")),
                    cursor.getString(cursor.getColumnIndexOrThrow("description")),
                    cursor.getString(cursor.getColumnIndexOrThrow("dueDate")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("isCompleted")) != 0
            );
        }

        cursor.close();
        db.close();
        return homework;
    }
}
//    //1.Modificar Homerwork.java-> Adapta a SQLite hay que generar el id o sea la clave primaria
//    //2. Crea clase BaseDeDatos extends SQLiteOpenHelper-> Crea tabla de homework
//    //3. Crea HomeworkDAO y desde alli crea metodo de INSERT,UPDATE, DELETE o sea CRUD .
//    //4. Implementar metodos CRUD
//    //5. Insertar Homework
//    //6. Actualizar Homework
//    //7. Eliminar Homework
//    //8. Seleccionar 1 Homework
//    //9. Seleccionar todos Homework
//    //10. USAR METODOS CRUD
//
//
//