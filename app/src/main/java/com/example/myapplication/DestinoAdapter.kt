package com.example.myapplication

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.ItemDestinoBinding
import com.example.myapplication.model.Destino // <-- Importación clave para que reconozca los campos
import com.google.firebase.firestore.FirebaseFirestore

class DestinoAdapter(private val listaDestinos: List<Destino>) : RecyclerView.Adapter<DestinoAdapter.DestinoViewHolder>() {

    class DestinoViewHolder(val binding: ItemDestinoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DestinoViewHolder {
        val binding = ItemDestinoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DestinoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DestinoViewHolder, position: Int) {
        val destino = listaDestinos[position]

        holder.binding.tvNombre.text = destino.nombre ?: ""
        holder.binding.tvPais.text = destino.pais ?: ""
        holder.binding.tvPrecio.text = "$${destino.precio}"
        holder.binding.tvDescripcion.text = destino.descripcion ?: ""

        // Carga de imágenes estable con Glide
        if (!destino.imagenUrl.isNullOrEmpty()) {
            Glide.with(holder.itemView.context)
                .load(destino.imagenUrl)
                .centerCrop()
                .into(holder.binding.ivImagen)
        }

        // Funcionalidad del botón Editar
        holder.binding.btnEditar.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, FormularioDestinoActivity::class.java).apply {
                putExtra("DESTINO_ID", destino.id)
                putExtra("IMAGEN_URL", destino.imagenUrl)
            }
            context.startActivity(intent)
        }

        // Funcionalidad del botón Eliminar
        holder.binding.btnEliminar.setOnClickListener {
            val context = holder.itemView.context
            FirebaseFirestore.getInstance().collection("destinos")
                .document(destino.id ?: "")
                .delete()
                .addOnSuccessListener {
                    Toast.makeText(context, "Destino eliminado", Toast.LENGTH_SHORT).show()
                }
        }
    }

    override fun getItemCount(): Int = listaDestinos.size
}