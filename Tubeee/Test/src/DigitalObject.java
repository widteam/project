/*  IMPORTOK  */
import java.util.List;
/** 
 * <table border=0>
 * 	<tr align=left>
 * 		<th>Nev:</th>
 * 		<td width=30>&nbsp;&nbsp;DigitalObject</td>
 * 	</tr>
 * 	<tr align=left>
 * 		<th>Tipus:</th>
 * 		<td>&nbsp;&nbsp;Abstract Class</td>
 * 	</tr>
 * 	<tr align=left>
 * 		<th>Interfacek: </th>
 * 		<td>&nbsp;&nbsp;---</td>
 * 	</tr>
 * 	<tr align=left>
 * 		<th>Szulok:</th>
 * 		<td>&nbsp;&nbsp;---</td>
 * </table> 
*<br>
* A Wire mellett ez a masik legfontosabb epitoeleme a digitalis
* aramkoroknek. Meghatarozzak, megjelenitik, vagy egy  belso fuggveny 
* szerint a kimenetukre csatlakozo Wire tipusu objektumok ertekeit 
* megvaltoztatjak. Minden DigitalObject tipusu objektum egyedi azonositoval
* rendelkezik.

*/

public abstract class DigitalObject {
	/* ATTRIBUTUMOK */
	public static final String strIDDelimiter = "#";
	/**
	 * Egyedi azonosito. Tartalmazza az az ot tartalmazo Composit nevet, sajat
	 * osztalyanak nevet es egy sorszamot
	 */
	protected String ID;

	/**
	 * {@link Wire} objektum-referenciakbol allo lista azon Wire objektumokrol,
	 * melyek az objektum bemeneteihez kapcsolodnak.
	 */
	protected List<Wire> wireIn;

	/**
	 * {@link Wire} objektum-referenciakbol allo lista azon Wire objektumokrol,
	 * melyek az objektum kimeneteihez kapcsolodnak.
	 */
	protected List<Wire> wireOut;

	/**
	 * Ha egy Gate egyik bemenete egy visszacsatolas kezdete, akkor tartalmaz
	 * egy Feedbacks tombot, mely referenciat tarol az osszes, az adott
	 * visszacsatolasban resztvevo DigitalObject-re
	 */
	protected List<DigitalObject> Feedbacks;

	
	/* KONSTRUKTOR */
	public DigitalObject() {
	}

	/* METODUSOK */

	/**
	 * Ha a DigitalObject kapu vagy kimenet, akkor a bemeno vezetekeirol
	 * lekerdezi az erteket, kiszamolja a logikai fuggvenyet. Ha a DigitalObject
	 * kapu vagy bemenet, akkor beallitja a kimeno vezetekeinek az erteket a
	 * korabbi szamitasi eredmenyere vagy a belso ertekere.
	 * Ha Composit, akkor kiszamolja minden belso lemenek erteket, majd a Composit
	 * PIN-jein talalhato ertekkel ter vissza
	 * 
	 * @return A kapu, composit vagy bemenet kimeneteinek aktualis ertekei listaba szervezve
	 * @throws ExceptionsWithConnection Az elemek kozotti helytelen kapcsolatokbl szarmazo hibak
	 * @throws ExceptionUnstableCircuit 
	 */
	abstract public int Count() throws ExceptionsWithConnection, ExceptionUnstableCircuit;

	/**
	 * Feladata az adott elem ertekenek kiszamitasa, ill. annak eldontese, hogy
	 * a DigitalObject stabil-e. Ezt ugy teszi, hogy kiszamolja az erteket
	 * {@link DigitalObject#Count() Count()}-tal, ha egy visszacsatolas
	 * kovetkezik a kimeneten, akkor vegigmegy a tombon es mindegyikre
	 * kiszamoltatja az adott elem erteket. Ezt megismeteli meg ketszer ugy,
	 * hogy minden alkalommal, amikor vegzett a Feedbacks tombon valo
	 * szamolassal kiszamolja az erteket ujbol es megnezi, hogy a sajat korabbi
	 * ertekevel megegyezo erteket szamolt ki. Ha a harmadik Count() utan
	 * megegyezik a ket ertek, akkor stabil a kapu es true-val ter vissza
	 * egyebkent false-al.
	 * 
	 * @return {@code true} ha stabil az aramkori elem, kulonben {@code false}

	 * @throws ExceptionUnstableCircuit Instabil aramkori visszacsatolas
	 * @throws ExceptionsWithConnection Az elemek kozotti helytelen kapcsolatokbl szarmazo hibak
	 */
	abstract public boolean Step() throws ExceptionUnstableCircuit, ExceptionsWithConnection;

	/**
	 * Hozzaadja a parameterkent kapott DigitalObjectet a Feedbacks tombjehez.
	 * 
	 * @param object
	 *            A Feedbacks tombhoz hozzaadni kivant objektum
	 */
	abstract public void AddToFeedbacks(DigitalObject object);

	/**
	 * Visszaadja az objektumok egyedi azonositojat
	 * 
	 * @return A DigitalObject azonositoja
	 */
	public String GetID() {
		return ID;
	}

	/**
	 * Visszaadja az objektumok Tipusat (Class nevet)
	 * 
	 * @return A DigitalObject Class neve
	 */
	public String GetType() {
		return (ID.split("#")[1]).trim();
	}
	/**
	 * Visszaadja az objektumok nevet(amit a felhasznalo adott meg a BHDL fajlban vagy ami generalodott)
	 * 
	 * @return A DigitalObject  neve
	 */
	public String GetName() {
		return (ID.split("#")[2]).trim();
	}
}
