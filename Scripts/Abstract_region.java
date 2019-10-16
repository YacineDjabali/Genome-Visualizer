/*
 * Abstract_Region est la classe abstraite qui sert de modèle aux 3 types de region génomique : region, subregion et subsubregion
 * Cette classe est caractérisée par les informations suivantes :
 * <ul>
 * <li>_type : Le nom du type d'élément, comme "gene" ou "mobile_genetic_element".</li>
 * <li>_start : Coordonnée génomique du début de l'élément</li>
 * <li>_end : Coordonnée génomique de fin de l'élément</li>
 * <li>_strand : Caractère unique qui indique le brin codant (biologie moléculaire) de l'élément; il peut prendre les valeurs de "+" (positif ou 5 '-> 3') ou "-" (négatif ou 3 '-> 5')</li>
 * <li>_eval : Valeur numérique indiquant généralement la confiance de l'annotation de l'élément ou son score.</li>
 * <li>_info : Toutes les autres informations relatives à cet élément.</li>
 * </ul>
 * Cette classe va permettre d'organiser (via le constructeur) et de manipuler (via les méthodes) les données relatives aux régions génomiques du fichier .gff3 .
 * </p>
 * @author YassJeanRaph
*/

import java.util.Hashtable;

public abstract class Abstract_region
{
	private String _type;
	private int _start;
	private int _end;
	private char _strand;
	private String _eval;
	private Hashtable<String, String> _info;
	
	public Abstract_region(String type, int start, int end, String eval, char strand)
	{
		this._type = type;
		this._start = start;
		this._end = end;
		this._strand = strand;
		this._eval = eval;
		this._info = new Hashtable<String, String>();
	}
	
	//-----------------Méthodes----------------------
		//Méthodes retournant des attributs
		
		//@return	L'attribut _type d'une région
		public String getType()
		{
			return this._type;
		}

		//@return	L'attribut _start d'une région
		public int getStart()
		{
			return this._start;
		}

		//@return	L'attribut _end d'une région
		public int getEnd()
		{
			return this._end;
		}

		//@return	Calcule le nombre de bases d'une subsubrégion (taille), à partir du début et de la fin de celle-ci.
		public int getLength()
		{
			return (this._end - this._start + 1);
		}

		//@return	Calcule la "phase de lecture" a partir du modulo 3 de start.
		public int getFrame()
		{
			return (this._start % 3 + 1 );
		}
		
		//@return	L'attribut _strand d'une région
		public char getStrand()
		{
			return this._strand;
		}

		//@return	L'attribut _eval d'une région
		public String getEval()
		{
			return this._eval;
		}
		
		//@return	L'attribut _info d'une région sous forme d'un hash
		public Hashtable<String, String> getInfo()
		{
			return this._info;
		}

		//@param 	Prend en argument une string étant une clé du hash _info
		//@return	Renvoie la valeur associée à la clé donnée en argument
		public String getInfo(String key)
		{
			return this._info.get(key);
		}


		//Méthodes modifiant des attributs

		
		//@param 	Une chaine de caractères
		// Cette méthode permet de créer/modifier l'attribut _type manuellement
		public void setType(String new_type)
		{
			this._type = new_type;
		}


		//@param 	Un entier
		// Cette méthode permet de créer/modifier l'attribut _start manuellement
		public void setStart(int new_start)
		{
			this._start = new_start;
		}

		//@param 	Un entier
		// Cette méthode permet de créer/modifier l'attribut _end manuellement
		public void setEnd(int new_end)
		{
			this._end = new_end;
		}

		//@param 	Un caractère
		// Cette méthode permet de créer/modifier l'attribut _strand manuellement
		public void setStrand(char new_strand)
		{
			this._strand = new_strand;
		}

		//@param 	Un caractère
		// Cette méthode permet de créer/modifier l'attribut _eval manuellement
		public void setEval(String new_eval)
		{
			this._eval = new_eval;
		}
		
		//Méthodes ajoutant des éléments à certains attributs

		//@param 	Une clé et une valeur (deux chaines de caractères)
		// Cette méthode permet d'ajouter une clé et une valuer associée au tableau de hash créer pour définir l'argument _info
		public void addInfo(String key, String value)
		{
			this._info.put(key, value);
		}

}
