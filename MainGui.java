package movieSelector;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.annotation.processing.Filer;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.Component;

/**
 * @author Jeff Daffern
 *
 */

public class MainGui extends JFrame
{
	private static final long serialVersionUID = 1L;

	private String description, movieName;
	private int length, comboBoxIndex, criticsRating;
	private static int randomIndex;
	private boolean isWatched;
	private Color menuBackground = new Color(19, 19, 27);
	private JPanel contentPane;
	private MovieInformation moviePanel;
	private JTextField textAddMovieLength, textAddMovieName;
	private JComboBox<Genre> comboBoxGenre, comboBoxAddGenre;
	private JComboBox<String> comboBoxRating, comboBoxAddRating, comboBoxCriticsRating, comboBoxAddCriticsRating,
			comboBoxWatched;
	private JTextArea textAreaDescription;
	private PrintWriter movieWriter;
	private File movieFile;
	private Genre genre;
	private Rating rating;
	private static List<Movie> movies = new ArrayList<Movie>();
	private JLabel lblNotification;
	private ImageIcon image;

	/**
	 * Create the frame.
	 */
	public MainGui()
	{
		ClassLoader cl = this.getClass().getClassLoader();

		setTitle("Movie Selector");
		setIconImage(new ImageIcon(cl.getResource("img/titleIcon.png")).getImage());
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent event)
			{
				MovieSelectorApp.stopProgram();
				dispose();
			}
		});
		setBounds(100, 100, 800, 600);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		contentPane.setBackground(Color.black);
		setContentPane(contentPane);

		JPanel panelOptionsMenu = createOptionsMenu();
		contentPane.add(panelOptionsMenu, BorderLayout.WEST);

		moviePanel = new MovieInformation();
		contentPane.add(moviePanel, BorderLayout.CENTER);
	}

	public void startProgram(File file)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					movieFile = file;

					loadFromFile();
					MainGui frame = new MainGui();
					frame.setVisible(true);
				} catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}
		});
	}

	private void loadFromFile()
	{
		Scanner fileReader;
		try
		{
			fileReader = new Scanner(movieFile);

			while (fileReader.hasNext())
			{
				fileReader.useDelimiter(Pattern.compile("(\\n)|,"));
				movieName = fileReader.next().trim();
				try
				{
					length = Integer.parseInt(fileReader.next().trim());
				} catch (NumberFormatException ex)
				{
					JOptionPane.showMessageDialog(null, ex.getMessage()
							+ " Please enter a number for movie length. ex: 160. do not include minutes or hours. the format should be time in minutes.");
					System.exit(-1);
				}

				try
				{
					String temp = fileReader.next().trim();
					if (temp.length() > 2)
					{
						temp = temp.substring(0, 2) + temp.substring(3);
						rating = Rating.valueOf(temp);
					} else
						rating = Rating.valueOf(temp);
				} catch (IllegalArgumentException ex)
				{
					JOptionPane.showMessageDialog(null, ex.getMessage()
							+ " The value for the column Genre was not valid. Please enter a valid rating (G, PG, PG-13, R, NC-17");
					System.exit(-1);
				}

				try
				{
					criticsRating = Integer.parseInt(fileReader.next().trim());
				} catch (NumberFormatException ex)
				{
					JOptionPane.showMessageDialog(null, ex.getMessage()
							+ " The value for the column criticsRating was not valid. Please enter a valid critic rating. ex: 90");
					System.exit(-1);
				}

				try
				{
					genre = Genre.valueOf(fileReader.next().trim());
				} catch (IllegalArgumentException ex)
				{
					JOptionPane.showMessageDialog(null, ex.getMessage()
							+ " The value for the column Genre was not valid. Please enter a valid rating (ACTION, COMEDY, HORROR, etc.). \nThe program will close now.");
					System.exit(-1);
				}

				description = fileReader.next().trim();
				isWatched = Boolean.parseBoolean(fileReader.next().trim());

				ClassLoader cl = this.getClass().getClassLoader();
				image = new ImageIcon(cl.getResource("img/" + movieName + ".jpg"));

				movies.add(new Movie(movieName, length, rating, criticsRating, genre, description, isWatched, image));
			}
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		movieFile.delete();
	}

	private JPanel createOptionsMenu()
	{
		JPanel panelOptionsMenu = new JPanel();
		panelOptionsMenu.setPreferredSize(new Dimension(325, 400));
		panelOptionsMenu.setBorder(new EmptyBorder(10, 0, 10, 0));
		panelOptionsMenu.setLayout(new BorderLayout(0, 0));
		panelOptionsMenu.setBackground(menuBackground);

		JPanel panelFields = createPanelFields();
		JPanel panelNotification = createPanelNotification();
		JPanel panelAddMovie = createPanelAddMovie();

		panelOptionsMenu.add(panelFields, BorderLayout.NORTH);
		panelOptionsMenu.add(panelNotification, BorderLayout.CENTER);
		panelOptionsMenu.add(panelAddMovie, BorderLayout.SOUTH);

		return panelOptionsMenu;
	}

	private JPanel createPanelNotification()
	{
		JPanel panelNotification = new JPanel();
		panelNotification.setBackground(menuBackground);
		panelNotification.setLayout(new BorderLayout(0, 0));

		lblNotification = new JLabel("");
		lblNotification.setHorizontalAlignment(SwingConstants.CENTER);
		lblNotification.setForeground(Color.RED);
		panelNotification.add(lblNotification, BorderLayout.SOUTH);

		return panelNotification;
	}

	private JPanel createPanelFields()
	{
		JPanel panelFields = new JPanel();
		panelFields.setPreferredSize(new Dimension(525, 200));
		panelFields.setBorder(new EmptyBorder(10, 0, 10, 0));
		panelFields.setLayout(new BorderLayout(0, 0));
		panelFields.setBackground(menuBackground);

		JLabel lblMovieOptions = new JLabel("Random Movie Selection");
		lblMovieOptions.setBorder(new EmptyBorder(0, 0, 10, 47));
		lblMovieOptions.setForeground(Color.white);
		lblMovieOptions.setHorizontalAlignment(SwingConstants.RIGHT);

		JPanel panelTextBoxNames = createPanelTextBoxNames();
		JPanel panelTextBoxes = createPanelTextBoxes();

		panelFields.add(lblMovieOptions, BorderLayout.NORTH);
		panelFields.add(panelTextBoxNames, BorderLayout.WEST);
		panelFields.add(panelTextBoxes, BorderLayout.EAST);
		return panelFields;
	}

	private JPanel createPanelTextBoxNames()
	{
		JPanel panelTextBoxNames = new JPanel();
		panelTextBoxNames.setPreferredSize(new Dimension(100, 250));
		panelTextBoxNames.setBorder(new EmptyBorder(0, 10, 0, 0));
		panelTextBoxNames.setLayout(new GridLayout(0, 1, 0, 5));
		panelTextBoxNames.setBackground(menuBackground);

		JLabel lblMovieGenre = new JLabel("Movie Genre:");
		lblMovieGenre.setHorizontalAlignment(SwingConstants.RIGHT);
		lblMovieGenre.setForeground(Color.white);
		panelTextBoxNames.add(lblMovieGenre);

		JLabel lblMovieRating = new JLabel("Movie Rating:");
		lblMovieRating.setHorizontalAlignment(SwingConstants.RIGHT);
		lblMovieRating.setForeground(Color.white);
		panelTextBoxNames.add(lblMovieRating);

		JLabel lblCriticsRating = new JLabel("Critics Rating:");
		lblCriticsRating.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCriticsRating.setForeground(Color.white);
		panelTextBoxNames.add(lblCriticsRating);

		JLabel lblWatched = new JLabel("Watched:");
		lblWatched.setForeground(Color.WHITE);
		lblWatched.setHorizontalAlignment(SwingConstants.RIGHT);
		panelTextBoxNames.add(lblWatched);

		Component verticalStrut = Box.createVerticalStrut(20);
		panelTextBoxNames.add(verticalStrut);

		return panelTextBoxNames;
	}

	/**
	 * @return
	 */
	private JPanel createPanelTextBoxes()
	{
		JPanel panelTextBoxes = new JPanel();
		panelTextBoxes.setPreferredSize(new Dimension(225, 250));
		panelTextBoxes.setBorder(new EmptyBorder(0, 10, 0, 10));
		panelTextBoxes.setLayout(new GridLayout(0, 1, 0, 5));
		panelTextBoxes.setBackground(menuBackground);

		comboBoxGenre = new JComboBox<Genre>();
		comboBoxGenre.addItem(null);
		comboBoxGenre.addItem(Genre.ACTION);
		comboBoxGenre.addItem(Genre.ANIME);
		comboBoxGenre.addItem(Genre.COMEDY);
		comboBoxGenre.addItem(Genre.MYSTERY);
		comboBoxGenre.addItem(Genre.DRAMA);
		comboBoxGenre.addItem(Genre.FANTASY);
		comboBoxGenre.addItem(Genre.HORROR);
		comboBoxGenre.addItem(Genre.CRIME);
		comboBoxGenre.addItem(Genre.POLITICAL);
		comboBoxGenre.addItem(Genre.ROMANCE);
		comboBoxGenre.addItem(Genre.SCIFI);
		comboBoxGenre.addItem(Genre.WESTERN);
		comboBoxGenre.setSelectedItem(null);
		panelTextBoxes.add(comboBoxGenre);

		comboBoxRating = new JComboBox<String>();
		comboBoxRating.addItem(null);
		comboBoxRating.addItem(Rating.G.label);
		comboBoxRating.addItem(Rating.PG.label);
		comboBoxRating.addItem(Rating.PG13.label);
		comboBoxRating.addItem(Rating.R.label);
		comboBoxRating.addItem(Rating.NC17.label);
		comboBoxRating.setSelectedItem(null);
		panelTextBoxes.add(comboBoxRating);

		comboBoxCriticsRating = new JComboBox<String>();
		comboBoxCriticsRating.addItem(null);
		comboBoxCriticsRating.addItem("90+");
		comboBoxCriticsRating.addItem("80+");
		comboBoxCriticsRating.addItem("70+");
		comboBoxCriticsRating.addItem("60+");
		comboBoxCriticsRating.addItem("50+");
		comboBoxCriticsRating.addItem("50 and below");
		comboBoxCriticsRating.setSelectedItem(null);
		panelTextBoxes.add(comboBoxCriticsRating);

		comboBoxWatched = new JComboBox<String>();
		comboBoxWatched.addItem(null);
		comboBoxWatched.addItem("Include Watched");
		comboBoxWatched.addItem("Only Unwatched");

		JPanel panelRandomButton = new JPanel();
		panelRandomButton.setLayout(new BorderLayout(0, 0));
		panelRandomButton.setBackground(menuBackground);

		JButton btnRandomMovie = new JButton("Random Movie");
		btnRandomMovie.setPreferredSize(new Dimension(120, 50));
		panelRandomButton.add(btnRandomMovie, BorderLayout.EAST);

		btnRandomMovie.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				selectRandomMovie();
			}

		});

		panelTextBoxes.add(comboBoxWatched);
		panelTextBoxes.add(panelRandomButton);

		return panelTextBoxes;
	}

	private JPanel createPanelAddMovie()
	{
		JPanel panelAddMovie = new JPanel();
		panelAddMovie.setBorder(new EmptyBorder(0, 0, 0, 20));
		panelAddMovie.setPreferredSize(new Dimension(525, 220));
		panelAddMovie.setLayout(new BorderLayout(0, 0));
		panelAddMovie.setBackground(menuBackground);

		JPanel panelAddTextBoxNames = createPanelAddTextBoxNames();
		JPanel panelAddTextBoxes = createPanelAddTextBoxes();

		JLabel lblAddMovieTitle = new JLabel("Add A Movie To The Current List");
		lblAddMovieTitle.setBorder(new EmptyBorder(0, 0, 0, 4));
		lblAddMovieTitle.setHorizontalAlignment(SwingConstants.RIGHT);
		lblAddMovieTitle.setForeground(Color.WHITE);

		panelAddMovie.add(panelAddTextBoxNames, BorderLayout.WEST);
		panelAddMovie.add(panelAddTextBoxes, BorderLayout.EAST);
		panelAddMovie.add(lblAddMovieTitle, BorderLayout.NORTH);

		return panelAddMovie;
	}

	private JPanel createPanelAddTextBoxNames()
	{
		JPanel panelAddTextBoxNames = new JPanel();
		panelAddTextBoxNames.setPreferredSize(new Dimension(100, 200));
		panelAddTextBoxNames.setBorder(new EmptyBorder(10, 10, 0, 0));
		panelAddTextBoxNames.setLayout(new GridLayout(0, 1, 0, 5));
		panelAddTextBoxNames.setBackground(menuBackground);

		JLabel lblAddMovieName = new JLabel("Movie Name:");
		lblAddMovieName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblAddMovieName.setForeground(Color.white);
		panelAddTextBoxNames.add(lblAddMovieName);

		JLabel lblAddMovieLength = new JLabel("Movie Length:");
		lblAddMovieLength.setHorizontalAlignment(SwingConstants.RIGHT);
		lblAddMovieLength.setForeground(Color.white);
		panelAddTextBoxNames.add(lblAddMovieLength);

		JLabel lblAddMovieGenre = new JLabel("Movie Genre:");
		lblAddMovieGenre.setHorizontalAlignment(SwingConstants.RIGHT);
		lblAddMovieGenre.setForeground(Color.white);
		panelAddTextBoxNames.add(lblAddMovieGenre);

		JLabel lblAddMovieRating = new JLabel("Movie Rating:");
		lblAddMovieRating.setHorizontalAlignment(SwingConstants.RIGHT);
		lblAddMovieRating.setForeground(Color.white);
		panelAddTextBoxNames.add(lblAddMovieRating);

		JLabel lblAddCriticsRating = new JLabel("Critics Rating:");
		lblAddCriticsRating.setHorizontalAlignment(SwingConstants.RIGHT);
		lblAddCriticsRating.setForeground(Color.white);
		panelAddTextBoxNames.add(lblAddCriticsRating);

		JLabel lblAddDescription = new JLabel("Description:");
		lblAddDescription.setHorizontalAlignment(SwingConstants.RIGHT);
		lblAddDescription.setForeground(Color.white);
		panelAddTextBoxNames.add(lblAddDescription);

		Component verticalStrut = Box.createVerticalStrut(20);
		panelAddTextBoxNames.add(verticalStrut);

		return panelAddTextBoxNames;
	}

	private JPanel createPanelAddTextBoxes()
	{
		JPanel panelAddTextBoxes = new JPanel();
		panelAddTextBoxes.setPreferredSize(new Dimension(200, 200));
		panelAddTextBoxes.setBorder(new EmptyBorder(10, 10, 0, 0));
		panelAddTextBoxes.setLayout(new GridLayout(0, 1, 0, 5));
		panelAddTextBoxes.setBackground(menuBackground);

		textAddMovieName = new JTextField();
		panelAddTextBoxes.add(textAddMovieName);
		textAddMovieName.setColumns(18);

		textAddMovieLength = new JTextField();
		panelAddTextBoxes.add(textAddMovieLength);
		textAddMovieLength.setColumns(18);

		comboBoxAddGenre = new JComboBox<Genre>();
		comboBoxAddGenre.addItem(null);
		comboBoxAddGenre.addItem(Genre.ACTION);
		comboBoxAddGenre.addItem(Genre.ANIME);
		comboBoxAddGenre.addItem(Genre.COMEDY);
		comboBoxAddGenre.addItem(Genre.MYSTERY);
		comboBoxAddGenre.addItem(Genre.DRAMA);
		comboBoxAddGenre.addItem(Genre.FANTASY);
		comboBoxAddGenre.addItem(Genre.HORROR);
		comboBoxAddGenre.addItem(Genre.CRIME);
		comboBoxAddGenre.addItem(Genre.POLITICAL);
		comboBoxAddGenre.addItem(Genre.ROMANCE);
		comboBoxAddGenre.addItem(Genre.SCIFI);
		comboBoxAddGenre.addItem(Genre.WESTERN);
		comboBoxAddGenre.setSelectedItem(null);
		panelAddTextBoxes.add(comboBoxAddGenre);

		comboBoxAddRating = new JComboBox<String>();
		comboBoxAddRating.addItem(null);
		comboBoxAddRating.addItem(Rating.G.label);
		comboBoxAddRating.addItem(Rating.PG.label);
		comboBoxAddRating.addItem(Rating.PG13.label);
		comboBoxAddRating.addItem(Rating.R.label);
		comboBoxAddRating.addItem(Rating.NC17.label);
		comboBoxAddRating.setSelectedItem(null);
		panelAddTextBoxes.add(comboBoxAddRating);

		comboBoxAddCriticsRating = new JComboBox<String>();
		comboBoxAddCriticsRating.addItem(null);
		comboBoxAddCriticsRating.addItem("90+");
		comboBoxAddCriticsRating.addItem("70+");
		comboBoxAddCriticsRating.addItem("60+");
		comboBoxAddCriticsRating.addItem("50+");
		comboBoxAddCriticsRating.addItem("50 and below");
		comboBoxAddCriticsRating.setSelectedItem(null);
		panelAddTextBoxes.add(comboBoxAddCriticsRating);

		textAreaDescription = new JTextArea(15, 10);
		textAreaDescription.setLineWrap(true);
		textAreaDescription.setWrapStyleWord(true);
		textAreaDescription.setTabSize(6);
		textAreaDescription.setFont(new Font("Georgia", Font.PLAIN, 11));

		JScrollPane scrollPaneTextAreaMovieDescription = new JScrollPane(textAreaDescription);
		scrollPaneTextAreaMovieDescription.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		scrollPaneTextAreaMovieDescription.setAutoscrolls(true);
		scrollPaneTextAreaMovieDescription.setFont(new Font("Georgia", Font.PLAIN, 11));
		panelAddTextBoxes.add(scrollPaneTextAreaMovieDescription);

		JPanel panelAddMovieButton = new JPanel();
		panelAddMovieButton.setLayout(new BorderLayout(0, 0));
		panelAddMovieButton.setBackground(menuBackground);

		JButton btnAddMovie = new JButton("Add Movie");
		btnAddMovie.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				addNewMovie();
			}
		});
		btnAddMovie.setPreferredSize(new Dimension(100, 50));
		panelAddMovieButton.add(btnAddMovie, BorderLayout.EAST);
		panelAddTextBoxes.add(panelAddMovieButton);

		return panelAddTextBoxes;
	}

	private void selectRandomMovie()
	{
		List<Movie> newList = new ArrayList<Movie>();
		List<Movie> toRemove = new ArrayList<Movie>();
		if (!movies.isEmpty())
		{
			for (Movie movie : movies)
			{
				newList.add(movie);
			}
			if (comboBoxGenre.getSelectedIndex() > 0)
			{
				comboBoxIndex = comboBoxGenre.getSelectedIndex();
				for (Movie movie : newList)
				{
					if (!(movie.getGenre().toString().contentEquals(comboBoxGenre.getItemAt(comboBoxIndex).toString())))
					{
						toRemove.add(movie);
					}
				}
				newList.removeAll(toRemove);
			}
			if (comboBoxRating.getSelectedIndex() > 0)
			{
				comboBoxIndex = comboBoxRating.getSelectedIndex();
				for (Movie movie : newList)
				{
					if (!(movie.getRating().toString()
							.contentEquals(comboBoxRating.getItemAt(comboBoxIndex).toString())))
					{
						toRemove.add(movie);
					}
				}
				newList.removeAll(toRemove);
			}
			if (comboBoxCriticsRating.getSelectedIndex() > 0)
			{
				comboBoxIndex = comboBoxCriticsRating.getSelectedIndex();
				for (Movie movie : newList)
				{
					if (!(movie.getCriticsRating() == Integer.parseInt(comboBoxCriticsRating.getItemAt(comboBoxIndex).substring(0, 1))))
					{
						toRemove.add(movie);
					}
				}
				newList.removeAll(toRemove);
			}
			if (comboBoxWatched.getSelectedIndex() == 2)
			{
				for (Movie movie : newList)
				{
					if (movie.isWatched())
					{
						toRemove.add(movie);
					}
				}
				newList.removeAll(toRemove);
			}
		}
		randomIndex = newList.isEmpty() ? (int) (Math.random() * movies.size())
				: (int) (Math.random() * newList.size());

		moviePanel.setMovieInfo(newList.isEmpty() ? movies.get(randomIndex) : newList.get(randomIndex));
	}

	/**
	 * Creates a movie object based on information from the user and writes it to a
	 * file.
	 */
	private void addNewMovie()
	{
		lblNotification.setForeground(Color.red);
		if (!(textAddMovieLength.getText().isBlank()))
		{
			try
			{
				length = Integer.parseInt(textAddMovieLength.getText());
			} catch (NumberFormatException ex)
			{
				lblNotification.setText("Please enter a number for movie length. ex: 160");
			}
		}
		if (length < 1)
			lblNotification.setText("Enter a Length greater than 0");
		else if (!(textAddMovieName.getText().isBlank()) && comboBoxAddGenre.getSelectedIndex() >= 0
				&& comboBoxAddRating.getSelectedIndex() >= 0 && comboBoxAddCriticsRating.getSelectedIndex() >= 0)
		{
			movieName = textAddMovieName.getText();

			comboBoxIndex = comboBoxAddRating.getSelectedIndex();
			if (comboBoxAddRating.getItemAt(comboBoxIndex).length() > 2)
			{
				String s = comboBoxAddRating.getItemAt(comboBoxIndex);
				s = s.substring(0, 2) + s.substring(3);
				rating = Rating.valueOf(s);
			} else
				rating = Rating.valueOf(comboBoxAddRating.getItemAt(comboBoxIndex));

			comboBoxIndex = comboBoxAddCriticsRating.getSelectedIndex();
			criticsRating = Integer.parseInt(comboBoxAddCriticsRating.getItemAt(comboBoxIndex).substring(0, 2));

			comboBoxIndex = comboBoxAddGenre.getSelectedIndex();
			genre = comboBoxAddGenre.getItemAt(comboBoxIndex);

			description = textAreaDescription.getText();

			Movie movie = new Movie(movieName, length, rating, criticsRating, genre, description, false, image);
			movies.add(movie);

			clearForm();

			lblNotification.setForeground(Color.white);
			lblNotification.setText("Movie added successfully.");

		} else
		{
			lblNotification.setText("Please fill out all fields.");
		}
	}

	/**
	 * Writes movie objects from movies list to a CSV file
	 * 
	 * @param movie
	 */
	public void writeListToFile(File movieFile)
	{
		try
		{
			movieWriter = new PrintWriter(movieFile);
			for (Movie movie : movies)
			{
				movieWriter.write(movie + "\n");
			}
			movieWriter.close();
		} catch (IOException ex)
		{
			ex.printStackTrace();
		}
		System.out.println("done.");
	}

	/**
	 * Resets all of the text and combo boxes to null or empty.
	 */
	private void clearForm()
	{
		comboBoxCriticsRating.setSelectedItem(null);
		comboBoxGenre.setSelectedItem(null);
		comboBoxRating.setSelectedItem(null);

		textAddMovieName.setText("");
		textAddMovieLength.setText("");
		comboBoxAddGenre.setSelectedItem(null);
		comboBoxAddRating.setSelectedItem(null);
		comboBoxAddCriticsRating.setSelectedItem(null);
		textAreaDescription.setText("");

		comboBoxIndex = 0;
	}

	public void setMovieWatched()
	{
		movies.get(randomIndex).setWatched(true);
	}
}
