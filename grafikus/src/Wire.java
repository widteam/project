/*  IMPORTOK  */
import java.util.ArrayList;
import java.util.List;
/** 
 * <table border=0>
 * 	<tr align=left>
 * 		<th>Nev:</th>
 * 		<td width=30>&nbsp;&nbsp;Wire</td>
 * 	</tr>
 * 	<tr align=left>
 * 		<th>Tipus:</th>
 * 		<td>&nbsp;&nbsp;Class</td>
 * 	</tr>
 * 	<tr align=left>
 * 		<th>Interface: </th>
 * 		<td>&nbsp;&nbsp;---</td>
 * 	</tr>
 * 	<tr align=left>
 * 		<th>Szulok:</th>
 * 		<td>&nbsp;&nbsp;---</td>
 * </table> 
 *<br>
 * Az aramkorben talalhato ertekek tarolasara szolgalo objektum. 
 * DigitalObject tipusu objektumok kozott teremt kapcsolatot.<br><br>
 */
public class Wire {	
	/** ID generalashoz szukseges konstans */
	private static final String strIDDelimiter = "#";
	/** Osztalyra jellemzo statikus tagvaltozo, a biztosan egyedi ID generalasara. */
	private static int WIRECounts;

	/**
	 * Egyedi karakteres azonosito, mely egyertelmuen meghataroz egy Wire
	 * objektumot.
	 */
	private String ID;

	/**
	 * A Wire objektumok altal tarolt ertek. A Gate objektumok ezek alapjan
	 * szamoljak ki kimenetuket
	 */
	private int Value;

	/**
	 * DigitalObject objektum-referencia, amely a vezetek bemenetehez
	 * kapcsolodik.
	 */
	protected List<DigitalObject> objectsIn;

	/**
	 * DigitalObject objektum-referencia, amely a vezetek kimenetehez
	 * kapcsolodik.
	 */
	protected List<DigitalObject> objectsOut;

	/**
	 * KONSTRUKTOR
	 * automatikusan letrehozott Wire-ekhez
	 * @param strCompositName A Wire-t tartalmazo Composit neve
	 */
	public Wire(String strCompositName) {		
		String strIDNumber = String.valueOf(WIRECounts++);
		final String strClassName = this.getClass().getName();
		ID = strCompositName + strIDDelimiter + strClassName + strIDDelimiter
				+ this.toString() + (strIDNumber);

		objectsIn = new ArrayList<DigitalObject>();
		objectsOut = new ArrayList<DigitalObject>();
		
		// LOGOLAS
		Logger.Log(Logger.log_type.DEBUG, strClassName+" ("+ID+") has been  created automtic.");
		Logger.Log(Logger.log_type.USER, "create "+this.GetName()+" ("+strClassName+").");
	}
	/**
	 * KONSTRUKTOR
	 * automatikusan letrehozott Wire-ekhez
	 * @param strCompositName A Wire-t tartalmazo Composit neve
	 * @param WireName a Wire kivant neve
	 */
	public Wire(String strCompositName, String WireName) {
		final String strClassName = this.getClass().getName();
		if(WireName.isEmpty() || WireName==null){
			String strIDNumber = String.valueOf(WIRECounts++);
			ID = strCompositName + strIDDelimiter + strClassName + strIDDelimiter
			+ this.toString() + strIDNumber;
			
			// LOGOLAS
			Logger.Log(Logger.log_type.DEBUG, strClassName+" ("+ID+") has been  created automtic.");
			Logger.Log(Logger.log_type.USER, "create "+this.GetName()+" ("+strClassName+").");
		}else{
			ID = strCompositName + strIDDelimiter + strClassName + strIDDelimiter+ WireName;
		}
		objectsIn = new ArrayList<DigitalObject>();
		objectsOut = new ArrayList<DigitalObject>();
		
		// LOGOLAS;
		Logger.Log(Logger.log_type.DEBUG, strClassName+" ("+ID+") has been  created by User.");
		Logger.Log(Logger.log_type.USER, "create "+this.GetName()+" ("+strClassName+").");
	}

	/* METODUSOK */
	/**
	 * A Wire objektum ertekenek lekerdezesere szolgalo metodus
	 * @return <i>A wire erteke</i>
	 */
	public int GetValue() {	
		// LOGOLAS;
		Logger.Log(Logger.log_type.INFO, "<"+this.GetName()+"> value is "+Value);	
		return Value;
	};
	/**
	 * A Wire objektum ertekenek lekerdezesere szolgalo metodus
	 * Ha van parametere  - barmi is legyen az erteke - nem logol.
	 * @param
	 * @return <i>A wire erteke</i>
	 */
	public int GetValue(boolean DoLog) {
		if (DoLog) {
			// LOGOLAS;
			Logger.Log(Logger.log_type.INFO, "<" + this.GetName()
					+ "> value is " + Value);
			return Value;
		} else {
			return Value;
		}
	};
	/**
	 * A Wire objektum ertekenek beallitasara szolgalo metodus
	 * 
	 * @param NewValue
	 *            A kivant ertek
	 */
	public void SetValue(int NewValue) {
		// LOGOLAS;
		Logger.Log(Logger.log_type.DEBUG, "Wire ("+this.GetID()+") set to " + NewValue);
		Value = NewValue;
	};

	/**
	 * Kapcsolatot teremt ket DigitalObject kozott. Beallitja a vezetek inputjat
	 * (ojectWhere), illetve hozzaadja az output tombjehez a DigitalObjectet
	 * (objectWhat).
	 * 
	 * @param objectWhere
	 *            {@linkplain objectsOut} listaba kerulo DigitalObect
	 * @param objectWhat
	 *            {@linkplain objectsIn} listaba kerulo DigitalObect
	 * @return
	 * @throws ExceptionWireHasMultipleInputs
	 *             Ha a Wire-nek egynel tobb bemenete lenne
	 */
	public void SetConnection(DigitalObject objectWhere,
			DigitalObject objectWhat) throws ExceptionWireHasMultipleInputs {
		if (objectWhat != null && objectsIn != null) {
			if (objectsIn.isEmpty()) {
				objectsIn.add(objectWhat);
			} else {
				 throw new ExceptionWireHasMultipleInputs(this);
			}
		}
		if (objectWhere != null && objectsOut != null) {
			objectsOut.add(objectWhere);
		}
		
		// LOGOLAS;
		String Where = "nothing";
		String What = "nothing";
		if(objectWhere!=null)Where=objectWhere.GetID();
		if(objectWhat!=null)What=objectWhat.GetID();
		Logger.Log(Logger.log_type.DEBUG, What + " added as input to " + ID + " object list");
		Logger.Log(Logger.log_type.DEBUG, Where + " added as output to " + ID + "object list");
	}
	
	/**
	 * A Wire egyedi azonositojanak lekerdezesere szolgalo metodus.
	 * @return <i>A Wire objektum azonositoja</i>
	 */
	public String GetID() {
		// LOGOLAS;
		Logger.Log(Logger.log_type.ADDITIONAL, "called Wire (" +ID+") GetID()");
		
		return ID;
	};
	/**
	 * A Wire nevenek lekerdezesere szolgalo metodus.
	 * @return <i>A Wire objektum neve. ID-bol nyert ertek!</i>
	 */
	public String GetName() {
		// LOGOLAS;
		Logger.Log(Logger.log_type.ADDITIONAL, "calles Wire (" +ID+") getName()");
		
		return (ID.split(strIDDelimiter)[2]).trim();
	}
}
