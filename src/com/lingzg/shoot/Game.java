package com.lingzg.shoot;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Game extends JPanel{
	
	private static final long serialVersionUID = 3843322449897176659L;
	public static final int WIDTH=400;
	public static final int HEIGHT=654;
	private static final int START=0;
	private static final int RUNNING=1;
	private static final int PAUSE=2;
	private static final int GAME_OVER=3;
	
	public static BufferedImage backGround;
	public static BufferedImage start;
	public static BufferedImage pause;
	public static BufferedImage gameOver;
	public static BufferedImage airplane;
	public static BufferedImage bigplane;
	public static BufferedImage bee;
	public static BufferedImage hero0;
	public static BufferedImage hero1;
	public static BufferedImage bullet;
	public static BufferedImage[] heroEmber=new BufferedImage[4];	
	public static BufferedImage[] airplaneEmber=new BufferedImage[4];	
	public static BufferedImage[] bigplaneEmber=new BufferedImage[4];	
	public static BufferedImage[] beeEmber=new BufferedImage[4];
	
	private List<FlyingObjct> flyings=new ArrayList<FlyingObjct>();
	private List<Bullet> bullets=new ArrayList<Bullet>();
	private List<Ember> embers=new ArrayList<Ember>();
	private Hero hero=new Hero();
	private int index=0;	
	private int score=0;
	private int level=0;
	private int state=START;
	
	static{
		try{			
			backGround=ImageIO.read(Game.class.getResource("background.png"));			
			start=ImageIO.read(Game.class.getResource("start.png"));			
			pause=ImageIO.read(Game.class.getResource("pause.png"));
			gameOver=ImageIO.read(Game.class.getResource("gameover.png"));
			airplane=ImageIO.read(Game.class.getResource("airplane.png"));
			bigplane=ImageIO.read(Game.class.getResource("bigplane.png"));
			hero0=ImageIO.read(Game.class.getResource("hero0.png"));		
			hero1=ImageIO.read(Game.class.getResource("hero1.png"));
			bee=ImageIO.read(Game.class.getResource("bee.png"));
			bullet=ImageIO.read(Game.class.getResource("bullet.png"));
			for(int i=0;i<4;i++){
				heroEmber[i]=ImageIO.read(Game.class.getResource("hero_ember"+i+".png"));				
				airplaneEmber[i]=ImageIO.read(Game.class.getResource("airplane_ember"+i+".png"));			
				bigplaneEmber[i]=ImageIO.read(Game.class.getResource("bigplane_ember"+i+".png"));			
				beeEmber[i]=ImageIO.read(Game.class.getResource("bee_ember"+i+".png"));
			}
		}catch(Exception e){
			e.getStackTrace();
		}
	}
	
	public void action(){
		MouseAdapter l=new MouseAdapter(){
			public void mouseMoved(MouseEvent e){
				if(state==RUNNING){
					int x=e.getX();
					int y=e.getY();
					hero.moveTo(x,y);
				}
			}
			public void mouseEntered(MouseEvent e){
				if(state==PAUSE){
					state=RUNNING;
				}
			}
			public void mouseExited(MouseEvent e){
				if(state!=GAME_OVER){
					state=PAUSE;
				}
			}
			public void mouseClicked(MouseEvent e){
				switch(state){
				case START:
					state=RUNNING;
					break;									
				case GAME_OVER:
					flyings.clear();
					bullets.clear();
					embers.clear();
		            hero = new Hero();
		            score = 0;
		            index=0;
		            state = START;
		            break;
				}
			}
		};
		this.addMouseListener(l);
		this.addMouseMotionListener(l);
		Timer timer=new Timer();
		TimerTask task=new TimerTask(){
			public void run() {
				index++;
				if(state==RUNNING){
					level=score/1000;
					level=level>10? 10: level;
					enterAction();
					stepAction();
					shootAction();
					bangAction();
					deleteAction();
					checkGameOver();
					emberAction();
				}
				repaint();
			}
		};
		timer.schedule(task,10,10);
	}
	
	public void paint(Graphics g){
		g.drawImage(backGround,0,0,null);					
		paintHero(g);
		paintBullet(g);
		paintFlyingObjct(g);
		paintScore(g);
		paintEmber(g);
		paintState(g);	
	}		
	public void paintHero(Graphics g){		
		g.drawImage(hero.getImage(), hero.getX(), hero.getY(), null);
	}
	
	public void paintBullet(Graphics g){
		for(Bullet b:bullets){
			if(!b.isBomb()){
				g.drawImage(b.getImage(),b.getX()-b.getWidth(),b.getY(),null);
			}
		}
	}
	
	public void paintFlyingObjct(Graphics g){
		for(FlyingObjct f:flyings){
			g.drawImage(f.getImage(),f.getX(),f.getY(), null);
		}
	}
	
	public void paintEmber(Graphics g){
		for(Ember e:embers){
			g.drawImage(e.getImage(),e.getX(),e.getY(), null);
		}		
	}
	
	public void paintScore(Graphics g){
		int x=10;
		int y=25;
		Font font=new Font(Font.SANS_SERIF,Font.BOLD, 14);
		g.setColor(new Color(0x3A3B3B));
		g.setFont(font);
		g.drawString("Score:"+score,x,y);
		y+=20;
		g.drawString("Life:"+hero.getLife(),x,y);
	}
	
	public void paintState(Graphics g){
		switch(state){
		case START:
			g.drawImage(start,0,0,null);
			break;
		case PAUSE:
			g.drawImage(pause,0,0,null);
			break;
		case GAME_OVER:
			g.drawImage(gameOver,0,0,null);
			break;
		}
	}
	
	public void enterAction(){			
		int speed=40-3*level;
		if(index%speed==0){
			FlyingObjct obj=FlyingObjct.randomOne();
			flyings.add(obj);
		}
	}
	
	public void stepAction(){
		for(FlyingObjct f:flyings){
			f.step();
		}
		for(Bullet b:bullets){
			b.step();
		}
		hero.step();
	}
	
	public void shootAction(){			
		int speed=30-2*level;
		if(index%speed==0){
			Bullet[] bs=hero.shoot();
			for(Bullet b : bs){
				bullets.add(b);
			}
		}
	}	
	
	public void bangAction(){
		for(Bullet b:bullets){
			bang(b);
		}
	}
	
	private void bang(Bullet bullet) {
		for(Iterator<FlyingObjct> it=flyings.iterator();it.hasNext();){
			FlyingObjct o = it.next();
			if(o.shootBy(bullet)){
				it.remove();
				FlyingObjct lost=o;
				if(lost instanceof Enemy){
					Enemy e=(Enemy)lost;
					score+=e.getScore();
				}
				if(lost instanceof Award){
					Award a=(Award)lost;
					int type=a.getAwardType();
					switch(type){
					case Award.LIFE:
						hero.addLife();
						break;
					case Award.DOUBLE_FIRE:
						hero.addDoubleFire();
						break;
					}
				}
				embers.add(new Ember(lost));
				break;
			}
		}
	}
	
	public void deleteAction(){			
		for(Iterator<FlyingObjct> it=flyings.iterator();it.hasNext();){
			FlyingObjct o = it.next();
			if(o.outOfBounds()){
				it.remove();
			}
		}
		for(Iterator<Bullet> it=bullets.iterator();it.hasNext();){
			Bullet o = it.next();
			if(o.outOfBounds()){
				it.remove();
			}
		}
	}
	
	public void emberAction(){
		for(Iterator<Ember> it=embers.iterator();it.hasNext();){
			Ember o = it.next();
			if(o.burnDown()){
				it.remove();
			}
		}
	}
	
	public boolean gameOver(){			
		for(Iterator<FlyingObjct> it=flyings.iterator();it.hasNext();){
			FlyingObjct f = it.next();
			if(hero.hit(f)){
				hero.decreaseLife();
				hero.doubleFireToZero();
				it.remove();
				embers.add(new Ember(hero));
				embers.add(new Ember(f));
			}
		}		
		return hero.getLife()<=0;		
	}
	
	public void checkGameOver(){
		if(gameOver()){
			state=GAME_OVER;
		}
	}
	
	public static void main(String[] args){
		JFrame frame=new JFrame();
		Game game=new Game();
		frame.add(game);
		frame.setSize(WIDTH, HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);		
		game.action();
	}
}	
