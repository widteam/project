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
		
		/**
		 * DEBUG
		 */
		System.out.println(strClassName+" ("+strClassName + strIDNumber+") has been  created automtic.");
	}
	public Wire(String strCompositName, String WireName) {
		final String strClassName = this.getClass().getName();
		if(WireName.isEmpty() || WireName==null){
			String strIDNumber = String.valueOf(WIRECounts++);
			ID = strCompositName + strIDDelimiter + strClassName + strIDDelimiter
					+ strClassName + strIDNumber;
			/**
			 * DEBUG
			 */
			System.out.println(strClassName+" ("+strClassName + strIDNumber+") has been  created automtic.");
		}else{
			ID = strCompositName + strIDDelimiter + strClassName + strIDDelimiter+ WireName;
			/**
			 * DEBUG
			 */
			System.out.println(strClassName+" ("+WireName+")has been  created by User.");
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
				// throw new WireHasMultipleInputsException;
			}
		}
		if (objectWhere != null && objectsOut != null) {
			objectsOut.add(objectWhere);
		}
		/**
		 * DEBUG
		 */
		String where ="";
		String what ="";
		if (objectWhere==null)where="NULL";else where=objectWhere.GetName();
		if (objectWhat==null)what="NULL";else what=objectWhat.GetName();
			System.out.println(what+" connected to "+where+" with "+this.GetName());
	};
	/**
	 * A Wire egyedi azonositojanak lekerdezesere szolgalo metodus.
	 * @return <i>A Wire objektum azonositoja</i>
	 */
	public String GetID() {
		return ID;
	};
	/**
	 * A Wire tipusanak lekerdezesere szolgalo metodus.
	 * @return <i>A Wire objektum tipusa</i>
	 */
	public String GetType() {
		return (ID.split("#")[1]).trim();
	}
	/**
	 * A Wire nevenek lekerdezesere szolgalo metodus.
	 * @return <i>A Wire objektum neve</i>
	 */
	public String GetName() {
		return (ID.split("#")[2]).trim();
	}
}