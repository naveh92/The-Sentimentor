package view;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import main.Constants;

public class BrowserMapView {
	public static void run() {
		// Run the HTML file
		try {
			Desktop.getDesktop().browse(new URI(Constants.NEW_MAP_URL));
		} 
    	catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    	catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
