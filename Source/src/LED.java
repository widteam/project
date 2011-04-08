import java.util.ArrayList;

/*
* N�v: 			LED
* T�pus: 		Class
* Interfacek:	---
* Sz�l�k		DigitalObject-->Output
* 
*********** Le�r�s **********
* A bemenet megjelen�t�s�re szolg�l� objektum/oszt�ly

*/
public class LED extends Output{
	/*  ATTRIB�TUMOK  */
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

	
	
	/*	MET�DUSOK	*/
	public int Count(){
	// Le�r�s: Kisz�molja egy DigitalObject �rt�k�t	
		/* Lek�rdezz�k a bemenetek �rt�keit */
		if(wireIn == null || wireIn.isEmpty()){
			//throw ElementHasNoInputsException;
		}else{
			Value = wireIn.get(0).GetValue();
		}
		return Value;
	};					
	public boolean Step(){
	/* Le�r�s: Feladata az adott elem �rt�k�nek kisz�m�t�sa, 
	 * ill. annak eld�nt�se, hogy a DigitalObject stabil-e
	*/	
		Count();
		return true;
	};
}
