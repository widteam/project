
public class ExceptionWrongBoard extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6753449683100645753L;
	public DigitalObject TheObject = null;
	public ExceptionWrongBoard(DigitalObject o){
		TheObject=o;
	}
}