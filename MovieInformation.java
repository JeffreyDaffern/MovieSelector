package movieSelector;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.border.EtchedBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MovieInformation extends JPanel
{
	private static final long serialVersionUID = 1L;
	private JTextArea txtrMovieDescription;
	private JLabel lblTitle, lblImage, lblGenre, lblRating, lblCriticScore;
	private Color contentBackground = new Color(23,27,39);
	private Color menuBackground = new Color(19,19,27);
	private JButton btnMarkWatched;
	private static boolean movieUpdated;
	
	/**
	 * Create the panel.
	 */
	public MovieInformation()
	{
		setPreferredSize(new Dimension(475, 400));
		setLayout(new BorderLayout(0, 0));

		JPanel panelDescriptions = createPanelMovieDescriptions();
		add(panelDescriptions, BorderLayout.WEST);

		JPanel panelImageAndDescription = createPanelMovie();
		add(panelImageAndDescription, BorderLayout.CENTER);
	}

	private JPanel createPanelMovie()
	{

		// Main Img Panel
		JPanel panelMovie = new JPanel();
		panelMovie.setLayout(new BoxLayout(panelMovie, BoxLayout.PAGE_AXIS));
		panelMovie.setBackground(contentBackground);
		
		// Image Container panel
		JPanel panelImage = new JPanel();
		panelImage.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		panelImage.setLayout(new BorderLayout(0, 0));
		panelImage.setBackground(contentBackground);
		
		lblTitle = new JLabel("Title");
		lblTitle.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblTitle.setForeground(Color.white);
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		panelImage.add(lblTitle, BorderLayout.NORTH);
		
		lblImage = new JLabel("");
		lblImage.setHorizontalAlignment(SwingConstants.CENTER);
		panelImage.add(lblImage, BorderLayout.CENTER);

		// Movie genre, rating, and critic score
		JPanel panelMovieInformation = new JPanel();
		panelMovieInformation.setLayout(new FlowLayout());
		panelMovieInformation.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
		panelMovieInformation.setBackground(contentBackground);
		
		lblRating = new JLabel("Rating:");
		panelMovieInformation.add(lblRating);
		panelMovieInformation.add(Box.createRigidArea(new Dimension(15, 0)));
		lblRating.setForeground(Color.white);

		lblGenre = new JLabel("Genre:");
		panelMovieInformation.add(lblGenre);
		panelMovieInformation.add(Box.createRigidArea(new Dimension(15, 0)));
		lblGenre.setForeground(Color.white);

		lblCriticScore = new JLabel("Critic Score:");
		lblCriticScore.setForeground(Color.white);

		btnMarkWatched = new JButton("Mark Watched");
		btnMarkWatched.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				movieUpdated = true;
			}
		});
		btnMarkWatched.setForeground(Color.WHITE);
		btnMarkWatched.setBorder(new EtchedBorder(EtchedBorder.LOWERED, contentBackground, Color.black));
		btnMarkWatched.setPreferredSize(new Dimension(110, 30));
		btnMarkWatched.setBackground(menuBackground);
		btnMarkWatched.setFocusPainted(false);
		
		panelMovieInformation.add(lblCriticScore);
		panelMovieInformation.add(Box.createRigidArea(new Dimension(15, 0)));
		panelMovieInformation.add(btnMarkWatched);
		
		// Movie Description
		JPanel panelMovieDescription = new JPanel();
		panelMovieDescription.setBackground(contentBackground);
		
		txtrMovieDescription = new JTextArea();
		txtrMovieDescription.setWrapStyleWord(true);
		txtrMovieDescription.setRows(6);
		txtrMovieDescription.setLineWrap(true);
		txtrMovieDescription.setPreferredSize(new Dimension(425, 50));
		txtrMovieDescription.setColumns(4);
		txtrMovieDescription.setFont(new Font("Georgia", Font.PLAIN, 11));
		txtrMovieDescription.setText("Movie Description");
		txtrMovieDescription.setBackground(menuBackground);
		txtrMovieDescription.setForeground(Color.white);
		panelMovieDescription.add(txtrMovieDescription);

		panelMovie.add(panelImage);
		panelMovie.add(panelMovieInformation);
		panelMovie.add(panelMovieDescription);

		return panelMovie;
	}

	private JPanel createPanelMovieDescriptions()
	{
		JPanel panelDescriptions = new JPanel();

		panelDescriptions.setLayout(new BorderLayout(0, 0)); 
		return panelDescriptions;
	}

	public void setMovieInfo(Movie movie)
	{
		lblTitle.setText(movie.getMovieName());
		lblGenre.setText("Genre: " + movie.getGenre());
		lblRating.setText("Rating: " + movie.getRating());
		lblCriticScore.setText("Critic Rating: " + movie.getCriticsRating());
		txtrMovieDescription.setText(movie.getDescription());
		lblImage.setIcon(movie.getImage());
	}
	public static boolean movieUpdated() {
		return movieUpdated;
	}

	public static void setMovieUpdated(boolean b)
	{
		movieUpdated = b;
	}
}
