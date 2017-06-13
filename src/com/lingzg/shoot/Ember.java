package com.lingzg.shoot;

import java.awt.image.BufferedImage;

public class Ember {
	private int x;
	private int y;
	private BufferedImage image;
	private BufferedImage[] images={};
	private int i;
	private int index;
	public Ember(){		
	}	
	public Ember(FlyingObjct obj) {
		images = obj.ember;
		image = obj.image;
		x = obj.x;
		y = obj.y;
		index = 0;
		i = 0;
	}
	public int getX() {
		return x;
	}	
	public int getY() {
		return y;		
	}
	public BufferedImage getImage() {
		return image;
	}	
	public boolean burnDown(){
		i++;
		if(i%10==0){
			if(index == images.length){
				return true;
			}
			image = images[index++];
		}
		return false;
	}
}
