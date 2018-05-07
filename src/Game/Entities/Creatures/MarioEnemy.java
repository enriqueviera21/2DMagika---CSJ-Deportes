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
	private Animation animDown, animUp, animLeft, animRight,explosion;

    private Boolean attacking=false;

    private int animWalkingSpeed = 150;
    private Inventory Marioinventory;
    private Rectangle MarioCam;

    private int healthcounter =0;

    private Random randint;
    private int moveCount=0;
    private int direction;
    private int area=100;
    
    private Animation animFireATT,animFireATTR,animFireATTU,animFireATTD;
    private Boolean fcactive=true;
    private Boolean FireBall=false;
    private Boolean LaunchedFireBall=false;
    private Boolean LaunchedFireBallL=false;
    private Boolean LaunchedFireBallR=false;
    private Boolean LaunchedFireBallU=false;
    private Boolean LaunchedFireBallD=false;
    private EntityBase exploded;
    private Boolean isExplosion=false;
    private int animFireSpeed = 250;
    private int FireSpeed = 2;
    private int FireMove = 0;
    private int movexp,moveyp,movexn,moveyn,tempmoveyp,tempmovexn,tempmoveyn,tempmovexp,fy,fx;

    public MarioEnemy(Handler handler, float x, float y) {
        super(handler, x, y);
        attackCooldown=800;
        bounds.x=8*2;
        bounds.y=18*2;
        bounds.width=16*2;
        bounds.height=14*2;
        speed=1.5f;
        health=1;

        MarioCam= new Rectangle();



        randint = new Random();
        direction = randint.nextInt(4) + 1;

        animDown = new Animation(animWalkingSpeed, Images.mario_front);
        animLeft = new Animation(animWalkingSpeed,Images.mario_left);
        animRight = new Animation(animWalkingSpeed,Images.mario_right);
        animUp = new Animation(animWalkingSpeed,Images.mario_back);
        explosion = new Animation(100, Images.explosion);
        animFireATT = new Animation(animFireSpeed,Images.FireBallLeft);
        animFireATTR = new Animation(animFireSpeed,Images.FireBallRight);
        animFireATTU = new Animation(animFireSpeed,Images.FireBallUp);
        animFireATTD = new Animation(animFireSpeed,Images.FireBallDown);

        Marioinventory= new Inventory(handler);
    }
    @Override
    public void render(Graphics g) {
        g.drawImage(getCurrentAnimationFrame(animDown,animUp,animLeft,animRight,Images.mario_front,Images.mario_back,Images.mario_left,Images.mario_right), (int) (x - handler.getGameCamera().getxOffset()), (int) (y - handler.getGameCamera().getyOffset()), width, height, null);
        if(isBeinghurt() && healthcounter<=120){
            g.setColor(Color.white);
            g.drawString("SkelyHealth: " + getHealth(),(int) (x-handler.getGameCamera().getxOffset()),(int) (y-handler.getGameCamera().getyOffset()-20));
        }
        if(FireBall) {
        	MarioFireBallAttack(g);
        }
//        if(isExplosion){
//		    g.drawImage(getCurrentAnimationFrame(explosion, explosion, explosion, explosion, Images.explosion, null, null, null), (int)(exploded.getX()), (int)(exploded.getY()), exploded.getWidth(), exploded.getHeight(), null);
//        }
    }
    @Override
    public void tick() {
        animDown.tick();
        animUp.tick();
        animRight.tick();
        animLeft.tick();
        animFireATT.tick();
        animFireATTR.tick();
        animFireATTU.tick();
        animFireATTD.tick();

        moveCount ++;
        if(moveCount>=60){
            moveCount=0;
            direction = randint.nextInt(4) + 1;
        }
        checkIfMove();

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
                if (e.getCollisionBounds(0, 0).intersects(ar) && e.equals(handler.getWorld().getEntityManager().getPlayer())) {

                    checkAttacks();
                    return;
                }
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

	@Override 
	public void checkAttacks(){
        attackTimer += System.currentTimeMillis() - lastAttackTimer;
        lastAttackTimer = System.currentTimeMillis();
        if(attackTimer < attackCooldown) {
        	readyFireAttack();
        	FireBall=false;
        	return;
        }else FireBall=true;
        	

        Rectangle ar = new Rectangle();

        if(lu){
            ar.x = fx;
            ar.y = fy;
            ar.width=197;
            ar.height=512;
        }else if(ld){
            ar.x = fx;
            ar.y = fy;
            ar.width=197;
            ar.height=512;
        }else if(ll){
            ar.x = fx;
            ar.y = fy;
            ar.width=512;
            ar.height=197;
        }else if(lr){
            ar.x = fx;
            ar.y = fy;
            ar.width=512;
            ar.height=197;
        }else{
            return;
        }

        attackTimer = 0;
        

        for(EntityBase e : handler.getWorld().getEntityManager().getEntities()){
            if(e.equals(this))
                continue;
            if(e.getCollisionBounds(0, 0).intersects(ar)){
            	if(e.getHealth()<attack)e.setHealth(attack);
            	e.hurt(attack);
                System.out.println(e + " has " + e.getHealth() + " lives.");
            	exploded = e;
                isExplosion=true;
                return;
            }
        }

    }

	private BufferedImage getCurrentFireAnimationFrame(){

        if(LaunchedFireBallR){

            return animFireATTR.getCurrentFrame();

        }else if(LaunchedFireBallD){

            return animFireATTD.getCurrentFrame();

        }else if(LaunchedFireBallU){

            return animFireATTU.getCurrentFrame();

        }else{   //ll

            return animFireATT.getCurrentFrame();
        }


    }
	public void readyFireAttack(){
        LaunchedFireBall=true;
        movexp =(int) (x - handler.getGameCamera().getxOffset()) + 48;
        moveyp =(int) (y - handler.getGameCamera().getyOffset()) + 64;
        movexn =(int) (x - handler.getGameCamera().getxOffset()) - 48;
        moveyn =(int) (y - handler.getGameCamera().getyOffset()) - 64;
        tempmovexp =(int) (x - handler.getGameCamera().getxOffset()) + 48;
        tempmoveyp =(int) (y - handler.getGameCamera().getyOffset()) + 64;
        tempmovexn =(int) (x - handler.getGameCamera().getxOffset()) - 48;
        tempmoveyn =(int) (y - handler.getGameCamera().getyOffset()) - 64;
        LaunchedFireBallL=false;
        LaunchedFireBallR=false;
        LaunchedFireBallU=false;
        LaunchedFireBallD=false;
        fy=(int) (y - handler.getGameCamera().getyOffset()) + (height / 2);
        fx=(int) (x - handler.getGameCamera().getxOffset()) + 16;
    }
	
	private void MarioFireBallAttack(Graphics g) {


        if (lr&&LaunchedFireBall&&!LaunchedFireBallL&&!LaunchedFireBallR&&!LaunchedFireBallD&&!LaunchedFireBallU) {
            LaunchedFireBall=false;
            LaunchedFireBallL=false;
            LaunchedFireBallR=true;
            LaunchedFireBallU=false;
            LaunchedFireBallD=false;

        } else if (ld&&LaunchedFireBall&&!LaunchedFireBallL&&!LaunchedFireBallR&&!LaunchedFireBallD&&!LaunchedFireBallU) {
            LaunchedFireBall=false;
            LaunchedFireBallL=false;
            LaunchedFireBallR=false;
            LaunchedFireBallU=false;
            LaunchedFireBallD=true;

        } else if (lu&&LaunchedFireBall&&!LaunchedFireBallL&&!LaunchedFireBallR&&!LaunchedFireBallD&&!LaunchedFireBallU) {
            LaunchedFireBall=false;
            LaunchedFireBallL=false;
            LaunchedFireBallR=false;
            LaunchedFireBallU=true;
            LaunchedFireBallD=false;

        } else if(ll&&LaunchedFireBall&&!LaunchedFireBallL&&!LaunchedFireBallR&&!LaunchedFireBallD&&!LaunchedFireBallU) {
            LaunchedFireBall=false;
            LaunchedFireBallL=true;
            LaunchedFireBallR=false;
            LaunchedFireBallU=false;
            LaunchedFireBallD=false;

        }
        if (LaunchedFireBallR) {
            movexp+=FireSpeed;
            g.drawImage(getCurrentFireAnimationFrame(), movexp, fy, 64, 32, null);
            if(movexp >= tempmovexp + 64*2){
                FireBall=false;
                attacking=false;
            }
        } else if (LaunchedFireBallD) {
            moveyp+=FireSpeed;
            g.drawImage(getCurrentFireAnimationFrame(), fx-6, moveyp, 32, 64, null);
            if(moveyp >= tempmoveyp + 64*2){
                FireBall=false;
                attacking=false;
            }
        } else if (LaunchedFireBallU) {
            moveyn-=FireSpeed;
            g.drawImage(getCurrentFireAnimationFrame(), fx, moveyn, 32, 64, null);
            if(moveyn <= tempmoveyn - 64*2){
                FireBall=false;
                attacking=false;
            }
        } else if(LaunchedFireBallL) {   //ll
            movexn-=FireSpeed;
            g.drawImage(getCurrentFireAnimationFrame(), movexn, fy, 64, 32, null);
            if(movexn <= tempmovexn - 64*2){
                FireBall=false;
                attacking=false;
            }
        }
	}


    @Override
    public void die() {
    		System.out.println("ded");
    		handler.getWorld().getItemManager().addItem(Item.newSkullItem.createNew((int)x + bounds.x + (randint.nextInt(96) -32),(int)y + bounds.y+(randint.nextInt(32) -32),(randint.nextInt(3) +1)));
    }

	
	public void fire() {
		
	}
}
