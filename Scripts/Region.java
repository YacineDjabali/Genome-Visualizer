/*
 * Region est la classe héritant de Abstract_region qui définit une region génomique
 * Cette classe est caractérisée par les informations suivantes :
 * <ul>
 * <li>_subregionList : Liste des subregions associées à la region (incluse dans le même intervalle)</li>
 * </ul>
 * Cette classe va permettre d'organiser (via le constructeur) et de manipuler (via les méthodes) les données relatives aux régions génomiques du fichier .gff3 .
 * </p>
 * @author YassJeanRaph
*/

import java.util.ArrayList;

public class Region extends Abstract_region
{
	private ArrayList<SubRegion> _subregionList;

    //Constructeur de la classe region
	public Region(String type, int start, int end, String eval, char strand)
	{
		super(type, start, end, eval, strand);
		this._subregionList = new ArrayList<SubRegion>();
	}

	//-----------------Méthodes----------------------
	//Méthodes retournant des attributs
	
	//@return	Toute la liste des subrégions associées à cette region (comprises dans cette région)
	public ArrayList<SubRegion> getSubRegionList()
	{
		return this._subregionList;
	}

	//@param 	Prend en argument un entier comme indice de la liste des subrégions
	//@return	Renvoie la subrégion associée à cette indice dans la liste des subrégions
	public SubRegion getSubRegionList(int index)
	{
		 return this._subregionList.get(index);
	}

	//@return	Renvoie la dernière subrégion ajoutée à la liste
	public SubRegion getLastSubRegionList()
	{
		return this._subregionList.get(_subregionList.size()-1);
	}

	//Méthodes ajoutant des éléments à certains attributs

	//@param 	Une subrégion
	// Cette méthode permet d'ajouter une subrégion à la liste des subrégions
	public void addSubRegion(SubRegion subregion_to_add)
	{
		this._subregionList.add(subregion_to_add);
	}

}
