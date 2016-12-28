package com.sciamlab.it.eurovoc;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.sciamlab.common.model.mdr.vocabulary.EUNamedAuthorityDataTheme;
import com.sciamlab.common.nlp.EurovocThesaurus;

public class EVoc {

	//partendo dai concetti ritornano i microthesauri (anche doppioni)
	private static final Map<String,String> OBSOLETE_CONCEPT_MAP = new HashMap<String,String>(){
		private static final long serialVersionUID = 1L;
		{
			put("181","1442"); // controllo degli alimenti --> ispezione degli alimenti
			put("5435","4620"); // ex urss --> urss
			put("5931","4778"); // ex iugoslavia --> iugoslavia
			put("5758","6128"); // ufficio comunitario dei marchi --> proprietà intellettuale

		}};

		public static HashSet<String> returnsMicrothesauri(HashSet<String> list, EurovocThesaurus thesaurusInfo){
			HashSet<String> results=new HashSet<String>();
			for(String c:list){
				if(OBSOLETE_CONCEPT_MAP.containsKey(c))
					c=OBSOLETE_CONCEPT_MAP.get(c);
				if(!thesaurusInfo.conceptToMicroThesaurusMap.containsKey(c)){
					System.out.println("-------------------------------------------------------");
					System.out.println("concetto non trovato: "+thesaurusInfo.conceptMap.get(c));
					System.out.println("il concetto è il numero: "+c);
					System.out.println("-------------------------------------------------------");
					continue;
				}
				for(String mt:thesaurusInfo.conceptToMicroThesaurusMap.get(c))
					results.add(mt);
			}
			return results;
		} 

		//mapping EUROVOC-DCAT
		//AGRI, ENER, GOVE, INTR, JUST, ECON, SOCI, EDUC, TECH, TRAN, ENVI, REGI
		@SuppressWarnings("serial")
		public static final Map<String, EUNamedAuthorityDataTheme.Theme> EUROVOC_TO_DCAT_CATEGORIES = new HashMap<String, EUNamedAuthorityDataTheme.Theme>(){{
			put("0406",EUNamedAuthorityDataTheme.Theme.GOVE); 		
			put("0411",EUNamedAuthorityDataTheme.Theme.GOVE); 		
			put("0416",EUNamedAuthorityDataTheme.Theme.GOVE); 		
			put("0421",EUNamedAuthorityDataTheme.Theme.GOVE); 		
			put("0426",EUNamedAuthorityDataTheme.Theme.GOVE); 		
			put("0431",EUNamedAuthorityDataTheme.Theme.GOVE); 		
			put("0436",EUNamedAuthorityDataTheme.Theme.GOVE); 		
			put("0806",EUNamedAuthorityDataTheme.Theme.GOVE); 		
			put("0811",EUNamedAuthorityDataTheme.Theme.INTR); 		
			put("0816",EUNamedAuthorityDataTheme.Theme.INTR); 		
			put("0821",EUNamedAuthorityDataTheme.Theme.GOVE); 		
			put("1006",EUNamedAuthorityDataTheme.Theme.GOVE); 		
			put("1011",EUNamedAuthorityDataTheme.Theme.JUST); 		
			put("1016",EUNamedAuthorityDataTheme.Theme.JUST); 		
			put("1021",EUNamedAuthorityDataTheme.Theme.ECON); 		
			put("1206",EUNamedAuthorityDataTheme.Theme.JUST); 		
			put("1211",EUNamedAuthorityDataTheme.Theme.JUST); 		
			put("1216",EUNamedAuthorityDataTheme.Theme.JUST); 		
			put("1221",EUNamedAuthorityDataTheme.Theme.JUST); 		
			put("1226",EUNamedAuthorityDataTheme.Theme.JUST); 		
			put("1231",EUNamedAuthorityDataTheme.Theme.JUST); 		
			put("1236",EUNamedAuthorityDataTheme.Theme.JUST); 		
			put("1606",EUNamedAuthorityDataTheme.Theme.ECON); 		
			put("1611",EUNamedAuthorityDataTheme.Theme.ECON); 		
			put("1616",EUNamedAuthorityDataTheme.Theme.GOVE); 		
			put("1621",EUNamedAuthorityDataTheme.Theme.ECON); 		
			put("1626",EUNamedAuthorityDataTheme.Theme.ECON); 		
			put("1631",EUNamedAuthorityDataTheme.Theme.ECON); 		
			put("2006",EUNamedAuthorityDataTheme.Theme.ECON); 		
			put("2011",EUNamedAuthorityDataTheme.Theme.ECON); 		
			put("2016",EUNamedAuthorityDataTheme.Theme.ECON); 		
			put("2021",EUNamedAuthorityDataTheme.Theme.ECON); 		
			put("2026",EUNamedAuthorityDataTheme.Theme.ECON); 		
			put("2031",EUNamedAuthorityDataTheme.Theme.ECON); 		
			put("2036",EUNamedAuthorityDataTheme.Theme.ECON); 
			put("2406",EUNamedAuthorityDataTheme.Theme.ECON); 		
			put("2411",EUNamedAuthorityDataTheme.Theme.ECON); 		
			put("2416",EUNamedAuthorityDataTheme.Theme.ECON); 		
			put("2421",EUNamedAuthorityDataTheme.Theme.ECON); 		
			put("2426",EUNamedAuthorityDataTheme.Theme.ECON); 		
			put("2431",EUNamedAuthorityDataTheme.Theme.ECON); 		
			put("2436",EUNamedAuthorityDataTheme.Theme.ECON); 		
			put("2441",EUNamedAuthorityDataTheme.Theme.ECON); 		
			put("2446",EUNamedAuthorityDataTheme.Theme.ECON); 		
			put("2451",EUNamedAuthorityDataTheme.Theme.ECON); 		
			put("2806",EUNamedAuthorityDataTheme.Theme.SOCI); 		
			put("2811",EUNamedAuthorityDataTheme.Theme.SOCI); 		
			put("2816",EUNamedAuthorityDataTheme.Theme.SOCI); 		
			put("2821",EUNamedAuthorityDataTheme.Theme.SOCI); 		
			put("2826",EUNamedAuthorityDataTheme.Theme.SOCI); 		
			put("2831",EUNamedAuthorityDataTheme.Theme.SOCI); 		
			put("2836",EUNamedAuthorityDataTheme.Theme.SOCI); 		
			put("2841",EUNamedAuthorityDataTheme.Theme.HEAL); 		
			put("2846",EUNamedAuthorityDataTheme.Theme.ENVI); 		
			put("3206",EUNamedAuthorityDataTheme.Theme.EDUC); 		
			put("3211",EUNamedAuthorityDataTheme.Theme.EDUC); 		
			put("3216",EUNamedAuthorityDataTheme.Theme.EDUC); 		
			put("3221",EUNamedAuthorityDataTheme.Theme.EDUC); 		
			put("3226",EUNamedAuthorityDataTheme.Theme.TECH); 		
			put("3231",EUNamedAuthorityDataTheme.Theme.TECH); 		
			put("3236",EUNamedAuthorityDataTheme.Theme.TECH); 		
			put("3606",EUNamedAuthorityDataTheme.Theme.TECH); 		
			put("3611",EUNamedAuthorityDataTheme.Theme.TECH); 		
			put("4006",EUNamedAuthorityDataTheme.Theme.ECON); 		
			put("4011",EUNamedAuthorityDataTheme.Theme.ECON); 		
			put("4016",EUNamedAuthorityDataTheme.Theme.JUST); 		
			put("4021",EUNamedAuthorityDataTheme.Theme.ECON); 		
			put("4026",EUNamedAuthorityDataTheme.Theme.ECON); 		
			put("4031",EUNamedAuthorityDataTheme.Theme.ECON); 		
			put("4406",EUNamedAuthorityDataTheme.Theme.SOCI); 		
			put("4411",EUNamedAuthorityDataTheme.Theme.SOCI); 		
			put("4416",EUNamedAuthorityDataTheme.Theme.SOCI); 		
			put("4421",EUNamedAuthorityDataTheme.Theme.SOCI); 		
			put("4426",EUNamedAuthorityDataTheme.Theme.JUST); 		
			put("4806",EUNamedAuthorityDataTheme.Theme.TRAN); 		
			put("4811",EUNamedAuthorityDataTheme.Theme.TRAN); 		
			put("4816",EUNamedAuthorityDataTheme.Theme.TRAN); 		
			put("4821",EUNamedAuthorityDataTheme.Theme.TRAN); 		
			put("4826",EUNamedAuthorityDataTheme.Theme.TRAN); 		
			put("5206",EUNamedAuthorityDataTheme.Theme.ENVI); 		
			put("5211",EUNamedAuthorityDataTheme.Theme.ENVI); 		
			put("5216",EUNamedAuthorityDataTheme.Theme.ENVI); 		
			put("5606",EUNamedAuthorityDataTheme.Theme.AGRI); 		
			put("5611",EUNamedAuthorityDataTheme.Theme.AGRI); 		
			put("5616",EUNamedAuthorityDataTheme.Theme.AGRI); 		
			put("5621",EUNamedAuthorityDataTheme.Theme.AGRI); 		
			put("5626",EUNamedAuthorityDataTheme.Theme.AGRI); 		
			put("5631",EUNamedAuthorityDataTheme.Theme.AGRI); 		
			put("5636",EUNamedAuthorityDataTheme.Theme.ENVI); 		
			put("5641",EUNamedAuthorityDataTheme.Theme.AGRI); 		
			put("6006",EUNamedAuthorityDataTheme.Theme.AGRI); 		
			put("6011",EUNamedAuthorityDataTheme.Theme.AGRI); 		
			put("6016",EUNamedAuthorityDataTheme.Theme.AGRI); 		
			put("6021",EUNamedAuthorityDataTheme.Theme.AGRI); 		
			put("6026",EUNamedAuthorityDataTheme.Theme.AGRI); 		
			put("6031",EUNamedAuthorityDataTheme.Theme.AGRI); 		
			put("6036",EUNamedAuthorityDataTheme.Theme.AGRI); 		
			put("6406",EUNamedAuthorityDataTheme.Theme.ECON); 		
			put("6411",EUNamedAuthorityDataTheme.Theme.TECH); 		
			put("6416",EUNamedAuthorityDataTheme.Theme.TECH); 		
			put("6606",EUNamedAuthorityDataTheme.Theme.ENER); 		
			put("6611",EUNamedAuthorityDataTheme.Theme.ENER); 		
			put("6616",EUNamedAuthorityDataTheme.Theme.ENER); 		
			put("6621",EUNamedAuthorityDataTheme.Theme.ENER); 		
			put("6626",EUNamedAuthorityDataTheme.Theme.ENER); 		
			put("6806",EUNamedAuthorityDataTheme.Theme.ECON); 		
			put("6811",EUNamedAuthorityDataTheme.Theme.ENER); 		
			put("6816",EUNamedAuthorityDataTheme.Theme.ECON); 		
			put("6821",EUNamedAuthorityDataTheme.Theme.ECON); 		
			put("6826",EUNamedAuthorityDataTheme.Theme.TECH); 		
			put("6831",EUNamedAuthorityDataTheme.Theme.JUST); 		
			put("6836",EUNamedAuthorityDataTheme.Theme.AGRI); 		
			put("6841",EUNamedAuthorityDataTheme.Theme.ECON); 		
			put("6846",EUNamedAuthorityDataTheme.Theme.ECON); 		
			put("7206",EUNamedAuthorityDataTheme.Theme.REGI); 		
			put("7211",EUNamedAuthorityDataTheme.Theme.REGI); 		
			put("7216",EUNamedAuthorityDataTheme.Theme.REGI); 		
			put("7221",EUNamedAuthorityDataTheme.Theme.REGI); 		
			put("7226",EUNamedAuthorityDataTheme.Theme.REGI); 		
			put("7231",EUNamedAuthorityDataTheme.Theme.REGI); 		
			put("7236",EUNamedAuthorityDataTheme.Theme.REGI); 		
			put("7241",EUNamedAuthorityDataTheme.Theme.REGI); 		
			put("7606",EUNamedAuthorityDataTheme.Theme.INTR); 		
			put("7611",EUNamedAuthorityDataTheme.Theme.INTR); 		
			put("7616",EUNamedAuthorityDataTheme.Theme.INTR); 		
			put("7621",EUNamedAuthorityDataTheme.Theme.INTR); 		
			put("7626",EUNamedAuthorityDataTheme.Theme.INTR); 	
		}};

		//take an eurovoc concept, returns dcat categories
		public static HashSet<String> returnsDCATCAT(HashSet<String> eurovocs, EurovocThesaurus thesaurusInfo){

			//System.out.println(eurovocs);
			HashSet<String> dcatset=new HashSet<String>();
			HashSet<String> microthesauri=new HashSet<String>();

			microthesauri.addAll(returnsMicrothesauri(eurovocs, thesaurusInfo));

			for(String mt:microthesauri){
				dcatset.add((EUROVOC_TO_DCAT_CATEGORIES.get(mt).toString()));
			}
			return dcatset;

		}

		public static void main(String[] args) {
			System.out.print("Loading thesaurus information... ");
			File f=new File("src/main/resources/eurovoc/eurovoc_xml");

			EurovocThesaurus thesaurusInfo = new EurovocThesaurus.Builder(f, "it").build();
			System.out.println("done");
			System.out.println("domainMap: "+thesaurusInfo.domainMap.size());
			System.out.println("microThesaurusMap: "+thesaurusInfo.microThesaurusMap.size());
			System.out.println("conceptMap: "+thesaurusInfo.conceptMap.size());		

			HashSet<String> hs=new HashSet<String>();
			hs.add("5158");
			hs.add("3560");
			hs.add("843");
			hs.add("3558");
			hs.add("5059");
			hs.add("394");
			hs.add("933");

			System.out.println(EVoc.returnsDCATCAT(hs, thesaurusInfo));

		}
}
