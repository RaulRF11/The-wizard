package net.wizard.superwizard;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.*;

public class Fire extends Image {
    Texture fire;

    float time = 0;
    float xVelocity = 0;
    float yVelocity = 0;
    boolean isFacingRight = true, left, right = true, enemyOverlaps = false;
    TiledMapTileLayer layer;

    final float MAX_VELOCITY = 10f;
    final float DAMPING = 0.87f;
    final float TIMETOREMOVE = 2f;

    public Fire(boolean left, boolean right) {
        this.left = left;
        this.right = right;
        final float width = 180;
        final float height = 120;
        this.setSize(1, height / width);

        fire = new Texture("fire.png");
    }
    
    public Rectangle rectangulo() {
        return new Rectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }

    @Override
    public void act(float delta) {
        time = time + delta;
        
        if(!left && !right)
            right = true;
        
        if (left) {
            xVelocity = -1 * MAX_VELOCITY;
            isFacingRight = false;
            if(time >= TIMETOREMOVE) {
                this.remove();
                time = 0;
            }
        }

        if (right) {
            xVelocity = MAX_VELOCITY;
            isFacingRight = true;
            if(time >= TIMETOREMOVE) {
                this.remove();
                time = 0;
            }
        }

        float x = this.getX();
        float y = this.getY();
        float xChange = xVelocity * delta;
        float yChange = yVelocity * delta;

        if (canMoveTo(x + xChange, y, false) == false) {
            xVelocity = xChange = 0;
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
        if (isFacingRight) {
            batch.draw(fire, this.getX(), this.getY(), this.getWidth(), this.getHeight());
        } else {
            batch.draw(fire, this.getX() + this.getWidth(), this.getY(), -1 * this.getWidth(), this.getHeight());
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
