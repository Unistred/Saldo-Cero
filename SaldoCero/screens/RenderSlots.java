package com.MalloPalermoGuzman.SaldoCero.screens;

import com.MalloPalermoGuzman.SaldoCero.JuegoPrincipal;
import com.MalloPalermoGuzman.SaldoCero.juegos.Slots;
import com.MalloPalermoGuzman.SaldoCero.entidades.Jugador;
import com.MalloPalermoGuzman.SaldoCero.enums.Simbolo;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
    private HashMap<Simbolo, Texture> simbolos;
    private Animation<TextureRegion> animacionRodillo;
    private float tiempoAnimacion;
    private boolean girando;

    public RenderSlots(JuegoPrincipal game, Jugador jugador) {
        this.game = game;
        this.slots = new Slots(jugador, 10);
        maquinaSlot = new Texture(Gdx.files.internal("texturas/juegos/slots/slot.png"));
        simbolos = new HashMap<>();
        simbolos.put(Simbolo.CEREZA, new Texture(Gdx.files.internal("texturas/juegos/slots/cereza.png")));
        simbolos.put(Simbolo.DIAMANTE, new Texture(Gdx.files.internal("texturas/juegos/slots/diamante.png")));
        simbolos.put(Simbolo.SIETE, new Texture(Gdx.files.internal("texturas/juegos/slots/siete.png")));
        simbolos.put(Simbolo.CAMPANA, new Texture(Gdx.files.internal("texturas/juegos/slots/campana.png")));
        simbolos.put(Simbolo.MONEDA, new Texture(Gdx.files.internal("texturas/juegos/slots/moneda.png")));
        Texture sheet = new Texture(Gdx.files.internal("texturas/juegos/slots/animacionSlot.png"));
        TextureRegion[][] frames = TextureRegion.split(sheet, 64, 64);
        TextureRegion[] secuencia = new TextureRegion[frames[0].length];
        for (int i = 0; i < frames[0].length; i++) {
            secuencia[i] = frames[0][i];
        }
        animacionRodillo = new Animation<>(0.05f, secuencia);
        tiempoAnimacion = 0f;
        girando = false;
    }

    public void render(float delta) {
        ScreenUtils.clear(0.1f, 0.1f, 0.15f, 1f);

        game.batch.begin();
        game.batch.draw(maquinaSlot, 200, 150, 256, 256);

        if (girando) {
            tiempoAnimacion += delta;
            TextureRegion frame = animacionRodillo.getKeyFrame(tiempoAnimacion, true);
            game.batch.draw(frame, 200, 150, 256, 256);
            if (tiempoAnimacion > 1.5f) {
                girando = false;
                slots.girarRodillos();
                slots.evaluarResultado();
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
        game.batch.end();
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && !girando){
            if (slots.intentarGirar()) {
                girando = true;
                tiempoAnimacion = 0f;
            }
        }
    }

    public void dispose() {
        maquinaSlot.dispose();
        for (Texture t : simbolos.values()) t.dispose();
    }
}
