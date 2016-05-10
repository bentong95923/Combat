package main.state;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import main.body.MapLoader;
import main.game.Handler;
import main.object.powerup.PowerUpGenerator;

public class GamePlay extends GameState {
	
	// Game mode state, 0: single-player, 1: multi-player, 2: Training
	private int gms;
	boolean isPaused = false, isContinue = false;
	// Count down from 3 is necessary before the game starts
	boolean startCountDown = true, countDownFinish = false, startTimer15Sec = false, gameFinish = false, requestQuit = false;
	boolean showPowerUp = false, powerUpGenerated = false;
	
	boolean holdEsc = false, holdP = false, holdUp = false, holdDown = false, haveSet = false;

	int pauseCount = 60, startCount = 180;
	int currentOption = 0, scoreP1 = 0, scoreP2 = 0;
	int tickCount = 0, count15sec = 0, min = 2, sec = 0;
	
	public List<BufferedImage> yesNo = new ArrayList<BufferedImage>();
	public List<BufferedImage> gamePlaystr = new ArrayList<BufferedImage>();	
		
	PowerUpGenerator powerUpGen;
	
	String[] yesNostr = {"Yes", "No"};
	
	Handler handler = new Handler();
	
	public BufferedImage background;
	
	MapLoader mapLoader = new MapLoader();	
			
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
		checkContentLoaded();		
		if (displayThisState) {
			// Game will run if player(s) do(es) not pause the game		
			if (isContinue) {
				handler.tick();
				tickTimer();
				
				if (min == 0 && sec == 0) {
					sm.setState(StateManager.EXIT, gms, scoreP1, scoreP2);
				}
				// Set up the life time (15secs) of the power up
				PowerUpSpawnTimer();
				// Set up the life time (15secs) of the power up
				powerUpLifeTimer();
				generatePowerups();
				removePowerUps();
			}
			pauseTimer();			
			// Set the 2 mins game timer
			gameTimer();		
			setStartCountDown();
			resetStartCountDown();
		}
	}

	public void render(Graphics g) {
		if (displayThisState) {
			Graphics2D g2d = (Graphics2D) g;
			
			// display background		
			g2d.drawImage(background, (int)0,(int)0,null);
			handler.render(g);
			
			
			displayScoreAndGameTimer(g);
			insertTransparentCover(g);
			
			if (startCountDown) {
				displayCountDownText(g2d);	
			// Start the game after the count down	
			} else {
				
				if (isPaused && !isContinue) {								
					if (requestQuit) {					
						displayQuitMenu(g2d);				
					} else {					
						displayPauseScreen(g2d);					
					}			
				}
			}
		}
	}
	
	public void displayScoreAndGameTimer(Graphics g) {
		if (displayThisState && (!startCountDown || isContinue)) {
			// Get the scores from the handler and display on screen
			scoreP1 = handler.getPlayerScore(1);
			scoreP2 = handler.getPlayerScore(2);
			String p1score = Integer.toString(scoreP1);
			String p2score = Integer.toString(scoreP2);
			Font score = new Font("Courier", Font.BOLD, 40);
			g.setFont(score);
			g.setColor(new Color(255, 128, 0));
			g.drawString(p1score, 60, 690);
			
			g.setFont(score);
			g.setColor(new Color(255, 128, 0));
			g.drawString(p2score, 940, 690);
			
			// Setting up the 2 mins timer
			// Generate string object to be covered from int to string
			String leadingZero = "";
			 
			if (sec < 10) {
				leadingZero = "0";
			}
			String secStr = leadingZero + Integer.toString(sec);
			
			String minStr = Integer.toString(min);
			 

			Graphics2D g2d = (Graphics2D) g;
			g2d.drawImage(gamePlaystr.get(4),600, 20, null);
			 
			// Display time left
			Font timeText = new Font("Courier", Font.BOLD, 40);
			g.setFont(timeText);
			g.setColor(new Color(255, 128, 0));
			g.drawString(minStr + ":" + secStr, 820, 60);
				 
		}
		
	}
	
	public void gameTimer() {
		if (!isPaused) {
			if (tickCount == 30) {
				if (sec == 0) {
					sec = 59;
					min--;
				} else {
					sec--;
				}
			}
		}		
	}
	
	public void tickTimer() {
		tickCount++;
		if (tickCount > 30) {
			tickCount = 0;
		}
	}
	
	public void powerUpLifeTimer() {
		if (startTimer15Sec) {
			if (tickCount == 30) {
				count15sec++;
			}
			if (count15sec == 15) {
				count15sec = 0;
				startTimer15Sec = false;
			}
			System.out.println("count15sec: "+count15sec);
		}
	}
	
	public void PowerUpSpawnTimer() {
		
		if ( (min == 1 && (sec < 30 && sec > 20)) || (min == 0 && (sec <= 59 && sec > 50)) ||
				(min == 0 && (sec < 30 && sec > 20)) ) {
			showPowerUp = true;
		} else {
			showPowerUp = false;
		}
	}	
	
	public void generatePowerups() {
		if (!powerUpGenerated && ((min == 1 && sec == 29) || (min == 0 && sec == 59)
			|| (min == 0 && sec == 29))) {
			powerUpGenerated = true;
			
			powerUpGen = new PowerUpGenerator();
			powerUpGen.generatePowerUps(handler);
		}
	}
	
	public void removePowerUps() {
		if (!showPowerUp) {
			powerUpGenerated = false;
			handler.setPowerUpTimeOut();
		} 
		handler.removePowerUps();		
	}
	
	public void setStartCountDown() {
		if (startCountDown) {
			if (!countDownFinish) {
				isPaused = true;
				isContinue = false;
			}
			startCount--;
		}
	}
	
	public void pauseTimer() {
		if (!startCountDown) {
			if (isPaused && !isContinue) {
				pauseCount--;
			}
			if (!isPaused) {
				pauseCount = 60;
			}
		}		
	}
	
	public void insertTransparentCover(Graphics g) {
		// Insert a back transparent cover on the screen when game is paused
		if (isPaused) {
			g.setColor(getTransparentColor());
			g.fillRect(0, 0, 1024, 768);
		}
	}
	
	public void checkContentLoaded() {
		// Check whether the required buffered images are loaded before displaying on screen
		if (!yesNo.isEmpty() && !gamePlaystr.isEmpty()) {	
			if (yesNo.size() == 4 && gamePlaystr.size() == 9) {
				displayThisState = true;
			}
		} else {
			displayThisState = false;
		}
	}
	
	/* Display the menu to ask if player(s) want(s)
	 * to go back to main menu. Only available after
	 * the count down
	*/
	public void displayQuitMenu(Graphics2D g2d) {
		
		// Display Yes or No options
		g2d.drawImage(gamePlaystr.get(0), 100, 200, null);
		
		int i = 0;
		
		// Setting to display current selection		
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
	}
	
	public void displayPauseScreen(Graphics2D g2d) {
		g2d.drawImage(gamePlaystr.get(1), 350, 250, null);
		// Display flash pause text
		if (pauseCount < 30) {
			g2d.drawImage(gamePlaystr.get(2), 175, 400, null);
		}			
		if (pauseCount < 0) {
			pauseCount = 60;
		}
	}
	
	public void displayCountDownText(Graphics2D g2d) {
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
		}	
	}
	
	public void resetStartCountDown() {
		if (startCount < 0) {
			startCountDown = false;
			startCount = 40;
		}
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
		
		// The window will be popped up for confirmation of exiting the game.
		if (!startCountDown && !holdEsc && k.getKeyCode() ==  KeyEvent.VK_ESCAPE) {
			holdEsc = true;
			// pause the game if the game was running
			if (isContinue == true && isPaused == false) {
				isPaused = true;
				isContinue = false;
				requestQuit = true;
				/* Current option will be "Yes" by default on the
				* quit pop up window.
				*/
				currentOption = 0;
			// resume the game if the game was paused
			} else {
				isPaused = false;
				isContinue = true;
				requestQuit = false;
			}
		}
		
		if (k.getKeyCode() == KeyEvent.VK_ENTER) {
			if (requestQuit) {
				if (currentOption == 0) {
					sm.setState(StateManager.MENU);
				} else {
					isPaused = false;
					isContinue = true;
					requestQuit = false;
					/* Current option will be "Yes" by default on the
					* quit pop up window.
					*/
					currentOption = 0;
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
			sm.setState(StateManager.EXIT, gms, scoreP1, scoreP2);
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
