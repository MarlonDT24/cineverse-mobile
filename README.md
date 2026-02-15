# CineVerse - Aplicación Móvil Nativa Android 📱

**CineVerse Mobile** es la interfaz de usuario final del ecosistema CineVerse, diseñada para ofrecer una experiencia fluida en la consulta de cartelera, gestión de eventos especiales y reserva de entradas. Desarrollada como parte del Proyecto Final de Grado (DAM 2026).

---

## ✨ Características Principales
- **Cartelera Dinámica:** Visualización de películas en sala consumidas desde el backend propio mediante Retrofit.
- **Eventos Especiales:** Integración con la API externa **SWAPI** para mostrar noches temáticas de Star Wars con datos de personajes y películas.
- **Gestión de Entradas:** Sistema de reservas local y consulta de historial de compras.
- **Soporte en Tiempo Real:** Chat de atención al cliente integrado con el panel administrativo web.

---

## 🛠️ Stack Tecnológico
- **Lenguaje:** Kotlin (100% Nativo).
- **Interfaz de Usuario:** Jetpack Compose (Declarative UI).
- **Arquitectura:** MVVM (Model-View-ViewModel).
- **Red:** Retrofit & OkHttp para el consumo de servicios REST.
- **Persistencia:** Room Database para el almacenamiento local de datos.
- **Procesamiento de Datos:** GSON para el mapeo de respuestas JSON mediante `@SerializedName`.

---

## 🚀 Instalación y Puesta en Marcha

Para compilar y ejecutar la aplicación en un entorno de desarrollo:

### 1. Requisitos Previos
- **Android Studio Ladybug** (o versión superior).
- **JDK 17** configurado.
- Backend de CineVerse en ejecución (para la sincronización de datos).

### 2. Configuración de Red
1. Localice la clase de configuración de **Retrofit** en el paquete `network`.
2. Actualice la constante `BASE_URL` con la dirección IP de su máquina local (ejemplo: `http://192.168.1.XX:8080/api/`). 
   *Nota: No use `localhost` si está probando en un dispositivo físico o emulador.*

### 3. Ejecución
1. Sincronice el proyecto con los archivos de **Gradle**.
2. Seleccione un dispositivo con **API 34** o superior.
3. Haga clic en **Run 'app'**.

---

## 📸 Evidencias de Funcionamiento
La aplicación ha sido validada mediante pruebas de usabilidad, logrando un renderizado de cartelera y eventos especiales en menos de 300ms tras la respuesta del servidor.

---

## 👤 Autor
**Marlon Daniel Torres Sangacha**

*Nota: Este cliente móvil requiere del servidor centralizado alojado en el repositorio [CineVerse-Web](https://github.com/MarlonDT24/cineverse-web).*
