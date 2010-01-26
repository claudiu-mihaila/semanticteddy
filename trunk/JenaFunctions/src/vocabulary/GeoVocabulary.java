package vocabulary;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

public class GeoVocabulary {
	protected static final String uri = "http://www.w3.org/2003/01/geo/wgs84_pos#";
	protected static final String prefix = "geo";
	
//	private static Model model = ModelFactory.createDefaultModel();
	
	public static final String POINT = getUri() + "point";
	public static final String LATITUDE = getUri() + "lat";
	public static final String LONGITUDE = getUri() + "long";
	
	public static String getUri() {
		return uri;
	}
	public static String getPrefix() {
		return prefix;
	}

}
