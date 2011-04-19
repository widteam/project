import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Composit extends DigitalObject {
	/**
	 * Leiras: Ez az attributum tarolja az összes kaput, kimenetet, bemenetet
	 * hierarchikus sorrendben Ez nem mas, mint egy listabol szervezett tömb. A
	 * tömb indexe azonositja a hierarchia szintet (0-Forrasok, 1-a forrasokhoz
	 * csatlakozo elemek, stb) az egyes szinteken pedig egy lista van az
	 * elemekröl
	 */
	private ArrayList<List<DigitalObject>> ComponentList;

	/**
	 * Egyszeru lista a Wire objektumokbol
	 */
	private List<Wire> WireList;

	/* KONSTRUKTOR */
	public Composit(String strOwnerCompositName,String CompositName) {		
		final String strClassName = this.getClass().getName();
		ID = strOwnerCompositName + strIDDelimiter + strClassName + strIDDelimiter + CompositName;
		WireList = new ArrayList<Wire>();
		wireIn = new ArrayList<Wire>(); // Inicializaljuk a wireIn listat
		wireOut = new ArrayList<Wire>();// Inicializaljuk a wireOut listat
		ComponentList = new ArrayList<List<DigitalObject>>();
		ComponentList.add(new ArrayList<DigitalObject>());
	}


	/* METÓDUSOK */
	public void SetFrequency(int Frequency, String ElementID) {
		// Leiras: A parameterben megadott azonositoju GENERATOR objektum
		// frekvenciajat modositja
		GENERATOR tmp;
		tmp = (GENERATOR) GetElementByID(ElementID);
		tmp.SetFrequency(Frequency);
	};

	public void SetSequence(int Sequence, String ElementID) {
		// Leiras: A parameterben megadott azonositoju GENERATOR objektum
		// szekvenciajat modositja
		GENERATOR GEN_to_setsequence; /* Temporalis valtozo */
		GEN_to_setsequence = (GENERATOR) GetElementByID(ElementID); /*
																	 * GetElemetByIDvel
																	 * megkapjuk
																	 * , az
																	 * objektumot
																	 */
		GEN_to_setsequence.SetSequence(Sequence); /*
												 * az generator objektum
												 * SetSequence(...) metodusat
												 * meghivjuk
												 */

	};
	public void SetSwitch(int Value, String ElementID) {
		SWITCH SW_to_set; /* Temporalis valtozo */
		SW_to_set = (SWITCH) GetElementByID(ElementID); 
		SW_to_set.Value = Value;

	};
	public void Toggle(String ElementID) {
		/*
		 * Leiras: A parameterben megadott azonositoju SWITCH objektum erteket
		 * az ellenkezöre allitja azaltal, hogy meghivja az objektum hasonlo
		 * nevu parameteret
		 */
		SWITCH SWITCH_to_toggle; /* Temporalis valtozo */
		SWITCH_to_toggle = (SWITCH) GetElementByID(ElementID); /*
																 * GetElemetByIDvel
																 * megkapjuk,
																 * majd egyet
																 */
		SWITCH_to_toggle.Toggle(); /*
									 * az switch objektum Toggle() metodusat
									 * meghivjuk
									 */

	};

	@Override
	public void AddToFeedbacks(DigitalObject feedback) {
		// Leiras: Hozzaadja a parameterkent kapott DigitalObjectet a Feedbacks
		// tömbjehez.
		if (Feedbacks != null)
			Feedbacks.add(feedback); // Hozzaadjuk a feedbackshez
	};

	public DigitalObject GetElementByID(String ElementID) {
		// Leiras: Megkeres egy adott elemet a ComponentList listakban
		
		/**
		 * Teljes ID alapjan
		 */
		if (ComponentList != null && !ComponentList.isEmpty()) {
			for (List<DigitalObject> sublist : ComponentList) {
				for (DigitalObject o : sublist) {
					if (o.ID == ElementID)
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
		// Leiras: Megkeres egy adott drotot a WireList listakban
		if (WireList != null && !WireList.isEmpty()) {
			for (Wire w : WireList) {
				if (w.GetID() == WireID)
					return w;
			}
		}
		return null;
	}
	public Wire GetWireByName(String WireName) {
		// Leiras: Megkeres egy adott drotot a WireList listakban
		if (WireList != null && !WireList.isEmpty()) {
			for (Wire w : WireList) {
				if(w.GetName().equalsIgnoreCase(WireName))
					return w;
			}
		}
		return null;
	}	
	public Wire ReplaceWire(Wire Mit, Wire Mivel) {
		// Leiras: Megkeres egy adott drotot a WireList listakban
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
	 * Itt van egy kisebb problema. Kivulröl kell stepet biztositani, de blulröl
	 * meg step components van. Step hivjon stepcomponentset es mondja meg hogy
	 * a reszobjektumok stabilak-e?
	 * 
	 * @see DigitalObject#Step()
	 */
	public boolean Step() {
		for(List <DigitalObject> sublist : ComponentList)
			for(DigitalObject o : sublist)
				if(!o.Step()) return false;       
		return true;
	};

	public int Count() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void StepComponents() {
		// Leiras: Meghivja az összes iComponent interfeszt megvalosito objektum
		// Step() metodusat.

		/*
		 * Elvileg mar fel van epulve a hierarchia igy nekem eleg megkapnom a
		 * ComponentListet
		 */
		DigitalObject obj;
		for (List<DigitalObject> sublist : ComponentList) {
			for (DigitalObject o : sublist) {
				obj = (DigitalObject) o;
				obj.Step();
			}
		}
	}

	/**
	 * Hozzaad a WireList-hez egy elemet
	 * @param wire
	 */
	public void AddToWireList(Wire wire) {
		WireList.add(wire);
	}
	/**
	 * Elvesz a WireList-bol egy elemet
	 * @param wire
	 */
	public boolean RemoveFromWireList(Wire w){
		return WireList.remove(w);
	}
	
	public void buildHierarchy(){
		List<DigitalObject> tmp_components = this.getFirstLevelOfComponentList();
		/*
		 * Elso lepesben az osszes Input objektumot hozzaadjuk a Hierarchia 0.
		 * szintjehez
		 */
		// 0. szint letrehozasa
		ComponentList=new ArrayList<List<DigitalObject>>();
		
		ComponentList.add(new ArrayList<DigitalObject>());
		/*
		 * Ha van a compositban forras
		 */
		for (DigitalObject obj : tmp_components) {
			if (obj.GetType().equalsIgnoreCase("SWITCH")
					|| obj.GetType().equalsIgnoreCase("GENERATOR")) {
				/* Hozzadjuk a szinthez az elemet */
				ComponentList.get(0).add(obj);
			}
		}
		/*
		 * Ha a nulladik szinnel tartunk, szamolni kell a bemeno drotokkal is
		 */
		if(this.wireIn!= null){
			for (Wire out : this.wireIn) {
				/*
				 * es az aktualis szint aktualis elemenek kimeno drotjai
				 * altal meghatarozott elemet hozzaadjuk a kovetkezo
				 * elemekhez
				 */
				for (DigitalObject wire_obj : out.objectsOut) {
					if (!ComponentList.get(0).contains(wire_obj))
						ComponentList.get(0).add(wire_obj);						
				}
			}				
		}
		/* Eltavolitjuk a listabol a mar hierarchikus elemeket */
		tmp_components.removeAll(ComponentList.get(0));

		/* Amig van elem a listaban */

		int iHierarchy = 0;
		while (!tmp_components.isEmpty()) {
			List<DigitalObject> components_next = new ArrayList<DigitalObject>();
			// Vegigmegyunk az aktualis szint osszes elemen
			for (DigitalObject obj : ComponentList.get(iHierarchy)) {
				// az aktualis szint aktualis elemenek kimenetein
				for (Wire out : obj.wireOut) {
					/*
					 * es az aktualis szint aktualis elemenek kimeno drotjai
					 * altal meghatarozott elemet hozzaadjuk a kovetkezo
					 * elemekhez
					 */
					for (DigitalObject wire_obj : out.objectsOut) {
						/*
						 * Itt meg kell nezni hogy korabi szinten szerepelt-e
						 * mar
						 */
						boolean contain = false;

						for (int i = 0; i < iHierarchy + 1; i++) {
							if (ComponentList.get(i).contains(wire_obj)) {
								contain = true;
							}
						}
						// ha nem volt meg korabbi szinten akkor most uj
						// szinthez adjuk
						if (!contain)
							if (!components_next.contains(wire_obj))
								components_next.add(wire_obj);
					}
				}
			}
			tmp_components.removeAll(components_next);
			ComponentList.add(new ArrayList<DigitalObject>());
			ComponentList.get(++iHierarchy).addAll(components_next);
		}
	}

	public void getFeedbacks() {
		// FEEDBACK
		/**
		 * Ha tartalmazza egy korabbi szint az obj elemet, akkor itt egy
		 * visszacsatolas van. Ha visszacsatolas, akkor meg FEEDBACK!
		 */
		int iHierarchy = 0;

		// vegig az osszes hierarchia szinten
		for (List<DigitalObject> sublist : ComponentList) {
			// vegig a reszlistakon
			for (DigitalObject obj : sublist) {
				// feedback inicializalasa
				DigitalObject feedback_start = null;

				/*
				 * Ha az elem egy COMPOSIT az kulon banasmodot igenyel ugyanis
				 * annak nem minden wire-jem utat jo helyre ha nem akkor mehet
				 * normalisan tehat a ket esetre kulon WIREOUT
				 */
				List<Wire> WIRESOUT = null;
				if (obj.GetType().equalsIgnoreCase("Composit")) {
					WIRESOUT = new ArrayList<Wire>();
					for (Wire w : obj.wireOut) {
						for (DigitalObject o : w.objectsIn) {
							if (o.GetType().equalsIgnoreCase("Composit")) {
								WIRESOUT.add(w);
							}
						}
					}
				} else {
					WIRESOUT = obj.wireOut;
				}
				// az aktualis elem kimeneteit nezzuk
				for (Wire out : WIRESOUT) { 
					/*
					 * Ellenoritzni kell, hogy a kimenet objektumai szerepeltek-e 
					 * mar korabbi szinten
					 */
					for (DigitalObject wire_obj : out.objectsOut) { 
						for (int i = 0; i < iHierarchy + 1; i++) {
							if (ComponentList.get(i).contains(wire_obj)) {
								/**
								 * a FEEDBACK kezdetebol elerheto elemek
								 * megtalalasa Elso lepesben a legalacsonyabb
								 * szintrol indulva felfele felepitunk egy
								 * reszgrafot. Akkor allunk meg ha feljebb
								 * elertuk az also elemet
								 */
								List<DigitalObject> tmp_list = new ArrayList<DigitalObject>();
								feedback_start = ComponentList.get(i).get(
										ComponentList.get(i).indexOf(wire_obj));
								int count = 0;

								DigitalObject listelement;
								tmp_list.add(feedback_start);
								while (!tmp_list.contains(obj)) {
									listelement = tmp_list.get(count++);
									/*
									 * Megint a gonosz Composit...
									 */
									List<Wire> CWIRESOUT = null;
									if (listelement.GetType().equalsIgnoreCase("Composit")) {
										CWIRESOUT = new ArrayList<Wire>();
										for (Wire w : listelement.wireOut) {
											for (DigitalObject o : w.objectsIn) {
												if (o.GetType().equalsIgnoreCase("Composit")) {
													CWIRESOUT.add(w);
												}
											}
										}
									} else {
										CWIRESOUT = listelement.wireOut;
									}
									for (Wire w : CWIRESOUT) {
										for (DigitalObject next_element : w.objectsOut) {
											tmp_list.add(next_element);
										}
									}
								}// reszgraf epitesenek vege

								/**
								 * a FEEDBACK vegebol visszafele elerheto elemek
								 * Ahol megegyezik a ket lista (ez es az elozo)
								 * az egy visszacsatolas
								 */
								feedback_start.Feedbacks =new ArrayList<DigitalObject>();
								feedback_start.Feedbacks.add(obj);
								count = 0;
								boolean added = false;
								while (true) {// feedback_start.Feedbacks.contains(feedback_start)){
									listelement = feedback_start.Feedbacks
											.get(count);
									if (listelement == feedback_start) {
										break;
									}
									/*
									 * Ha az elem egy COMPOSIT az kulon banasmodot igenyel
									 * ugyanis annak nem minden wire-jem utat jo helyre
									 * ha nem akkor mehet normalisan	
									 * tehat a ket esetre kulon WIREOUT	
									 */
									List<Wire> WIRESIN = null;
									if(obj.GetType().equalsIgnoreCase("Composit")){
										WIRESIN = new ArrayList<Wire>();
									for(Wire w:obj.wireIn){
											for(DigitalObject o:w.objectsOut){
												if(o.GetType().equalsIgnoreCase("Composit")){
													WIRESIN.add(w);
												}
											}
										}
									}
									else{
										WIRESIN = listelement.wireIn;
									}
									for (Wire w : WIRESIN) {
										for (DigitalObject next_element : w.objectsIn) {
											if (tmp_list.contains(next_element)) {
												feedback_start.Feedbacks
														.add(next_element);
												added = true;
											}
										}
									}
									if (added)
										count++;

								}// feedback epitesenek vege
							}// if vege: szerepelt-e korabban
						}// vege: korabbi szintek vegignezese
					}// vege: kimeno drotokon levo objektumok
				}// vege:kimeno drotok
			}// vege: sublistban az elemek
			iHierarchy++;
		}// vege: hierarchiak
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