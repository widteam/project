/*  IMPORTOK  */
import java.util.*;

/** 
 * <table border=0>
 * 	<tr align=left>
 * 		<th>Nev:</th>
 * 		<td width=30>&nbsp;&nbsp;Gate</td>
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
 * 		<td>&nbsp;&nbsp;DigitalObject</td>
 * </table> 
*<br>
* Absztrakt osztaly a logikai kapuk (pl. ES, VAGY, INVERTER) leszarmaztatasara.
* A bemeneti vezetek(ek)nek lekerdezi az erteket, es kiszamolja a kimenet uj
* erteket, es be is allitja azt, az leszarmazott osztaly igazsagtablaja szerint

*/


public abstract class Gate extends DigitalObject{
	/*	ATTRIBUTUMOK	*/
	/**
	 * A legutolso ciklus ( {@code Step()} ) eredmenyet (kimeneti erteket) tarolja,
	 *  a stabilitasellenorzes celjabol ({@code Count()} metodus).
	 */
	protected int PreviousValue ;

	/**
	 * KONSTRUKTOR
	 */
	public Gate(){
		Feedbacks = new ArrayList<DigitalObject>();	// Feedback tomb inicializalva
		PreviousValue = -1;				// Don't care
	}
	
	/*	METODUSOK	*/
	
	/**
	 * Hozzaadja a parameterkent kapott {@link DigitalObject}et a Feedbacks tombjehez.
	 * @param feedback  DigitalObject tipusu objektum, ami reszt vesz egy visszacsatolasban
	 */
	public void AddToFeedbacks(DigitalObject feedback){
		if(Feedbacks != null)
			Feedbacks.add(feedback);		// Hozzaadjuk a feedbackshez	
	};	
	
	/**
	 * Egy ujabb {@link Wire} -t ad hozza a kimeneteihez
	 * @param WireToOutput a hozzaadni kivant wire objektum
	 */
	public void AddOutput(Wire WireToOutput){
		if(wireOut != null)
			wireOut.add(WireToOutput);
	};
	
}
