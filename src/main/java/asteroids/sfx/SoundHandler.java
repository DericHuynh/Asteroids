package asteroids.sfx;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.FloatControl.Type;

import asteroids.resources.ResourceManager;

public class SoundHandler {

	private File musicSound;
	private File laserSound;
	private File explosionSound;

	public SoundHandler() {

		ResourceManager resourceManager = new ResourceManager();
		
		musicSound = resourceManager.GetResource("music.wav");
		laserSound = resourceManager.GetResource("laser.wav");
		explosionSound = resourceManager.GetResource("explosion.wav");

	}

	public void playMusic(float volumeDelta) {

		playSound(musicSound, volumeDelta, Clip.LOOP_CONTINUOUSLY);

	}

	public void playLaserSound(float volumeDelta) {

		playSound(laserSound, volumeDelta, 0);

	}

	public void playExplosionSound(float volumeDelta) {

		playSound(explosionSound, volumeDelta, 0);

	}

	private static void playSound(File soundFile, float volumeDelta, int loop) {
		//https://stackoverflow.com/questions/26305/how-can-i-play-sound-in-java
		try {
			Clip clip = AudioSystem.getClip();
			FloatControl floatControl;
			AudioInputStream inputStream = AudioSystem.getAudioInputStream(soundFile);
			clip.open(inputStream);
			floatControl = (FloatControl) clip.getControl(Type.MASTER_GAIN);
			floatControl.setValue(volumeDelta);
			clip.loop(loop);
			clip.start();
		} catch(Exception e) {
			e.printStackTrace();
		}

	}

}
