# Changelog

Todos los cambios notables en este proyecto serán documentados en este archivo.

## [0.1.0] - 07-07-2026

### Añadido
- Inicialización y estructura base del proyecto interactivo **Saldo Cero** utilizando el framework **LibGDX** a través de la herramienta `gdx-liftoff`.
- Configuración del módulo de ejecución obligatorio para la plataforma de escritorio (**LWJGL3**).
- Incorporación del archivo `.gitignore` optimizado para excluir archivos temporales de Gradle, compilación (`build/`) y configuraciones específicas del IDE (`.idea/`).
- Redacción del documento de presentación del repositorio (`README.md`) detallando integrantes, descripción, tecnologías y guía de instalación.
- Activación e inicio de la sección **Wiki** en el repositorio remoto con el documento formal y detallado de la propuesta de tesis del videojuego
- 
## [0.1.1] - 31-08-2026

### Añadido
- Configuración de navegación entre pantallas principales (MainMenuScreen y GameScreen) usando la clase Game de LibGDX.
- Creación del enum Simbolo para representar las figuras y multiplicadores de la tragamonedas.
- Implementación del enum EstadoJugador para el control de movimiento/interacción.
- Creación de la clase Jugador con gestión de saldo y cordura.
- Implementación de la clase Slots con lógica de tiro, aleatorización de rodillos y evaluación de premios.
- Organización del proyecto en paquetes (enums, juegos, screens, usuarios).
