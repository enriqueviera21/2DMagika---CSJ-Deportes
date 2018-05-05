package Worlds;

import java.awt.event.KeyEvent;

import Game.Entities.EntityBase;
import Game.Entities.Creatures.Player;
import Game.Entities.Creatures.SkelyEnemy;
import Game.Entities.Statics.*;
import Game.GameStates.State;
import Game.Items.Item;
import Main.Handler;

/**
 * Created by Elemental on 1/2/2017.
 */
public class World2 extends BaseWorld{

    private Handler handler;
    private boolean spawnLoot = true;

    public World2(Handler handler, String path, Player player){
        super(handler,path,player);
        this.handler = handler;

        entityManager.addEntity(new Tree(handler, 100, 250));
        entityManager.addEntity(new Rock(handler, 100, 450));
        entityManager.addEntity(new Tree(handler, 533, 276));
        entityManager.addEntity(new Rock(handler, 684, 1370));
        entityManager.addEntity(new Tree(handler, 765, 888));
        entityManager.addEntity(new Rock(handler, 88, 1345));
        entityManager.addEntity(new Tree(handler, 77, 700));
        entityManager.addEntity(new Rock(handler, 700, 83));
        entityManager.addEntity(new SkelyEnemy(handler, 1000, 500));
        entityManager.addEntity(new Bush(handler, 200, 200));

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
        
        //FOR DEBUGGING! PLEASE REMOVE AFTER!
    	if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_N)) {
    		System.out.println(entityManager.getPlayer().getX() + "\t" + entityManager.getPlayer().getY());
        }
    	
    	if (spawnLoot) { //Spawn the loot once on start
    		handler.getWorld().getItemManager().addItem(Item.newWhiteLootBag.createNew(1110,275,1));
    		spawnLoot = false;
    	}
    }

}