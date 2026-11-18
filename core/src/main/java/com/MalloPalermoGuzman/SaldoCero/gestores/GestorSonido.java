package com.MalloPalermoGuzman.SaldoCero.gestores;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;

public class GestorSonido {
    private float volumenInicial = 0.5f;
    private float pasoVolumen = 0.1f;
    private float volumen = volumenInicial;
    private boolean silenciado = false;
    private Sound sonidoGirar;
    //private Sound sonidoPremio;
    //private Sound sonidoChoque;

    public void cargarSonidos() {
        sonidoGirar = Gdx.audio.newSound(Gdx.files.internal("audio/efectos/slotGirando.wav"));
        //sonidoPremio = Gdx.audio.newSound(Gdx.files.internal("audio/efectos/premio.wav"));
        //sonidoChoque = Gdx.audio.newSound(Gdx.files.internal("audio/efectos/choque.wav"));
    }

    public void reproducirGirar() {
        if (sonidoGirar != null) sonidoGirar.play();
    }

    //public void reproducirPremio() {
    //    if (sonidoPremio != null) sonidoPremio.play();
    //}

    //public void reproducirChoque() {
    //    if (sonidoChoque != null) sonidoChoque.play();
    //}
    public void fijarVolumen(float volumen){
        this.volumen = MathUtils.clamp(volumen, 0f, 1f);
    }
    public void subirVolumen(){
        silenciado = false;
        fijarVolumen(volumen + pasoVolumen);
    }
    public void bajarVolumen(){
        silenciado = false;
        fijarVolumen(volumen - pasoVolumen);
    }
    public void alternarSilencio(){
        silenciado = !silenciado;
    }
    public float getVolumen() {
        return volumen;
    }
    public boolean isSilenciado() {
        return silenciado;
    }
    public void dispose() {
        if (sonidoGirar != null) sonidoGirar.dispose();
        //if (sonidoPremio != null) sonidoPremio.dispose();
       // if (sonidoChoque != null) sonidoChoque.dispose();
    }
}
