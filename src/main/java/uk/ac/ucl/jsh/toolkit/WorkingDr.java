package uk.ac.ucl.jsh.toolkit;

/**
 * WorkingDr holds the current working directory of Jsh shell, it follows the
 * singleton pattern
 */
public final class WorkingDr {
    /**
     * the reference to the single WorkingDr instance
     */
    private static final WorkingDr INSTANCE = new WorkingDr(System.getProperty("user.dir"));

    /**
     * the reference to the path of current working directory as a sting
     */
    private String WD;

    /**
     * constructs the single instance of WorkingDr, takes the path string and set it
     * to the current working directory
     */
    private WorkingDr(String workingDrPath) {
        this.setWD(workingDrPath);
    }

    /**
     * a methods that return the single instance of WorkingDr
     * 
     * @return WorkingDr The single instance of WorkingDr
     */
    public static WorkingDr getInstance() {
        return INSTANCE;
    }

    /**
     * a getter method to get the path of current working directory as a string
     * 
     * @return String The path of current working directory as a string
     */
    public String getWD() {
        return this.WD;
    }

    /**
     * a setter method to set new current working directory
     * 
     * @param workingDrPath The path of the new current working directory as a
     *                      string
     */
    public void setWD(String workingDrPath) {
        this.WD = workingDrPath;
    }

}