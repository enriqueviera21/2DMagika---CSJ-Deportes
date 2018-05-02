package Game.Entities.Statics;

import java.awt.Graphics;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import Game.Items.Item;
import Game.Tiles.Tile;
import Main.Handler;
import Resources.Images;

public class Bush extends Tree{
	 private File audioFile;
	    private AudioInputStream audioStream;
	    private AudioFormat format;
	    private DataLine.Info info;
	    private Clip audioClip;

    public Bush(Handler handler, float x, float y) {
	 super(handler, x, y);
     bounds.x=0;
     bounds.y= 0;
     bounds.width = 75;
     bounds.height = 100;
     health=2;

     try {
         audioFile = new File("res/music/Chopping.wav");
         audioStream = AudioSystem.getAudioInputStream(audioFile);
         format = audioStream.getFormat();
         info = new DataLine.Info(Clip.class, format);
         audioClip = (Clip) AudioSystem.getLine(info);
         audioClip.open(audioStream);
         audioClip.setMicrosecondPosition(2000);

     } catch (UnsupportedAudioFileException e) {
         e.printStackTrace();
     } catch (IOException e) {
         e.printStackTrace();
     } catch (LineUnavailableException e) {
         e.printStackTrace();
     }

 }


 @Override
 public void render(Graphics g) {
     renderLife(g);
     g.drawImage(Images.bush,(int)(x-handler.getGameCamera().getxOffset()),(int)(y-handler.getGameCamera().getyOffset()),75,96,null);

 }
 
 @Override
 public void die() {
     handler.getWorld().getItemManager().addItem(Item.newStickItem.createNew((int)x + bounds.x,(int)y + bounds.y,3));


 }

}
