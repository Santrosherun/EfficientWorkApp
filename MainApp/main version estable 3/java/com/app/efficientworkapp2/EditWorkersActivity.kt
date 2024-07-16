package com.app.efficientworkapp2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class EditWorkersActivity : AppCompatActivity() {

    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var workerAdapter: WorkerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_workers)

        sharedViewModel = ViewModelProvider(this).get(SharedViewModel::class.java)

        val recyclerViewEditWorkers: RecyclerView = findViewById(R.id.recyclerViewEditWorkers)
        val buttonAddWorker: Button = findViewById(R.id.buttonAddWorker)
        val buttonSaveWorkers: Button = findViewById(R.id.buttonSaveWorkers)

        recyclerViewEditWorkers.layoutManager = LinearLayoutManager(this)
        workerAdapter = WorkerAdapter(mutableListOf(), { worker ->
            sharedViewModel.removeWorker(worker)
        }, showDeleteButton = true)
        recyclerViewEditWorkers.adapter = workerAdapter

        sharedViewModel.workerList.observe(this, Observer { workers ->
            workerAdapter.updateList(workers)
        })

        buttonAddWorker.setOnClickListener {
            showAddWorkerDialog()
        }

        buttonSaveWorkers.setOnClickListener {
            saveWorkersAndReturn()
        }
    }

    private fun showAddWorkerDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Agregar Trabajador")

        val input = EditText(this)
        input.hint = "Nombre del Trabajador"
        builder.setView(input)

        builder.setPositiveButton("Agregar") { dialog, _ ->
            val workerName = input.text.toString().trim()
            if (workerName.isEmpty()) {
                Toast.makeText(this, "El nombre del trabajador no puede estar vacío", Toast.LENGTH_SHORT).show()
            } else {
                sharedViewModel.addWorker(workerName)
                dialog.dismiss()
            }
        }
        builder.setNegativeButton("Cancelar") { dialog, _ -> dialog.cancel() }

        builder.show()
    }

    private fun saveWorkersAndReturn() {
        // Guardar y regresar a la actividad principal
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}