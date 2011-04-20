import java.util.*;	// List, ArrayList-hez

//committok miatt k�l�n class, v�g�n majd bele lehet rakni egybe, de igy nem ny�lok keresztbe nektek
/**
 * Hierarchia �pit�se �s feedback keres�s c�lj�t szolg�l� class.. a l�nyeg az egyetlen public fg, a CountHierarchy
 * 
 * 
 */
public class HierarchyCounter {
	List<DigitalObject> sources;
	List<DigitalObject> list;
	List<Wire> wires;
	ArrayList<List<DigitalObject>> components;
	
	/**
	 * A param�ter hierarchi�j�t adja vissza. Ha nincs m�g, akkor -1.
	 * 
	 * @param ennek : Ennek a kapunak/objektumnak a szintj�t keress�k
	 * @return : ezen a hierarchiaszinten van a param�terk�nt adott objektum. ha nincs, akkor -1.
	 */
	private int getSzint(DigitalObject ennek){
		int ret=-1; //ha nincs, akkor -1
		for(int i=0; i<components.size(); i++){
			if(-1!=components.get(i).indexOf(ennek)) ret=i; 
		}
		return ret;
	}
	
	/**
	 * A kapott objektumot �trakja a k�rt szintre
	 * 
	 * @param ezt
	 * @param ide
	 */
	private void moveToSzint(DigitalObject ezt, int ide){
		if(components.size()<ide) components.add(new ArrayList<DigitalObject>()); //ha nincs m�g szint+1 hierarchia
		int szint=getSzint(ezt);
		if(szint==-1) list.remove(ezt); //ha nincs a hierarchi�ban, a bej�ratlanb�l vessz�k ki
		else components.get(szint).remove(ezt); //ha a list�ban van, kivessz�k
		if(components.size()==ide) components.add(new ArrayList<DigitalObject>());//ha nincs ide-edik szint, l�trehozzuk.
		
		components.get(ide).add(ezt);//hozz�adjuk a k�rt hierarchi�ra
	}
	
	/**
	 * Visszaadja, hogy van e �t 'from' �s 'to' k�z�tt (ir�nyitottan)
	 * 
	 * @param from Az a kapu/komponens, ahonnan keress�k az utat
	 * @param to Az a kapu/komponens, ahova keress�k az utat
	 * @return boolean, ha van �t akkor true, ha nincs akkor false
	 */
	private boolean isRoute(DigitalObject from, DigitalObject to, DigitalObject bigFrom){
		//System.out.println("isRoute"+from.ID+" "+to.ID);
		boolean ret=false;//alapb�l false
		for(int i=0; i<from.wireOut.size();i++){//minden kimenet wire 
			for(int j=0; j<from.wireOut.get(i).objectsOut.size();j++){ //minden kik�t�s�re
				if(from.wireOut.get(i).objectsOut.get(j)==to) return true;//ha megvan 
				else if(from.wireOut.get(i).objectsOut.get(j)==bigFrom) return false;//ha k�rbe�rt�nk m�sik feedback k�r�n mint k�ne
				else if(isRoute(from.wireOut.get(i).objectsOut.get(j), to, bigFrom)) return true; //vagy rekurzivan megvan
			}//j
		}//i
		return ret;
	}
	
	/**
	 * Megkeresi az utat 'from' �s 'to' k�z�tt, majd a 'list' v�g�re rakja az utvonalat.
	 * Ha t�bb �ton is el lehet jutni, akkor mindkett�t berakja.
	 * 
	 * @param from Az a kapu/komponens, ahonnan keress�k az utat
	 * @param to Az a kapu/komponens, ahova keress�k az utat
	 * @param list Az a lista, aminek a v�g�re az utat adjuk
	 * @return az updatelt list�t adja vissza.. feedbacknek lehet haszn�lni
	 */
	private List<DigitalObject> getRoute(DigitalObject from, DigitalObject to, DigitalObject fromorig, List<DigitalObject> list){
		//System.out.println("getRoute"+from.ID+" "+to.ID);
		for(int i=0; i<from.wireOut.size();i++){ //from minden kimenet wire�nek 
			for(int j=0; j<from.wireOut.get(i).objectsOut.size();j++){ //minden kimenet objektum�ra
				if(from.wireOut.get(i).objectsOut.get(j)==to) {
					list.add(from.wireOut.get(i).objectsOut.get(j));//ha megtal�ltuk to-t, akkor hozz�adjuk a list�hoz, �s done.
				}
				else if(isRoute(from.wireOut.get(i).objectsOut.get(j), to, from)){
					list.add(from.wireOut.get(i).objectsOut.get(j)); //ha nem tal�ltuk, de ezen az �gon van �t to fel�, akkor feedback t�mbbe adjuk, �s 
					getRoute(from.wireOut.get(i).objectsOut.get(j), to, fromorig, list); //rekurzivan tov�bb keress�k az utat
				}
			}//j
		}//i
		
		return list;
	}

	/**
	 * Kisz�molja az �sszes elem hierarchi�j�t a componentsArgon, valamint a feedbackeket is felder�ti.
	 * 
	 * @param wiresArg : �sszek�ttet�sek list�ja
	 * @param componentsArg : komponensek list�ja
	 */
	public void CountHierarchy(List<Wire> wiresArg, ArrayList<List<DigitalObject>> componentsArg){
		sources=new ArrayList<DigitalObject>(); //forr�sokat k�l�nvessz�k
		list=new ArrayList<DigitalObject>(); //bej�ratlan
		wires=wiresArg;
		components=componentsArg;
				
		//main
		for(int i=0 ; i<components.size(); i++){
			for(int j=0; j<components.get(i).size();j++){
				String classs=components.get(i).get(j).getClass().toString();
				//hozz�adjuk a sourceokhoz vagy a bej�ratlanokhoz
				if(classs.equals("class SWITCH") || classs.equals("class GENERATOR")) sources.add(components.get(i).get(j));
				else list.add(components.get(i).get(j));
			}
		}
		
		//embedded composit
		for (List<DigitalObject> lists : components) {
			for (DigitalObject obj : lists){
				for(Wire a:obj.wireIn){
					System.out.println(a.GetID());
					for(DigitalObject b:a.objectsIn){
						System.out.println("  "+b.GetID());
						if(b.GetType().equalsIgnoreCase("PIN")){
							sources.add(b);
						}
					}
				}
			}
		}
		
		System.out.println(list.size() + ", " +sources.size());
		//Megvan a source lista (nem komponensre), �s megvan a t�bbi elem egy m�sik t�mbben.
		components.get(0).clear();
		for(int i=0; i<sources.size(); i++){
			components.get(0).add(sources.get(i));
		}
		//components[0], azaz sources k�sz.
		
		
		int szint=0;
		while(list.size()>0){
			for(int i=0; i<components.get(szint).size(); i++){ //minden forr�s
				for(int j=0; j<components.get(szint).get(i).wireOut.size();j++){ //minden kimenet wire�nek
					for(int k=0; k<components.get(szint).get(i).wireOut.get(j).objectsOut.size();k++){ //minden kimenete
						DigitalObject current=components.get(szint).get(i).wireOut.get(j).objectsOut.get(k);
						if(-1==list.indexOf(current)){
							; //innen kivett�k, �s ink�bb k�l�n van a feedback keres�s.. de itt feedback van �m.
							
						} else {
							//ha m�g nem volt
							moveToSzint(current, szint+1);
						}
					}//k
				}//j
			}//i
			
			szint++;
		}//while list.size>0
		
		//feedback
		for(int i=0; i<components.size(); i++){ //minden szint
			for(int j=0; j<components.get(i).size();j++){ //minden eleme
				if(components.get(i).get(j).wireIn!=null) //ha nem input
				for(int k=0; k<components.get(i).get(j).wireIn.size();k++){//minden wireinje
					if(getSzint(components.get(i).get(j).wireIn.get(k).objectsIn.get(0)) >= getSzint(components.get(i).get(j))){
						//ha a szint >= akkor visszacsatol�s van.
						//System.out.println(components.get(i).get(j).wireIn.get(k).objectsIn.get(0).ID+getSzint(components.get(i).get(j).wireIn.get(k).objectsIn.get(0))+">="+components.get(i).get(j).ID+getSzint(components.get(i).get(j)));
						List<DigitalObject> feeds=new ArrayList<DigitalObject>(); //itt szerzett feedbackek (k�l�n t�mb, v�g�n hozz�adjuk, hogy 8-as kezelve legyen)
						feeds=getRoute(components.get(i).get(j), components.get(i).get(j).wireIn.get(0).objectsIn.get(0), components.get(i).get(j), feeds); //kisz�moljuk a feedback t�mbh�z hozz�adand� dolgokat. Ha nincs �t, �res
						
						//System.out.print(feeds.size() +"feedback found from "+ components.get(i).get(j).ID + "to: ");
						for(int l=0; l<feeds.size();l++){
							//felt�ltj�k a feedback t�mb�t a feeds-el.. a 8asok miatt ez van itt..
							components.get(i).get(j).Feedbacks.add(feeds.get(l));//a most kapott feedbackeket hozz�adjuk
							//System.out.print(feeds.get(l).ID+" ");
						}
						if(feeds.size()==0) components.get(i).get(j).Feedbacks.add(components.get(i).get(j).wireIn.get(k).objectsIn.get(0));
						//System.out.print(components.get(i).get(j).wireIn.get(k).objectsIn.get(0).ID+" ");
						//System.out.println();
							
					}
				}//k
			}//j
		}//i
		
		componentsArg=components; //biztos ami biztos mencsen.
		
	}//countHierarchy
}//class
