import java.util.Calendar;

class Bicycle {
	private User user;
	private String ID;
	private boolean inside;
	private Calendar admissionDate;
	

	/**
	 * Creates a new bicycle
	 * 
	 * @param user
	 *            the owner
	 * @param ID
	 *            the bicycle ID
	 */
	public Bicycle(User user, String ID) {

		this.user = user;
		this.ID = ID;

	}

	/**
	 * Compares this bicycle to another bicycle
	 * 
	 * @param bicycle
	 *            the bicycle to compare to
	 * @return true if the ID numbers match, else false
	 */
	public boolean equals(Object bicycle) {
		if (bicycle instanceof Bicycle) {
			return compareTo((Bicycle) bicycle) == 0;
		}
		return false;
	}

	/**
	 * Compares this bicycle to another bicycle
	 * 
	 * @param bicycle
	 *            the bicycle to compare to
	 * @return 0 if the ID numbers match, <0 if the ID of this bicycle is
	 *         smaller, >0 if the ID of this bicycle is bigger
	 */
	public int compareTo(Bicycle bicycle) {

		return ID.compareTo(bicycle.ID);

	}
	
	public void setAdmissionDate(Calendar date){
		admissionDate = date;
	}
	
	public Calendar getAdmissionDate(){
		return admissionDate;
	}
	
	public void setInsideGarageState(boolean inside){
		this.inside = inside;
	}
	
	public boolean getInsideGarageState(){
		return inside;
	}
			
	/**
	 * Gets the ID of this user
	 * 
	 * @return the ID of this user
	 */
	
	public String getID() {
		return ID;
	}

	/**
	 * Gets the owner of this bicycle
	 * 
	 * @return the owner
	 */
	public User getOwner() {
		return user;
	}

	public void setInside(boolean b) {
		// TODO Auto-generated method stub
		
	}

	public boolean isInside() {
		// TODO Auto-generated method stub
		return false;
	}
}
