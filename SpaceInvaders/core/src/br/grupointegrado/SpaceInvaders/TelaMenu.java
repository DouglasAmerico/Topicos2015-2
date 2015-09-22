package br.grupointegrado.SpaceInvaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.viewport.FillViewport;

/**
 * Created by Douglas on 14/09/2015.
 */
public class TelaMenu extends TelaBase{
    private OrthographicCamera camera;
    private Stage palco;
    private ImageTextButton btnIniciar;
    private Label lbTitulo;
    private Label lbPontuacao;

    private BitmapFont fontTitulo;
    private BitmapFont fontBotoes;

    private Texture texturaBotao;
    private Texture getTexturaBotaoPressionado;

    public TelaMenu(MainGame game) {
        super(game);
    }

    @Override
    public void show() {
        camera = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        palco = new Stage(new FillViewport(camera.viewportWidth,camera.viewportHeight,camera));
        Gdx.input.setInputProcessor(palco); //define o palco como processador de entradas, para funcionar o onclique do botao

        initFontes();
        initLables();
        initBotoes();

    }

    private void initBotoes() {
        texturaBotao = new Texture("buttons/button.png");
        getTexturaBotaoPressionado = new Texture("buttons/button-down.png");

        ImageTextButton.ImageTextButtonStyle estilo = new ImageTextButton.ImageTextButtonStyle();
        estilo.font = fontBotoes;
        estilo.up = new SpriteDrawable(new Sprite(texturaBotao));
        estilo.down = new SpriteDrawable(new Sprite(getTexturaBotaoPressionado));

        btnIniciar = new ImageTextButton("Iniciar o Jogo",estilo);
        palco.addActor(btnIniciar);

        btnIniciar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // chamar a tela do jogo
                game.setScreen(new TelaJogo(game));
            }
        });
    }

    private void initLables() {
        Label.LabelStyle estilo = new Label.LabelStyle();
        estilo.font = fontTitulo;

        lbTitulo = new Label("Space Invaders",estilo);
        palco.addActor(lbTitulo);
    }

    private void initFontes() {
        FreeTypeFontGenerator gerador = new FreeTypeFontGenerator(Gdx.files.internal("fonts/roboto.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
        params.size = 48;
        params.color = new Color(.25f,.25f,.85f,1);
        params.shadowOffsetX = 2;
        params.shadowOffsetY = 2;
        params.shadowColor = Color.BLACK;

        fontTitulo = gerador.generateFont(params);
        gerador.dispose();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        atualizaLable();
        atualizarBotoes();
        
        palco.act(delta);
        palco.draw();
    }

    private void atualizarBotoes() {
        float x = (camera.viewportWidth/2) - (btnIniciar.getPrefWidth()/2);
        float y = (camera.viewportHeight/2) - (btnIniciar.getPrefWidth()/2);

        btnIniciar.setPosition(x,y);
    }

    private void atualizaLable() {
        float x = camera.viewportWidth /2 - lbTitulo.getPrefWidth();
        float y = camera.viewportHeight -100;


        Label.LabelStyle estilo = new Label.LabelStyle();
        estilo.font = fontTitulo;

        lbTitulo.setPosition(x,y);
        palco.addActor(lbTitulo);

        Preferences preferencia = Gdx.app.getPreferences("SpaceInvaders");
        int pontuacaoMaxima = preferencia.getInteger("pontuacaoMaxima", 0);

        estilo = new Label.LabelStyle();
        estilo.font = fontBotoes;

        lbPontuacao = new Label("Pontuação Maxima: "+pontuacaoMaxima+" pontos",estilo);
        palco.addActor(lbPontuacao);
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false,width,height);
        camera.update();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        palco.dispose();
        fontTitulo.dispose();
        texturaBotao.dispose();
        getTexturaBotaoPressionado.dispose();
        fontBotoes.dispose();
    }
}
