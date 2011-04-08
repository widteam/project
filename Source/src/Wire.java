/*
* Név: 			Wire
* Típus: 		Class
* Interfacek:	---
* Szülõk		---
* 
*********** Leírás **********
 * Az áramkörben található értékek tárolására szolgáló objektum. 
 * DigitalObject típusú objektumok között teremt kapcsolatot.

*/

/*  IMPORTOK  */
import java.util.*;

public class Wire{
	/*  ATTRIBÚTUMOK  */
	private static int WIRECounts;	// Statikus változó az egyedi ID értékhez
		
	private String ID;
	/* Leírás: Egyedi karakteres azonosító, mely egyértelmûen meghatároz 
	 * egy Wire objektumot.
	*/
	
	private int Value;
	/* Leírás: A Wire objektumok által tárolt érték. 
	 * A Gate objektumok ezek alapján számolják ki kimenetüket
	*/
	
	private List<DigitalObject> objectsIn;
	/* Leírás: DigitalObject objektum-referencia, amely a vezeték 
	 * bemenetéhez kapcsolódik.
	*/
	
	private List<DigitalObject> objectsOut;
	/* Leírás: DigitalObject objektum-referencia, amely a vezeték 
	 * kimenetéhez kapcsolódik.
	*/
	
	/*  KONSTRUKTOR  */
	public Wire(String strCompositName){
		final String strIDDelimiter = "#";
		String strIDNumber  = String.valueOf(WIRECounts++);
		final String strIDName  = this.getClass().getName();
		ID = strCompositName + strIDDelimiter + strIDName + strIDDelimiter + strIDNumber;
		
		objectsIn = new ArrayList<DigitalObject>();
		objectsOut = new ArrayList<DigitalObject>();
	}
	/*	METÓDUSOK	*/
	public String GetID(){
	// Leírás: A Wire egyedi azonosítójának lekérdezésére szolgáló metódus.
		return ID;	
	};
	public int GetValue(){
	// Leírás: A Wire objektum értékének lekérdezésére szolgáló metódus
		return Value;
	};
	public void SetValue(int NewValue){
	// Leírás: // Leírás: A Wire objektum értékének beállítására szolgáló metódus
		Value = NewValue;
	};
	public void SetConnection(DigitalObject ojectWhere, DigitalObject objectWhat){
	/* Leírás: Kapcsolatot teremt két DigitalObject között. 
	 * Beállítja a vezeték inputját (ojectWhere), illetve hozzáadja az output tömbjéhez 
	 * a DigitalObjectet (objectWhat).
	 */
		if(objectWhat != null && objectsIn != null){
			if(objectsIn.isEmpty()){
				objectsIn.add(objectWhat);
			}else {
				//throw WireHasMultipleInputsException;
			}
		}
		if(ojectWhere != null && objectsOut != null){
			objectsOut.add(ojectWhere);
		}
	};
}
