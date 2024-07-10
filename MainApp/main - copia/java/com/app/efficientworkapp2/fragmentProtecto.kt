package com.app.efficientworkapp2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecyclerFragment : Fragment() {

    private lateinit var sharedViewModel: SharedViewModel // Sirve para pasar datos entre fragments
    private lateinit var recyclerView: RecyclerView // Es la lista dinamica principal, contenida dentro de los cards
    private lateinit var adapter: MyAdapter // Es el adaptador para la lista dinamica

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_proyectos, container, false) // el layout que se va a mostrar utilizando el inflater

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java) // Inicializando el sharedViewModel para pasar datos entre activity-fragment

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)

        adapter = MyAdapter(mutableListOf()) { item ->
            // Acción al mantener presionado un proyecto
            showDeleteProjectDialog(item)
        }
        recyclerView.adapter = adapter

        sharedViewModel.itemList.observe(viewLifecycleOwner, Observer { items -> // Verificar si ha cambiado la lista
            adapter.updateList(items)
        })

        return view // Retorna la vista que se va a mostrar
    }

    private fun showDeleteProjectDialog(item: Item) {
        // Función para mostrar el diálogo de eliminar proyecto
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Eliminar Proyecto")
            .setMessage("¿Estás seguro de que deseas eliminar el proyecto '${item.title}'?")
            .setPositiveButton("Eliminar") { _, _ ->
                sharedViewModel.removeItem(item)
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}