package main;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyUtil {

	private static Properties properties = null;
	public static Properties getProperyFile(){
		
		if ( properties == null){
			properties = new Properties();
			try {
				FileInputStream fis = new FileInputStream("properties/runtime.properties");
				properties.load(fis);
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
		
		return properties;
		
	}
	
}
