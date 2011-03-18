import java.util.ArrayList;

/*
* N�v: 			GENERATOR
* T�pus: 		Class
* Interfacek:	iComponent
* Sz�l�k		DigitalObject-->Input
* 
*********** Le�r�s **********
* Speci�lis Input objektum, mely a kimenet�t ciklikusan v�ltoztatja egy,
* felhaszn�l� �ltal szerkeszthet� szekvencia alapj�n. 
* Az objektum nyilv�ntartja a szekvenci�t, illetve az aktu�lis poz�ci�t
* a mint�ban. A gener�tor Reset utas�t�s�nak h�v�s�val a poz�ci� a 
* minta elej�re �ll�that�.

*/
public class GENERATOR extends Input{
	/*  ATTRIB�TUMOK  */
	private static int GENERATORCounts;
	
	private int Frequency;
	/* Le�r�s: A gener�tor l�ptet�s�nek a gyakoris�g�t t�roljuk ebben. 
	 * Az itt megadott sz�m� Step() h�v�s ut�n fog csak l�pni.
	*/
	
	private int Sequence;
	// LE�r�s: Ebben t�roljuk a kimenetre kik�ldend� mint�t. �rtelmez�se bin�ris.
	
	private int SequencePos;
	/* Le�r�s: Az attrib�tum tartja nyilv�n az aktu�lis poz�ci�t a szekvenci�ban. 
	 * A Reset() h�v�s 0 �rt�kre �ll�tja
	*/
	
	
	/*  KONSTRUKTOR  */
	public GENERATOR(int StartFrequency, int StartSequence, Wire WiresOut){
		wireIn = null;
		wireOut = new ArrayList<Wire>();
		
		ID = "GENERATOR" + GENERATORCounts++;
		Frequency = StartFrequency;
		Sequence = StartSequence;
		SequencePos = 0;
		
		Value = 0;

	}
	
	
	/*  MET�DUSOK  */
	public void Reset(){
	// Le�r�s: A SequencePos. �rt�k�t �ll�tja alap�rtelmezettre, azaz a minta elej�re
	};
	public void SetSequence(int NewSequence){
	// Le�r�s: a Frequency �rt�k�t �ll�tja be, a param�terben megadott �rt�kre.
	};
	public void SetFrequency(int NewFrequency){
	// Le�r�s: a minta �rt�k�t �ll�tja be, a param�terben megadott �rt�kre.
	};
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
