package asteroids.io;

import java.io.Serializable;

/**
 * Score class.
 */
public class Score implements Serializable, Comparable<Object> {

	/**
	 * Score UID for IO
	 */
	private static final long serialVersionUID = 1985219L;
	private String initials;
	private Long score;

	/**
	 * A constructor
	 * pre: n/a
	 * post: Score and initials have been initialized.
	 */
	public Score(String initials, long score) {

		this.setInitials(initials);
		this.setScore(score);

	}

	/**
	 * Returns initials
	 * pre: n/a
	 * post: initials has been returned.
	 */
	public String getInitials() {
		return initials;
	}

	/**
	 * Sets initials to a new value.
	 * pre: n/a
	 * post: initials has been set.
	 */
	public void setInitials(String initials) {
		this.initials = initials;
	}

	/**
	 * Returns score
	 * pre: n/a
	 * post: score has been returned.
	 */
	public long getScore() {
		return score;
	}

	/**
	 * Sets score to a new value.
	 * pre: n/a
	 * post: Score has been set.
	 */
	public void setScore(Long score) {
		this.score = score;
	}

	/**
	 * Creates a new empty score.
	 * pre: n/a
	 * post: A score with initials of "___" and a score
	 * if 0 is returned.
	 */
	public static Score newEmptyScore() {
		return new Score("___", new Long(0L));
	}

	/**
	 * Compares this score to another score
	 * pre: o extends the score class.
	 * post: 1 is returned if this objects score is greater than the others score.
	 * 0 is returned if both scores are equal.
	 * -1 is returned otherwise.
	 */
	@Override
	public int compareTo(Object o) {

		Score otherScoreObject;
		long otherScore;

		otherScoreObject = (Score) o;
		otherScore = otherScoreObject.getScore();

		return score < otherScore ? 1 : (score == otherScore ? 0 : -1);

	}

}
