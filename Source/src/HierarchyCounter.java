import java.util.*;	// List, ArrayList-hez

//committok miatt külön class, végén majd bele lehet rakni egybe, de igy nem nyúlok keresztbe nektek

public class HierarchyCounter {
	List<DigitalObject> sources;
	List<DigitalObject> list;
	List<Wire> wires;
	ArrayList<List<DigitalObject>> components;
	
	public int getSzint(DigitalObject ennek){
		int ret=-1;
		for(int i=0; i<components.size(); i++){
			if(-1!=components.get(i).indexOf(ennek));
		}
		return ret;
	}
	
	public void moveToSzint(DigitalObject ezt, int ide){
		if(components.size()<ide) components.add(new ArrayList<DigitalObject>()); //ha nincs még szint+1 hierarchia
		if(getSzint(ezt)<0) list.remove(ezt); //ha nincs a hierarchiában, a bejáratlanból vesszük ki
		if(components.get(getSzint(ezt)).indexOf(ezt)!=-1) components.get(getSzint(ezt)).remove(ezt); //ha a listában van, kivesszük
		components.get(ide).add(ezt);//hozzáadjuk a kért hierarchiára
	}
	
	public void foo(){}
	
	public void CountHierarchy(List<Wire> wiresArg, ArrayList<List<DigitalObject>> componentsArg){
		sources=new ArrayList<DigitalObject>(); //forrásokat különvesszük
		list=new ArrayList<DigitalObject>(); //bejáratlan
		list.clear();
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
							if(getSzint(current)<szint+1) foo(); //feed back lesz TODO
							if(getSzint(current)>=szint+1) moveToSzint(current, szint+1); //nõ a szintje
							
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
