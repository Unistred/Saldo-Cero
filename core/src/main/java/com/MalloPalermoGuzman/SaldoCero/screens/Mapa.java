package com.MalloPalermoGuzman.SaldoCero.screens;
import com.MalloPalermoGuzman.SaldoCero.JuegoPrincipal;
import com.MalloPalermoGuzman.SaldoCero.colisiones.ManejarColisiones;
import com.MalloPalermoGuzman.SaldoCero.entidades.Obstaculo;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.MalloPalermoGuzman.SaldoCero.entidades.Jugador;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import java.util.ArrayList;




public class Mapa implements Screen {
    private static final int ID_SLOT = 4;
    private static final float ALCANCE = 12;
    private static final float VISTA_ANCHO = 448;
    private static final float VISTA_ALTO = 384;
    private float limiteIzq, limiteDer, limiteAbajo, limiteArriba;
    private Texture fondoHud;
    private TiledMap map =new TmxMapLoader().load("texturas/mapa/casino_saldo_cero.tmx");
    private OrthogonalTiledMapRenderer renderer = new OrthogonalTiledMapRenderer(map);
    private JuegoPrincipal juegoPrincipal;
    private OrthographicCamera camera;
    private Viewport viewport;
    private ArrayList<Rectangle> paredes = new ArrayList<>();
    private ArrayList<Obstaculo> slots = new ArrayList<>();
    private Jugador jugador;
    private TextureRegion spriteJugador;
    private RenderSlots renderSlots;
    private OrthographicCamera cameraSlots;
    private Viewport viewportSlots;
    private boolean jugandoSlots = false;

    public Mapa(JuegoPrincipal juegoPrincipal) {
        this.juegoPrincipal = juegoPrincipal;
    }

    private void cargarParedes(){
        TiledMapTileLayer capa1 = (TiledMapTileLayer) map.getLayers().get(0);
        TiledMapTileLayer capa2 = (TiledMapTileLayer) map.getLayers().get(1);
        float tamaño = capa1.getTileHeight();
        for (int i = 0; i < capa1.getWidth(); i++){
            for(int j = 0; j < capa1.getHeight(); j++){
                TiledMapTileLayer.Cell c1 = capa1.getCell(i, j);
                TiledMapTileLayer.Cell c2 = capa2.getCell(i, j);
                boolean pared = c1 != null && (c1.getTile().getId() == 2 || c1.getTile().getId() == 3);
                boolean objeto = c2 != null;
                boolean slot = c2 != null && c2.getTile().getId() == ID_SLOT;

                if(slot){
                    Obstaculo maquina = new Obstaculo(i * tamaño, j * tamaño);
                    maquina.getHitbox().setSize(tamaño, tamaño);
                    slots.add(maquina);
                } else if(pared || objeto){
                    paredes.add(new Rectangle(i * tamaño, j * tamaño, tamaño, tamaño));
                }
            }
        }
        limiteIzq = Float.MAX_VALUE;
        limiteAbajo = Float.MAX_VALUE;
        limiteDer = 0;
        limiteArriba = 0;
        for(Rectangle pared : paredes){
            limiteIzq = Math.min(limiteIzq, pared.x);
            limiteAbajo = Math.min(limiteAbajo, pared.y);
            limiteDer = Math.max(limiteDer, pared.x + pared.width);
            limiteArriba = Math.max(limiteArriba, pared.y + pared.height);
        }
    }
    private void seguirJugador(){
        float anchoVista = viewport.getWorldWidth();
        float altoVista = viewport.getWorldHeight();
        Rectangle hitbox = jugador.getHitbox();
        float x = hitbox.x + hitbox.width / 2;
        float y = hitbox.y + hitbox.height / 2;

        if (anchoVista >= limiteDer - limiteIzq){ x = (limiteIzq + limiteDer) / 2;}
        else{ x = MathUtils.clamp(x, limiteIzq + anchoVista / 2, limiteDer - anchoVista / 2);}
        if(altoVista >= limiteArriba - limiteAbajo) {y = (limiteAbajo + limiteArriba) / 2;}
        else {y = MathUtils.clamp(y, limiteAbajo + altoVista / 2, limiteArriba - altoVista / 2); }
        camera.position.set(x, y, 0);
    }
    private boolean verificarCercaDeSlot(){
        for(Obstaculo slot : slots){
            Rectangle hb = slot.getHitbox();
            Rectangle zona = new Rectangle(hb.x - ALCANCE, hb.y - ALCANCE, hb.width + ALCANCE * 2, hb.height + ALCANCE * 2);
            if (Intersector.overlaps(jugador.getHitbox(), zona)){
                return true;
            }
        }
        return false;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(VISTA_ANCHO, VISTA_ALTO, camera);
        Pixmap pixel = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixel.setColor(0, 0, 0, 0.6f);
        pixel.fill();
        fondoHud = new Texture(pixel);
        pixel.dispose();

        cameraSlots = new OrthographicCamera();
        viewportSlots = new FitViewport(JuegoPrincipal.VIRTUAL_WIDTH, JuegoPrincipal.VIRTUAL_HEIGHT, cameraSlots);
        cameraSlots.position.set(JuegoPrincipal.VIRTUAL_WIDTH / 2f, JuegoPrincipal.VIRTUAL_HEIGHT / 2f, 0);
        cameraSlots.update();

        paredes = new ArrayList<>();
        slots = new ArrayList<>();
        cargarParedes();
        jugador = new Jugador(512, 640, "julio");
        jugador.getHitbox().setSize(32, 32);
        spriteJugador = new TextureRegion(jugador.getTextura(), 0, 0, 32, 32);

        renderSlots = new RenderSlots(juegoPrincipal, jugador);
        jugandoSlots = false;
        seguirJugador();
        camera.update();
    }

    @Override
    public void render(float delta) {
        if (jugandoSlots) {
            renderizarSlots(delta);
            return;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            juegoPrincipal.setScreen(new MainMenuScreen(juegoPrincipal));
            dispose();
            return;
        }
        jugador.update(delta);
        for(Rectangle pared : paredes){
            if (Intersector.overlaps(jugador.getHitbox(), pared)){
                jugador.deshacerMovimiento();
                break;
            }
        }

        for(Obstaculo slot : slots){
            if (ManejarColisiones.verificarChoque(jugador, slot)){
                jugador.deshacerMovimiento();
                break;
            }
        }

        boolean cerca = verificarCercaDeSlot();
        if (cerca && Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            jugandoSlots = true;
        }
        seguirJugador();
        ScreenUtils.clear(0,0,0,1);
        viewport.apply();
        camera.update();
        renderer.setView(camera);
        renderer.render();
        juegoPrincipal.batch.setProjectionMatrix(camera.combined);
        juegoPrincipal.batch.begin();
        Rectangle hitbox = jugador.getHitbox();
        juegoPrincipal.batch.draw(spriteJugador, hitbox.x, hitbox.y, hitbox.width, hitbox.height);
        dibujarHud(cerca);
        juegoPrincipal.batch.end();
    }
    private void dibujarHud(boolean cerca) {
        float ancho = viewport.getWorldWidth();
        float izq = camera.position.x - ancho / 2;
        float arriba = camera.position.y + viewport.getWorldHeight() / 2;
        float abajo = camera.position.y - viewport.getWorldHeight() / 2;

        juegoPrincipal.batch.draw(fondoHud, izq, arriba - 28, ancho, 28);
        juegoPrincipal.font.setColor(Color.WHITE);
        juegoPrincipal.font.draw(juegoPrincipal.batch, "Saldo: $" + jugador.getSaldo(), izq + 12, arriba - 8);
        juegoPrincipal.font.draw(juegoPrincipal.batch, "Cordura: " + jugador.getCordura(), izq + 150, arriba - 8);
        juegoPrincipal.font.draw(juegoPrincipal.batch, "M: Menu principal", izq + ancho - 130, arriba - 8);

        if (cerca) {
            juegoPrincipal.batch.draw(fondoHud, izq, abajo, ancho, 28);
            juegoPrincipal.font.draw(juegoPrincipal.batch, "Presiona E para jugar", izq + ancho / 2 - 60, abajo + 20);
        }
    }

    private void renderizarSlots(float delta) {
        viewportSlots.apply();
        juegoPrincipal.batch.setProjectionMatrix(cameraSlots.combined);
        renderSlots.render(delta);
        juegoPrincipal.batch.begin();
        juegoPrincipal.font.setColor(Color.WHITE);
        juegoPrincipal.font.draw(juegoPrincipal.batch, "ESC para volver al mapa", 220, 60);
        juegoPrincipal.batch.end();
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            jugandoSlots = false;
        }
    }

    @Override
    public void resize(int width, int height) {
        if (width <= 0 || height <= 0) return;
        viewport.update(width, height);
        viewportSlots.update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        if (fondoHud != null) fondoHud.dispose();
        if (jugador != null) jugador.dispose();
        if (renderSlots != null) renderSlots.dispose();
        for(Obstaculo slot : slots){
            slot.dispose();
        }
    }
}