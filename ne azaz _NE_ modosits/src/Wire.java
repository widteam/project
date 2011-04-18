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
	/* ATTRIBUTUMOK */
	private static final String strIDDelimiter = "#";
	/** Statikus valtozo az egyedi ID ertekhez */
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

	/** KONSTRUKTOR */
	public Wire(String strCompositName) {		
		String strIDNumber = String.valueOf(WIRECounts++);
		final String strClassName = this.getClass().getName();
		ID = strCompositName + strIDDelimiter + strClassName + strIDDelimiter
				+ strClassName + strIDNumber;

		objectsIn = new ArrayList<DigitalObject>();
		objectsOut = new ArrayList<DigitalObject>();
	}
	public Wire(String strCompositName, String WireName) {
		final String strClassName = this.getClass().getName();
		if(WireName.isEmpty() || WireName==null){
			String strIDNumber = String.valueOf(WIRECounts++);
			ID = strCompositName + strIDDelimiter + strClassName + strIDDelimiter
					+ strClassName + strIDNumber;
		}else{
			ID = strCompositName + strIDDelimiter + strClassName + strIDDelimiter+ WireName;
		}
		objectsIn = new ArrayList<DigitalObject>();
		objectsOut = new ArrayList<DigitalObject>();
	}

	/** METODUSOK */



	/**
	 * A Wire objektum ertekenek lekerdezesere szolgalo metodus
	 * 
	 * @param
	 * @return <i>A wire erteke</i>
	 */
	public int GetValue() {
		return Value;
	};

	/**
	 * A Wire objektum ertekenek beallitasara szolgalo metodus
	 * 
	 * @param NewValue
	 *            A kivant ertek
	 * @return
	 */
	public void SetValue(int NewValue) {
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
	 * @throws WireHasMultipleInputsException
	 *             Ha a Wire-nek egynel tobb bemenete lenne
	 */
	public void SetConnection(DigitalObject objectWhere,
			DigitalObject objectWhat) {
		if (objectWhat != null && objectsIn != null) {
			if (objectsIn.isEmpty()) {
				objectsIn.add(objectWhat);
			} else {
				// throw WireHasMultipleInputsException;
			}
		}
		if (objectWhere != null && objectsOut != null) {
			objectsOut.add(objectWhere);
		}
	};
	/**
	 * A Wire egyedi azonositojanak lekerdezesere szolgalo metodus.
	 * 
	 * @param
	 * @return <i>A Wire objektum azonositoja</i>
	 */
	public String GetID() {
		return ID;
	};
	public String GetType() {
		return (ID.split("#")[1]).trim();
	}
	public String GetName() {
		return (ID.split("#")[2]).trim();
	}
}
