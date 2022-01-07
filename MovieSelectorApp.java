package movieSelector;

import java.io.File;

public class MovieSelectorApp
{
	private static File movieFile;
	private static boolean stopProgram = false;
	private static MainGui program;
	
	public static void main(String[] args) throws InterruptedException
	{
		LoadScreen loadScreen = new LoadScreen();
		loadScreen.startLoadScreen();
		
		while (!LoadScreen.fileLoaded())
		{
			try {
				Thread.sleep(2000);
			}catch(InterruptedException  ex)
			{
				ex.printStackTrace();
			}
		}
		
		program = new MainGui();
		program.startProgram(movieFile); 
		
		while (!stopProgram)
		{
			if (MovieInformation.movieUpdated()) {
				updateMovie();
				MovieInformation.setMovieUpdated(false);
			}
			try {
				Thread.sleep(2000);
			}catch(InterruptedException  ex)
			{
				ex.printStackTrace();
			}
		}
		
		program.writeListToFile(movieFile);
	}
	
	private static void updateMovie()
	{
		program.setMovieWatched();
	}

	public static void setFile(File file)
	{
		movieFile = file;
	}
	
	public static void stopProgram()
	{
		stopProgram = true;
	}

}
