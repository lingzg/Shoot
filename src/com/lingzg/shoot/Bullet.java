package com.lingzg.shoot;

public class Bullet extends FlyingObjct{
	private int speed=3;
	private boolean bomb;
	public Bullet(int x,int y){
		this.x=x;
		this.y=y;
		image=Game.bullet;
		width=image.getWidth();
		height=image.getHeight();
	}	
	public void setBomb(boolean bomb) {
		this.bomb = bomb;
	}
	public void step() {		
		y-=speed;
	}	
	public boolean outOfBounds() {		
		return y<-height;
	}
	public boolean isBomb() {		
		return bomb;
	}
}
