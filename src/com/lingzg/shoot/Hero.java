package com.lingzg.shoot;

import java.awt.image.BufferedImage;


public class Hero extends FlyingObjct{
	protected BufferedImage[] images;
	protected int index=0;	
	private int doubleFire;
	private int life=3;
	public Hero(){
		doubleFire=0;
		this.image=Game.hero0;
		this.ember=Game.heroEmber;
		images=new BufferedImage[]{Game.hero0,Game.hero1};				
		width=image.getWidth();		
		height=image.getHeight();		
		x=150;
		y=400;
		step();
	}	
	public int getLife() {
		return life;
	}	
	public void step() {
		if(images.length>0){
			image=images[index++/10%images.length];			
		}	
	}
	public Bullet[] shoot(){
		int xStep=width/4;
		int yStep=20;
		if(doubleFire>0){
			Bullet[] bullets=new Bullet[2];
			bullets[0]=new Bullet(x+xStep,y-yStep);
			bullets[1]=new Bullet(x+3*xStep,y-yStep);
			doubleFire-=2;
			return bullets;
		}else{
			Bullet[] bullets=new Bullet[1];
			bullets[0]=new Bullet(x+2*xStep,y-yStep);
			return bullets;
		}
	}
	public void moveTo(int x,int y){		
			this.x=x-width/2;
			this.y=y-height/2;		
	}
	public void addDoubleFire(){
		doubleFire+=40;
	}
	public void addLife(){
		life++;
	}	
	public boolean outOfBounds() {		
		return x < 0||x >Game.WIDTH - width || y < 0|| y > Game.HEIGHT - height;
		
	}
	public void decreaseLife(){
		life--;
	}
	public void doubleFireToZero(){
		doubleFire=0;
	}
}

