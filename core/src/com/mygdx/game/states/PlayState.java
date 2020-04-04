package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.FlappyBird;
import com.mygdx.game.sprites.Bird;
import com.mygdx.game.sprites.Tube;

//created by Lizochka

public class PlayState extends State {
    private Bird bird;
    private Texture bg;

    public static final int TUBE_SPACING = 125;
    public static final int TUBE_COUNT = 4;

    private Array<Tube> tubes;

    public PlayState(GameStateManager gsm) {
        super(gsm);
        bird = new Bird(50, FlappyBird.HEIGHT/2);
        bg = new Texture("bg.png");
        // установим камеру по середине игрового окна
        camera.setToOrtho(false, FlappyBird.WIDTH / 2, FlappyBird.HEIGHT / 2);


        tubes = new Array<Tube>();

        for(int i=1; i<=TUBE_COUNT; i++) {
            tubes.add(new Tube((TUBE_SPACING + Tube.TUBE_WIDTH) * i));
        }
    }

    @Override
    protected void handleInput() {
        if(Gdx.input.justTouched()) {
            bird.jump();
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        bird.update(dt);
        camera.position.x = bird.getPosition().x;

        // движение труб
        for (Tube tube : tubes) {
            if(camera.position.x - (camera.viewportWidth / 2) >
                    tube.getPosTopTube().x + tube.getTopTube().getWidth()) {
                tube.reposition(tube.getPosTopTube().x + (Tube.TUBE_WIDTH + TUBE_SPACING) * TUBE_COUNT);
            }
            if(tube.collides(bird.getBounds())){
                gsm.set(new PlayState(gsm));
            }
        }
        camera.update();
    }

    @Override
    public void render(SpriteBatch sb) {
        // делаем так, чтобы камера при постоянной отрисовки захватывала именно игровое поле
        // и в таком виде она и будет следить за птицей
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        // создав getter'ы текстуры птицы и позиции в классе Bird мы можем получить их значения
        // даже учитывая, что они являются private. Данный подход являются частью инкапсуляции(т.е. безопасности данных)
        sb.draw(bg, camera.position.x - camera.viewportWidth / 2, 0, FlappyBird.WIDTH, FlappyBird.HEIGHT);
        sb.draw(bird.getBird(), bird.getPosition().x, bird.getPosition().y);

        // отрисовываем 4 трубы
        for(Tube tube : tubes) {
            sb.draw(tube.getTopTube(), tube.getPosTopTube().x, tube.getPosTopTube().y);
            sb.draw(tube.getBottomTube(), tube.getPosBotTube().x, tube.getPosBotTube().y);
        }

        sb.end();

    }

    @Override
    public void dispose() {

    }
}