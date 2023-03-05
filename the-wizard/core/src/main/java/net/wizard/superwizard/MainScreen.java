package net.wizard.superwizard;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class MainScreen extends ScreenAdapter {
    final Juego game;
    Texture fondo, menu;

    OrthographicCamera camera;
    Vector3 touchPoint;
    
    Rectangle play, scores, settings;
    
    int difficulty;

    public MainScreen(final Juego game, int difficulty) {
        this.game = game;
        this.difficulty = difficulty;

        camera = new OrthographicCamera(800, 480);
        camera.position.set(800 / 2, 480 / 2, 0);
        touchPoint = new Vector3();
        fondo = new Texture(Gdx.files.internal("background.png"));
        menu = new Texture(Gdx.files.internal("menu.png"));
        
        play = new Rectangle(345, 278 - 110 / 2, 110, 20);
        scores = new Rectangle(263, 246 - 110 / 2, 270, 17);
        settings = new Rectangle(320, 212 - 110 / 2, 155, 17);
    }
    
    public void update() {
        if (Gdx.input.justTouched()) {
            camera.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

            if (play.contains(touchPoint.x, touchPoint.y)) {
                    //Assets.playSound(Assets.clickSound);
                    game.setScreen(new GameScreen(game, 3, 3, 0, false, difficulty));
                    return;
            }
            if (scores.contains(touchPoint.x, touchPoint.y)) {
                    //Assets.playSound(Assets.clickSound);
                    game.setScreen(new ScoresScreen(game, difficulty));
                    return;
            }
            if (settings.contains(touchPoint.x, touchPoint.y)) {
                    //Assets.playSound(Assets.clickSound);
                    game.setScreen(new SettingsScreen(game));
                    return;
            }
        }
    }
    
    public void draw() {
        GL20 gl = Gdx.gl;
        gl.glClearColor(1, 0, 0, 1);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.disableBlending();
        game.batch.begin();
        game.batch.draw(fondo, 0, 0, 800, 480);
        game.batch.end();
        
        game.batch.enableBlending();
        game.batch.begin();
        //game.batch.draw(Assets.logo, 160 - 274 / 2, 480 - 10 - 142, 274, 142);
        game.batch.draw(menu, 250, 200 - 110 / 2, 300, 110);
        //game.batch.draw(Settings.soundEnabled ? Assets.soundOn : Assets.soundOff, 0, 0, 64, 64);
        game.batch.end();
    }

    @Override
    public void render(float delta) {
        update();
        draw();
    }

    @Override
    public void show() {
        
    }

    @Override
    public void resize(int width, int height) {
        
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
        
    }
}
