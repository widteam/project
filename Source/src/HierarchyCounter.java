import java.util.*;	// List, ArrayList-hez

//committok miatt külön class, végén majd bele lehet rakni egybe, de igy nem nyúlok keresztbe nektek

public class HierarchyCounter {
	
	public void CountHierarchy(List<Wire> wires, ArrayList<List<DigitalObject>> components){
		List<DigitalObject> sources=new ArrayList<DigitalObject>(); //forrásokat különvesszük
		List<DigitalObject> list=new ArrayList<DigitalObject>(); //bejáratlan
		list.clear();
		//componentnél más a source!! ez todo
		for(int i=0 ; i<components.size(); i++){
			for(int j=0; j<components.get(i).size();j++){
				String classs=components.get(i).get(j).getClass().toString();
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
						if(-1==list.indexOf(components.get(szint).get(i).wireOut.get(j).objectsOut.get(k))){
							//ha már volt, 
							//a) feedback
							//b) hierarchia nõhet
							
						} else {
							//ha még nem volt
							if(components.size()<szint+1) components.add(new ArrayList<DigitalObject>()); //ha nincs még szint+1 hierarchia
							components.get(szint+1).add(components.get(szint).get(i).wireOut.get(j).objectsOut.get(k));//hozzáadjuk a köv. hierarchiára
							list.remove(components.get(szint).get(i).wireOut.get(j).objectsOut.get(k)); //és levesszük a bejáratlan listáról
						}
					}
				}
			}
			
			szint++;
		}
		
	}
}
