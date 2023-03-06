package net.wizard.superwizard;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.*;

public class Wizard extends Image {
    TextureRegion stand, jump;
    Animation walk;

    float time = 0;
    float xVelocity = 0;
    float yVelocity = 0;
    boolean canJump = false;
    boolean isFacingRight = true, left, right;
    TiledMapTileLayer layer;

    final float GRAVITY = -2.5f;
    final float MAX_VELOCITY = 10f;
    final float DAMPING = 0.87f;
    
    boolean sound;
    
    Sound jumpSound;

    public Wizard(boolean sound) {
        final float width = 60;
        final float height = 90;
        this.setSize(1, height / width);
        this.sound = sound;

        Texture wizardTexture = new Texture("wizard.png");
        TextureRegion[][] grid = TextureRegion.split(wizardTexture, (int) width, (int) height);

        stand = grid[2][0];
        jump = grid[2][3];
        walk = new Animation(0.15f, grid[2][7], grid[2][6], grid[2][5], grid[2][4], grid[2][3], grid[2][2], grid[2][1], grid[2][0]);
        walk.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        
        jumpSound = Gdx.audio.newSound(Gdx.files.internal("jump.wav"));
    }
    
    public Rectangle rectangulo() {
        return new Rectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }
    
    public void setSound(boolean sound) {
        this.sound = sound;
    }

    @Override
    public void act(float delta) {
        time = time + delta;
        
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            if (canJump) {
                if(sound)
                    jumpSound.play();
                yVelocity = yVelocity + MAX_VELOCITY * 4;
            }
            canJump = false;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            xVelocity = -1 * MAX_VELOCITY;
            isFacingRight = false;
            left = true;
            right = false;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            xVelocity = MAX_VELOCITY;
            isFacingRight = true;
            right = true;
            left = false;
        }

        yVelocity = yVelocity + GRAVITY;

        float x = this.getX();
        float y = this.getY();
        float xChange = xVelocity * delta;
        float yChange = yVelocity * delta;

        if (canMoveTo(x + xChange, y, false) == false) {
            xVelocity = xChange = 0;
        }

        if (canMoveTo(x, y + yChange, yVelocity > 0) == false) {
            canJump = yVelocity < 0;
            yVelocity = yChange = 0;
        }

        this.setPosition(x + xChange, y + yChange);

        xVelocity = xVelocity * DAMPING;
        if (Math.abs(xVelocity) < 0.5f) {
            xVelocity = 0;
        }
    }
    
    public boolean leftTouched() {
        return left;
    }
    
    public boolean rightTouched() {
        return right;
    }

    public void draw(Batch batch, float parentAlpha) {
        TextureRegion frame;

        if (yVelocity != 0) {
            frame = jump;
        } else if (xVelocity != 0) {
            frame = (TextureRegion) walk.getKeyFrame(time);
        } else {
            frame = stand;
        }

        if (isFacingRight) {
            batch.draw(frame, this.getX(), this.getY(), this.getWidth(), this.getHeight());
        } else {
            batch.draw(frame, this.getX() + this.getWidth(), this.getY(), -1 * this.getWidth(), this.getHeight());
        }
    }

    private boolean canMoveTo(float startX, float startY, boolean shouldDestroy) {
        float endX = startX + this.getWidth();
        float endY = startY + this.getHeight();

        int x = (int) startX;
        while (x < endX) {

            int y = (int) startY;
            while (y < endY) {
                if (layer.getCell(x, y) != null) {
                    if (shouldDestroy) {
                        layer.setCell(x, y, null);
                    }
                    return false;
                }
                y = y + 1;
            }
            x = x + 1;
        }

        return true;
    }
}
