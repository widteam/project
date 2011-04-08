/*
* N�v: 			Wire
* T�pus: 		Class
* Interfacek:	---
* Sz�l�k		---
* 
*********** Le�r�s **********
 * Az �ramk�rben tal�lhat� �rt�kek t�rol�s�ra szolg�l� objektum. 
 * DigitalObject t�pus� objektumok k�z�tt teremt kapcsolatot.

*/

/*  IMPORTOK  */
import java.util.*;

public class Wire{
	/*  ATTRIB�TUMOK  */
	private static int WIRECounts;	// Statikus v�ltoz� az egyedi ID �rt�khez
		
	private String ID;
	/* Le�r�s: Egyedi karakteres azonos�t�, mely egy�rtelm�en meghat�roz 
	 * egy Wire objektumot.
	*/
	
	private int Value;
	/* Le�r�s: A Wire objektumok �ltal t�rolt �rt�k. 
	 * A Gate objektumok ezek alapj�n sz�molj�k ki kimenet�ket
	*/
	
	private List<DigitalObject> objectsIn;
	/* Le�r�s: DigitalObject objektum-referencia, amely a vezet�k 
	 * bemenet�hez kapcsol�dik.
	*/
	
	private List<DigitalObject> objectsOut;
	/* Le�r�s: DigitalObject objektum-referencia, amely a vezet�k 
	 * kimenet�hez kapcsol�dik.
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
	/*	MET�DUSOK	*/
	public String GetID(){
	// Le�r�s: A Wire egyedi azonos�t�j�nak lek�rdez�s�re szolg�l� met�dus.
		return ID;	
	};
	public int GetValue(){
	// Le�r�s: A Wire objektum �rt�k�nek lek�rdez�s�re szolg�l� met�dus
		return Value;
	};
	public void SetValue(int NewValue){
	// Le�r�s: // Le�r�s: A Wire objektum �rt�k�nek be�ll�t�s�ra szolg�l� met�dus
		Value = NewValue;
	};
	public void SetConnection(DigitalObject ojectWhere, DigitalObject objectWhat){
	/* Le�r�s: Kapcsolatot teremt k�t DigitalObject k�z�tt. 
	 * Be�ll�tja a vezet�k inputj�t (ojectWhere), illetve hozz�adja az output t�mbj�hez 
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
