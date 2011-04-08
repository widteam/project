import java.util.ArrayList;

/*
* Név: 			LED
* Típus: 		Class
* Interfacek:	---
* Szülõk		DigitalObject-->Output
* 
*********** Leírás **********
* A bemenet megjelenítésére szolgáló objektum/osztály

*/
public class LED extends Output{
	/*  ATTRIBÚTUMOK  */
	private static int LEDCount;
	
	
	/*  KONSTRUKTOR  */
	public LED(String strCompositName, Wire WireIn){
		final String strIDDelimiter = "#";
		String strIDNumber  = String.valueOf(LEDCount++);
		final String strIDName  = this.getClass().getName();
		ID = strCompositName + strIDDelimiter + strIDName + strIDDelimiter + strIDNumber;
			
		wireOut = null;
		wireIn = new ArrayList<Wire>();
		wireIn.add(WireIn);
	}

	
	
	/*	METÓDUSOK	*/
	public int Count(){
	// Leírás: Kiszámolja egy DigitalObject értékét	
		/* Lekérdezzük a bemenetek értékeit */
		if(wireIn == null || wireIn.isEmpty()){
			//throw ElementHasNoInputsException;
		}else{
			Value = wireIn.get(0).GetValue();
		}
		return Value;
	};					
	public boolean Step(){
	/* Leírás: Feladata az adott elem értékének kiszámítása, 
	 * ill. annak eldöntése, hogy a DigitalObject stabil-e
	*/	
		Count();
		return true;
	};
}
