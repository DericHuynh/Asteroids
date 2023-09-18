package asteroids.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;

import asteroids.resources.ResourceManager;

/**
 * SaveLoad class
 */
public class SaveLoad {

	private File savedHighscores;

	/**
	 * A constructor.
	 * pre: n/a
	 * post: SaveLoad object has been created.
	 * savedHighscores file has been created.
	 */
	public SaveLoad() {
		
		ResourceManager resourceManager = new ResourceManager();
		
		savedHighscores = resourceManager.GetResource("Highscores.sav");

	}

	/**
	 * Loads and returns highscores from saveHighscores file.
	 * pre: n/a
	 * post: highscores have been returned.
	 */
	public Score[] loadHighscores() {

		Score[] highscores = new Score[10];
		FileInputStream fileReader;
		ObjectInputStream objectReader;
		Score tempScore;

		if(savedHighscores.exists()) {
			try {
				fileReader = new FileInputStream(savedHighscores);
				objectReader = new ObjectInputStream(fileReader);
				for(int i = 0; i < 10; i++) {
					tempScore = (Score) objectReader.readObject();
					if(tempScore == null) {
						highscores[i] = Score.newEmptyScore();
					} else {
						highscores[i] = tempScore;
					}
				}
				fileReader.close();
				objectReader.close();
			} catch (Exception e) {
				e.printStackTrace();
				//If there's an error, make a new highscore.
				for(int i = 0; i < 10; i++) {
					highscores[i] = Score.newEmptyScore();
				}
			}
		} else {
			for(int i = 0; i < 10; i++) {
				highscores[i] = Score.newEmptyScore();
			}
		}

		return highscores;

	}

	/**
	 * Writes highscores to savedHighscores file.
	 * pre: n/a
	 * post: highscores have been written.
	 */
	public boolean saveHighscores(Score[] highscores) {

		FileOutputStream fileWriter;
		ObjectOutputStream objectWriter;

		try {
			savedHighscores.delete();
			savedHighscores.createNewFile();
			fileWriter = new FileOutputStream(savedHighscores);
			objectWriter = new ObjectOutputStream(fileWriter);
			for(Score score : highscores ) {
				if(score == null) {
					objectWriter.writeObject(Score.newEmptyScore());
				} else {
					objectWriter.writeObject(score);
				}

			}
			objectWriter.close();
			fileWriter.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;

	}

}
