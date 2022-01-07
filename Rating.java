package movieSelector;

/**
 * Defines a movie rating attribute
 * @author Jeff Daffern
 *
 */
public enum Rating
{
	G("G"), PG("PG"), PG13("PG-13"), R("R"), NC17("NC-17");
	
	public final String label;
	
	private Rating(String label) {
		this.label = label;
	}
	
}
