package gal.cifpacarballeira.unidad4_tarea7gestordeberes;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

// Extiende de AndroidViewModel para poder usar el contexto de la aplicación
public class HomeworkViewModel extends AndroidViewModel {
    private final HomeworkDAO homeworkDAO;
    private final MutableLiveData<List<Homework>> homeworkList = new MutableLiveData<>();

    // Constructor sin parámetros
    public HomeworkViewModel(Application application) {
        super(application);
        // Inicializa HomeworkDAO aquí
        this.homeworkDAO = new HomeworkDAO(application.getApplicationContext());
        loadHomeworkList();
    }

    public LiveData<List<Homework>> getHomeworkList() {
        return homeworkList;
    }

    public void loadHomeworkList() {
        List<Homework> tareas = homeworkDAO.obtenerTodasLasTareas();
        homeworkList.setValue(tareas);
    }

    public void insertarTarea(Homework homework) {
        long id = homeworkDAO.insertarTarea(homework);
        if (id > 0) {
            homework.setId((int) id);
            loadHomeworkList();
        }
    }

    public void actualizarTarea(Homework homework) {
        int filas = homeworkDAO.actualizarTarea(homework);
        if (filas > 0) {
            loadHomeworkList();
        }
    }

    public void eliminarTarea(Homework homework) {
        int filas = homeworkDAO.eliminarTarea(homework.getId());
        if (filas > 0) {
            loadHomeworkList();
        }
    }
}
