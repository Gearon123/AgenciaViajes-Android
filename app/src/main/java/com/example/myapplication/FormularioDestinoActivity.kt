package com.example.myapplication

import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityFormularioDestinoBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.io.File

class FormularioDestinoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFormularioDestinoBinding
    private var imagenUri: Uri? = null
    private val db = FirebaseFirestore.getInstance()
    private var destinoId: String? = null

    private val seleccionarGaleria = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            imagenUri = uri
            binding.ivPreview.setImageURI(uri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormularioDestinoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val paises = arrayOf("El Salvador", "Guatemala", "Honduras", "Costa Rica", "México")
        binding.spinnerPais.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, paises)

        destinoId = intent.getStringExtra("DESTINO_ID")

        binding.btnSeleccionarImagen.setOnClickListener { seleccionarGaleria.launch("image/*") }
        binding.btnGuardar.setOnClickListener { validarYGuardar() }
    }

    private fun validarYGuardar() {
        val nombre = binding.etNombre.text.toString().trim()
        val pais = binding.spinnerPais.selectedItem.toString()
        val precioStr = binding.etPrecio.text.toString().trim()
        val descripcion = binding.etDescripcion.text.toString().trim()

        // Validación 1: No se permiten campos nulos o vacíos
        if (nombre.isEmpty() || precioStr.isEmpty() || descripcion.isEmpty()) {
            Toast.makeText(this, "No se permiten campos nulos o vacíos", Toast.LENGTH_SHORT).show()
            return
        }

        // Validación 2: El precio debe ser obligatoriamente mayor a 0
        val precio = precioStr.toDoubleOrNull()
        if (precio == null || precio <= 0) {
            binding.etPrecio.error = "El precio debe ser mayor a 0"
            return
        }

        // Validación 3: Descripción mínimo de 20 caracteres
        if (descripcion.length < 20) {
            binding.etDescripcion.error = "Mínimo 20 caracteres"
            return
        }

        // Validación 4: Cada destino debe tener una imagen asociada
        if (imagenUri == null && destinoId == null) {
            Toast.makeText(this, "Debe seleccionar una imagen", Toast.LENGTH_SHORT).show()
            return
        }

        Toast.makeText(this, "Guardando destino...", Toast.LENGTH_SHORT).show()

        // Copiamos la imagen de forma permanente a la memoria interna de la app
        val rutaImagenFinal = if (imagenUri != null) {
            guardarImagenEnMemoriaInterna(imagenUri!!)
        } else {
            intent.getStringExtra("IMAGEN_URL") ?: ""
        }

        guardarEnFirestore(nombre, pais, precio, descripcion, rutaImagenFinal)
    }

    private fun guardarImagenEnMemoriaInterna(uri: Uri): String {
        val archivo = File(filesDir, "img_${System.currentTimeMillis()}.jpg")
        contentResolver.openInputStream(uri)?.use { input ->
            archivo.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        return archivo.absolutePath
    }

    private fun guardarEnFirestore(nombre: String, pais: String, precio: Double, descripcion: String, url: String) {
        val datos = mutableMapOf<String, Any>(
            "nombre" to nombre, "pais" to pais, "precio" to precio, "descripcion" to descripcion
        )
        if (url.isNotEmpty()) datos["imagenUrl"] = url

        if (destinoId == null) {
            val nuevoDoc = db.collection("destinos").document()
            datos["id"] = nuevoDoc.id
            nuevoDoc.set(datos).addOnSuccessListener {
                Toast.makeText(this, "¡Guardado con éxito!", Toast.LENGTH_SHORT).show()
                finish()
            }
        } else {
            db.collection("destinos").document(destinoId!!).update(datos).addOnSuccessListener {
                Toast.makeText(this, "¡Actualizado con éxito!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}