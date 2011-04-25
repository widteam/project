
public class ExceptionWrongParameter extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5800159138062632452L;
	public DigitalObject TheObject = null;
	public ExceptionWrongParameter(DigitalObject o){
		TheObject = o;
	}
}
