import java.util.ArrayList;

/*
* N�v: 			GENERATOR
* T�pus: 		Class
* Interfacek:	iComponent
* Sz�l�k		DigitalObject-->Output
* 
*********** Le�r�s **********
* A bemenet megjelen�t�s�re szolg�l� objektum/oszt�ly

*/
public class LED extends Output{
	/*  ATTRIB�TUMOK  */
	private static int LEDCount;
	
	
	/*  KONSTRUKTOR  */
	public LED(Wire WireIn){
		wireOut = null;
		wireIn = new ArrayList<Wire>();
		wireIn.add(WireIn);
		ID = "LED" + LEDCount++;

	}

	
	
	/*	MET�DUSOK	*/
	public void Count(){
	// Le�r�s: Kisz�molja egy DigitalObject �rt�k�t	
	};					
	public boolean Step(){
	/* Le�r�s: Feladata az adott elem �rt�k�nek kisz�m�t�sa, 
	 * ill. annak eld�nt�se, hogy a DigitalObject stabil-e
	*/
		return true;
	};
}
