package main.state;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import main.body.MapLoader;
import main.body.PowerUp;
import main.game.Handler;
import main.object.powerup.PowerUpGenerator;

public class GamePlay extends GameState {
	
	// Game mode state, 0: single-player, 1: multi-player, 2: Training
	private int gms;
	boolean isPaused = false, isContinue = false;
	// Count down from 3 is necessary before the game starts
	boolean startCountDown = true, countDownFinish = false, gameFinish = false, requestQuit = false;
	
	boolean holdEsc = false, holdP = false, holdUp = false, holdDown = false, haveSet = false;

	int pauseCount = 60, startCount = 180;
	int currentOption = 0, scoreply1 = 0, scoreply2 = 0;
	
	public List<BufferedImage> yesNo = new ArrayList<BufferedImage>();
	public List<BufferedImage> gamePlaystr = new ArrayList<BufferedImage>();	
	
	public List<PowerUp> powerUpSet = new ArrayList<PowerUp>();
	
	PowerUpGenerator powerUpGen = new PowerUpGenerator();
	
	String[] yesNostr = {"Yes", "No"};
	
	Handler handler = new Handler();
	
	public BufferedImage background;
	
	MapLoader mapLoader = new MapLoader();
	
	int tickCount = 0, min = 2, sec = 0;
	
	Random random = new Random();
			
	public GamePlay(StateManager sm, int gms) {
		this.sm = sm;
		this.gms = gms;
		
	}

	public void init() {
		// initialize background image for different game modes	
		switch(gms) {
		case 0:	background = imgLoader.loadingImage("/img/backgrounds/singleplayer.jpg"); break;
		case 1: background = imgLoader.loadingImage("/img/backgrounds/multiplayer.jpg"); break;
		case 2: background = imgLoader.loadingImage("/img/backgrounds/training.jpg"); break;
		}
		// initialize the map
		mapLoader.generateLevel(handler,sm);
		
		for (int i = 0; i < 4; i++) {
			yesNo.add(imgLoader.loadingImage("/font/gameplay/" + (i+1) + ".png"));
		}
		
		for (int i = 4; i < 13; i++) {
			gamePlaystr.add(imgLoader.loadingImage("/font/gameplay/" + (i+1) + ".png"));
		}
		
	}

	
	public void tick() {
		// Game will run if player(s) do(es) not pause the game		
		if (isContinue) {
			handler.tick();
			tickCount++;
			if (tickCount > 30) {
				tickCount = 0;
			}
			
			if (min == 0 && sec == 0) {
				sm.setState(StateManager.EXIT, gms, scoreply1, scoreply2);
			}
		}
	}

	public void render(Graphics g) {
		
		Graphics2D g2d = (Graphics2D) g;
		
		switch(gms) {
					// read getBackground method in Background class for state number
		case 0: break;	
		case 1: break;		
		case 2: break;		
		}
				
		g2d.drawImage(background, (int)0,(int)0,null);
		handler.render(g);
		
		if (isPaused) {

			g2d.setColor(getTransparentColor());
			g2d.fillRect(0, 0, 1024, 768);
		}

				
		if (startCountDown) {
			if (!countDownFinish) {
				isPaused = true;
				isContinue = false;
			}

			startCount--;
			// Display flash pause text
			if (startCount <= 30*4 && startCount > 30*3) {
				
				g2d.drawImage(gamePlaystr.get(5), 490, 330, null);
				
			} else if (startCount <= 30*3 && startCount > 30*2) {
				g2d.drawImage(gamePlaystr.get(6), 490, 330, null);
			} else if (startCount <= 30*2 && startCount > 30) {
				g2d.drawImage(gamePlaystr.get(7), 490, 330, null);
			} else {
				if (startCount <= 30 && startCount > 0 ) {
					g2d.drawImage(gamePlaystr.get(8), 420, 330, null);
					if(!haveSet) {
						isContinue = true;
						isPaused = false;
						countDownFinish = true;
						haveSet = true;
					}
				}

				if (startCount < 0) {

					startCountDown = false;
					startCount = 40;
				}
			}			
		// Start the game after the count down	
		} else {
						
			if (isPaused && !isContinue) {
				pauseCount--;
				/* Ask if player(s) want(s) to go back to main menu
				*  only available after the countdown
				*/
				if (requestQuit) {
					
					g2d.drawImage(gamePlaystr.get(0), 100, 200, null);
					int i = 0;
					
					if (i == currentOption) {
						g2d.drawImage(yesNo.get(1), 725, 500, null);
					} else {
						g2d.drawImage(yesNo.get(0), 725, 500, null);
					}
					i++;
					
					if (i == currentOption) {
						g2d.drawImage(yesNo.get(3), 730, 607, null);
					} else {
						g2d.drawImage(yesNo.get(2), 730, 607, null);
					}
				
				// Pause game
				} else {

					g2d.drawImage(gamePlaystr.get(1), 350, 250, null);
					// Display flash pause text
					if (pauseCount < 30) {
						g2d.drawImage(gamePlaystr.get(2), 175, 400, null);
					}			
					if (pauseCount < 0) {
						pauseCount = 60;
					}
				}
			
			}
			// Reset pause counter
			if (!isPaused) {
				pauseCount = 60;
			}

			// collision statement
			if (true) {
			}
			
			if (sec <= 30 && sec >=1) {
				// PowerUp Generation (per 30 sec)
				powerUpSet = powerUpGen.getPowerUps();
				powerUpGen.render(g);
			}
					
			
			if (!startCountDown && !isPaused) {
				scoreply1 = handler.p1score;
				scoreply2 = handler.p2score;
				String p1score = Integer.toString(handler.p1score);
				String p2score = Integer.toString(handler.p2score);
				Font score = new Font("Courier", Font.BOLD, 40);
				g.setFont(score);
				g.setColor(new Color(255, 128, 0));
				g.drawString(p1score, 60, 690);
				
				g.setFont(score);
				g.setColor(new Color(255, 128, 0));
				g.drawString(p2score, 940, 690);
				
				// Setting up the 2 min timer
				 // Generate string object to be covered from int to string
				 String leadingZero = "";
				 
				 if (sec < 10) {
					 leadingZero = "0";
				 }
				 String secStr = leadingZero + Integer.toString(sec);
				 if (tickCount == 30) {
					if (sec == 0) {
						sec = 59;
						min--;
					} else {
						sec--;
					}
				 }
				 String minStr = Integer.toString(min);
				 
				 g2d.drawImage(gamePlaystr.get(4),600, 20, null);
				 
				 // Display time left
				 Font timeText = new Font("Courier", Font.BOLD, 40);
				 g.setFont(timeText);
				 g.setColor(new Color(255, 128, 0));
				 g.drawString(minStr + ":" + secStr, 820, 60);
				 
			 }
			
		}
					
	}

	public void generatePowerup() {
		// TODO Auto-generated method stub
		
			
	}

	public boolean checkCollideWithMap (PowerUp powerup) {
		
		return false;
	}
	
	public void keyPressed (KeyEvent k) {
		
		if (!startCountDown && !holdP && !requestQuit && k.getKeyCode() == KeyEvent.VK_P) {
			holdP = true;
			// Pause game when P is pressed
			if (isContinue) {
				isPaused = true;
				isContinue = false;
			} else {
			// Resume game when P is pressed after the pause
				isPaused = false;
				isContinue = true;
				pauseCount = 60;
			}
		} 
		
		if (isContinue) {
			handler.keyPressed(k, gms);
		}
		
		// The window will be popped up for confirmation of exiting the game..
		if (countDownFinish && !holdEsc && k.getKeyCode() ==  KeyEvent.VK_ESCAPE) {
			holdEsc = true;
			if (isContinue == true && isPaused == false) {
				isPaused = true;
				isContinue = false;
				requestQuit = true;
				
				
			} else {
				isPaused = false;
				isContinue = true;
				requestQuit = false;
			}
		}
		
		if (k.getKeyCode() == KeyEvent.VK_ENTER) {
			if (requestQuit) {
				if (currentOption == 0) {
					sm.setState(sm.MENU);
				} else {
					isPaused = false;
					isContinue = true;
					requestQuit = false;
				}
			}
		}
		
		if (!holdUp && k.getKeyCode() == KeyEvent.VK_UP) {
			holdUp = true;
			if (requestQuit) {
				currentOption--;
				if(currentOption < 0) {
					currentOption = yesNostr.length - 1;
				}
			}
		}
		if(!holdDown && k.getKeyCode() == KeyEvent.VK_DOWN){
			holdDown = true;
			if (requestQuit) {
				currentOption++;
				if(currentOption >= yesNostr.length) {
					currentOption = 0;
				}
			}
		}
		
		if (k.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
			sm.setState(StateManager.EXIT, gms, scoreply1, scoreply2);
		}
		
	}
	
	/* method will be called if a key is released,
	 * and response according to what key was released.		
	 */
	public void keyReleased (KeyEvent k) {	
		switch(k.getKeyCode()) {
		case (KeyEvent.VK_ESCAPE):holdEsc = false; break;
		case (KeyEvent.VK_P)   : holdP = false; break;
		case (KeyEvent.VK_UP)  : holdUp = false; break;
		case (KeyEvent.VK_DOWN): holdDown = false; break;
		}
		handler.keyReleased(k, gms);
	}
	
	public Color getTransparentColor() {
		return new Color(0f, 0f, 0f, .5f);
	}
	
	
	
}
