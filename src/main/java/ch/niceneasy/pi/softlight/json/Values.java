package ch.niceneasy.pi.softlight.json;

/**
 * The Class Values.
 */
public class Values {

	/** The red. */
	private String red;

	/** The green. */
	private String green;

	/** The blue. */
	private String blue;

	/** The timestamp. */
	private Long timestamp = System.currentTimeMillis();

	/**
	 * Gets the red.
	 *
	 * @return the red
	 */
	public String getRed() {
		return red;
	}

	/**
	 * Sets the red.
	 *
	 * @param red the new red
	 */
	public void setRed(String red) {
		this.red = red;
	}

	/**
	 * Gets the green.
	 *
	 * @return the green
	 */
	public String getGreen() {
		return green;
	}

	/**
	 * Sets the green.
	 *
	 * @param green the new green
	 */
	public void setGreen(String green) {
		this.green = green;
	}

	/**
	 * Gets the blue.
	 *
	 * @return the blue
	 */
	public String getBlue() {
		return blue;
	}

	/**
	 * Sets the blue.
	 *
	 * @param blue the new blue
	 */
	public void setBlue(String blue) {
		this.blue = blue;
	}

	/**
	 * Gets the timestamp.
	 *
	 * @return the timestamp
	 */
	public Long getTimestamp() {
		return timestamp;
	}

	/**
	 * Sets the timestamp.
	 *
	 * @param timestamp the new timestamp
	 */
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

}
