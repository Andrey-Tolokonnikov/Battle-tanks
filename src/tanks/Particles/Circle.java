package Particles;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Circle {
    private float x;
    private float y;
    private float r;
    private float rStep;
    private Color color;

    public Circle(float x, float y, float r, Color color, float rStep){
        this.x = x;
        this.y = y;
        this.r = r;
        this.color = color;
        this.rStep = rStep;
    }
    public void update(){
        this.r -= rStep;
        this.r = Math.max(this.r, 0);
    }
    public Boolean isExpired(){
        return this.r == 0;
    }
    public void render(GraphicsContext g){
        g.setFill(this.color);
        g.fillOval(this.x, this.y, r*2, r*2);
    }
}
