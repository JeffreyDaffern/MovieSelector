package movieSelector;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;
import javax.swing.Box;
import java.awt.Component;
import javax.swing.SwingConstants;

public class LoadScreen extends JFrame
{
	private final JFileChooser openFileChooser = new JFileChooser();
	private static final long serialVersionUID = 1L;
	private static JPanel logoPanel, loadPanel, contentPane;
	private JButton btnClose;
	private Box horizontalBox;
	private JButton btnLoadFile;
	private Component rigidArea;
	private Component rigidArea_1;
	private Component rigidArea_2;
	private Color contentBackground = new Color(23, 27, 39);
	private Color menuBackground = new Color(19, 19, 27);
	private JLabel name, logo, attribution, messageLabel;
	private static LoadScreen frame;
	private File movieFile;
	private static boolean fileLoaded = false;

	public void startLoadScreen()
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					frame = new LoadScreen();
					frame.setVisible(true);

					ComponentResizer cr = new ComponentResizer();
					cr.registerComponent(frame);
					cr.setSnapSize(new Dimension(10, 10));
					cr.registerComponent(logoPanel, loadPanel, contentPane);
				} catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public LoadScreen()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(500, 200, 600, 300);
		setUndecorated(true);

		ClassLoader cl = this.getClass().getClassLoader();
		setTitle("Movie Selector");
		setIconImage(new ImageIcon(cl.getResource("img/titleIcon.png")).getImage());

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		contentPane.setBackground(Color.black);
		setContentPane(contentPane);

		createPanels();
		contentPane.add(logoPanel, BorderLayout.WEST);
		contentPane.add(loadPanel, BorderLayout.CENTER);

	}

	/**
	 * 
	 */
	private void createPanels()
	{
		ClassLoader cl = this.getClass().getClassLoader();

		logoPanel = new JPanel();
		logoPanel.setLayout(new FlowLayout());
		logoPanel.setPreferredSize(new Dimension(175, 300));
		logoPanel.setBackground(menuBackground);

		name = new JLabel("Movie Selector");
		name.setForeground(Color.white);
		logoPanel.add(name);

		logo = new JLabel();
		logo.setHorizontalAlignment(SwingConstants.CENTER);
		logo.setIcon(new ImageIcon(LoadScreen.class.getResource("/img/LoadScreen.png")));
		logo.setPreferredSize(new Dimension(195, 195));
		logoPanel.add(logo);

		attribution = new JLabel();
		attribution.setIcon(new ImageIcon(cl.getResource("img/TmdbAttribution.png")));
		logoPanel.add(attribution);

		loadPanel = new JPanel();
		loadPanel.setLayout(new BorderLayout(0, 0));
		loadPanel.setPreferredSize(new Dimension(425, 300));
		loadPanel.setBackground(contentBackground);

		messageLabel = new JLabel();
		messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
		loadPanel.add(messageLabel, BorderLayout.SOUTH);

		horizontalBox = Box.createHorizontalBox();
		loadPanel.add(horizontalBox, BorderLayout.CENTER);

		rigidArea = Box.createRigidArea(new Dimension(20, 20));
		horizontalBox.add(rigidArea);

		openFileChooser.setFileFilter(new FileNameExtensionFilter("CSV files", "csv"));

		btnLoadFile = new JButton("Load");
		btnLoadFile.setBackground(menuBackground);
		btnLoadFile.setForeground(Color.white);
		btnLoadFile.setMaximumSize(new Dimension(200, 150));

		btnLoadFile.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				int returnValue = openFileChooser.showOpenDialog(btnLoadFile);

				if (returnValue == JFileChooser.APPROVE_OPTION)
				{
					movieFile = openFileChooser.getSelectedFile(); 
					messageLabel.setForeground(Color.white);
					messageLabel.setText("File Loaded Successfully");

					MovieSelectorApp.setFile(movieFile);
					fileLoaded = true;
					frame.dispose();
				} else
				{
					messageLabel.setForeground(Color.RED);
					messageLabel.setText("No file chosen");
				}
			}
		});
		horizontalBox.add(btnLoadFile);

		rigidArea_2 = Box.createRigidArea(new Dimension(20, 20));
		horizontalBox.add(rigidArea_2);

		btnClose = new JButton("Close");
		btnClose.setBackground(menuBackground);
		btnClose.setForeground(Color.white);
		btnClose.setMaximumSize(new Dimension(200, 150));

		btnClose.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.exit(1);
			}
		});
		horizontalBox.add(btnClose);

		rigidArea_1 = Box.createRigidArea(new Dimension(20, 20));
		horizontalBox.add(rigidArea_1);
	}

	public static boolean fileLoaded()
	{
		return fileLoaded;
	}
}
