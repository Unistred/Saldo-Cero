a### Añadido
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

## [0.2.0] - 19-10-2026
### Añadido
- Integración completa de sistemas de audio, slots y corrección de movimiento en el mapa
- Implementación de GestorMusica y GestorSonido para la gestión centralizada de pistas de fondo y efectos especiales.
- Incorporación de archivos multimedia de audio (.mp3 y .wav) y conexión del sonido de giro en las máquinas tragamonedas.
- Creación de la clase RenderSlots con soporte para animaciones de rodillos mediante Animation<TextureRegion>.
- Integración de la estructura del mapa de Tiled (casino_saldo_cero.tmx) con OrthogonalTiledMapRenderer.
- Corrección de la velocidad del jugador (fijada en 150f) para solucionar el bloqueo de movimiento en el mapa.
- Configuración del sistema de colisiones con paredes y slots utilizando Intersector y ManejarColisiones.
- Implementación de la cámara seguidora con ExtendViewport y el HUD dinámico de saldo, cordura y avisos en pantalla.

## [0.2.1] - 09-11-2026
### Añadido
- Corrección de la textura de la animacion del jugador y ajuste de hitbox para mejorar la interacción con el entorno y la detección de colisiones. La textura del jugador ahora es de 25x28 píxeles, y la hitbox se ajusta a 25x28 píxeles para reflejar con precisión la forma del personaje y permitir un movimiento más fluido y realista dentro del mapa.

## [0.2.2] - 16-11-2026
### Añadido
- Creacion de la base de datos con sqlLite

## [0.2.3] - 17-11-2026
### Añadido
## Arreglo de bugs
- Corregida la velocidad del movimiento diagonal del jugador, evitando que sea superior a la del movimiento horizontal o vertical.
- Corregida la gestión del volumen para evitar que los valores superen los límites establecidos.
- Corregido el control del volumen inicial de los efectos de sonido, estableciéndolo en un 50%.
- Corregida la administración de recursos para evitar liberar gestores de música que siguen siendo utilizados.
- Mejorada la liberación de recursos al cerrar el juego, incluyendo la pantalla activa y los elementos compartidos.

## Mejoras de estructura del codigo

- Incorporado ControladorEntrada para centralizar la lectura del teclado y separar los controles de la lógica del juego.
- Modificada la clase Jugador para recibir la dirección de movimiento mediante Vector2, desacoplando el movimiento de la lectura directa del teclado.
- Normalizada la dirección de movimiento para mantener una velocidad uniforme en todas las direcciones.
- Modificada ObjetoJuego para permitir objetos sin textura, conservando únicamente su hitbox cuando corresponde.
- Optimizada la clase Obstaculo para utilizar solamente su hitbox, evitando cargar texturas innecesarias.
- Centralizada en JuegoPrincipal la gestión global del volumen y el silencio de música y efectos de sonido.
- Incorporados controles independientes para subir, bajar y silenciar el volumen mediante el teclado (+, - y N).
- Mejorados GestorMusica y GestorSonido para conservar el volumen configurado y aplicar correctamente el estado de silencio.
- Mejorada la administración de la música del menú, incorporando su detención al iniciar el juego.
- Incorporada la visualización del volumen y del estado de silencio en el menú principal y en el HUD del mapa.
- Modificado Mapa para delegar el movimiento, la interacción y la pausa mediante el controlador de entrada, manteniendo la navegación del menú de pausa con su implementación actual.