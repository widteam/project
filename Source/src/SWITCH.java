import java.util.ArrayList;

/*
* N�v: 			SWITCH
* T�pus: 		Class
* Interfacek:	---
* Sz�l�k		DigitalObject-->Input
* 
*********** Le�r�s **********
* Speci�lis Input objektum, mely a kimenet�t ciklikusan v�ltoztatja egy,
* felhaszn�l� �ltal szerkeszthet� szekvencia alapj�n. 
* Az objektum nyilv�ntartja a szekvenci�t, illetve az aktu�lis poz�ci�t
* a mint�ban. A gener�tor Reset utas�t�s�nak h�v�s�val a poz�ci� a 
* minta elej�re �ll�that�.

*/
public class SWITCH extends Input{
	/*  ATTRIB�TUMOK  */
	private static int SWITCHCount;
	
	
	/*  KONSTRUKTOR  */
	public SWITCH(String strCompositName, Wire WireOut){
		final String strIDDelimiter = "#";
		String strIDNumber  = String.valueOf(SWITCHCount++);
		final String strIDName  = this.getClass().getName();
		ID = strCompositName + strIDDelimiter + strIDName + strIDDelimiter + strIDNumber;
			
		wireIn = null;
		wireOut = new ArrayList<Wire>();
		wireOut.add(WireOut);	
		Value = 0;
	}

	
	
	/*  MET�DUSOK  */
	public void Toggle(){
	// Le�r�s: A Value v�ltoz� �rt�k�t �ll�tja null�b�l egybe, egyb�l null�ba
		if(Value == 0) Value = 1;
		else Value = 0;		
	};
	public int Count(){
	// Le�r�s: Kisz�molja egy DigitalObject �rt�k�t	
		/* Az �SSZES kimenetre kiadjuk a kisz�m�tott eredm�nyt.*/
		
		if(wireOut == null || wireOut.isEmpty()){
			// throw InputNotConnectedWarning
		}
		for(Wire OutPut:wireOut){
			OutPut.SetValue(Value);	
		}
		return Value;	
	};					
	public boolean Step(){
	/* Le�r�s: Feladata az adott elem �rt�k�nek kisz�m�t�sa, 
	 * ill. annak eld�nt�se, hogy a DigitalObject stabil-e
	 */
			Count();								// MEgh�vja a Count met�dust
			return true;							// A SWITCH mindig igazzal t�r vissza
		}
}
