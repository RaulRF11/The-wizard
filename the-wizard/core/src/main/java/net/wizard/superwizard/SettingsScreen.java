package net.wizard.superwizard;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import javax.swing.JOptionPane;

public class SettingsScreen extends ScreenAdapter {
    final Juego game;
    Texture fondo, menu, back, difficulties;

    OrthographicCamera camera;
    Vector3 touchPoint;
    
    Rectangle easy, normal, difficult, goback;
    
    int difficulty = 2;
    
    boolean sound;
    
    Sound clickSound;

    public SettingsScreen(final Juego game, boolean sound) {
        this.game = game;
        this.sound = sound;

        camera = new OrthographicCamera(800, 480);
        camera.position.set(800 / 2, 480 / 2, 0);
        touchPoint = new Vector3();
        fondo = new Texture(Gdx.files.internal("background.png"));
        menu = new Texture(Gdx.files.internal("difficulty.png"));
        back = new Texture(Gdx.files.internal("back.png"));
        difficulties = new Texture(Gdx.files.internal("difficulties.png"));
        
        clickSound = Gdx.audio.newSound(Gdx.files.internal("click.wav"));
        
        easy = new Rectangle(339, 283 - 110 / 2, 125, 17);
        normal = new Rectangle(312, 246 - 110 / 2, 180, 18);
        difficult = new Rectangle(319, 210 - 110 / 2, 168, 17);
        goback = new Rectangle(25, 75 - 110 / 2, 100, 20);
    }
    
    public void update() {
        if (Gdx.input.justTouched()) {
            camera.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

            if (easy.contains(touchPoint.x, touchPoint.y)) {
                if(sound)
                    clickSound.play();
                difficulty = 1;
                JOptionPane.showMessageDialog(null, "Se ha establecido la dificultad fácil");
                return;
            }
            if (normal.contains(touchPoint.x, touchPoint.y)) {
                if(sound)
                    clickSound.play();
                difficulty = 2;
                JOptionPane.showMessageDialog(null, "Se ha establecido la dificultad normal");
                return;
            }
            if (difficult.contains(touchPoint.x, touchPoint.y)) {
                if(sound)
                    clickSound.play();
                difficulty = 3;
                JOptionPane.showMessageDialog(null, "Se ha establecido la dificultad difícil");
                return;
            }
            if (goback.contains(touchPoint.x, touchPoint.y)) {
                if(sound)
                    clickSound.play();
                game.setScreen(new MainScreen(game, difficulty, sound));
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
        game.batch.draw(menu, 300, 200 - 110 / 2, 200, 110);
        game.batch.draw(back, 20, 20, 110, 20);
        game.batch.draw(difficulties, 302, 315 - 110 / 2, 200, 35);
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
