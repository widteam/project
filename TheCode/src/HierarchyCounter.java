import java.util.*;	// List, ArrayList-hez

//committok miatt k�l�n class, v�g�n majd bele lehet rakni egybe, de igy nem ny�lok keresztbe nektek

public class HierarchyCounter {
	
	public void CountHierarchy(List<Wire> wires, ArrayList<List<iComponent>> components){
		List<iComponent> sources=new ArrayList<iComponent>();
		List<iComponent> list=new ArrayList<iComponent>();
		list.clear();
		//componentn�l m�s a source!! ez todo
		for(int i=0 ; i<components.size(); i++){
			for(int j=0; j<components.get(i).size();j++){
				String classs=components.get(i).get(j).getClass().toString();
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
			for(int i=0; i<sources.size(); i++){
				sources.get(i).....
			}
			
			szint++;
		}
	}
}
