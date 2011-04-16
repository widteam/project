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
	 * A paraméter hierarchiáját adja vissza
	 * 
	 * @param ennek : Ennek a kapunak/objektumnak a szintjét keressük
	 * @return : ezen a hierarchiaszinten van a paraméterként adott objektum
	 */
	private int getSzint(DigitalObject ennek){
		int ret=-1;
		for(int i=0; i<components.size(); i++){
			if(-1!=components.get(i).indexOf(ennek));
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
		components.get(ide).add(ezt);//hozzáadjuk a kért hierarchiára
	}
	
	/**
	 * Visszaadja, hogy van e út 'from' és 'to' között (irányitottan)
	 * 
	 * @param from Az a kapu/komponens, ahonnan keressük az utat
	 * @param to Az a kapu/komponens, ahova keressük az utat
	 * @return boolean, ha van út akkor true, ha nincs akkor false
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
	 * Megkeresi az utat 'from' és 'to' között, majd a 'list' végére rakja az utvonalat.
	 * Ha több úton is el lehet jutni, akkor mindkettõt berakja.
	 * 
	 * @param from Az a kapu/komponens, ahonnan keressük az utat
	 * @param to Az a kapu/komponens, ahova keressük az utat
	 * @param list Az a lista, aminek a végére az utat adjuk
	 * @return az updatelt listát adja vissza.. feedbacknek lehet használni
	 */
	private List<DigitalObject> getRoute(DigitalObject from, DigitalObject to, List<DigitalObject> list){
		for(int i=0; i<from.wireOut.size();i++){ //from minden kimenet wireének 
			for(int j=0; j<from.wireOut.get(i).objectsOut.size();j++){ //minden kimenet objektumára
				if(from.wireOut.get(i).objectsOut.get(j)==to) {
					list.add(from.wireOut.get(i).objectsOut.get(j));//ha megtaláltuk to-t, akkor hozzáadjuk a listához, és done.
				}
				else if(isRoute(from.wireOut.get(i).objectsOut.get(j), to)){
					list.add(from.wireOut.get(i).objectsOut.get(j)); //ha nem találtuk, de ezen az ágon van út to felé, akkor feedback tömbbe adjuk, és 
					getRoute(from.wireOut.get(i).objectsOut.get(j), to, list); //rekurzivan tovább keressük az utat
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
				
		//componentnél más a source tipusa!! ez TODO
		for(int i=0 ; i<components.size(); i++){
			for(int j=0; j<components.get(i).size();j++){
				String classs=components.get(i).get(j).getClass().toString();
				//hozzáadjuk a sourceokhoz vagy a bejáratlanokhoz
				if(classs.equals("class SWITCH") || classs.equals("class GENERATOR")) sources.add(components.get(i).get(j));
				else list.add(components.get(i).get(j));
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
							//ha már volt, 
							//a) feedback
							//b) hierarchia nõhet
							
							if(getSzint(current)<szint+1) current.Feedbacks=getRoute(components.get(szint).get(i), current, components.get(szint).get(i).Feedbacks) ;
							//ha nincs út, vagyis nem feedback, nem ad hozzá semmit. pl ha egyik lába 0, másik 5ös hierarchiájú
							//feedback számitása, hozzáadása az eddigi feedback tömbhöz
							
							
						} else {
							//ha még nem volt
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
