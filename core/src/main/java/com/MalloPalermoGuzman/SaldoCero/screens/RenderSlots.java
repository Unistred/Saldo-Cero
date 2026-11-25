package com.MalloPalermoGuzman.SaldoCero.screens;

import com.MalloPalermoGuzman.SaldoCero.JuegoPrincipal;
import com.MalloPalermoGuzman.SaldoCero.baseDeDatos.GestorBD;
import com.MalloPalermoGuzman.SaldoCero.configNiveles.ConfigNivel;
import com.MalloPalermoGuzman.SaldoCero.configNiveles.Partida;
import com.MalloPalermoGuzman.SaldoCero.juegos.Slots;
import com.MalloPalermoGuzman.SaldoCero.enums.Simbolo;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.HashMap;

public class RenderSlots {

    private final JuegoPrincipal game;
    private final Slots slots;

    private Texture maquinaSlot;
    private Texture sheet;
    private HashMap<Simbolo, Texture> simbolos;

    private Animation<TextureRegion> animacionRodillo;
    private float tiempoAnimacion;
    private boolean girando;

    public RenderSlots(JuegoPrincipal game) {
        this.game = game;
        this.slots = new Slots(game.partida);
        maquinaSlot = new Texture(Gdx.files.internal("texturas/juegos/slots/slot.png"));
        simbolos = new HashMap<>();
        simbolos.put(Simbolo.CEREZA, new Texture(Gdx.files.internal("texturas/juegos/slots/cereza.png")));
        simbolos.put(Simbolo.DIAMANTE, new Texture(Gdx.files.internal("texturas/juegos/slots/diamante.png")));
        simbolos.put(Simbolo.SIETE, new Texture(Gdx.files.internal("texturas/juegos/slots/siete.png")));
        simbolos.put(Simbolo.CAMPANA, new Texture(Gdx.files.internal("texturas/juegos/slots/campana.png")));
        simbolos.put(Simbolo.MONEDA, new Texture(Gdx.files.internal("texturas/juegos/slots/moneda.png")));
        sheet = new Texture(Gdx.files.internal("texturas/juegos/slots/animacionSlot.png"));
        TextureRegion[][] frames = TextureRegion.split(sheet, 64, 64);
        TextureRegion[] secuencia = new TextureRegion[frames[0].length];
        for (int i = 0; i < frames[0].length; i++) {
            secuencia[i] = frames[0][i];
        }
        animacionRodillo = new Animation<>(0.05f, secuencia);
        tiempoAnimacion = 0f;
        girando = false;
    }

    public boolean isGirando() {
        return girando;
    }

    /** Impide volver al mapa mientras los rodillos todavia estan girando. */
    public void avisarSalidaBloqueada() {
        slots.setMensaje("Termina el giro antes de salir");
    }

    /** Guarda la ultima tirada en SQLite. Seguro llamarlo dos veces: la bandera evita duplicados. */
    public void registrarApuestaPendiente() {
        if (!slots.isRegistroPendiente()) return;
        GestorBD.registrarApuesta(game.idJugador, "Slots", slots.getUltimaApuesta(),
                slots.getUltimoGanado(), game.partida.getSaldo(), game.partida.getCorduraRedondeada());
        slots.marcarRegistrado();
        game.registrarLogrosPendientes();
    }

    // si entradaHabilitada es false (se acabo el tiempo del nivel) se sigue dibujando, pero no se puede girar ni cambiar la apuesta
    public void render(float delta, boolean entradaHabilitada) {
        ScreenUtils.clear(0.1f, 0.1f, 0.15f, 1f);
        game.batch.begin();
        game.batch.draw(maquinaSlot, 200, 150, 256, 256);

        if (girando) {
            tiempoAnimacion += delta;
            TextureRegion frame = animacionRodillo.getKeyFrame(tiempoAnimacion, true);
            game.batch.draw(frame, 200, 150, 256, 256); // reemplaza la máquina vacía

            if (tiempoAnimacion > 4.4f) {
                girando = false;
                slots.girarRodillos();
                slots.evaluarResultado();
                registrarApuestaPendiente();
            }
        } else {
            game.batch.draw(maquinaSlot, 200, 150, 256, 256);
            game.batch.draw(simbolos.get(slots.getRodillo1()), 192, 188, 164f, 164f);
            game.batch.draw(simbolos.get(slots.getRodillo2()), 242, 188, 164f, 164f);
            game.batch.draw(simbolos.get(slots.getRodillo3()), 294, 188, 164f, 164f);
        }
        game.font.setColor(Color.GOLD);
        game.font.draw(game.batch, slots.getMensaje(), 220, 120);
        game.font.setColor(Color.GREEN);
        game.font.draw(game.batch, "Saldo: $" + slots.getSaldoJugador(), 220, 90);
        dibujarPanelDerecho();
        game.batch.end();
        if (entradaHabilitada) {
            procesarEntrada();
        }
    }

    // columna de la derecha: nivel, tiempos, meta y apuesta
    private void dibujarPanelDerecho() {
        Partida partida = game.partida;
        ConfigNivel nivel = partida.getNivel();
        game.font.setColor(Color.WHITE);
        game.font.draw(game.batch, nivel.getNombre(), 500, 380);
        game.font.draw(game.batch, "Tiempo: " + partida.getTiempoRestanteTexto(), 500, 355);
        game.font.draw(game.batch, "Total: " + partida.getTiempoTotalTexto(), 500, 335);
        game.font.draw(game.batch, "Meta: $" + nivel.getMetaSaldo(), 500, 310);
        game.font.draw(game.batch, "Cordura: " + partida.getCorduraRedondeada(), 500, 290);
        game.font.setColor(Color.GOLD);
        game.font.draw(game.batch, "Apuesta: $" + slots.getApuesta(), 500, 270);
        game.font.setColor(Color.LIGHT_GRAY);
        game.font.draw(game.batch, "Flechas arriba/abajo: cambiar apuesta", 500, 248);
        game.font.setColor(Color.WHITE);
    }

    private void procesarEntrada() {
        if (game.controladorEntrada.quiereGirarRodillosSlot()) {
            if (!girando) {
                if (slots.intentarGirar()) {
                    girando = true;
                    tiempoAnimacion = 0f;
                    game.gestorSonido.reproducirGirar();
                    // No se registra aca: el premio todavia no se conocia. Se guarda al terminar el giro.
                }
            } else {
                slots.setMensaje("Espere a que termine de girar para volver a girar.");
            }
        }
        // la apuesta solo se cambia con los rodillos quietos
        if (!girando) {
            if (game.controladorEntrada.quiereSubirApuesta()) slots.subirApuesta();
            if (game.controladorEntrada.quiereBajarApuesta()) slots.bajarApuesta();
        }
    }

    public void dispose() {
        registrarApuestaPendiente();
        maquinaSlot.dispose();
        sheet.dispose();
        for (Texture t : simbolos.values()) t.dispose();
    }
}
