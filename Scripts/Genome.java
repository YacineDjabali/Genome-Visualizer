/*
 * Genome est la classe qui définit l'ensemble du génome du fichier d'input gff3.
 * Cette classe est caractérisée par les informations suivantes :
 * <ul>
 * <li>_length : La taille du génome étudié.</li>
 * <li>_id : L'identifiant du génome </li>
 * <li>_url : Lien vers la page de l'espèce ou souche dont provient le génome</li>
 * <li> _regionList : Liste des régions qui composent le génome </li>
 * <li>_subregionList : Liste des sous régions qui composent le génome </li>
 * <li>_subsubregionList : Liste des sous sous régions qui composent le génome</li>
 * <li>_info : Toutes les autres informations relatives au génome.</li>
 * </ul>
 * Cette classe va permettre d'organiser (via le constructeur) et de manipuler (via les méthodes) les données génomiques du fichier .gff3 .
 * </p>
 * @author YassJeanRaph
*/


import java.util.ArrayList;
import java.util.Hashtable;


public class Genome
{
	private int _length;
	private String _id;
	private String _url;
	private ArrayList<Region> _regionList;
	private ArrayList<SubRegion> _subregionList;
	private ArrayList<SubSubRegion> _subsubregionList;
	private Hashtable<String, String> _info;

	//Constructeur de la classe Genome
	public Genome()
	{
			this._id = null;
			this._url = null;
			this._length = 0;
			this._regionList = new ArrayList<Region>();
			this._subregionList = new ArrayList<SubRegion>();
			this._subsubregionList = new ArrayList<SubSubRegion>();
			this._info = new Hashtable<String, String>();
	}

	//Surcharge du contructeur 
	public Genome(String id, int length, String url)
	{
	  	this._id = id;
	  	this._url = url;
	  	this._length = length;
	  	this._regionList = new ArrayList<Region>();
	  	this._subregionList = new ArrayList<SubRegion>();
	  	this._subsubregionList = new ArrayList<SubSubRegion>();
	  	this._info = new Hashtable<String, String>();
	}

	//-----------------Méthodes----------------------
	//Méthodes retournant des attributs

	//@return	L'attribut _id du génome
	public String getID()
	{
		return this._id;
	}

	//@return	L'attribut _url du génome	
	public String getUrl()
	{
		return this._url;
	}

	//@return	L'attribut _length du génome
	public int getLength()
	{
		return this._length;
	}


	//@param 	Prend en argument un indice (entier)
	//@return	Renvoie la région associée à cette indice dans la liste des régions
	public Region getRegionList(int index)
	{
		return this._regionList.get(index);
	}

	//@return	Renvoie la dernière region ajoutée à liste des régions
	public Region getLastRegionList()
	{
		return this._regionList.get(_regionList.size()-1);
	}

	//@return	Renvoie l'ensemble de la liste des régions
	public ArrayList<Region> getRegionList()
	{
		return this._regionList;
	}

	//@param 	Prend en argument un indice (entier)
	//@return	Renvoie la subrégion associée à cette indice dans la liste des subrégions
	public SubRegion getSubRegionList(int index)
	{
		return this._subregionList.get(index);
	}

	//@return	Renvoie la dernière subrégion ajoutée à la liste des subrégions
	public SubRegion getLastSubRegionList()
	{
		return this._subregionList.get(_subregionList.size()-1);
	}

	//@return	Renvoie l'ensemble de la liste des subrégions
	public ArrayList<SubRegion> getSubRegionList()
	{
		return this._subregionList;
	}

	//@param 	Prend en argument un indice (entier)
	//@return	Renvoie la subsubrégion associée à cette indice dans la liste des subsubrégions
	public SubSubRegion getSubSubRegionList(int index)
	{
	  return this._subsubregionList.get(index);
	}

	//@return	Renvoie la dernière subsubrégion ajoutée à la liste des subsubrégions
	public SubSubRegion getLastSubSubRegionList()
	{
		return this._subsubregionList.get(_subsubregionList.size()-1);
	}

	//@return	Renvoie l'ensemble de la liste des subsubrégions
	public ArrayList<SubSubRegion> getSubSubRegionList()
	{
	 return this._subsubregionList;
	}

	//@return	Renvoie l'attribut _info (hash) d'un génome
	public Hashtable<String, String> getInfo()
	{
	 return this._info;
	}

	//@param 	Prend en argument une clé du tableau de hash créer pour l'argument _info (string)
	//@return	Renvoie la valeur associée à la clé donnée en argument
	public String getInfo(String key)
	{
	 return this._info.get(key);
	}

	//Méthodes modifiant des attributs

	//@param	Un entier
	// Cette méthode permet de créer/modifier un nouvelle argument _length manuellement
	public void setLength(int new_length)
	{
	  this._length = new_length;
	}

	//@param 	Une chaine de caractères
	// Cette méthode permet de créer/modifier un nouvelle argument _id manuellement
	public void setID(String new_id)
	{
	  this._id = new_id;
	}

	//@param 	Une chaine de caractères
	// Cette méthode permet de créer/modifier un nouvelle argument _url manuellement
	public void setUrl(String new_url)
	{
	  this._url = new_url;
	}

	//Méthodes ajoutant des éléments aux attributs

	//@param 	Une région
	// Cette méthode permet d'ajouter une région à la liste des régions
	public void addRegion(Region region_to_add)
	{
		this._regionList.add(region_to_add);
	}

	//@param 	Une subregion
	// Cette méthode permet d'ajouter une subrégion à la liste des subrégions
	public void addSubRegion(SubRegion subregion_to_add)
	{
		this._subregionList.add(subregion_to_add);
	}

	//@param 	Une subsubregion
	// Cette méthode permet d'ajouter une subsubrégion à la liste des subsubrégions
	public void addSubSubRegion(SubSubRegion subsubregion_to_add)
	{
		this._subsubregionList.add(subsubregion_to_add);
	}

	//@param 	Une clé et une valeur (deux chaines de caractères)
	// Cette méthode permet d'ajouter une clé et une valuer associée au tableau de hash créer pour définir l'argument _info
	public void addInfo(String key, String value)
	{
		this._info.put(key, value);
	}
}
