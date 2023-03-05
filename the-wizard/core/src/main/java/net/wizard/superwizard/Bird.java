package net.wizard.superwizard;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.*;

public class Bird extends Image {
    TextureRegion stand, jump;
    Animation walk;

    float time = 0;
    float xVelocity = 0;
    float yVelocity = 0;
    boolean canJump = false, left = true, right = false, fireOverlaps = false;
    boolean isFacingRight = true;
    TiledMapTileLayer layer;

    final float MAX_VELOCITY = 5f;
    final float DAMPING = 0.87f;
    final float TIMETOMOVE = 1f;

    public Bird() {
        final float width = 30;
        final float height = 35;
        this.setSize(1, height / width);

        Texture koalaTexture = new Texture("bird.png");
        TextureRegion[][] grid = TextureRegion.split(koalaTexture, (int) width, (int) height);

        stand = grid[0][0];
        jump = grid[0][0];
        walk = new Animation(0.15f, grid[0][0], grid[0][1], grid[1][0], grid[1][1]);
        walk.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
    }
    
    public Rectangle rectangulo() {
        return new Rectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }

    @Override
    public void act(float delta) {
        time = time + delta;
        
        if (canJump) {
            yVelocity = yVelocity + MAX_VELOCITY * 4;
        }
        canJump = false;

        if (left) {
            xVelocity = -1 * MAX_VELOCITY;
            isFacingRight = false;
            if(time >= TIMETOMOVE) {
                left = false;
                right = true;
                time = 0;
            }
        }

        if (right) {
            xVelocity = MAX_VELOCITY;
            isFacingRight = true;
            if(time >= TIMETOMOVE) {
                left = true;
                right = false;
                time = 0;
            }
        }

        yVelocity = yVelocity;

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
