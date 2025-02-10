package gal.cifpacarballeira.unidad4_tarea7gestordeberes;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HomeworkAdapter adapter;
    private List<Homework> homeworkList;
    private HomeworkDAO homeworkDAO;
    private ListaHomeworkViewModel listaHomeworkViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        homeworkDAO = new HomeworkDAO(this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listaHomeworkViewModel=new ViewModelProvider(this).get(ListaHomeworkViewModel.class);
        final Observer<ArrayList> observador1= new Observer<ArrayList>() {
            @Override
            public void onChanged(ArrayList arrayList) {

            }
        }

        // Cargar lista de tareas desde la base de datos
        loadHomeworkList();
        adapter = new HomeworkAdapter(homeworkList, homework -> showBottomSheet(homework));
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> showAddHomeworkDialog(null));
    }

    private void loadHomeworkList() {
        homeworkList = homeworkDAO.obtenerTodasLasTareas();
        if (homeworkList == null) {
            homeworkList = new ArrayList<>();
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private void showAddHomeworkDialog(Homework homeworkToEdit) {
        NewHomeworkDialogFragment dialog = new NewHomeworkDialogFragment();
        if (homeworkToEdit != null) {
            Bundle args = new Bundle();
            args.putParcelable("homework", homeworkToEdit);
            dialog.setArguments(args);
        }

        dialog.setOnHomeworkSavedListener(homework -> {
            if (homeworkToEdit == null) {
                long id = homeworkDAO.insertarTarea(homework);
                if (id > 0) {
                    homework.setId((int) id);
                    homeworkList.add(homework);
                }
            } else {
                int rows = homeworkDAO.actualizarTarea(homework);
                if (rows > 0) {
                    int index = homeworkList.indexOf(homeworkToEdit);
                    homeworkList.set(index, homework);
                }
            }
            adapter.notifyDataSetChanged();
        });

        dialog.show(getSupportFragmentManager(), "AddHomeworkDialog");
    }

    private void showBottomSheet(Homework homework) {
        // Crear el diálogo
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_homework_options, null);
        view.findViewById(R.id.editOption).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            showAddHomeworkDialog(homework);
        });

        view.findViewById(R.id.deleteOption).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            showDeleteConfirmation(homework);
        });

        view.findViewById(R.id.completeOption).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            homework.setCompleted(true);
            int rows = homeworkDAO.actualizarTarea(homework);
            if (rows > 0) {
                adapter.notifyDataSetChanged();
                Toast.makeText(this, "Tarea marcada como completada", Toast.LENGTH_SHORT).show();
            }
        });

        // Mostrar el diálogo
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
    }

    private void showDeleteConfirmation(Homework homework) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmar eliminación")
                .setMessage("¿Estás seguro de que deseas eliminar este deber?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    int rows = homeworkDAO.eliminarTarea(homework.getId());
                    if (rows > 0) {
                        homeworkList.remove(homework);
                        adapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}