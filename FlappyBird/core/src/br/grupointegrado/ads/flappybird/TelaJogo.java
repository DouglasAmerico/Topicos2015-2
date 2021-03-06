package br.grupointegrado.ads.flappybird;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FillViewport;

import br.grupointegrado.ads.flappybird.MainGame;
import br.grupointegrado.ads.flappybird.Obstaculo;
import br.grupointegrado.ads.flappybird.Passaro;
import br.grupointegrado.ads.flappybird.TelaBase;
import br.grupointegrado.ads.flappybird.Util;

public class TelaJogo extends TelaBase{

    private static final String PREF_FLAPPY_BIRD = "PREF_FLAPPY_BIRD";
    private static final java.lang.String PREF_MAIOR_PONTUACAO = "PREF_MAIOR_PONTUACAO";
    private OrthographicCamera camera; // Camera do jogo
    private World mundo; // Representa o mundo do Box2D
    private Passaro passaro;
    private Body chao;
    private Array<Obstaculo> obstaculos = new Array<Obstaculo>();
    private Box2DDebugRenderer debug; // Desenha o mundo na tela para ajudar no desenvolvimento

    private int pontuacao = 0;
    private BitmapFont fontepontuacao;
    private Stage palcoInformacoes;
    private Label lbPontuacao;
    private ImageButton btnPlay;
    private ImageButton btnGameOver;
    private OrthographicCamera cameraInfo;

    private Texture[] texturasPassaro;
    private Texture texturaObstaculoCima;
    private Texture texturaObstaculoBaixo;
    private Texture texturaChao;
    private Texture texturaFundo;
    private Texture texturaPlay;
    private Texture texturaGameOver;

    private SpriteBatch pincel;

    private Sprite spriteChao1;
    private Sprite spriteChao2;

    private Label lbMaiorPontuacao;
    private BitmapFont fonte;

    private boolean jogoIniciado = false;

    public TelaJogo(MainGame game) {
        super(game);
    }

    public Music musicaFundo;
    private Sound somAsas;
    private Sound somGameOver;

    @Override
    public void show() {
        initAudio();
        camera = new OrthographicCamera(Gdx.graphics.getWidth() / Util.ESCALA, Gdx.graphics.getHeight() / Util.ESCALA);
        cameraInfo = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        debug = new Box2DDebugRenderer();
        mundo = new World(new Vector2(0, -9.8f), false);
        mundo.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                detectaColisao(contact.getFixtureA(), contact.getFixtureB());
            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }

        });

        pincel = new SpriteBatch();

        initTexturas();
        initChao();
        initPassaro();
        initFonte();
        initInformacoes();
    }

    private void initAudio() {
        musicaFundo = Gdx.audio.newMusic(Gdx.files.internal("songs/music.mp3"));
        musicaFundo.setLooping(true);

        somAsas = Gdx.audio.newSound(Gdx.files.internal("songs/wing.ogg"));
        somAsas = Gdx.audio.newSound(Gdx.files.internal("songs/game-over.mp3"));
    }

    private void initTexturas() {
        texturasPassaro = new Texture[3];
        texturasPassaro[0] = new Texture("sprites/bird-1.png");
        texturasPassaro[1] = new Texture("sprites/bird-2.png");
        texturasPassaro[2] = new Texture("sprites/bird-3.png");

        texturaObstaculoCima = new Texture("sprites/toptube.png");
        texturaObstaculoBaixo = new Texture("sprites/bottomtube.png");

        texturaFundo = new Texture("sprites/bg.png");
        texturaChao = new Texture("sprites/ground.png");

        texturaPlay = new Texture("sprites/playbtn.png");
        texturaGameOver = new Texture("sprites/gameover.png");
    }

    private boolean gameOver = false;

    /**
     * Verifica se o passaro esta envolvido na colisao
     * @param fixtureA
     * @param fixtureB
     */

    private void detectaColisao(Fixture fixtureA, Fixture fixtureB) {
        if ("PASSARO".equals(fixtureA.getUserData()) || ("PASSARO".equals(fixtureB.getUserData()))){
            // game over

        if (!gameOver) {
            somGameOver.play(1); // 1 pois fica com 100% do volume
        }

        gameOver = true;
            salvarPontuacao();
        }
    }

    private void salvarPontuacao() {
        Preferences pref = Gdx.app.getPreferences(PREF_FLAPPY_BIRD);
        int maiorPontuação = pref.getInteger(PREF_MAIOR_PONTUACAO, 0);
        if (pontuacao > maiorPontuação) {
            pref.putInteger(PREF_MAIOR_PONTUACAO, pontuacao);
            pref.flush();
        }
    }

    private void initFonte() {
        FreeTypeFontGenerator.FreeTypeFontParameter fonteparam = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fonteparam.size = 24;
        fonteparam.color = Color.WHITE;
        fonteparam.shadowColor = Color.BLACK;
        fonteparam.shadowOffsetX = 4;
        fonteparam.shadowOffsetY = 4;

        FreeTypeFontGenerator gerador = new FreeTypeFontGenerator(Gdx.files.internal("fonts/roboto.ttf"));
        fontepontuacao = gerador.generateFont(fonteparam);

        fonteparam.size = 24;
        fonteparam.shadowOffsetX = 2;
        fonteparam.shadowOffsetX = 2;
        fonte = gerador.generateFont(fonteparam);

        gerador.dispose();
    }

    private void initInformacoes() {
        palcoInformacoes = new Stage(new FillViewport(cameraInfo.viewportWidth, cameraInfo.viewportHeight, cameraInfo));
        Gdx.input.setInputProcessor(palcoInformacoes);

        Label.LabelStyle estilo = new Label.LabelStyle();
        estilo.font = fontepontuacao;
        lbPontuacao = new Label("0", estilo);
        palcoInformacoes.addActor(lbPontuacao);
        lbPontuacao.setAlignment(Align.center);

        //Inicia Botao
        ImageButton.ImageButtonStyle estiloBotao = new ImageButton.ImageButtonStyle();
        estiloBotao.up = new SpriteDrawable(new Sprite(texturaPlay));

        btnPlay = new ImageButton(estiloBotao);
        btnPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                jogoIniciado = true;
            }
        });
        palcoInformacoes.addActor(btnPlay);

        estiloBotao = new ImageButton.ImageButtonStyle();
        estiloBotao.up = new SpriteDrawable(new Sprite(texturaGameOver));

        btnGameOver = new ImageButton(estiloBotao);
        btnGameOver.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                reiniciarJogo();
            }
        });
        palcoInformacoes.addActor(btnGameOver);

        Preferences pref = Gdx.app.getPreferences(PREF_FLAPPY_BIRD);
        int maiorPontuacao = pref.getInteger(PREF_MAIOR_PONTUACAO,0);

        estilo = new Label.LabelStyle();
        estilo.font = fonte;

        lbMaiorPontuacao = new Label("Maior "+maiorPontuacao,estilo);
        palcoInformacoes.addActor(lbMaiorPontuacao);

    }

    private void reiniciarJogo() {
        game.setScreen(new TelaJogo(game));
    }

    private void initChao() {
        chao = Util.criarCorpo(mundo, BodyDef.BodyType.StaticBody, 0, 0);

        float inicioCamera = 0;
        float altura = (Util.ALTURA_CHAO * Util.PIXEL_METRO) / Util.ESCALA;

        spriteChao1 = new Sprite(texturaChao);
        spriteChao1.setBounds(inicioCamera, 0, camera.viewportWidth, altura);
        spriteChao1 = new Sprite(texturaChao);
        spriteChao2.setBounds(inicioCamera + camera.viewportWidth, 0, camera.viewportWidth, altura);
    }

    private void initPassaro() {
        passaro = new Passaro(mundo, camera, null);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.25f, .25f, .25f, 1); // Limpa a tela e pinta a cor de fundo
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Mantem o buffer de cores

        capituraTecla();
        atualizar(delta);
        renderizar(delta);
        debug.render(mundo, camera.combined.cpy().scl(Util.PIXEL_METRO));
    }

    private boolean pulando = false;

    private void capituraTecla() {
        pulando = false;
        if (Gdx.input.justTouched()){
            pulando = true;
        }
    }

    /**
     * Renderiza/desenha as imagens
     * @param delta
     */
    private void renderizar(float delta) {
        pincel.begin();

        pincel.setProjectionMatrix(cameraInfo.combined);
        pincel.draw(texturaFundo, 0, 0, cameraInfo.viewportWidth, cameraInfo.viewportHeight);

        pincel.setProjectionMatrix(camera.combined);
        //desenhar o passaro
        passaro.renderizar(pincel);
        //desenhar os obstaculos
        for (Obstaculo obs : obstaculos) {
            obs.renderisar(pincel);
        }
        //desenhar o chão
        spriteChao1.draw(pincel);
        spriteChao2.draw(pincel);

        pincel.end();

        palcoInformacoes.draw();
    }

    /**
     * Atualizacao e calculo dos corpos
     * @param delta
     */
    private void atualizar(float delta) {

        if (gameOver && musicaFundo.isPlaying()) {
            musicaFundo.stop();
        } else if (!gameOver && musicaFundo.isPlaying()) {
            musicaFundo.setVolume(0.1f); // 10% do volume da musica
            musicaFundo.play();
        }

        palcoInformacoes.act(delta);

        passaro.getCorpo().setFixedRotation(!gameOver);
        passaro.atualizar(delta, !gameOver);
        if (jogoIniciado){
            mundo.step(1f / 60f, 6, 2);
            atualizarObstaculo();
        }
        atualizaInformacoes();

        if (!gameOver) {
            atualizarCamera();
            atualizarChao();
        }

        if (pulando && !gameOver){
            somAsas.play(1);
            passaro.pular();
        }
    }

    private void atualizaInformacoes() {
        lbMaiorPontuacao.setPosition(10, cameraInfo.viewportHeight - lbMaiorPontuacao.getPrefHeight());
        lbMaiorPontuacao.setVisible(!jogoIniciado);

        lbPontuacao.setText(pontuacao + "");
        lbPontuacao.setPosition(
                cameraInfo.viewportWidth / 2 - lbPontuacao.getPrefWidth() / 2,
                cameraInfo.viewportHeight - lbPontuacao.getPrefHeight());
        lbPontuacao.setVisible(!jogoIniciado);

        lbPontuacao.setText(pontuacao + "");
        lbPontuacao.setPosition(cameraInfo.viewportWidth / 2 - lbPontuacao.getPrefWidth() / 2, camera.viewportHeight / 2 - lbPontuacao.getPrefHeight() / 2);

        btnPlay.setPosition(cameraInfo.viewportWidth / 2 - btnPlay.getPrefWidth() / 2, cameraInfo.viewportHeight / 2 - btnPlay.getPrefHeight() / 2);
        btnPlay.setVisible(!jogoIniciado);

        btnPlay.setPosition(cameraInfo.viewportWidth / 2 - btnGameOver.getPrefWidth() / 2, cameraInfo.viewportHeight / 2 - btnGameOver.getPrefHeight() / 2);
        btnGameOver.setVisible(gameOver);
    }

    private void atualizarObstaculo() {
        // Enquanto a lista tiver menos do que 4, crie obstaculo
        while(obstaculos.size < 4){
            Obstaculo ultimo = null;
            if (obstaculos.size > 0)
                ultimo = obstaculos.peek();
            Obstaculo o = new Obstaculo(mundo, camera, ultimo, texturaObstaculoCima,texturaObstaculoBaixo);
            obstaculos.add(o);
        }

        // Verifica se os obstaculos sairam da tela para remoce-lo
        for (Obstaculo o : obstaculos){
            float inicioCamera = passaro.getCorpo().getPosition().x - (camera.viewportWidth / 2 / Util.PIXEL_METRO) - o.getLargura();
            if (inicioCamera > o.getPosX()){
                o.remove();
                obstaculos.removeValue(o, true);
            } else if (!o.isPassou() && o.getPosX() < passaro.getCorpo().getPosition().x){
                o.setPassou(true);
                // calcular pontuação
                pontuacao++;
                // Reproduzir som
            }
        }
    }

    private void atualizarCamera() {
        camera.position.x = (passaro.getCorpo().getPosition().x + 34 / Util.PIXEL_METRO) * Util.PIXEL_METRO;
        camera.update();
    }

    /**
     * Atualiza a posicao do chao para acompanhar o passaro
     */
    private void atualizarChao() {
        Vector2 posicao = passaro.getCorpo().getPosition();
        chao.setTransform(posicao.x, 0, 0);

        float inicioCamera = camera.position.x - (camera.viewportWidth / 2) - camera.viewportWidth;

        if (spriteChao1.getX() < inicioCamera) {
            spriteChao1.setBounds(spriteChao2.getX() + camera.viewportWidth, 0 , spriteChao1.getWidth(), spriteChao1.getHeight());
        }

        if (spriteChao2.getX() < inicioCamera) {
            spriteChao2.setBounds(spriteChao1.getX() + camera.viewportWidth, 0 , spriteChao1.getWidth(), spriteChao2.getHeight());
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width / Util.ESCALA, height / Util.ESCALA);
        camera.update();
        redimensionaChao();
        cameraInfo.setToOrtho(false, width, height);
        cameraInfo.update();
    }

    /**
     * Configura o tamanho do chao de acordo com a tela
     */
    private void redimensionaChao() {
        chao.getFixtureList().clear();
        float largura = camera.viewportWidth / Util.PIXEL_METRO;
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(largura / 2, Util.ALTURA_CHAO / 2);
        Fixture forma = Util.criarForma(chao, shape, "CHAO");
        shape.dispose();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        debug.dispose();
        mundo.dispose();
        palcoInformacoes.dispose();
        fontepontuacao.dispose();

        texturasPassaro[0].dispose();
        texturasPassaro[1].dispose();
        texturasPassaro[2].dispose();

        texturaObstaculoCima.dispose();
        texturaObstaculoBaixo.dispose();

        texturaFundo.dispose();
        texturaChao.dispose();

        texturaPlay.dispose();
        texturaGameOver.dispose();
        pincel.dispose();

        fonte.dispose();
        musicaFundo.dispose();
        somAsas.dispose();
        somGameOver.dispose();
    }
}