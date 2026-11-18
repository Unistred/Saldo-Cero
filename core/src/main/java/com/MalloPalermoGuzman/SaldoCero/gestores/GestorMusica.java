package com.MalloPalermoGuzman.SaldoCero.gestores;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.MathUtils;
public class GestorMusica {
    private static final float VOLUMEN_INICIAL = 0.5f;
    private static final float PASO_VOLUMEN = 0.1f;

    private Music musicaActual;
    private float volumen = VOLUMEN_INICIAL;
    private boolean silenciado = false;

    public void reproducir(String nombreArchivo, boolean enBucle) {
        if (musicaActual != null) {
            musicaActual.stop();
            musicaActual.dispose();
        }
        String ruta = "audio/musica/" + nombreArchivo;
        musicaActual = Gdx.audio.newMusic(Gdx.files.internal(ruta));
        musicaActual.setLooping(enBucle);
        aplicarVolumen();
        musicaActual.play();
    }

    public void fijarVolumen(float volumen) {
        this.volumen = redondear(MathUtils.clamp(volumen, 0f, 1f));
        aplicarVolumen();
    }

    public void subirVolumen() {
        silenciado = false;
        fijarVolumen(volumen + PASO_VOLUMEN);
    }

    public void bajarVolumen() {
        silenciado = false;
        fijarVolumen(volumen - PASO_VOLUMEN);
    }

    public void alternarSilencio() {
        silenciado = !silenciado;
        aplicarVolumen();
    }

    public float getVolumen() {
        return volumen;
    }

    public boolean isSilenciado() {
        return silenciado;
    }

    private void aplicarVolumen() {
        if (musicaActual != null) {
            musicaActual.setVolume(silenciado ? 0f : volumen);
        }
    }

    private float redondear(float valor) {
        return Math.round(valor * 100f) / 100f;
    }

    public void pausar() {
        if (musicaActual != null && musicaActual.isPlaying()) {
            musicaActual.pause();
        }}

    public void reanudar() {
        if (musicaActual != null && !musicaActual.isPlaying()) {
            musicaActual.play();
        }}
    public void detener() {
        if (musicaActual != null) {
            musicaActual.stop();
            musicaActual.dispose();
            musicaActual = null;
        }}

    public void dispose() {
        if (musicaActual != null) {
            musicaActual.dispose();
            musicaActual = null;
        }}}