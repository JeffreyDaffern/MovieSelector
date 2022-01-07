package movieSelector;

import java.io.Serializable;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * Represents a movie that has a name, length, rating, critic score, genre, and
 * description.
 * 
 * @author Jeff Daffern
 *
 */
public class Movie implements Serializable
{
	private static final long serialVersionUID = 1L;
	private String movieName, description;
	private int length, criticsRating;
	private Rating rating;
	private Genre genre;
	private boolean isWatched;
	private ImageIcon image;

	// private MovieImage movieImage;

	/**
	 * Constructor to initialize the fields
	 * 
	 * @param movieName
	 * @param description
	 * @param length
	 * @param criticsRating
	 * @param rating
	 * @param genre
	 */
	public Movie(String movieName, int length, Rating rating, int criticsRating, Genre genre, String description,
			boolean isWatched, ImageIcon image)
	{
		this.movieName = movieName;
		this.description = description;
		this.length = length;
		this.criticsRating = criticsRating;
		this.rating = rating;
		this.genre = genre;
		this.isWatched = isWatched;
		this.image = image;
	}

	/**
	 * Name of the movie
	 * 
	 * @return the movieName
	 */
	public String getMovieName()
	{
		return movieName;
	}

	/**
	 * Description of the movie
	 * 
	 * @return the description
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * Lenth of the movie
	 * 
	 * @return the length
	 */
	public int getLength()
	{
		return length;
	}

	/**
	 * Critic score for the movie
	 * 
	 * @return the criticsRating
	 */
	public int getCriticsRating()
	{
		return criticsRating;
	}

	/**
	 * Movie Rating
	 * 
	 * @return the rating
	 */
	public Rating getRating()
	{
		return rating;
	}

	/**
	 * Genre of the movie
	 * 
	 * @return the genre
	 */
	public Genre getGenre()
	{
		return genre;
	}

	public boolean isWatched()
	{
		return isWatched;
	}

	public void setWatched(boolean b)
	{
		isWatched = b;
	}

	public Icon getImage()
	{
		// TODO Auto-generated method stub
		return image;
	}

	@Override
	public String toString()
	{
		return movieName + ", " + length + ", " + rating.label + ", " + criticsRating + ", " + genre + ", "
				+ description + "," + isWatched;
	}

}
