
package com.app.efficientworkapp2

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private var isFabOpen = false
    private lateinit var fab: FloatingActionButton
    private lateinit var buttonAddProject: Button
    private lateinit var buttonAddWorkers: Button
    private lateinit var sharedViewModel: SharedViewModel // SharedViewModel para pasar datos entre fragments
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedViewModel = ViewModelProvider(this).get(SharedViewModel::class.java) // Inicializando el SharedViewModel

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_proyecto, RecyclerFragment())
                .commit()
        }

        fab = findViewById(R.id.fab)
        buttonAddProject = findViewById(R.id.button_add_project)
        buttonAddWorkers = findViewById(R.id.button_add_workers)
        val fabOpenAnimation = AnimationUtils.loadAnimation(this, R.anim.fab_open)
        val fabCloseAnimation = AnimationUtils.loadAnimation(this, R.anim.fab_close)

        fab.setOnClickListener {
            // Acción a realizar al hacer clic en el FAB
            val fabRotation = AnimationUtils.loadAnimation(this, R.anim.fab_rotation)
            fab.startAnimation(fabRotation)
            if (isFabOpen) {
                buttonAddProject.startAnimation(fabCloseAnimation)
                buttonAddWorkers.startAnimation(fabCloseAnimation)
                buttonAddProject.visibility = View.GONE
                buttonAddWorkers.visibility = View.GONE
                isFabOpen = false
            } else {
                buttonAddProject.visibility = View.VISIBLE
                buttonAddWorkers.visibility = View.VISIBLE
                buttonAddProject.startAnimation(fabOpenAnimation)
                buttonAddWorkers.startAnimation(fabOpenAnimation)
                isFabOpen = true
            }
        }
        buttonAddProject.setOnClickListener {
            showAddProjectDialog() // Llamar a la función para mostrar el diálogo de agregar proyecto
        }

        buttonAddWorkers.setOnClickListener {
            // Acción para el botón "Agregar Trabajadores"
            Toast.makeText(this, "Funcionalidad de agregar trabajadores por implementar", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showAddProjectDialog() {
        // Función para mostrar el diálogo de agregar proyecto
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Agregar Proyecto")

        val input = EditText(this) // EditText para el nombre del proyecto
        input.hint = "Nombre del Proyecto"
        builder.setView(input)

        // Agregar botones al diálogo
        builder.setPositiveButton("Agregar") { dialog, _ ->
            val projectName = input.text.toString().trim()
            // Verificar que el nombre del proyecto no esté vacío
            if (projectName.isEmpty()) {
                Toast.makeText(this, "El nombre del proyecto no puede estar vacío", Toast.LENGTH_SHORT).show()
            } else {
                // Verificar que el nombre del proyecto no exista ya
                val exists = sharedViewModel.itemList.value?.any { it.title == projectName } == true
                if (exists) {
                    Toast.makeText(this, "El nombre del proyecto ya existe", Toast.LENGTH_SHORT).show()
                } else {
                    val newItem = Item(projectName, "Descripción del Proyecto $projectName")
                    sharedViewModel.addItem(newItem) // Agregar el nuevo proyecto al ViewModel
                    dialog.dismiss()
                }
            }
        }
        builder.setNegativeButton("Cancelar") { dialog, _ -> dialog.cancel() } // Agregar el botón "Cancelar"

        builder.show()
    }

    private fun showAddWorkerDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Agregar Trabajador")

        val input = EditText(this) // EditText para el nombre del trabajador
        input.hint = "Nombre del Trabajador"
        builder.setView(input)

        builder.setPositiveButton("Agregar") { dialog, _ ->
            val workerName = input.text.toString().trim()
            if (workerName.isEmpty()) {
                Toast.makeText(this, "El nombre del trabajador no puede estar vacío", Toast.LENGTH_SHORT).show()
            } else {
                sharedViewModel.addWorker(workerName) // Agregar el nuevo trabajador al ViewModel
                dialog.dismiss()
            }
        }
        builder.setNegativeButton("Cancelar") { dialog, _ -> dialog.cancel() }

        builder.show()
    }
}
