package Particles;

import Utils.Time;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class ParticleEmitter {
    private float x;
    private float y;
    private float r;
    private float rStep;
    private Color color;
    private float frequency;
    private Random rand = new Random();

    private float particlesInterval;
    private float lastParticleCreationMoment = Time.getTime();

    private List<Circle> particles = new ArrayList<Circle>();
    public ParticleEmitter(float x, float y, float r, Color color, float rStep, float f){
        this.x = x;
        this. y = y;
        this.r = r;
        this.frequency = f;
        this.particlesInterval = Time.second / this.frequency;
        this.color = color;
        this.rStep = rStep;
        rand.setSeed(Time.getTime());
    }
    public void update(float newX, float newY){
        this.x = newX;
        this.y = newY;
        if(Time.getTime() - this.lastParticleCreationMoment > this.particlesInterval){
            this.addNewParticle();
        }
        this.updateParticles();
        this.removeExpiredParticles();
    }
    public void render(GraphicsContext g){
        for(Circle circle: this.particles){
            circle.render(g);
        }
    }
    public void addNewParticle(){
        float x = this.x + rand.nextInt(6) - 3;
        float y = this.y + rand.nextInt(6) - 3;
        this.particles.add(new Circle(x, y, this.r, this.color, this.rStep));
    }
    public void updateParticles(){
        for(Circle circle: this.particles){
            circle.update();
        }
    }
    public void removeExpiredParticles(){
        Iterator iter = this.particles.iterator();
        while(iter.hasNext()){
            Circle circle = (Circle) iter.next();
            if(circle.isExpired()){
                iter.remove();
            }
        }
    }
    public void clearParticles(){
        this.particles.clear();
    }
}
