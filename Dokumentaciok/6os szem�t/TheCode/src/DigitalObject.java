/*
* Nev: 			DigitalObject
* Tipus: 		Abstract Class
* Interfacek:	iComponent
* Szulok		---
* 
*********** Leiras **********
* A Wire mellett ez a masik legfontosabb epitoeleme a digitalis
* aramkoroknek. Meghatarozzak, megjelenitik, vagy egy  belso fuggveny 
* szerint a kimenetukre csatlakozo Wire tipusu objektumok ertekeit 
* megvaltoztatjak. Minden DigitalObject tipusu objektum egyedi azonositoval
* rendelkezik.

*/

/*  IMPORTOK  */
import java.util.*;

public abstract class DigitalObject implements iComponent {
	/*	ATTRIBuTUMOK	*/
	protected String ID;
	// Leiras: Egyedi azonosito. Tartalmazza az osztaly nevet es egy szamot
	
	protected List<Wire> wireIn;
	/* Leiras: Wire objektum-referenciakbol allo lista azon Wire objektumokrol, 
	 * melyek az objektum bemeneteihez kapcsolodnak.
	*/
	
	protected List<Wire> wireOut;
	/* Leiras: Wire objektum-referenciakbol allo lista azon Wire objektumokrol,
	 * melyek az objektum kimeneteihez kapcsolodnak	
	*/
	
	/*  KONSTRUKTOR  */
	public DigitalObject(){
	}
	/*  METoDUSOK  */
	public String GetID(){
	// Leiras: Visszaadja az objektumok egyedi azonositojat
		_TEST stack = new _TEST();		/* TEST */
		stack.PrintHeader(ID,"", ID + ":String");	/* TEST */		
		stack.PrintTail(ID,"", ID + ":String"); 	/* TEST */
		return null;
	}
}
