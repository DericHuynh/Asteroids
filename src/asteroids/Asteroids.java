/**
 * Deric Huynh
 * Final Project 30
 */
package asteroids;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import asteroids.assets.AssetManager;
import asteroids.assets.BodyPart;
import asteroids.assets.Particle;
import asteroids.entities.Alien;
import asteroids.entities.Asteroid;
import asteroids.entities.Player;
import asteroids.entities.Projectile;
import asteroids.gui.AsteroidsGUI;
import asteroids.io.SaveLoad;
import asteroids.io.Score;
import asteroids.sfx.SoundHandler;
import utility.Utility;

/**
 * Asteroids class, main class
 */
public class Asteroids implements KeyListener {
	
	public static final int TARGET_FPS = 60;
	private long fps;
	private Score newHighScore;
	private Score[] highscores;
	private URL filePath;
	private SaveLoad saveAndLoad;
	private boolean gotoMainMenu;
	private boolean exit;
	private boolean wantsToRespawn;
	private boolean restartGame;
	private boolean hadLastDeath;
	private boolean gameOver;
	private boolean inMainMenu;
	private boolean inHighScoreMenu;
	private boolean inGameOver;
	private int livesLeft;
	private long score;
	private double randomParticleElapsedTime;
	private double playerAliveElapsedTime;
	private double alienElapsedTime;
	private Random random;
	private AsteroidsGUI gui;
	private SoundHandler sounds;
	private Player player;
	private ArrayList<Projectile> playerProjectiles;
	private ArrayList<Projectile> alienProjectiles;
	private ArrayList<Asteroid> asteroids;
	private ArrayList<Particle> particles;
	private ArrayList<BodyPart> bodyParts;
	private ArrayList<Alien> aliens;
	
	/**
	 * Creates a new game and runs it.
	 * pre: n/a
	 * post: New game has been created and run.
	 */
	public static void main(String[] args) {
		
		Asteroids game = new Asteroids();
		
		game.startGame();
		
	}
	
	/**
	 * Initializes the game, creating new lists and objects.
	 * Values are also initialized.
	 * pre: n/a
	 * post: Lists and Objects have been created. Values have been set.
	 */
	private void initializeGame() {
		
		player = new Player();
		random = new Random();
		sounds = new SoundHandler();
		playerProjectiles = new ArrayList<Projectile>();
		alienProjectiles = new ArrayList<Projectile>();
		asteroids = new ArrayList<Asteroid>();
		particles = new ArrayList<Particle>();
		bodyParts = new ArrayList<BodyPart>();
		aliens = new ArrayList<Alien>();
		newHighScore = null;
		hadLastDeath = false;
		alienElapsedTime = 0;
		randomParticleElapsedTime = 0;
		playerAliveElapsedTime = 0;
		livesLeft = 3;
		score = 0L;
		
	}
	
	/**
	 * Creates a new gui for the game.
	 * pre: n/a
	 * post: New gui has been created.
	 */
	private void startGui() {
	
		gui = new AsteroidsGUI(this);
		
		gui.start();
		
		synchronized (gui) {
			try {
				gui.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * Starts the game and enters a game loop.
	 * pre: n/a
	 * post: The game has started and only exits
	 * whenever the exit flag has been set.
	 */
	public void startGame() {
		
		final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;
		long lastFpsTime = 0;
		long currentTime;
		long previousTime;
		long elapsedTime;
		long frames = 0;
		double delta;
		
		filePath = this.getClass().getResource("/asteroids/resources/Highscores.sav");
		saveAndLoad = new SaveLoad(filePath);
		highscores = saveAndLoad.loadHighscores();
		saveAndLoad.saveHighscores(highscores);
		
		gameOver = true;
		inMainMenu = true;
		
		initializeGame();
		
		startGui();
		
		sounds.playMusic(5.0f);
		
		currentTime = System.nanoTime();
		previousTime = currentTime;
		
		while(!exit) {
			currentTime = System.nanoTime();
			elapsedTime = currentTime - previousTime;
			previousTime = currentTime;
			delta = elapsedTime / (double)OPTIMAL_TIME;
			lastFpsTime += elapsedTime;
			frames++;
			if(lastFpsTime >= 1000000000) {
				lastFpsTime = 0;
				System.out.println("Fps: " + frames);
				fps = frames;
				frames = 0;
			}
			update(delta);
			render();
		} 
		
		gui.exit();
		
	}
	
	/**
	 * Adds a particle to the game.
	 * pre: n/a
	 * post: A particle with specified x and y coordinates
	 * is created and added to the particles list.
	 */
	private void addParticle(double x, double y) {

		Particle particle;
		
		particle = new Particle(random, x, y);
		
		particles.add(particle);
		
	}
	
	/**
	 * A random particle is added to the game.
	 * pre: n/a
	 * post: A particle with random x and y coordinates is
	 * added to the game.
	 */
	private void addRandomParticle() {
		
		Particle particle;
		int frameHeight, frameWidth;
		int x, y;
		
		frameWidth = gui.getWidth();
		frameHeight = gui.getHeight();
		x = random.nextInt(frameWidth);
		y = random.nextInt(frameHeight);
		
		particle = new Particle(x, y);
		
		particles.add(particle);
		
	}
	
	/**
	 * Adds a bunch of particles originating from a coordinate
	 * pre: n/a
	 * post: An amount of Paticles with specified x and y
	 * has been created and added to game.
	 */
	private void addParticleDeath(double x, double y, int amount) {
		
		for (int k = 0; k < amount; k++) {
			addParticle(x, y);
		}
		
	}
	
	/**
	 * Adds a BodyPart with specified parameters
	 * pre: n/a
	 * post: Projectile with specified x, y, speed, angle, initialRotation and size
	 * has been created and added to the bodyParts list.
	 */
	private void addBodyPart(double x, double y, double speed, double angle, double initialRotation, int size) {
		
		BodyPart bodyPart;
		
		bodyPart = new BodyPart(random, x, y, speed, angle, initialRotation, size);
		
		bodyParts.add(bodyPart);
		
	}
	
	/**
	 * Adds a projectile with specified parameters
	 * pre: n/a
	 * post: Projectile with specified x, y, speed, angle
	 * has been created and added to the playerProjectiles list.
	 */
	private void addPlayerProjectile(double x, double y, double speed, double angle) {
		
		Projectile projectile;

		projectile = new Projectile(x, y, speed, angle, 2);
		
		sounds.playLaserSound(-27.0f);
		
		playerProjectiles.add(projectile);
		
	}
	
	/**
	 * Adds a projectile with specified parameters
	 * pre: n/a
	 * post: Projectile with specified x, y, speed, angle
	 * has been created and added to the alienProjectiles list.
	 */
	private void addAlienProjectile(double x, double y, double speed, double angle) {
		
		Projectile projectile;

		projectile = new Projectile(x, y, speed, angle, 0);
		
		sounds.playLaserSound(-27.0f);
		
		alienProjectiles.add(projectile);
		
	}
	
	/**
	 * Adds an alien with specified parameters
	 * pre: n/a
	 * post: Alien with specified x, y, speed, angle
	 * has been created and added to the aliens list.
	 */
	private void addAlien(double x, double y, double speed, double angle) {
		
		Alien alien;
		
		alien = new Alien(random, x, y, speed, angle);
		
		aliens.add(alien);
		
	}
	
	/**
	 * Adds a random alien to the game.
	 * pre: n/a
	 * post: Alien with random x, y, speed and angle is 
	 * added to the game.
	 */
	private void addRandomAlien() {
		
		int x, y;
		double speed, angle;
		int frameWidth, frameHeight;
		int direction;
		double angleOffset;
		
		frameWidth = gui.getWidth();
		frameHeight = gui.getHeight();
		angleOffset = Utility.getRandomFromRange(random, -45, 45);
		direction = random.nextInt(4);
		
		speed = random.nextDouble() + 0.5;
		angle = (direction + 1) * -90 + angleOffset;
		x = 0;
		y = 0 ;
		
		switch(direction) {
			case 0: //Top
				x = random.nextInt(frameWidth);
				y = -100;
				break;
			case 1: //Right
				x = frameWidth;
				y = random.nextInt(frameHeight);
				break;
			case 2: //Bottom
				x = random.nextInt(frameWidth);
				y = frameHeight;
				break;
			case 3: //Left
				x = -100;
				y = random.nextInt(frameHeight);
				break;
		}
		
		addAlien(x, y, speed, angle);
		
	}
	
	/**
	 * Adds an asteroid with specified parameters
	 * pre: n/a
	 * post: Asteroid with specified x, y, speed, angle, size and rotationSpeed
	 * has been created and added to the asteroids list.
	 */
	private void addAsteroid(double x, double y, double speed, double angle, double size, double rotationSpeed) {
		
		Asteroid asteroid;
		
		asteroid = new Asteroid(random, x, y, speed, angle, size, rotationSpeed);
		
		asteroids.add(asteroid);
		
	}
	
	/**
	 * Adds a random asteroid.
	 * pre: n/a
	 * post: an asteroid with a random size, directions, x, y, speed and rotation speed
	 * has been created and added to the game.
	 */
	private void addRandomAsteroid() {
		
		int x, y;
		double speed, angle, size;
		int frameWidth, frameHeight;
		int direction;
		double randomRotationSpeed, angleOffset;
		
		frameWidth = gui.getWidth();
		frameHeight = gui.getHeight();
		randomRotationSpeed = Utility.getRandomFromRange(random, -1, 1);
		angleOffset = Utility.getRandomFromRange(random, -45, 45);
		direction = random.nextInt(4);
		
		speed = random.nextDouble() + 0.5;
		size = random.nextDouble() * 1.3 + 1.7;
		angle = (direction + 1) * -90 + angleOffset;
		x = 0;
		y = 0 ;
		
		switch(direction) {
			case 0: //Top
				x = random.nextInt(frameWidth);
				y = -100;
				break;
			case 1: //Right
				x = frameWidth;
				y = random.nextInt(frameHeight);
				break;
			case 2: //Bottom
				x = random.nextInt(frameWidth);
				y = frameHeight;
				break;
			case 3: //Left
				x = -100;
				y = random.nextInt(frameHeight);
				break;
		}
		
		addAsteroid(x, y, speed, angle, size, randomRotationSpeed);
		
	}
	
	/**
	 * Updates the program.
	 * pre: n/a
	 * post: MainMenu, HighScoreMenu, GameOverScreen 
	 * or game has been updated.
	 */
	public void update(double delta) {
		
		if(gameOver) {
			if(inMainMenu) {
				updateMainMenu(delta);
			} else if(inHighScoreMenu) {
				updateHighScoreMenu(delta);
			} else if(inGameOver) {
				updateGameOver(delta);
			}
		} else {
			updateGame(delta);
		}
		
		if(gotoMainMenu) {
			score = 0L;
			gotoMainMenu = false;
			gameOver = true;
			inMainMenu = true;
			initializeGame();
		}
		
	}
	
	/**
	 * Updates the game.
	 * pre: n/a
	 * post: Asteroids, aliens, projectiles, animations and player
	 * has been updated. Player has respawned if flagged.
	 */
	private void updateGame(double delta) {
		
		updateAsteroids(delta);
		updateAliens(delta);
		updatePlayerProjectiles(delta);
		updateAlienProjectiles(delta);
		updateAnimations(delta);
		
		if(player.isAlive()) {
			updatePlayer(delta);
			playerAliveElapsedTime += delta;
			updateCollisions(delta);
			wantsToRespawn = false;
		} else {
			if(wantsToRespawn) {
				wantsToRespawn = false;
				respawn();
			}
		}	
		
		if(hadLastDeath) {
			long tempScore;
			gameOver = true;
			inMainMenu = false;
			inGameOver = true;
			hadLastDeath = false;
			Arrays.sort(highscores);
			tempScore = highscores[9].getScore();
			if(score > tempScore) {
				newHighScore = Score.newEmptyScore();
				newHighScore.setScore(score);
				highscores[9] = newHighScore;
				Arrays.sort(highscores);
			}
		}
		
	}
	
	/**
	 * Updates the gameOver.
	 * pre: n/a
	 * post: background has been updated. If player
	 * has flagged to restart the game, game is reinitialized.
	 */
	private void updateGameOver(double delta) {
		
		updateAsteroids(delta);
		updateAliens(delta);
		updatePlayerProjectiles(delta);
		updateAnimations(delta);
		
		if(restartGame) {
			saveAndLoad.saveHighscores(highscores);
			restartGame = false;
			inGameOver = false;
			inMainMenu = true;
			initializeGame();
		}
		
	}
	
	/**
	 * Updates the mainMenu background.
	 * pre: n/a
	 * post: background has been updated.
	 */
	private void updateMainMenu(double delta) {
		
		updateAsteroids(delta);
		updateAnimations(delta);
		
	}

	/**
	 * Updates the highscore background.
	 * pre: n/a
	 * post: background has been updated.
	 */
	private void updateHighScoreMenu(double delta) {
		
		updateAsteroids(delta);
		updateAnimations(delta);
		
	}
	
	/**
	 * Updates the player
	 * pre: Player is not equal to null.
	 * post: Player has been updated. Player projectile has been shot
	 * if player is firing and it is time to fire.
	 */
	private void updatePlayer(double delta) {
		
		int playerX, playerY;
		int playerWidth, playerHeight;
		int frameWidth, frameHeight;
		double playerSpeed, playerRotation;
		
		player.update(delta);
		
		playerX = (int) player.getX();
		playerY = (int) player.getY();
		
		playerWidth = (int) player.getWidth();
		playerHeight = (int) player.getHeight();
		
		frameWidth = gui.getWidth();
		frameHeight = gui.getHeight();
		
		player.isAlive();
		
		if(playerX < 0 - playerWidth) {
			player.setX(frameWidth);
		} else if(playerX > frameWidth) {
			player.setX(0 - playerWidth);
		}
		
		if(playerY < 0 - playerHeight) {
			player.setY(frameHeight);
		} else if(playerY > frameHeight) {
			player.setY(0 - playerHeight);
		}
		
		if(player.isTimeToFire() && player.isFiring()) {
			int[] playerTip;
			player.setTimeToFire(false);
			playerX = (int) player.getX();
			playerY = (int) player.getY();
			playerSpeed = player.getSpeed();
			playerRotation = player.getRotation();
			playerTip = player.getPlayerTip();
			addPlayerProjectile(playerTip[0],
								playerTip[1], 
								playerSpeed + 6,
								playerRotation);
		}
		
	}
	
	/**
	 * Updates all of the asteroids and adds new ones if applicable
	 * pre: asteroids list is not equal to null
	 * post: All asteroids have been updated. New ones added if there's not enough.
	 */
	private void updateAsteroids(double delta) {

		int asteroidX, asteroidY;
		int asteroidWidth, asteroidHeight;
		int frameWidth, frameHeight;
		int amountOfAsteroids;
		int amountOfBigAsteroids;
		Asteroid asteroid;
		
		frameWidth = gui.getWidth();
		frameHeight = gui.getHeight();
		amountOfAsteroids = asteroids.size();
		amountOfBigAsteroids = 0;
		
		for(int i = asteroids.size() - 1; i >= 0; i--) {
			asteroid = asteroids.get(i);
			asteroid.update(delta);
			asteroidX = (int) asteroid.getX();
			asteroidY = (int) asteroid.getY();
			asteroidWidth = (int) asteroid.getWidth();
			asteroidHeight = (int) asteroid.getHeight();
			if(asteroidX < 0 - asteroidWidth) {
				asteroid.setX(frameWidth);
			} else if(asteroidX > frameWidth) {
				asteroid.setX(0 - asteroidWidth);
			}
			if(asteroidY < 0 - asteroidHeight) {
				asteroid.setY(frameHeight);
			} else if(asteroidY > frameHeight) {
				asteroid.setY(0 - asteroidHeight);

			}	
			if(asteroid.getSize() > 2) {
				amountOfBigAsteroids++;
			} 
		}
		
		if(amountOfBigAsteroids < 2 || (amountOfBigAsteroids < 8 && amountOfAsteroids < 24)) {
			addRandomAsteroid();
		}
		
	}
	
	/**
	 * Updates all of the aliens and adds new ones if applicable
	 * pre: aliens list is not equal to null
	 * post: All aliens have been updated. New ones added if enough time has passed.
	 */
	private void updateAliens(double delta) {
		
		boolean playerIsAlive;
		int alienX, alienY;
		int alienWidth, alienHeight;
		int frameWidth, frameHeight;
		int alienMiddleX, alienMiddleY;
		int playerX, playerY;
		int playerWidth, playerHeight;
		int playerMiddleX, playerMiddleY;
		double alienAngleGoal;
		double alienDistanceGoal;
		double alienGoalX, alienGoalY;
		double alienFiringAngle;
		double accelerationAngle;
		Alien alien;
		
		frameWidth = gui.getWidth();
		frameHeight = gui.getHeight();
		
		if(player.isAlive()) {
			alienElapsedTime += delta;
			if(alienElapsedTime > Asteroids.TARGET_FPS * 15) {
				addRandomAlien();
				alienElapsedTime = 0;
			}
		}
		
		playerIsAlive = player.isAlive();
		playerX = (int) player.getX();
		playerY = (int) player.getY();
		playerWidth = (int) player.getWidth();
		playerHeight = (int) player.getHeight();
		playerMiddleX = playerX + playerWidth / 2;
		playerMiddleY = playerY + playerHeight / 2;
		
		for(int i = aliens.size() - 1; i >= 0; i--) {
			alien = aliens.get(i);
			alien.update(delta);
			alienX = (int) alien.getX();
			alienY = (int) alien.getY();
			alienWidth = (int) alien.getWidth();
			alienHeight = (int) alien.getHeight();
			//Edge looping
			if(alienX < 0 - alienWidth) {
				alien.setX(frameWidth);
			} else if(alienX > frameWidth) {
				alien.setX(0 - alienWidth);
			}
			if(alienY < 0 - alienHeight) {
				alien.setY(frameHeight);
			} else if(alienY > frameHeight) {
				alien.setY(0 - alienHeight);
			}
			alienX = (int) alien.getX();
			alienY = (int) alien.getY();
			alienMiddleX = alienX + alienWidth / 2;
			alienMiddleY = alienY + alienHeight / 2;
			if(playerIsAlive) {
				//Alien goal targeting
				alienDistanceGoal = alien.getDistanceGoal();
				alienAngleGoal = alien.getAngleGoal();
				alienGoalX = alienDistanceGoal * Math.cos(Math.toRadians(alienAngleGoal)) + playerMiddleX;
				alienGoalY = alienDistanceGoal * Math.sin(Math.toRadians(alienAngleGoal)) + playerMiddleY;
				accelerationAngle = Math.toDegrees(Math.atan2(alienGoalY - alienY, alienGoalX - alienX));
				alien.setAccelerationAngle(accelerationAngle);
			} else {
				//Alien wander
				alien.rotateAcceleration(Utility.getRandomFromRange(random, -90, 90) * delta);
			}
			//Alien firing
			if(alien.isTimeToFire() && playerIsAlive && playerAliveElapsedTime >= TARGET_FPS * 1) {
				alien.setTimeToFire(false);
				alien.getSpeed();
				alienFiringAngle = Math.toDegrees(Math.atan2(playerMiddleY - alienY, playerMiddleX - alienX));
				addAlienProjectile(alienMiddleX,
									alienMiddleY, 
									8, 
									alienFiringAngle);
			}
			if(!alien.isAlive()) {
				aliens.remove(i);
			}
		}
		
	}
	
	/**
	 * Updates playerProjectiles
	 * pre: playerProjectiles is not equal to null.
	 * post: playerProjectiles is updated.
	 */
	private void updatePlayerProjectiles(double delta) {
		
		int projectileX, projectileY;
		int projectileWidth, projectileHeight;
		int frameWidth, frameHeight;
		Projectile projectile;
		
		frameWidth = gui.getWidth();
		frameHeight = gui.getHeight();
		
		for(int i = playerProjectiles.size() - 1; i >= 0; i--) {
			projectile = playerProjectiles.get(i);
			projectile.update(delta);
			projectileX = (int) projectile.getX();
			projectileY = (int) projectile.getY();
			projectileWidth = (int) projectile.getWidth();
			projectileHeight = (int) projectile.getHeight();
			if(projectile.isAlive()) {
				if(projectileX < 0 - projectileWidth) {
					projectile.setX(frameWidth - projectileWidth);
				} else if(projectileX > frameWidth + projectileWidth) {
					projectile.setX(0 - projectileWidth);
				}
				if(projectileY < 0 - projectileHeight) {
					projectile.setY(frameHeight - projectileHeight);
				} else if(projectileY > frameHeight) {
					projectile.setY(0 - projectileHeight);
				}	
			} else {
				if(projectileX < 0 - projectileWidth || 
				   projectileY < 0 - projectileHeight || 
				   projectileX > frameWidth - projectileWidth || 
				   projectileY > frameHeight - projectileHeight) {
					playerProjectiles.remove(projectile);
				}	
			}
		}
		
	}
	
	/**
	 * Updates alienProjectiles
	 * pre: alienProjectiles is not equal to null.
	 * post: alienProjectiles is updated.
	 */
	private void updateAlienProjectiles(double delta) {
		
		int projectileX, projectileY;
		int projectileWidth, projectileHeight;
		int frameWidth, frameHeight;
		Projectile projectile;
		
		frameWidth = gui.getWidth();
		frameHeight = gui.getHeight();
		
		for(int i = alienProjectiles.size() - 1; i >= 0; i--) {
			projectile = alienProjectiles.get(i);
			projectile.update(delta);
			projectileX = (int) projectile.getX();
			projectileY = (int) projectile.getY();
			projectileWidth = (int) projectile.getWidth();
			projectileHeight = (int) projectile.getHeight();
			if(projectile.isAlive()) {
				if(projectileX < 0 - projectileWidth) {
					projectile.setX(frameWidth - projectileWidth);
				} else if(projectileX > frameWidth + projectileWidth) {
					projectile.setX(0 - projectileWidth);
				}
				if(projectileY < 0 - projectileHeight) {
					projectile.setY(frameHeight - projectileHeight);
				} else if(projectileY > frameHeight) {
					projectile.setY(0 - projectileHeight);
				}	
			} else {
				if(projectileX < 0 - projectileWidth || 
				   projectileY < 0 - projectileHeight || 
				   projectileX > frameWidth - projectileWidth || 
				   projectileY > frameHeight - projectileHeight) {
					alienProjectiles.remove(projectile);
				}	
			}
		}
		
	}
	
	/**
	 * Checks collisions between Asteroids and playerProjectiles, asteroids and player,
	 * player and alienProjectiles and playerProjectiles and aliens.
	 * pre: asteroids, playerProjectiles, player, aliens and alienProjectiles are not equal to null
	 * post: Collisions have been checked. Player death has been checked, handled if
	 * player is dead.
	 */
	private void updateCollisions(double delta) {
		
		double x, y, speed, tempSpeed, tempAngle, angle, rotationSpeed;
		double size;
		long scoreIncrease;
		Asteroid tempAsteroid;
		Alien tempAlien;
		Projectile tempProjectile;
		boolean playerDied;
		
		playerDied = false;
		
		//Player Projectiles Collision
		for(int j = playerProjectiles.size() - 1; j >= 0; j--) {
			tempProjectile = playerProjectiles.get(j);
			//Asteroid detection
			for(int i = asteroids.size() - 1; i >= 0; i--) {
				tempAsteroid = asteroids.get(i);
				if (tempAsteroid.contains(tempProjectile)) {
					size = tempAsteroid.getSize();
					x = tempAsteroid.getX() + tempAsteroid.getWidth() / 2;
					y = tempAsteroid.getY() + tempAsteroid.getHeight() / 2;
					addParticleDeath(x, y, 30);
					if (size > 1) {
						x = tempAsteroid.getX();
						y = tempAsteroid.getY();
						tempSpeed = tempAsteroid.getSpeed();
						tempAngle = tempAsteroid.getAngle();
						angle = tempAngle + Utility.getRandomFromRange(random, -90, 90);
						speed = tempSpeed + Utility.getRandomFromRange(random, -1, 1) + 0.2;
						rotationSpeed = Utility.getRandomFromRange(random, -1, 1);
						addAsteroid(x, y, speed, angle, size / 2, rotationSpeed);
						angle = tempAngle + Utility.getRandomFromRange(random, -90, 90);
						speed = tempSpeed + Utility.getRandomFromRange(random, -1, 1) + 0.2;
						rotationSpeed = Utility.getRandomFromRange(random, -1, 1);
						addAsteroid(x, y, speed, angle, size / 2, rotationSpeed);
					}
					sounds.playExplosionSound((float) (-22.0 + size * 4));
					scoreIncrease = (long) (Math.pow(2, size) * 40);
					handleLifeGain(score, scoreIncrease);
					score += scoreIncrease;
					playerProjectiles.remove(tempProjectile);
					asteroids.remove(tempAsteroid);
				}
			}
			//Alien Detection
			for(int i = aliens.size() - 1; i >= 0; i--) {
				tempAlien = aliens.get(i);
				if (tempAlien.contains(tempProjectile)) {
					for (int k = 0; k < 20; k++) {
						x = tempAlien.getX() + tempAlien.getWidth() / 2;
						y = tempAlien.getY() + tempAlien.getHeight() / 2;
						addParticle(x, y);
					}
					sounds.playExplosionSound(-16.0f);
					score += 1500;
					playerProjectiles.remove(tempProjectile);
					aliens.remove(tempAlien);
				}
			}
		}
		
		//Asteroid detection
		for(int i = asteroids.size() - 1; i >= 0; i--) {
			tempAsteroid = asteroids.get(i);
			if (tempAsteroid.contains(player)) {
				playerDied = true;
			}
		}
		
		//Alien projectile Detection
		for(int i = alienProjectiles.size() - 1; i >= 0; i--) {
			tempProjectile = alienProjectiles.get(i);
			if (player.contains(tempProjectile)) {
				x = player.getX() + player.getWidth() / 2;
				y = player.getY() + player.getHeight() / 2;
				addParticleDeath(x, y, 40);
				score += 500;
				playerProjectiles.remove(tempProjectile);
				playerDied = true;
			}
		}
		
		if(playerDied) {
			handlePlayerDeath();
		}
		
		
	}
	
	/**
	 * Checks if player warrants gaining a new life.
	 * pre: n/a
	 * post: Player gains a new life if the (score + scoreIncrease) / 10000 > score / 10000,
	 * assuming the produced values are floor-rounded to an integer.
	 */
	private void handleLifeGain(long score, long scoreIncrease) {
		
		long newScore;
		
		newScore = score + scoreIncrease;
		
		if(Math.floor((double)newScore / 10000.0) > Math.floor((double)score / 10000.0)) {
			if(livesLeft < 3) {
				livesLeft += 1;
			}
		}
		
	}

	/**
	 * Handles the death of the player, lowering the amount of lives he has left,
	 * and setting a flag if the player died with no lives left.
	 * pre: player is not equal to null. LivesLeft is initialized.
	 * post: The amount of lives left has been lowered by 1, the hadLastDeath flag
	 * set to true if the player is left with less than 0 livesLeft.
	 */
	private void handlePlayerDeath() {
	
		int[] shape, shapeLengths;
		double x, y;
		double[] shapeAngles;
		double speed, angle, rotation;
		
		shape = player.getPlayerShape();
		rotation = player.getRotation();
		shapeAngles = AssetManager.getPlayerAngles();
		shapeLengths = AssetManager.getPlayerLengths();
		speed = player.getSpeed();
		angle = player.getAngle();
		
		//Body part death effect
		for(int j = 0; j < shape.length / 2; j += 1) {
			x = shape[j * 2];
			y = shape[j * 2 + 1];
			addBodyPart(x, y, speed, angle, rotation + shapeAngles[j], shapeLengths[j]);
		}
		
		livesLeft -= 1;
		player.setAlive(false);
		playerAliveElapsedTime = 0;
		sounds.playExplosionSound(-12.0f);
		
		//Died without any lives left
		if(livesLeft < 0) {
			hadLastDeath = true;
		}
		
	}
	
	/**
	 * Updates all of the animation lists. This includes the bodyParts list
	 * and the particles list.
	 * pre: animation lists not null.
	 * post: 
	 */
	private void updateAnimations(double delta) {
		
		Particle particle;
		BodyPart bodyPart;
		
		randomParticleElapsedTime += delta;
		
		if(randomParticleElapsedTime > TARGET_FPS / 40) {
			addRandomParticle();
			randomParticleElapsedTime = 0;
		}
		
		for(int i = particles.size() - 1; i >= 0; i--) {
			particle = particles.get(i);
			particle.update(delta);
			if(particle.isAlive() == false) {
				particles.remove(i);
			}
		}
		
		for(int i = bodyParts.size() - 1; i >= 0; i--) {
			bodyPart = bodyParts.get(i);
			bodyPart.update(delta);
			if(bodyPart.isActive() == false) {
				bodyParts.remove(i);
			}
		}
		
	}
	
	/**
	 * Asks the gui to render the current frame.
	 * pre: n/a
	 * post: Gui has been asked to repaint.
	 */
	private void render() {
		
		gui.repaint();
		
	}
	
	/**
	 * Draws the state of the game to the graphics context.
	 * pre: g is not equal to null.
	 * post: The state of the game has been drawn.
	 */
	public void draw(Graphics g) {
		
		if(gameOver) {
			if(inMainMenu) {
				drawMainMenu(g);
			} else if(inHighScoreMenu) {
				drawHighscoreMenu(g);
			} else if(inGameOver) {
				drawGameOver(g);
			}
		} else {
			drawGame(g);
		}
		
		drawFPS(g);
		
	}
	
	/**
	 * Draws an fps counter in the top right corner.
	 * pre: g is not equal to null.
	 * post: An fps counter in the top right corner has been drawn
	 */
	private void drawFPS(Graphics g) {
		
		int frameWidth;
		int textWidth;
		String text;
		Font normalFont;
		FontMetrics metrics;
		
		text = String.valueOf(fps) + " FPS";
		
		g.setColor(Color.white);
		
		frameWidth = gui.getWidth();
		gui.getHeight();
		normalFont = new Font("Monospaced", Font.PLAIN, 16);
		g.setFont(normalFont);
		metrics = g.getFontMetrics();
		
		textWidth = metrics.stringWidth(text);
		g.drawString(text, (int) (frameWidth - textWidth - 20), 16);
		
		
		
	}
	
	/**
	 * Draws the main menu of the game, including the background.
	 * pre: g is not equal to null.
	 * post: The asteroids, playerProjectiles and 
	 * animations (bodyparts and particles) have been drawn.
	 * The title and instructions have been drawn.
	 */
	private void drawMainMenu(Graphics g) {
		
		int frameWidth, frameHeight;
		int textSize;
		int textWidth;
		String titleText, startGameText;
		String highscoreMenuText, exitText;
		Font titleFont;
		FontMetrics metrics;
		
		frameWidth = gui.getWidth();
		frameHeight = gui.getHeight();
		titleText = "Asteroids";
		startGameText = "Press [Enter] to play";
		highscoreMenuText = "Press [H] for highscores";
		exitText = "Press [Esc] to exit";
		
		//Draw background
		g.setColor(Color.gray);
		drawAsteroids(g);
		drawPlayerProjectiles(g);
		drawAnimations(g);
		
		//Draw Title
		g.setColor(Color.white);
		textSize = Math.min(frameWidth / 8, frameHeight / 8);
		titleFont = new Font("Monospaced", Font.PLAIN, textSize);
		g.setFont(titleFont);
		metrics = g.getFontMetrics();
		textWidth = metrics.stringWidth(titleText);
		g.drawString(titleText, (frameWidth - textWidth) / 2, (int)((frameHeight / 24) * 4));
		
		//Draw StartGame Text
		textSize = Math.min(frameWidth / 32, frameHeight / 32);
		titleFont = new Font("Monospaced", Font.PLAIN, textSize);
		g.setFont(titleFont);
		metrics = g.getFontMetrics();
		textWidth = metrics.stringWidth(startGameText);
		g.drawString(startGameText, (int) (frameWidth - textWidth) / 2 , (int) (((frameHeight - metrics.getHeight()) / 24) * 11));
		
		//Draw highscore menu Text
		textWidth = metrics.stringWidth(highscoreMenuText);
		g.drawString(highscoreMenuText, (int) (frameWidth - textWidth) / 2, (int) (((frameHeight - metrics.getHeight()) / 24) * 13));
		
		//Draw exit Text
		textWidth = metrics.stringWidth(exitText);
		g.drawString(exitText, (int) (frameWidth - textWidth) / 2, (int) (((frameHeight - metrics.getHeight()) / 24) * 15));
		
	}
	
	/**
	 * Draws the highscore menu, including the background.
	 * pre: g is not equal to null.
	 * post: The asteroids and animations (bodyparts and particles) have been drawn.
	 * The highscores and instructions have been drawn.
	 */
	private void drawHighscoreMenu(Graphics g) {

		int frameWidth, frameHeight;
		int textSize;
		int textWidth;
		String highscoreText, topHighscoresText;
		String instructionsText;
		Font font;
		FontMetrics metrics;
		
		frameWidth = gui.getWidth();
		frameHeight = gui.getHeight();
		topHighscoresText = "Highscores:";
		instructionsText = "Press [Enter] to continue...";
		
		//Draw background
		g.setColor(Color.GRAY);
		drawAsteroids(g);
		drawAnimations(g);
		
		//Draw backdrop
		g.setColor(Color.black);
		g.fillRect((frameWidth / 3), (int) ((frameHeight / 24) * 2), frameWidth / 3, (int) ((frameHeight / 24) * 19));
		g.setColor(Color.white);
		g.drawRect((frameWidth / 3), (int) ((frameHeight / 24) * 2), frameWidth / 3, (int) ((frameHeight / 24) * 19));
		
		//Draw divider
		g.drawLine((frameWidth / 3), (int) ((frameHeight / 24) * 19.1), (frameWidth / 3) * 2, (int) ((frameHeight / 24) * 19.1));
		
		//Draw highscore text
		textSize = Math.min(frameWidth / 32, frameHeight / 32);
		font = new Font("Monospaced", Font.PLAIN, textSize);
		g.setFont(font);
		metrics = g.getFontMetrics();
		textWidth = metrics.stringWidth(topHighscoresText);
		g.drawString(topHighscoresText, (frameWidth - textWidth) / 2, (int)((frameHeight / 24) * 3.4));
		
		//Draw top scores
		for(int i = 0; i < 10; i++) {
			highscoreText = (i + 1) + ". " + highscores[i].getInitials() + "  " + highscores[i].getScore();
			textWidth = metrics.stringWidth(highscoreText);
			g.drawString(highscoreText,(int) ((frameWidth / 3) * 1.12) - metrics.stringWidth(String.valueOf(i + 1)), (int) ((frameHeight / 24) * (5 + i * 1.45)));
		}
		
		//Draw instructions
		textWidth = metrics.stringWidth(instructionsText);
		g.drawString(instructionsText, (frameWidth - textWidth) / 2, (int)((frameHeight / 24) * 20.2));
		
	}
	
	/**
	 * Draws the game over screen, including the background.
	 * pre: g is not equal to null.
	 * post: The asteroids, playerProjectiles and 
	 * animations (bodyparts and particles) have been drawn.
	 * The current score, highscores and instructions have been drawn.
	 */
	private void drawGameOver(Graphics g) {

		int frameWidth, frameHeight;
		int textSize;
		int textWidth;
		String gameOverText, scoreText;
		String highscoreText, topHighscoresText;
		String instructionsText;
		Font font;
		FontMetrics metrics;
		
		frameWidth = gui.getWidth();
		frameHeight = gui.getHeight();
		gameOverText = "Game Over";
		scoreText = "Score: " + String.valueOf(score);
		topHighscoresText = "Highscores:";
		instructionsText = "Press [Enter] to continue...";
		
		//Draw background
		g.setColor(Color.GRAY);
		drawAsteroids(g);
		drawPlayerProjectiles(g);
		drawAnimations(g);
		
		//Draw backdrop
		g.setColor(Color.black);
		g.fillRect((frameWidth / 3), (frameHeight / 24), frameWidth / 3, (int) ((frameHeight / 24) * 21));
		g.setColor(Color.white);
		g.drawRect((frameWidth / 3), (frameHeight / 24), frameWidth / 3, (int) ((frameHeight / 24) * 21));
		
		//Draw Dividers
		g.drawLine((frameWidth / 3), (int) ((frameHeight / 24) * 3.5), (frameWidth / 3) * 2, (int) ((frameHeight / 24) * 3.5));
		g.drawLine((frameWidth / 3), (int) ((frameHeight / 24) * 20), (frameWidth / 3) * 2, (int) ((frameHeight / 24) * 20));
		
		//Draw Game Over
		textSize = Math.min(frameWidth / 32, frameHeight / 32);
		font = new Font("Monospaced", Font.PLAIN, textSize);
		g.setFont(font);
		metrics = g.getFontMetrics();
		textWidth = metrics.stringWidth(gameOverText);
		g.drawString(gameOverText, (frameWidth - textWidth) / 2, (int)((frameHeight / 24) * 2.2));
		
		//Draw player score
		textWidth = metrics.stringWidth(scoreText);
		g.drawString(scoreText, (frameWidth - textWidth) / 2, (int)((frameHeight / 24) * 3));
		
		//Draw highscore text
		textWidth = metrics.stringWidth(topHighscoresText);
		g.drawString(topHighscoresText, (frameWidth - textWidth) / 2, (int)((frameHeight / 24) * 4.7));
		
		//Draw top scores
		for(int i = 0; i < 10; i++) {
			highscoreText = (i + 1) + ". " + highscores[i].getInitials() + "  " + highscores[i].getScore();
			textWidth = metrics.stringWidth(highscoreText);
			g.drawString(highscoreText,(int) ((frameWidth / 3) * 1.12) - metrics.stringWidth(String.valueOf(i + 1)), (int) ((frameHeight / 24) * (6 + i * 1.45)));
		}
		
		//Draw instructions
		textWidth = metrics.stringWidth(instructionsText);
		g.drawString(instructionsText, (frameWidth - textWidth) / 2, (int)((frameHeight / 24) * 21.2));
		
	}
	

	/**
	 * Draws the game.
	 * pre: g is not equal to null.
	 * post: The asteroids, aliens, playerProjectiles, alienProjectiles,
	 * animations and hud have been drawn. The player is drawn if he's alive, 
	 * else the spawn instructions are drawn.
	 */
	private void drawGame(Graphics g) {

		g.setColor(Color.WHITE);
		drawAsteroids(g);
		drawAliens(g);
		drawPlayerProjectiles(g);
		drawAlienProjectiles(g);
		drawAnimations(g);
		drawHud(g);

		if(player.isAlive()) {
			drawPlayer(g);
		} else {
			drawSpawnText(g);
		}
		
	}
	
	/**
	 * Draws the player.
	 * pre: g is not equal to null. Player is not equal to null. 
	 * post: Player has been drawn.
	 */
	private void drawPlayer(Graphics g) {
		
		player.draw(g);
		
	}
	
	/**
	 * Draws spawn text instructions.
	 * pre: g is not equal to null
	 * post: Spawn text instruction has been drawn.
	 */
	private void drawSpawnText(Graphics g) {
		
		Font font;
		FontMetrics metrics;
		String text;
		int textSize;
		int frameWidth, frameHeight;
		int x, y;
		
		g.setColor(Color.white);
		
		frameWidth = gui.getWidth();
		frameHeight = gui.getHeight();
		
		text = "Press [Enter] to respawn...";
		
		textSize = Math.min(frameWidth / 32, frameHeight / 32);
		font = new Font("Monospaced", Font.PLAIN, textSize);
		g.setFont(font);
		metrics = g.getFontMetrics();
		x = (frameWidth - metrics.stringWidth(text)) / 2;
		y = (frameHeight - metrics.getHeight()) / 2;
		
		g.drawString(text, x, y);
		
	}
	
	/**
	 * Draws the aliens.
	 * pre: g is not equal to null. aliens is not equal to null.
	 * post: Aliens have been drawn.
	 */
	private void drawAliens(Graphics g) {
		
		Alien[] tempAlienArray;
		int tempArraySize;
		
		tempArraySize = aliens.size();
		tempAlienArray = aliens.toArray(new Alien[tempArraySize]);
		
		for(Alien alien : tempAlienArray) {
			if(alien != null) {
				alien.draw(g);
			}
		}
		
	}
	
	/**
	 * Draws the playerProjectiles.
	 * pre: g is not equal to null. playerProjectiles is not equal to null.
	 * post: playerProjectiles have been drawn.
	 */
	private void drawPlayerProjectiles(Graphics g) {
		
		Projectile[] tempProjectileArray;
		int tempArraySize;
		
		tempArraySize = playerProjectiles.size();
		tempProjectileArray = playerProjectiles.toArray(new Projectile[tempArraySize]);
		
		for(Projectile projectile : tempProjectileArray) {
			if(projectile != null) {
				projectile.draw(g);
			}
		}
		
	}
	
	/**
	 * Draws the alienProjectiles.
	 * pre: g is not equal to null. alienProjectiles is not equal to null.
	 * post: alienProjectiles have been drawn.
	 */
	private void drawAlienProjectiles(Graphics g) {
		
		Projectile[] tempProjectileArray;
		int tempArraySize;
		
		tempArraySize = alienProjectiles.size();
		tempProjectileArray = alienProjectiles.toArray(new Projectile[tempArraySize]);
		
		for(Projectile projectile : tempProjectileArray) {
			if(projectile != null) {
				projectile.draw(g);
			}
		}
		
	}
	
	/**
	 * Draws all of the animation lists.
	 * pre: g is not equal to null. particles is not equal to null.
	 * bodyParts is not equal to null.
	 * post: All animations have been drawn.
	 */
	private void drawAnimations(Graphics g) {
		
		Particle[] tempParticleArray;
		BodyPart[] tempBodyPartArray;
		int tempArraySize;
		
		tempArraySize = particles.size();
		tempParticleArray = particles.toArray(new Particle[tempArraySize]);
		tempArraySize = bodyParts.size();
		tempBodyPartArray = bodyParts.toArray(new BodyPart[tempArraySize]);
		
		for(Particle particle : tempParticleArray) {
			if(particle != null) {
				particle.draw(g);
			}
		}
		
		for(BodyPart bodyPart : tempBodyPartArray) {
			if(bodyPart != null) {
				bodyPart.draw(g);
			}
		}
		
	}
	
	/**
	 * Draws all of the animation lists.
	 * pre: g is not equal to null. particles is not equal to null.
	 * bodyParts is not equal to null.
	 * post: All animations have been drawn.
	 */
	private void drawAsteroids(Graphics g) {
		
		Asteroid[] tempAsteroidArray;
		int tempArraySize;
		
		tempArraySize = asteroids.size();
		tempAsteroidArray = asteroids.toArray(new Asteroid[tempArraySize]);
		
		for(Asteroid asteroid : tempAsteroidArray) {
			if(asteroid != null) {
				asteroid.draw(g);
			}
		}
		
	}
	
	/**
	 * Draws the hud.
	 * pre: g is not equal to null.
	 * post: The hud has been drawn. 
	 * This includes the score and the visual lives left counter
	 */
	private void drawHud(Graphics g) {
		
		Polygon livesLeftShape;
		Font scoreFont;
		String scoreText;
		g.setColor(Color.white);
		
		scoreText = String.valueOf(score);
		livesLeftShape = new Polygon();
		
		scoreFont = new Font("Monospaced", Font.PLAIN, 32);
		g.setFont(scoreFont);
		
		g.getFontMetrics();
	
		g.drawString(scoreText, 31, 35);
		
		AssetManager.addLivesLeftPoints(livesLeftShape);
		livesLeftShape.translate(30, 50);
		
		for(int i = 0; i < livesLeft; i++) {
			g.drawPolygon(livesLeftShape);
			livesLeftShape.translate(40, 0);
		}
		
	}
	
	/**
	 * Respawn's the player. This also destroys entities that 
	 * could result in an immediate death.
	 * pre: asteroids is not equal to null. alienProjectiles is not equal to null.
	 * aliens is not equal to null. player is not equal to null.
	 * post: Asteroids, alienProjectiles and aliens in the safe area are deleted.
	 * Player is alive and set to the middle of the screen.
	 */
	private void respawn() {
		
		double x, y;
		int frameWidth, frameHeight;
		Asteroid asteroid;
		Projectile alienProjectile;
		Alien alien;
		
		frameWidth = gui.getWidth();
		frameHeight = gui.getHeight();
		
		//Destroy asteroids in safe-area
		for (int i = asteroids.size() - 1; i >= 0; i--) {
			asteroid = asteroids.get(i);
			x = asteroid.getX() + asteroid.getWidth() / 2;
			y = asteroid.getY() + asteroid.getHeight() / 2;
			if (x > frameWidth / 3 && x < frameWidth * 2 / 3 && y > frameHeight / 3 && y < frameHeight * 2 / 3) {
				asteroids.remove(i);
			}
		}
		
		//Destroy projectiles in safe-area
		for (int i = alienProjectiles.size() - 1; i >= 0; i--) {
			alienProjectile = alienProjectiles.get(i);
			x = alienProjectile.getX() + alienProjectile.getWidth() / 2;
			y = alienProjectile.getY() + alienProjectile.getHeight() / 2;
			if (x > frameWidth / 3 && x < frameWidth * 2 / 3 && y > frameHeight / 3 && y < frameHeight * 2 / 3) {
				alienProjectiles.remove(i);
			}
		}
		
		//Destroy aliens in safe-area
		for (int i = aliens.size() - 1; i >= 0; i--) {
			alien = aliens.get(i);
			x = alien.getX() + alien.getWidth() / 2;
			y = alien.getY() + alien.getHeight() / 2;
			if (x > frameWidth / 3 && x < frameWidth * 2 / 3 && y > frameHeight / 3 && y < frameHeight * 2 / 3) {
				aliens.remove(i);
			}
		}
		
		player.setSpeed(0.0);
		player.setX(frameWidth / 2 - player.getWidth());
		player.setY(frameHeight / 2 - player.getHeight());
		player.setAlive(true);
		
	}
	
	/**
	 * A function to flag that the program should exit.
	 * pre: n/a.
	 * post: Program has been flagged to exit.
	 */
	public void exit() {
		this.exit = true;
	}
	
	/**
	 * A key has been pressed. 
	 * pre: player is not equal to null.
	 * post: 
	 * If the game was playing, the keycode was checked through this list:
	 * If the key was Up, player starts accelerating.
	 * If the key was either left or right, player starts turning. 
	 * If the key was spacebar, player starts firing.
	 * If the key was Esc, program goes to the main menu.
	 * If the key was Enter, respawn flag is set.
	 * If the game isn't playing, the keycode was checked through the menus,
	 * The main menu checks Enter, H and Esc. These characters, in order, starts the game,
	 * opens the highscore menu and exits the program.
	 * The highscore menu checks for Enter, which goes back to the main menu.
	 * The gameOver screen checks for Enter, which goes to the main menu.
	 */
	public void keyPressed(KeyEvent event) {
		// TODO Auto-generated method stub
		int code = event.getKeyCode();
		
		if(!gameOver) {
			switch(code) {
			case KeyEvent.VK_UP:
				player.setAccelerating(true);
				break;
			case KeyEvent.VK_LEFT:
				player.setTurningLeft(true);
				break;
			case KeyEvent.VK_RIGHT:
				player.setTurningRight(true);
				break;
			case KeyEvent.VK_SPACE:
				player.setFiring(true);
				break;
			case KeyEvent.VK_ESCAPE:
				gotoMainMenu = true;
				break;
			case KeyEvent.VK_ENTER:
				wantsToRespawn = true;
				break;
			}
		} else if(inGameOver) {
			switch(code) {
			case KeyEvent.VK_ENTER:
				restartGame = true;
				break;
			case KeyEvent.VK_ESCAPE:
				exit = true;
				break;
			}
		} else if(inHighScoreMenu) {
			switch(code) {
			case KeyEvent.VK_ENTER:
				inMainMenu = true;
				inHighScoreMenu = false;
				break;
			}
		} else {
			switch(code) {
			case KeyEvent.VK_ENTER:
				gameOver = false;
				inMainMenu = false;
				wantsToRespawn = true;
				break;
			case KeyEvent.VK_H:
				inMainMenu = false;
				inHighScoreMenu = true;
				break;
			case KeyEvent.VK_ESCAPE:
				exit = true;
				break;
			}
		}
		
		
	}

	/**
	 * A pressed key has been released. 
	 * pre: player is not equal to null.
	 * post: If the key was Up, player stopped accelerating.
	 * If the key was either left or right, player stopped turning. 
	 * If the key was spacebar, player stopped firing.
	 */
	public void keyReleased(KeyEvent event) {
		
		int code = event.getKeyCode();
		
		switch(code) {
			case KeyEvent.VK_UP:
				player.setAccelerating(false);
				break;
			case KeyEvent.VK_LEFT:
				player.setTurningLeft(false);
				break;
			case KeyEvent.VK_RIGHT:
				player.setTurningRight(false);
				break;
			case KeyEvent.VK_SPACE:
				player.setFiring(false);
				break;
		}
		
	}

	/**
	 * Types out the character into the gameOver highscore screen.
	 * pre: n/a
	 * post: Character has been added to the players initials String,
	 * If the character was backspace, a character has been removed.
	 * Any amount of characters less than 3 is padded with underscores.
	 */
	public void keyTyped(KeyEvent event) {
		
		char character = event.getKeyChar();
		String characterString;
		String scoreInitials;
		int length;
		
		characterString = String.valueOf(character);
		
		if(inGameOver && newHighScore != null) {
			scoreInitials = newHighScore.getInitials();
			if(characterString.matches("\b") && !scoreInitials.startsWith("_")) {
				scoreInitials = scoreInitials.replaceAll("_", "");
				length = scoreInitials.length();
				scoreInitials = scoreInitials.substring(0, length - 1);
				length = scoreInitials.length();
				for(int i = length; i < 3; i++) {
					scoreInitials += "_";
				}
				newHighScore.setInitials(scoreInitials);
				saveAndLoad.saveHighscores(highscores);
			} else if(characterString.matches("[a-zA-Z0-9]") && scoreInitials.endsWith("_")) {
				scoreInitials = scoreInitials.replaceAll("_", "");
				scoreInitials += String.valueOf(character);
				length = scoreInitials.length();
				for(int i = length; i < 3; i++) {
					scoreInitials += "_";
				}
				newHighScore.setInitials(scoreInitials);
				saveAndLoad.saveHighscores(highscores);
			}
		}
		
	}
	
	
	

}
