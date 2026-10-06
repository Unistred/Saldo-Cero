package com.MalloPalermoGuzman.SaldoCero.gestores;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
public class GestorMusica {
    private Music musicaActual;

    public void reproducir(String nombreArchivo, boolean enBucle) {
        if (musicaActual != null) {
            musicaActual.stop();
            musicaActual.dispose();
        }
        String ruta = "audio/musica/" + nombreArchivo;
        musicaActual = Gdx.audio.newMusic(Gdx.files.internal(ruta));
        musicaActual.setLooping(enBucle);
        musicaActual.play();
    }

    public void fijarVolumen(float volumen) {
        if (musicaActual != null) {
            musicaActual.setVolume(volumen);
        }}

    public void pausar() {
        if (musicaActual != null && musicaActual.isPlaying()) {
            musicaActual.pause();
        }}

    public void reanudar() {
        if (musicaActual != null && !musicaActual.isPlaying()) {
            musicaActual.play();
        }}

    public void dispose() {
        if (musicaActual != null) {
            musicaActual.dispose();
            musicaActual = null;
        }}}
