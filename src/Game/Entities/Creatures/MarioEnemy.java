package Game.Entities.Creatures;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.datatransfer.SystemFlavorMap;
import java.awt.image.BufferedImage;
import java.util.Random;

import Game.Entities.EntityBase;
import Game.Entities.Statics.StaticEntity;
import Game.Inventories.Inventory;
import Game.Items.Item;
import Main.Game;
import Main.Handler;
import Resources.Animation;
import Resources.Images;

public class MarioEnemy extends SkelyEnemy{
	private Animation animDown, animUp, animLeft, animRight;


    private int animWalkingSpeed = 150;
    private Inventory Marioinventory;
    private Rectangle MarioCam;

    private int healthcounter =0;

    private Random randint;
    private int moveCount=0;
    private int direction;
    public static Boolean isDead=false;
        	
    public MarioEnemy(Handler handler, float x, float y) {
        super(handler, x, y);
        attackCooldown=2000;
        bounds.x=8*2;
        bounds.y=18*2;
        bounds.width=16*2;
        bounds.height=14*2;
        speed=1.5f;
        health=20;
        MarioCam= new Rectangle();
        attack = 20;



        randint = new Random();
        direction = randint.nextInt(4) + 1;

        animDown = new Animation(animWalkingSpeed, Images.mario_front);
        animLeft = new Animation(animWalkingSpeed,Images.mario_left);
        animRight = new Animation(animWalkingSpeed,Images.mario_right);
        animUp = new Animation(animWalkingSpeed,Images.mario_back);

        Marioinventory= new Inventory(handler);
    }
    @Override
    public void render(Graphics g) {
        g.drawImage(getCurrentAnimationFrame(animDown,animUp,animLeft,animRight,Images.mario_front,Images.mario_back,Images.mario_left,Images.mario_right), (int) (x - handler.getGameCamera().getxOffset()), (int) (y - handler.getGameCamera().getyOffset()), width, height, null);
        if(isBeinghurt() && healthcounter<=120){
            g.setColor(Color.white);
            g.drawString("Mario Health: " + getHealth(),(int) (x-handler.getGameCamera().getxOffset()),(int) (y-handler.getGameCamera().getyOffset()-20));
            g.setColor(Color.BLACK);
            g.drawRect((int)(x-handler.getGameCamera().getxOffset()-1),(int)(y-handler.getGameCamera().getyOffset()-21),76,11);
            if(this.getHealth()>=16){
                g.setColor(Color.GREEN);
                g.fillRect((int)(x-handler.getGameCamera().getxOffset()),(int)(y-handler.getGameCamera().getyOffset()-20),(int) (getHealth()*4.68),10);

            }else if(this.getHealth()>=6 && getHealth()<16){
                g.setColor(Color.YELLOW);
                g.fillRect((int)(x-handler.getGameCamera().getxOffset()),(int)(y-handler.getGameCamera().getyOffset()-20),(int) (getHealth() * 4.68),10);

            }else if(this.getHealth() < 5){
                g.setColor(Color.RED);
                g.fillRect((int)(x-handler.getGameCamera().getxOffset()),(int)(y-handler.getGameCamera().getyOffset()-20),(int) (getHealth() * 4.68),10);

            }
        }
    }
    @Override
    public void tick() {
        animDown.tick();
        animUp.tick();
        animRight.tick();
        animLeft.tick();

        moveCount ++;
        if(moveCount>=60){
            moveCount=0;
            direction = randint.nextInt(4) + 1;
        }
        checkIfMove();
        checkAttacks();

        move();
        if(y<850)y=851; //bounds
        

        if(isBeinghurt()){
            healthcounter++;
            if(healthcounter>=120){
                setBeinghurt(false);
                System.out.print(isBeinghurt());
            }
        }
        if(healthcounter>=120&& !isBeinghurt()){
            healthcounter=0;
        }


        Marioinventory.tick();


    }


    private void checkIfMove() {
        xMove = 0;
        yMove = 0;

        MarioCam.x = (int) (x - handler.getGameCamera().getxOffset() - (64 * 3));
        MarioCam.y = (int) (y - handler.getGameCamera().getyOffset() - (64 * 3));
        MarioCam.width = 64 * 7;
        MarioCam.height = 64 * 7;

        if (MarioCam.contains(handler.getWorld().getEntityManager().getPlayer().getX() - handler.getGameCamera().getxOffset(), handler.getWorld().getEntityManager().getPlayer().getY() - handler.getGameCamera().getyOffset())
                || MarioCam.contains(handler.getWorld().getEntityManager().getPlayer().getX() - handler.getGameCamera().getxOffset() + handler.getWorld().getEntityManager().getPlayer().getWidth(), handler.getWorld().getEntityManager().getPlayer().getY() - handler.getGameCamera().getyOffset() + handler.getWorld().getEntityManager().getPlayer().getHeight())) {

            Rectangle cb = getCollisionBounds(0, 0);
            Rectangle ar = new Rectangle();
            int arSize = 13;
            ar.width = arSize;
            ar.height = arSize;

            if (lu) {
                ar.x = cb.x + cb.width / 2 - arSize / 2;
                ar.y = cb.y - arSize;
            } else if (ld) {
                ar.x = cb.x + cb.width / 2 - arSize / 2;
                ar.y = cb.y + cb.height;
            } else if (ll) {
                ar.x = cb.x - arSize;
                ar.y = cb.y + cb.height / 2 - arSize / 2;
            } else if (lr) {
                ar.x = cb.x + cb.width;
                ar.y = cb.y + cb.height / 2 - arSize / 2;
            }

            for (EntityBase e : handler.getWorld().getEntityManager().getEntities()) {
                if (e.equals(this))
                    continue;
            }
            if (x >= handler.getWorld().getEntityManager().getPlayer().getX() - 8 && x <= handler.getWorld().getEntityManager().getPlayer().getX() + 8) {//nada

                xMove = 0;
            } else if (x < handler.getWorld().getEntityManager().getPlayer().getX()) {//move right

                xMove = speed;

            } else if (x > handler.getWorld().getEntityManager().getPlayer().getX()) {//move left

                xMove = -speed;
            }

            if (y >= handler.getWorld().getEntityManager().getPlayer().getY() - 8 && y <= handler.getWorld().getEntityManager().getPlayer().getY() + 8) {//nada
                yMove = 0;
            } else if (y < handler.getWorld().getEntityManager().getPlayer().getY()) {//move down
                yMove = speed;

            } else if (y > handler.getWorld().getEntityManager().getPlayer().getY()) {//move up
                yMove = -speed;
            }
            


        } else {


            switch (direction) {
                case 1://up
                    yMove = -speed;
                    break;
                case 2://down
                    yMove = speed;
                    break;
                case 3://left
                    xMove = -speed;
                    break;
                case 4://right
                    xMove = speed;
                    break;

            }
        }
    }
    
    public void checkAttacks(){
        attackTimer += System.currentTimeMillis() - lastAttackTimer;
        lastAttackTimer = System.currentTimeMillis();
        if(attackTimer < attackCooldown)
            return;

        Rectangle cb = getCollisionBounds(0, 0);
        Rectangle ar = new Rectangle();
        int arSize = 20;
        ar.width = arSize;
        ar.height = arSize;

        if(lu){
            ar.x = cb.x + cb.width / 2 - arSize / 2;
            ar.y = cb.y - arSize;
        }else if(ld){
            ar.x = cb.x + cb.width / 2 - arSize / 2;
            ar.y = cb.y + cb.height;
        }else if(ll){
            ar.x = cb.x - arSize;
            ar.y = cb.y + cb.height / 2 - arSize / 2;
        }else if(lr){
            ar.x = cb.x + cb.width;
            ar.y = cb.y + cb.height / 2 - arSize / 2;
        }else{
            return;
        }

        attackTimer = 0;

        for(EntityBase e : handler.getWorld().getEntityManager().getEntities()){
            if(e.equals(this))
                continue;
            if(e.getCollisionBounds(0, 0).intersects(ar)){
                e.hurt(attack);
                System.out.println(e + " has " + e.getHealth() + " lives.");
                if(e.equals(handler.getWorld().getEntityManager().getPlayer()))
                	handler.getWorld().getEntityManager().getPlayer().speed--;
                return;
            }
        }

    }


    @Override
    public void die() {
    		handler.getWorld().getItemManager().addItem(Item.fireRuneItem.createNew((int)x + bounds.x + (randint.nextInt(96) -32),(int)y + bounds.y+(randint.nextInt(32) -32),(randint.nextInt(3) +3)));
        	handler.getWorld().getEntityManager().getPlayer().speed=3;
        	isDead=true;
    }


}
