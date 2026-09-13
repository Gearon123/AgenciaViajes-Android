# Aplicación Android: Agencia de Viajes ✈️

**Alumno:** Daniel Alexander Girón Cornejo
Enlace del Video de Defensa:https://www.youtube.com/watch?v=mhuKwV8arYs

Desarrollo nativo en Kotlin para la gestión de un catálogo de destinos turísticos, integrando Firebase.

## 🛠️ Tecnologías y Arquitectura
* **UI/UX:** Material Design con `MaterialCardView` y bordes redondeados.
* **Imágenes:** Carga asíncrona mediante la librería **Glide**.
* **Base de Datos:** Firebase Firestore.

## ⚠️ Instrucciones Críticas para la Evaluación

**1. Configuración de Firebase:** 
El archivo `google-services.json` ya está incluido en este repositorio. Puede clonar, sincronizar Gradle y compilar directamente sin necesidad de crear un nuevo proyecto en Firebase Console.

**2. Persistencia de Imágenes:**
Para cumplir con los requerimientos sin depender del plan de pago (Blaze) de Firebase Storage, las fotos seleccionadas de la galería se copian y persisten localmente en la memoria interna de la aplicación (`filesDir`). 
👉 **Al compilar por primera vez, las imágenes de destinos antiguos no cargarán porque apuntan a la memoria de otro dispositivo. Por favor, cree un NUEVO DESTINO desde su emulador o dispositivo físico para visualizar el flujo completo.**
