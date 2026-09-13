package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.ActivityCatalogoBinding
import com.example.myapplication.model.Destino
import com.google.firebase.firestore.FirebaseFirestore

class CatalogoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCatalogoBinding
    private val db = FirebaseFirestore.getInstance()
    private val listaDestinos = mutableListOf<Destino>()
    private lateinit var adaptador: DestinoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCatalogoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar RecyclerView
        binding.recyclerViewDestinos.layoutManager = LinearLayoutManager(this)
        adaptador = DestinoAdapter(listaDestinos)
        binding.recyclerViewDestinos.adapter = adaptador

        // Botón flotante para agregar un nuevo destino
        binding.fabAgregar.setOnClickListener {
            val intent = Intent(this, FormularioDestinoActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        cargarDestinosDesdeFirestore()
    }

    private fun cargarDestinosDesdeFirestore() {
        db.collection("destinos").get()
            .addOnSuccessListener { resultado ->
                listaDestinos.clear()
                for (documento in resultado) {
                    val destino = documento.toObject(Destino::class.java)
                    listaDestinos.add(destino)
                }
                adaptador.notifyDataSetChanged()
            }
    }
}