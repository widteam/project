import java.util.*;	// List, ArrayList-hez

//committok miatt külön class, végén majd bele lehet rakni egybe, de igy nem nyúlok keresztbe nektek
/**
 * Hierarchia épitése és feedback keresés célját szolgáló class.. a lényeg az egyetlen public fg, a CountHierarchy
 * 
 * 
 */
public class HierarchyCounter {
	List<DigitalObject> sources;
	List<DigitalObject> list;
	List<Wire> wires;
	ArrayList<List<DigitalObject>> components;
	
	/**
	 * A paraméter hierarchiáját adja vissza. Ha nincs még, akkor -1.
	 * 
	 * @param ennek : Ennek a kapunak/objektumnak a szintjét keressük
	 * @return : ezen a hierarchiaszinten van a paraméterként adott objektum. ha nincs, akkor -1.
	 */
	private int getSzint(DigitalObject ennek){
		int ret=-1; //ha nincs, akkor -1
		for(int i=0; i<components.size(); i++){
			if(-1!=components.get(i).indexOf(ennek)) ret=i; 
		}
		return ret;
	}
	
	/**
	 * A kapott objektumot átrakja a kért szintre
	 * 
	 * @param ezt
	 * @param ide
	 */
	private void moveToSzint(DigitalObject ezt, int ide){
		if(components.size()<ide) components.add(new ArrayList<DigitalObject>()); //ha nincs még szint+1 hierarchia
		int szint=getSzint(ezt);
		if(szint==-1) list.remove(ezt); //ha nincs a hierarchiában, a bejáratlanból vesszük ki
		else components.get(szint).remove(ezt); //ha a listában van, kivesszük
		if(components.size()==ide) components.add(new ArrayList<DigitalObject>());//ha nincs ide-edik szint, létrehozzuk.
		
		components.get(ide).add(ezt);//hozzáadjuk a kért hierarchiára
	}
	
	/**
	 * Visszaadja, hogy van e út 'from' és 'to' között (irányitottan)
	 * 
	 * @param from Az a kapu/komponens, ahonnan keressük az utat
	 * @param to Az a kapu/komponens, ahova keressük az utat
	 * @return boolean, ha van út akkor true, ha nincs akkor false
	 */
	private boolean isRoute(DigitalObject from, DigitalObject to, DigitalObject bigFrom){
		//System.out.println("isRoute"+from.ID+" "+to.ID);
		boolean ret=false;//alapból false
		for(int i=0; i<from.wireOut.size();i++){//minden kimenet wire 
			for(int j=0; j<from.wireOut.get(i).objectsOut.size();j++){ //minden kikötésére
				if(from.wireOut.get(i).objectsOut.get(j)==to) return true;//ha megvan 
				else if(from.wireOut.get(i).objectsOut.get(j)==bigFrom) return false;//ha körbeértünk másik feedback körön mint kéne
				else if(isRoute(from.wireOut.get(i).objectsOut.get(j), to, bigFrom)) return true; //vagy rekurzivan megvan
			}//j
		}//i
		return ret;
	}
	
	/**
	 * Megkeresi az utat 'from' és 'to' között, majd a 'list' végére rakja az utvonalat.
	 * Ha több úton is el lehet jutni, akkor mindkettõt berakja.
	 * 
	 * @param from Az a kapu/komponens, ahonnan keressük az utat
	 * @param to Az a kapu/komponens, ahova keressük az utat
	 * @param list Az a lista, aminek a végére az utat adjuk
	 * @return az updatelt listát adja vissza.. feedbacknek lehet használni
	 */
	private List<DigitalObject> getRoute(DigitalObject from, DigitalObject to, DigitalObject fromorig, List<DigitalObject> list){
		//System.out.println("getRoute"+from.ID+" "+to.ID);
		for(int i=0; i<from.wireOut.size();i++){ //from minden kimenet wireének 
			for(int j=0; j<from.wireOut.get(i).objectsOut.size();j++){ //minden kimenet objektumára
				if(from.wireOut.get(i).objectsOut.get(j)==to) {
					list.add(from.wireOut.get(i).objectsOut.get(j));//ha megtaláltuk to-t, akkor hozzáadjuk a listához, és done.
				}
				else if(isRoute(from.wireOut.get(i).objectsOut.get(j), to, from)){
					list.add(from.wireOut.get(i).objectsOut.get(j)); //ha nem találtuk, de ezen az ágon van út to felé, akkor feedback tömbbe adjuk, és 
					getRoute(from.wireOut.get(i).objectsOut.get(j), to, fromorig, list); //rekurzivan tovább keressük az utat
				}
			}//j
		}//i
		
		return list;
	}

	/**
	 * Kiszámolja az összes elem hierarchiáját a componentsArgon, valamint a feedbackeket is felderíti.
	 * 
	 * @param wiresArg : összeköttetések listája
	 * @param componentsArg : komponensek listája
	 */
	public void CountHierarchy(List<Wire> wiresArg, ArrayList<List<DigitalObject>> componentsArg){
		sources=new ArrayList<DigitalObject>(); //forrásokat különvesszük
		list=new ArrayList<DigitalObject>(); //bejáratlan
		wires=wiresArg;
		components=componentsArg;
				
		//main
		for(int i=0 ; i<components.size(); i++){
			for(int j=0; j<components.get(i).size();j++){
				String classs=components.get(i).get(j).getClass().toString();
				//hozzáadjuk a sourceokhoz vagy a bejáratlanokhoz
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
		//Megvan a source lista (nem komponensre), és megvan a többi elem egy másik tömbben.
		components.get(0).clear();
		for(int i=0; i<sources.size(); i++){
			components.get(0).add(sources.get(i));
		}
		//components[0], azaz sources kész.
		
		
		int szint=0;
		while(list.size()>0){
			for(int i=0; i<components.get(szint).size(); i++){ //minden forrás
				for(int j=0; j<components.get(szint).get(i).wireOut.size();j++){ //minden kimenet wireének
					for(int k=0; k<components.get(szint).get(i).wireOut.get(j).objectsOut.size();k++){ //minden kimenete
						DigitalObject current=components.get(szint).get(i).wireOut.get(j).objectsOut.get(k);
						if(-1==list.indexOf(current)){
							; //innen kivettük, és inkább külön van a feedback keresés.. de itt feedback van ám.
							
						} else {
							//ha még nem volt
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
						//ha a szint >= akkor visszacsatolás van.
						//System.out.println(components.get(i).get(j).wireIn.get(k).objectsIn.get(0).ID+getSzint(components.get(i).get(j).wireIn.get(k).objectsIn.get(0))+">="+components.get(i).get(j).ID+getSzint(components.get(i).get(j)));
						List<DigitalObject> feeds=new ArrayList<DigitalObject>(); //itt szerzett feedbackek (külön tömb, végén hozzáadjuk, hogy 8-as kezelve legyen)
						feeds=getRoute(components.get(i).get(j), components.get(i).get(j).wireIn.get(0).objectsIn.get(0), components.get(i).get(j), feeds); //kiszámoljuk a feedback tömbhöz hozzáadandó dolgokat. Ha nincs út, üres
						
						//System.out.print(feeds.size() +"feedback found from "+ components.get(i).get(j).ID + "to: ");
						for(int l=0; l<feeds.size();l++){
							//feltöltjük a feedback tömböt a feeds-el.. a 8asok miatt ez van itt..
							components.get(i).get(j).Feedbacks.add(feeds.get(l));//a most kapott feedbackeket hozzáadjuk
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
