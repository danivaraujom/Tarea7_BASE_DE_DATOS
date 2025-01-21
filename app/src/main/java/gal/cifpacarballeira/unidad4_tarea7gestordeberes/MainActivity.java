package gal.cifpacarballeira.unidad4_tarea7gestordeberes;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
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
        // Inicialización de HomeworkDAO
        homeworkDAO = new HomeworkDAO(this);

        // Configuración del RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Cargar lista de tareas desde la base de datos
        loadHomeworkList();

        // Configuración del adaptador
        adapter = new HomeworkAdapter(homeworkList, homework -> showBottomSheet(homework));
        recyclerView.setAdapter(adapter);

        // Configuración del botón flotante
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> showAddHomeworkDialog(null));
    }

    private void loadHomeworkList() {
        // Obtén las tareas desde el DAO
        homeworkList = homeworkDAO.getAllHomework();
        if (homeworkList == null) {
            homeworkList = new ArrayList<>();
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private void showAddHomeworkDialog(Homework homeworkToEdit) {
        NewHomeworkDialogFragment dialog = new NewHomeworkDialogFragment();

        // Si se edita, pasa la tarea existente al diálogo
        if (homeworkToEdit != null) {
            Bundle args = new Bundle();
            args.putParcelable("homework", homeworkToEdit);
            dialog.setArguments(args);
        }

        dialog.setOnHomeworkSavedListener(homework -> {
            if (homeworkToEdit == null) {
                // Insertar nueva tarea
                long id = homeworkDAO.insertHomework(homework);
                if (id > 0) {
                    homework.setId((int) id);
                    homeworkList.add(homework);
                }
            } else {
                // Actualizar tarea existente
                int rows = homeworkDAO.updateHomework(homework);
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

        // Inflar el layout del diálogo
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_homework_options, null);

        // Configuración de las opciones
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
            int rows = homeworkDAO.updateHomework(homework);
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
                    int rows = homeworkDAO.deleteHomework(homework.getId());
                    if (rows > 0) {
                        homeworkList.remove(homework);
                        adapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}
