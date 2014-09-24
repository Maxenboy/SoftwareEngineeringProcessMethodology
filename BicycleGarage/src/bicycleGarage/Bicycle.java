package bicycleGarage;
import java.io.Serializable;
import java.util.Calendar;

public class Bicycle implements Comparable<Bicycle>, Serializable {
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

        /**
        * Set this bicycle's admission time to the specified time
        * @param date
        *           date which the bicycle entered the garage
        */

        public void setAdmissionDate(Calendar date){
                admissionDate = date;
        }

        /**
        * Get the time of this bicycle's last garage admittance
        * @return the time when this bicycle was last put inside the garage
        */
        
        public Calendar getAdmissionDate(){
                return admissionDate;
        }
        
        /** Sets whether this bicycle is inside the garage or not
        * @param inside true if inside, false if outside
        */

        public void setInside(boolean inside){
                this.inside = inside;
        }
        
        /**
        * Finds out whether this bicycle is inside the garage or not
        * @return true if inside, false if outside
        */

        public boolean isInside(){
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
}
