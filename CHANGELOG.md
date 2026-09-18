### Añadido
- Configuración de navegación entre pantallas principales (MainMenuScreen y GameScreen) usando la clase Game de LibGDX.
- Creación del enum Simbolo para representar las figuras y multiplicadores de la tragamonedas.
- Implementación del enum EstadoJugador para el control de movimiento/interacción.
- Creación de la clase Jugador con gestión de saldo y cordura.
- Implementación de la clase Slots con lógica de tiro, aleatorización de rodillos y evaluación de premios.
- Organización del proyecto en paquetes (enums, juegos, screens, usuarios).


## [0.1.2] - 03-09-2026

### Añadido
Implementación de StretchViewport y suavizado visual
- Se añadieron StretchViewport en pantallas principales
- Se activó filtrado lineal en fuentes/texturas para reducir pixelación

## [0.1.3] - 08-09-2026

### Añadido
Mejoras visuales y renderizado de slots
- Suavizado de texturas en el viewport
- Incorporación de texturas para jugador, máquina de slots y símbolos
- Implementación del renderizado y animación del juego de slots

## [0.1.4] - 17-09-2026

### Añadido

- Eliminar la clase Usuarios y crear el paquete/clase Entidades.
- Mover la antigua clase Jugador (antes dentro de Usuarios) a Entidades.
- Reorganizar colisiones para mantener únicamente ManejarColisiones.
- Integrar ObjetoJuego y Obstaculo dentro de Entidades.
- Corregir bug en el minijuego de slots/ruleta que permitía volver a girar mientras la animación de giro seguía activa.
