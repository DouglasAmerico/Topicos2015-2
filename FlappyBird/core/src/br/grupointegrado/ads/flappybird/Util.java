package br.grupointegrado.ads.flappybird;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Douglas on 05/10/2015.
 */
public class Util {

    public static final float ESCALA = 2;
    public static final float PIXEL_METRO = 32;
    public static final float ALTURA_CHAO = 80 / PIXEL_METRO; // Altura do chao em metros

    /**
     * Cria um corpo dentro do mundo
     * @param mundo
     * @param tipo
     * @param x
     * @param y
     * @return
     */
    public static Body criarCorpo(World mundo, BodyDef.BodyType tipo, float x, float y){
        BodyDef definicao = new BodyDef();
        definicao.type = tipo;
        definicao.position.set(x,y);
        definicao.fixedRotation = true;
        Body corpo = mundo.createBody(definicao);
        return corpo;
    }

    /**
     * Cria uma forma para o corpo
     * @param corpo
     * @param shape Forma geometrica do corpo
     * @param nome Nome utilizado para identificar a colisao
     * @return
     */
    public static Fixture criarForma(Body corpo, Shape shape, String nome){
        FixtureDef def = new FixtureDef();
        def.density = 1; // densidade do corpo
        def.friction = 0.06f; //friccao/atrito entre um corpo e outro
        def.restitution = 0.3f; // elasticidade do corpo
        def.shape = shape;
        Fixture forma = corpo.createFixture(def);
        forma.setUserData(nome); //Identificacao da forma
        return forma;
    }

}
