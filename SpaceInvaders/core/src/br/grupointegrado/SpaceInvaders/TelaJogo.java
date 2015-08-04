package br.grupointegrado.SpaceInvaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.FillViewport;


/**
 * Created by Douglas on 03/08/2015.
 */
public class TelaJogo extends TelaBase {

    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Stage palco;
    private BitmapFont fonte;
    private Label lbPontuacao;

    /**
     * Contrutor Padrão da Tela de Jogo
     * @param game Referencia para a classe principal
     */
    public TelaJogo(MainGame game) {
        super(game);
    }

    private void initfonte(){
        fonte = new BitmapFont();
    }

    private void initinformacoes(){
        Label.LabelStyle lbStilo = new Label.LabelStyle();
        lbStilo.font = fonte;
        lbStilo.fontColor = Color.WHITE;

        lbPontuacao = new Label("0 pontos", lbStilo);
        palco.addActor(lbPontuacao);
    }
    /**
     * Chamado quando a tela é exebida
     */
    @Override
    public void show() {
        camera = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        batch = new SpriteBatch();
        palco = new Stage(new FillViewport(camera.viewportWidth,camera.viewportHeight,camera));

        initfonte();
        initinformacoes();
    }

    /**
     * Chamado a todo quadro de atuaçização do jogo, ou famoso fps;
     * @param delta tempo entre um quado e outro em segundos;
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.15f,.15f,25f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        lbPontuacao.setPosition(10, camera.viewportHeight - 20);
        palco.act(delta);
        palco.draw();
    }

    /**
     * ele e chamado sempre que a uma alteração no tamanho da tela
     * @param width novo valor de largura da tela
     * @param height novo valor de altura da tela
     */
    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false,width,height);
        camera.update();
    }

    /**
     * é chamado sempre que o jogo for minimizado
     */
    @Override
    public void pause() {
    }

    /**
     * e chamado sempre que o jogo voltar  para o primeiro palano
     */
    @Override
    public void resume() {
    }

    /**
     * é chamado quando a nossa tela for destruida
     */
    @Override
    public void dispose() {
        batch.dispose();
        palco.dispose();
        fonte.dispose();
    }
}
