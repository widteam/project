
public class ExceptionObjectNotFound extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8908958365995686874L;
	public Composit Container=null;
	public String ItemID="";
	ExceptionObjectNotFound(Composit c, String id){
		Container=c;
		ItemID = id;
	}
}
