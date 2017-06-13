package com.lingzg.shoot;

import java.awt.image.BufferedImage;

public abstract class FlyingObjct {
	protected int x;
	protected int y;
	protected int width;
	protected int height;
	protected BufferedImage image;
	protected BufferedImage[] ember;
			
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public BufferedImage getImage() {
		return image;
	}
	public void setImage(BufferedImage image) {
		this.image = image;
	}			
	public String toString() {
		return "("+x+","+y+","+width+","+height+")";
	}
	public abstract void step();
	public abstract boolean outOfBounds();
	public boolean hit(FlyingObjct other){		
		return other.x>=this.x-other.width&&other.x<=this.x+this.width&&
		             other.y>=this.y-other.height&&other.y<=this.y+this.height;
	}
	public boolean shootBy(Bullet bullet){
		if(bullet.isBomb()){
			return false;
		}
		int x = bullet.x;  
		int y = bullet.y;  
		boolean shoot = this.x<x && x<this.x+width && this.y<y && y<this.y+height;
		if(shoot){
			bullet.setBomb(true);
		}
		return shoot;
	}
	public static FlyingObjct randomOne(){
		int type=(int)(Math.random()*20);
		switch(type){
		case 0:
			return new Bee();
		case 1:
		case 2:
		case 3:
		case 4:
			return new Bigplane();
		default:
			return new Airplane();
		}
	}
	public void moveRight(){
		x+=100;
	}
	private static class Airplane extends FlyingObjct implements Enemy{		
		private int speed=2;		
		public Airplane(){		
			image=Game.airplane;
			ember=Game.airplaneEmber;
			width=image.getWidth();
			height=image.getHeight();
			x=(int)(Math.random()*(Game.WIDTH-width));
			y=-height;			
		}					
		public int getScore(){
			return 5;
		}		
		public void step() {
			y+=speed;			
		}
		public boolean outOfBounds() {			
			return y>Game.HEIGHT;
		}
	}
	private static class Bigplane extends FlyingObjct implements Enemy,Award{	   
		private int speed=2;		
		public Bigplane(){	
			image=Game.bigplane;
			ember=Game.bigplaneEmber;
			width=image.getWidth();
			height=image.getHeight();
			x=(int)(Math.random()*(Game.WIDTH-width));
			y=-height;			
		}				
		public int getScore(){
			return 10;
		}
		public int getAwardType(){
			return Award.DOUBLE_FIRE;
		}
		public void step() {		
			y+=speed;
		}		
		public boolean outOfBounds() {			
			return y>Game.HEIGHT;
		}
	}
	private static class Bee extends FlyingObjct implements Award{	
		private int xSpeed=1;
		private int ySpeed=2;		
		public Bee(){			
			image=Game.bee;
			ember=Game.beeEmber;
			width=image.getWidth();
			height=image.getHeight();
			x=(int)(Math.random()*(Game.WIDTH-width));
			y=-height;			
		}				
		public int getAwardType(){
			switch((int)(Math.random()*100)){
			case 0:
				return Award.LIFE;
			default:
				return Award.DOUBLE_FIRE;
			}
		}
		public void step() {
			x+=xSpeed;
			y+=ySpeed;
			if(x>=Game.WIDTH-width){
				xSpeed=-1;
			}
			if(x<=0){
				xSpeed=1;
			}
		}		
		public boolean outOfBounds() {			
			return y>Game.HEIGHT;
		}
	}		
}
