/*
 * Subrégion est la classe héritant de Abstract_region qui définit une sous region génomique
 * Cette classe est caractérisée par les informations suivantes :
 * <ul>
 * <li>_phase : Indique la pb (0, 1 ou 2) à partir de laquelle commence réellement le premier codon
 * <li>_subsubregionList : Liste des subsubregions associées à la subregion (incluse dans le même intervalle)</li>
 * </ul>
 * Cette classe va permettre d'organiser (via le constructeur) et de manipuler (via les méthodes) les données relatives aux subrégions génomiques du fichier .gff3 .
 * </p>
 * @author YassJeanRaph
*/

import java.util.ArrayList;

public class SubRegion extends Abstract_region
{
	private char _phase;
	private ArrayList<SubSubRegion> _subsubregionList;


	//Constructeur de la classe Subregion
	public SubRegion(String type, int start, int end, String eval, char strand, char phase)
	{
		super(type, start, end, eval, strand);
		this._phase = phase;
		this._subsubregionList = new ArrayList<SubSubRegion>();
	}


	//-----------------Méthodes----------------------
	//Méthodes retournant des attributs

	//@return	L'attribut _phase d'une subrégion
	public char getPhase()
	{
		return this._phase;
	}

	//@return La liste des subsubrégions associées à une subrégion
	public ArrayList<SubSubRegion> getSubSubRegionList()
	{
		return this._subsubregionList;
	}

	//@param 	Prend en argument un entier comme indice de la liste des subsubrégions
	//@return	Renvoie la subsubrégion associée à cette indice dans la liste des subsubrégions
	public SubSubRegion getSubSubRegionList(int index)
	{
		 return this._subsubregionList.get(index);
	}

	//@return	Renvoie la dernière subsubrégion ajoutée à la liste
	public SubSubRegion getLastSubSubRegionList()
	{
		return this._subsubregionList.get(_subsubregionList.size()-1);
	}

	
	//Méthodes modifiant des attributs

	
	//@param 	Un caractère
	// Cette méthode permet de créer/modifier l'attribut _phase manuellement
	public void setPhase(char new_phase)
	{
		this._phase = new_phase;
	}


	//Méthodes ajoutant des éléments à certains attributs

	//@param 	Une subsubrégion
	// Cette méthode permet d'ajouter une subsubrégion à la liste des subsubrégions
	public void addSubSubRegion(SubSubRegion subsubregion_to_add)
	{
		this._subsubregionList.add(subsubregion_to_add);
	}

}
