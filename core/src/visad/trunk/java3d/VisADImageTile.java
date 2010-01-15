package visad.java3d;
import visad.*;
import visad.data.CachedBufferedByteImage;

import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.ImageComponent2D.Updater;
import javax.media.j3d.Behavior;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Switch;
import javax.media.j3d.BoundingSphere;

import javax.vecmath.Point3d;
import java.awt.image.*;
import java.awt.color.*;
import java.util.Enumeration;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnElapsedFrames;
import javax.media.j3d.WakeupOnElapsedTime;
import javax.media.j3d.WakeupOnBehaviorPost;


public class VisADImageTile implements ImageComponent2D.Updater {

   BufferedImage[] images;
   int numImages;
   public ImageComponent2D imageComp;
   public int current_index = 0;
   
   public int height;
   public int width;
   public int yStart;
   public int xStart;


   public VisADImageTile(int numImages, int height, int yStart, int width, int xStart) {
     this.numImages = numImages;
     this.height = height;
     this.yStart = yStart;
     this.width = width;
     this.xStart = xStart;
     images = new BufferedImage[numImages];
   }

   public void setImages(BufferedImage[] images) {
     this.images = images;
     this.numImages = images.length;
   }

   public BufferedImage[] getImages() {
     return this.images;
   }

   public BufferedImage getImage(int index) {
     return images[index];
   }

   public void setImage(int index, BufferedImage image) {
     images[index] = image;
   }

   public void setImageComponent(ImageComponent2D imageComp) {
     this.imageComp = imageComp;
   }

   public void updateData(ImageComponent2D imageC2d, int x, int y, int lenx, int leny) {
     if (images != null) {
       //-imageComp.set(images[current_index]); // This should probably not be done in updateData
     }
   }


   private int lookAheadIndexBaseIndex = 0;

   public void setCurrent(int idx) {
     current_index = idx;

     //Have a local array here in case the images array changes in another thread
     BufferedImage[] theImages = images;

     if (imageComp != null && theImages != null && idx>=0 && idx< theImages.length) {
      //-imageComp.updateData(this, 0, 0, 0, 0); // See note above

       BufferedImage image = theImages[idx];
       if(image == null) {
	   //	   System.err.println ("Animate image is null for index:" + idx);
       } else {
	   imageComp.set(image);
	   //Do the lookahead
	   if(image instanceof CachedBufferedByteImage) {
	       //Find the next image
	       CachedBufferedByteImage nextImage = null;

	       //If we are at the end of the loop then go to the beginning
	       int nextIdx = idx+1;
	       if(nextIdx>=theImages.length)
		   nextIdx = 0;
	       nextImage = (CachedBufferedByteImage)theImages[nextIdx];
	       if(nextImage!=null && !nextImage.inMemory()) {
		   final CachedBufferedByteImage imageToLoad = nextImage;
		   Runnable r = new Runnable() {
			   public  void run() {
			       imageToLoad.getBytesFromCache();
			   }
		       };
		   Thread t = new Thread(r);
		   t.start();
	       }
	   }
       }
     }
     /** use if stepping via a Behavior
     if (animate != null) {
       animate.setCurrent(idx);
       animate.postId(777);
     }
     */
   }
}
