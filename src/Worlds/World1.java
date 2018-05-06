package Worlds;

import java.awt.event.KeyEvent;

import Game.Entities.Creatures.Player;
import Game.Entities.Creatures.SkelyEnemy;
import Game.Entities.Statics.*;
import Game.GameStates.State;
import Main.Handler;

/**
 * Created by Elemental on 1/2/2017.
 */
public class World1 extends BaseWorld{

    private Handler handler;
    private BaseWorld caveWorld;
    private BaseWorld map2World;

    public World1(Handler handler, String path, Player player){
        super(handler,path,player);
        this.handler = handler;
        caveWorld = new CaveWorld(handler,"res/Maps/caveMap.map",player);
        map2World = new World2(handler,"res/Maps/map2.map",player);

        entityManager.addEntity(new Tree(handler, 100, 250));
        entityManager.addEntity(new Rock(handler, 100, 450));
        entityManager.addEntity(new Tree(handler, 533, 276));
        entityManager.addEntity(new Rock(handler, 684, 1370));
        entityManager.addEntity(new Tree(handler, 765, 888));
        entityManager.addEntity(new Rock(handler, 88, 1345));
        entityManager.addEntity(new Tree(handler, 77, 700));
        entityManager.addEntity(new Rock(handler, 700, 83));
        entityManager.addEntity(new Door(handler, 100, 0, map2World));
//        entityManager.addEntity(new SkelyEnemy(handler, 1250, 500));
        entityManager.addEntity(new SkelyEnemy(handler, 1000, 500));
        entityManager.addEntity(new Chest(handler, 450, 200));
        entityManager.addEntity(new Bush(handler, 400, 1200));
        entityManager.addEntity(new Bush(handler, 341, 442));
        entityManager.addEntity(new Bush(handler, 1000, 642));

        entityManager.getPlayer().setX(spawnX);
        entityManager.getPlayer().setY(spawnY);
    }
    
    @Override
    public void tick(){
        entityManager.tick();
        itemManager.tick();
        countP++;
        if(countP>=30){
            countP=30;
        }

        if(handler.getKeyManager().pbutt && countP>=30){
            handler.getMouseManager().setUimanager(null);
            countP=0;
            State.setState(handler.getGame().pauseState);
        }
    	if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_N)) {
    		handler.setWorld(map2World);
    		//Coordinates to spawn in world2
    		entityManager.getPlayer().setX(70);
            entityManager.getPlayer().setY(180);
        }
    	
    	//FOR DEBUGGING! PLEASE REMOVE AFTER!
    	if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_G)) {
    		System.out.println(entityManager.getPlayer().getX() + "\t" + entityManager.getPlayer().getY());
        }
    }


}