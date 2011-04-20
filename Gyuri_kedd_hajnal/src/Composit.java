import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Composit extends DigitalObject {
	/* ATTRIBÚTUMOK */

	private ArrayList<List<DigitalObject>> ComponentList;
	/*
	 * Leírás: Ez az attribútum tárolja az összes kaput, kimenetet, bemenetet
	 * hierarchikus sorrendben Ez nem más, mint egy listából szervezett tömb. A
	 * tömb indexe azonosítja a hierarchia szintet (0-Források, 1-a forrásokhoz
	 * csatlakozó elemek, stb) az egyes szinteken pedig egy lista van az
	 * elemekrõl
	 */
	private List<Wire> WireList;

	// Leírás: Egyszerû lista a Wire objektumokból

	/* KONSTRUKTOR */
	public Composit(String strOwnerCompositName,String CompositName) {		
		final String strClassName = this.getClass().getName();
		ID = strOwnerCompositName + strIDDelimiter + strClassName + strIDDelimiter + CompositName;
		WireList = new ArrayList<Wire>();
		wireIn = new ArrayList<Wire>(); // Inicializaljuk a wireIn listat
		wireOut = new ArrayList<Wire>();// Inicializaljuk a wireOut listat
		ComponentList = new ArrayList<List<DigitalObject>>();
		ComponentList.add(new ArrayList<DigitalObject>());
		System.out.println("create "+CompositName+" (composit)");
	}


	/* METÓDUSOK */
	public void SetFrequency(int Frequency, String ElementID) {
		// Leírás: A paraméterben megadott azonosítójú GENERATOR objektum
		// frekvenciáját módosítja
		GENERATOR tmp;
		tmp = (GENERATOR) GetElementByID(ElementID);
		tmp.SetFrequency(Frequency);
	};

	public void SetSequence(String Sequence, String ElementID) {
		// Leírás: A paraméterben megadott azonosítójú GENERATOR objektum
		// szekvenciáját módosítja
		GENERATOR GEN_to_setsequence; /* Temporális változó */
		GEN_to_setsequence = (GENERATOR) GetElementByID(ElementID); /*
																	 * GetElemetByIDvel
																	 * megkapjuk
																	 * , az
																	 * objektumot
																	 */
		GEN_to_setsequence.SetSequence(Sequence); /*
												 * az generátor objektum
												 * SetSequence(...) metódusát
												 * meghívjuk
												 */

	};
	public void SetSwitch(int Value, String ElementID) {
		SWITCH SW_to_set; /* Temporális változó */
		SW_to_set = (SWITCH) GetElementByID(ElementID); 
		SW_to_set.Value = Value;

	};
	public void Toggle(String ElementID) {
		/*
		 * Leírás: A paraméterben megadott azonosítójú SWITCH objektum értékét
		 * az ellenkezõre állítja azáltal, hogy meghívja az objektum hasonló
		 * nevû paraméterét
		 */
		SWITCH SWITCH_to_toggle; /* Temporális változó */
		SWITCH_to_toggle = (SWITCH) GetElementByID(ElementID); /*
																 * GetElemetByIDvel
																 * megkapjuk,
																 * majd egyet
																 */
		SWITCH_to_toggle.Toggle(); /*
									 * az switch objektum Toggle() metódusát
									 * meghívjuk
									 */

	};

	@Override
	public void AddToFeedbacks(DigitalObject feedback) {
		// Leírás: Hozzáadja a paraméterként kapott DigitalObjectet a Feedbacks
		// tömbjéhez.
		if (Feedbacks != null)
			Feedbacks.add(feedback); // Hozzáadjuk a feedbackshez
	};

	public DigitalObject GetElementByID(String ElementID) {
		// Leírás: Megkeres egy adott elemet a ComponentList listákban
		
		/**
		 * Teljes ID alapjan
		 */
		if (ComponentList != null && !ComponentList.isEmpty()) {
			for (List<DigitalObject> sublist : ComponentList) {
				for (DigitalObject o : sublist) {
					if (o.ID.endsWith(ElementID))
						return  o;
				}
			}
		}
		return null;
	}
	public DigitalObject GetElementByName(String ElementName) {
		/**
		 * Nev alapjan
		 */
		if (ComponentList != null && !ComponentList.isEmpty()) {
			for (List<DigitalObject> sublist : ComponentList) {
				for (DigitalObject o : sublist) {
					if(o.GetName().equalsIgnoreCase(ElementName))
						return  o;
				}
			}
		}
		return null;
	}
	
	public Wire GetWireByID(String WireID) {
		// Leírás: Megkeres egy adott drótot a WireList listákban
		if (WireList != null && !WireList.isEmpty()) {
			for (Wire w : WireList) {
				if (w.GetID() == WireID)
					return w;
			}
		}
		return null;
	}
	public Wire GetWireByName(String WireName) {
		// Leírás: Megkeres egy adott drótot a WireList listákban
		if (WireList != null && !WireList.isEmpty()) {
			for (Wire w : WireList) {
				if(w.GetName().equalsIgnoreCase(WireName))
					return w;
			}
		}
		return null;
	}	
	public Wire ReplaceWire(Wire Mit, Wire Mivel) {
		// Leírás: Megkeres egy adott drótot a WireList listákban
		if (WireList != null && !WireList.isEmpty()) {
			for (Wire w : WireList) {
				if(w==Mit){
					return WireList.set(WireList.indexOf(w), Mivel);
				}
			}
		}
		return null;
	}
	/*
	 * Itt van egy kisebb probléma. Kívülrõl kell stepet biztosítani, de blülrõl
	 * meg step components van. Step hívjon stepcomponentset és mondja meg hogy
	 * a részobjektumok stabilak-e?
	 * 
	 * @see DigitalObject#Step()
	 */
	@Override
	public boolean Step() {
		DigitalObject obj;
		for (List<DigitalObject> sublist : ComponentList) {
			for (DigitalObject o : sublist) {
				obj = (DigitalObject) o;
				obj.Step();
				//ha main
				if(ID.startsWith("null")){
					//ha output
					if(obj.wireOut.size()==0){
						System.out.println("<"+obj.GetName()+"> value is "+obj.wireIn.get(0).GetValue());
						
					}
					//ha wire
					for(int i=0; i<obj.wireOut.size();i++){
						System.out.println("<"+obj.wireOut.get(i).GetName()+"> value is "+obj.wireOut.get(0).GetValue() );
					}//for i
				}//if
			}//sublist
		}//list digitalo
		return false;
	};

	public int Count() {
		DigitalObject obj;
		for (List<DigitalObject> sublist : ComponentList) {
			for (DigitalObject o : sublist) {
				obj = (DigitalObject) o;
				obj.Count();
				//ha main
				if(ID.startsWith("null")){
					//ha output
					if(obj.wireOut.size()==0){
						System.out.println("<"+obj.GetName()+"> value is "+obj.wireIn.get(0).GetValue());
						
					}
					//ha wire
					for(int i=0; i<obj.wireOut.size();i++){
						System.out.println("<"+obj.wireOut.get(i).GetName()+"> value is "+obj.wireOut.get(0).GetValue() );
					}//for i
				}//if
			}//sublist
		}//list digitalo
		return 0;
	}

	public void StepComponents() {
		Step();
	}

	/**
	 * Hozzaad a WireList-hez egy elemet
	 * @param wire
	 */
	public void AddToWireList(Wire wire) {
		String source = wire.GetID();
		String s;
		boolean alreadyExist=false;
		for(Wire w : WireList)
		{
			s = w.GetID();
			if(s.equalsIgnoreCase(source)) 
			{
				alreadyExist=true;
				break;
			}				
		}
		if(!alreadyExist) {
			WireList.add(wire);
			if(ID.startsWith("main"))
				System.out.println("create "+wire.GetName()+" (Wire)");
		}
	}
	/**
	 * Elvesz a WireList-bol egy elemet
	 * @param wire
	 */
	public boolean RemoveFromWireList(Wire w){
		return WireList.remove(w);
	}
	
	public void buildHierarchy(){
		HierarchyCounter cntr=new HierarchyCounter();
		cntr.CountHierarchy(WireList, ComponentList);
//		List<DigitalObject> tmp_components = this.getFirstLevelOfComponentList();
//		/*
//		 * Elso lepesben az osszes Input objektumot hozzaadjuk a Hierarchia 0.
//		 * szintjehez
//		 */
//		// 0. szint letrehozasa
//		ComponentList=new ArrayList<List<DigitalObject>>();
//		
//		ComponentList.add(new ArrayList<DigitalObject>());
//		/*
//		 * Ha van a compositban forras, azaz main
//		 */
//		for (DigitalObject obj : tmp_components) {
//			if (obj.GetType().equalsIgnoreCase("SWITCH")
//					|| obj.GetType().equalsIgnoreCase("GENERATOR")) {
//				/* Hozzadjuk a szinthez az elemet */
//				ComponentList.get(0).add(obj);
//			}
//		}
//		
//		for (DigitalObject obj : tmp_components) {
//			for(Wire a:obj.wireIn)
//				for(DigitalObject b:a.objectsIn){
//					if(b.GetType().equalsIgnoreCase("PIN")){
//						ComponentList.get(0).add(b);
//					}
//				}
//		}
//				
////		if(this.wireIn!= null){
////			for(int m=0; wireIn.size()>m; m++){
////				Wire he=new Wire("someComposite");
////				Wire hee=new Wire("someCompositeOther");
////				he.SetConnection(null, wireIn.get(m).objectsOut.get(0));
////				ComponentList.get(0).add(new PIN(he,)));
////			}
////		}
////		
//		/*
//		 * Ha a nulladik szinnel tartunk, szamolni kell a bemeno drotokkal is
//		 */
//		if(this.wireIn!= null){
//			for (Wire out : this.wireIn) {
//				/*
//				 * es az aktualis szint aktualis elemenek kimeno drotjai
//				 * altal meghatarozott elemet hozzaadjuk a kovetkezo
//				 * elemekhez
//				 */
//				for (DigitalObject wire_obj : out.objectsOut) {
//					if (!ComponentList.get(0).contains(wire_obj))
//						ComponentList.get(0).add(wire_obj);						
//				}
//			}				
//		}
//		/* Eltavolitjuk a listabol a mar hierarchikus elemeket */
//		tmp_components.removeAll(ComponentList.get(0));
//
//		/* Amig van elem a listaban */
//
//		int iHierarchy = 0;
//		while (!tmp_components.isEmpty()) {
//			List<DigitalObject> components_next = new ArrayList<DigitalObject>();
//			// Vegigmegyunk az aktualis szint osszes elemen
//			for (DigitalObject obj : ComponentList.get(iHierarchy)) {
//				// az aktualis szint aktualis elemenek kimenetein
//				for (Wire out : obj.wireOut) {
//					/*
//					 * es az aktualis szint aktualis elemenek kimeno drotjai
//					 * altal meghatarozott elemet hozzaadjuk a kovetkezo
//					 * elemekhez
//					 */
//					for (DigitalObject wire_obj : out.objectsOut) {
//						/*
//						 * Itt meg kell nezni hogy korabi szinten szerepelt-e
//						 * mar
//						 */
//						boolean contain = false;
//
//						for (int i = 0; i < iHierarchy + 1; i++) {
//							if (ComponentList.get(i).contains(wire_obj)) {
//								contain = true;
//							}
//						}
//						// HA esetleg a kapu kimentete a Composithoz csatlakozna
//						//(azaz megy ki a kulvilagba)
//						if (wire_obj == this) {
//							contain = true;
//						}
//
//
//						// ha nem volt meg korabbi szinten akkor most uj
//						// szinthez adjuk
//						if (!contain)
//							if (!components_next.contains(wire_obj))
//								components_next.add(wire_obj);
//					}
//				}
//			}
//			tmp_components.removeAll(components_next);
//			ComponentList.add(new ArrayList<DigitalObject>());
//			ComponentList.get(++iHierarchy).addAll(components_next);
//		}
	}


	public void Debug(boolean AllComponent){
		/* KIIRATAS, DEBUG */
		if(AllComponent){
			Stack<Composit> compi_stack = new Stack<Composit>();
			compi_stack.push(this);
			GetAllComposit(compi_stack);
			
			while(!compi_stack.isEmpty()){
				Composit c = compi_stack.pop();
				c.Debug(false);
			}
		}
		else{
			int szint = 0;
			for (List<DigitalObject> sublist : ComponentList) {
				System.out.println();
				System.out.print("Szint: ");
				System.out.print(szint++);
				System.out.print("\t");
				for (DigitalObject o : sublist) {
					System.out.print(o.GetID() + ", ");
					if (o.Feedbacks != null && !o.Feedbacks.isEmpty()) {
						System.out.print("FEEDBACK [");
						for (DigitalObject f_o : o.Feedbacks) {
							System.out.print(" " + f_o.ID + ", ");
						}
						System.out.print("]");
					}
				}
			}
		}
	}

	private void GetAllComposit(Stack<Composit> compi_stack){
		for (List<DigitalObject> sublist : ComponentList) {
			for (DigitalObject o : sublist) {					
				if(o.GetType().equalsIgnoreCase("Composit")){
					Composit comp = (Composit) o ;					
					compi_stack.add(comp);
					comp.GetAllComposit(compi_stack);
				}
			}
		}	
	}
	public List<DigitalObject> getFirstLevelOfComponentList(){
		return ComponentList.get(0);
	}
}