package Worlds;

import java.awt.event.KeyEvent;

import javax.swing.JOptionPane;

import Game.Entities.Creatures.Player;
import Game.Entities.Creatures.SkelyEnemy;
import Game.Entities.Statics.Bush;
import Game.Entities.Statics.Rock;
import Game.Entities.Statics.Tree;
import Game.GameStates.State;
import Game.Items.Item;
import Main.Handler;

/**
 * Created by Elemental on 1/2/2017.
 */
public class World2 extends BaseWorld{

    private Handler handler;
    private boolean spawnLoot = true;
    private boolean hasWon = false;

    public World2(Handler handler, String path, Player player){
        super(handler,path,player);
        this.handler = handler;

        entityManager.addEntity(new Tree(handler, 510, 130));
        entityManager.addEntity(new Tree(handler, 900, 500));
        entityManager.addEntity(new Rock(handler, 584, 754));
        entityManager.addEntity(new SkelyEnemy(handler, 400, 112));
        entityManager.addEntity(new SkelyEnemy(handler, 400, 160));
        entityManager.addEntity(new SkelyEnemy(handler, 400, 211));
        entityManager.addEntity(new Bush(handler, 210, 130));
        entityManager.addEntity(new Tree(handler, 210, 190));
        entityManager.addEntity(new Bush(handler, 210, 240));
        entityManager.addEntity(new Tree(handler, 140, 250));
        entityManager.addEntity(new Tree(handler, 135, 75));
        
        
        
        entityManager.addEntity(new Rock(handler, 100, 450));
        entityManager.addEntity(new Tree(handler, 533, 276));
        entityManager.addEntity(new Rock(handler, 684, 1370));
        entityManager.addEntity(new Tree(handler, 765, 888));
        entityManager.addEntity(new Rock(handler, 88, 1345));
        entityManager.addEntity(new Tree(handler, 77, 700));
        entityManager.addEntity(new Rock(handler, 700, 83));
//        entityManager.addEntity(new SkelyEnemy(handler, 1000, 500));
//        entityManager.addEntity(new Bush(handler, 200, 200));

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
    		handler.getWorld().getItemManager().addItem(Item.fireRuneItem.createNew(145,200,1));
    		handler.getWorld().getItemManager().addItem(Item.fireRuneItem.createNew(145,250,1));
    		handler.getWorld().getItemManager().addItem(Item.newWhiteLootBag.createNew(1110,275,1));
    		spawnLoot = false;
    	}
    	
    	//Win Condition
    	if ((entityManager.getPlayer().getX() > 1100) && (entityManager.getPlayer().getY() > 1100) && (handler.getWorld().getEntityManager().getPlayer().checkIfHasWinLevel2())) {
    		if (!hasWon) {
    			JOptionPane.showMessageDialog(null, "You win!");
    			hasWon = true;
    		}
    	}
    }

}