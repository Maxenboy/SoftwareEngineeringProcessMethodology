class User {

	private String name, PIN;
	private int ID;

	/**
	 * Creates a new user
	 * 
	 * @param name
	 *            the name of the user
	 * @param PIN
	 *            the PIN code of the user
	 * @param ID
	 *            the ID number of the user
	 */
	public User(String name, String PIN, int ID) {
		this.name = name;
		this.PIN = PIN;
		this.ID = ID;
	}

	/**
	 * Compares this user to another user
	 * 
	 * @param user
	 *            the user to compare to
	 * @return true if the ID numbers match, else false
	 */
	public boolean equals(Object user) {
		if (user instanceof User) {
			return ID == ((User) user).ID;
		}
		return false;
	}

	/**
	 * Compares this user to another user
	 * 
	 * @param user
	 *            the user to compare to
	 * @return 0 if the ID numbers match, <0 if the ID of this user is smaller,
	 *         >0 if the ID of this user is bigger
	 */
	public int compareTo(User user) {

		return ID - ((User) user).ID;

	}

	/**
	 * Gets the name of this user
	 * 
	 * @return the name of this user
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the PIN of this user
	 * 
	 * @return the PIN of this user
	 */
	public String getPIN() {
		return PIN;
	}

	/**
	 * Gets the ID of this user
	 * 
	 * @return the ID of this user
	 */
	public int getID() {
		return ID;
	}

	/**
	 * Changes the name of this user
	 * 
	 * @param name
	 *            the new name of this user
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Changes the PIN code of this user
	 * 
	 * @param PIN
	 *            the new PIN code of this user
	 */
	public void setPIN(String PIN) {
		this.PIN = PIN;
	}
}
