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
	 * A param�ter hierarchi�j�t adja vissza
	 * 
	 * @param ennek : Ennek a kapunak/objektumnak a szintj�t keress�k
	 * @return : ezen a hierarchiaszinten van a param�terk�nt adott objektum
	 */
	private int getSzint(DigitalObject ennek){
		int ret=-1;
		for(int i=0; i<components.size(); i++){
			if(-1!=components.get(i).indexOf(ennek));
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
		components.get(ide).add(ezt);//hozz�adjuk a k�rt hierarchi�ra
	}
	
	/**
	 * Visszaadja, hogy van e �t 'from' �s 'to' k�z�tt (ir�nyitottan)
	 * 
	 * @param from Az a kapu/komponens, ahonnan keress�k az utat
	 * @param to Az a kapu/komponens, ahova keress�k az utat
	 * @return boolean, ha van �t akkor true, ha nincs akkor false
	 */
	private boolean isRoute(DigitalObject from, DigitalObject to){
		boolean ret=false;
		for(int i=0; i<from.wireOut.size();i++){
			for(int j=0; j<from.wireOut.get(i).objectsOut.size();j++){
				if(from.wireOut.get(i).objectsOut.get(j)==to) ret=true;//ha megvan 
				else if(isRoute(from.wireOut.get(i).objectsOut.get(j), to)) ret=true; //vagy rekurzivan megvan
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
	private List<DigitalObject> getRoute(DigitalObject from, DigitalObject to, List<DigitalObject> list){
		for(int i=0; i<from.wireOut.size();i++){ //from minden kimenet wire�nek 
			for(int j=0; j<from.wireOut.get(i).objectsOut.size();j++){ //minden kimenet objektum�ra
				if(from.wireOut.get(i).objectsOut.get(j)==to) {
					list.add(from.wireOut.get(i).objectsOut.get(j));//ha megtal�ltuk to-t, akkor hozz�adjuk a list�hoz, �s done.
				}
				else if(isRoute(from.wireOut.get(i).objectsOut.get(j), to)){
					list.add(from.wireOut.get(i).objectsOut.get(j)); //ha nem tal�ltuk, de ezen az �gon van �t to fel�, akkor feedback t�mbbe adjuk, �s 
					getRoute(from.wireOut.get(i).objectsOut.get(j), to, list); //rekurzivan tov�bb keress�k az utat
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
				
		//componentn�l m�s a source tipusa!! ez TODO
		for(int i=0 ; i<components.size(); i++){
			for(int j=0; j<components.get(i).size();j++){
				String classs=components.get(i).get(j).getClass().toString();
				//hozz�adjuk a sourceokhoz vagy a bej�ratlanokhoz
				if(classs.equals("class SWITCH") || classs.equals("class GENERATOR")) sources.add(components.get(i).get(j));
				else list.add(components.get(i).get(j));
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
							//ha m�r volt, 
							//a) feedback
							//b) hierarchia n�het
							
							if(getSzint(current)<szint+1) current.Feedbacks=getRoute(components.get(szint).get(i), current, components.get(szint).get(i).Feedbacks) ;
							//ha nincs �t, vagyis nem feedback, nem ad hozz� semmit. pl ha egyik l�ba 0, m�sik 5�s hierarchi�j�
							//feedback sz�mit�sa, hozz�ad�sa az eddigi feedback t�mbh�z
							
							
						} else {
							//ha m�g nem volt
							moveToSzint(components.get(szint).get(i).wireOut.get(j).objectsOut.get(k), szint+1);
						}
					}
				}
			}
			
			szint++;
		}//while list.size>0
		
		componentsArg=components;
		
	}//countHierarchy
}//class
