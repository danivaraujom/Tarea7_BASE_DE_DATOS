package gal.cifpacarballeira.unidad4_tarea7gestordeberes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;

public class ListaHomeworkViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Homework>> lista = new MutableLiveData<>(new ArrayList<>());

    public LiveData<ArrayList<Homework>> getLista() {
        return lista;
    }

    public void setHomeworkList(ArrayList<Homework> listaHomework) {
        lista.setValue(listaHomework);
    }

    public void agregarTarea(Homework homework) {
        ArrayList<Homework> tareaTemporal = lista.getValue();
        if (homework != null && tareaTemporal != null) {
            tareaTemporal.add(homework);
            lista.setValue(tareaTemporal);
        }
    }

    public void eliminarTarea(Homework homework) {
        ArrayList<Homework> tareaTemporal = lista.getValue();
        if (homework != null && tareaTemporal != null) {
            tareaTemporal.remove(homework);
            lista.setValue(tareaTemporal);
        }
    }
}
