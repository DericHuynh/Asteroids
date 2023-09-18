package asteroids.resources;

import java.io.File;
import java.net.URI;
import java.net.URL;

public class ResourceManager {
	
	/**
	 * Returns a resource given a file name.
	 * pre: Name representing the name of the resource, including the type of file (i.e resource.png)
	 * post: A File object containing the resource requested.
	 */
	public File GetResource(String Name) {
		Class<?> currentClass;
		String currentPackageName;
		String fullFilePathString;
		URL resourceURL;
		URI resourceURI;
		File resource = null;
		
		currentClass = this.getClass();
		currentPackageName = currentClass.getPackageName();
		currentPackageName = currentPackageName.replace('.', '/');
		fullFilePathString = "/" + currentPackageName + "/" + Name;

		try {
			resourceURL = currentClass.getResource(fullFilePathString);
			resourceURI = resourceURL.toURI();
			resource = new File(resourceURI);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return resource;
	}
	
	/**
	 * Returns a resources file path as a string
	 * pre: Name representing the name of the resource, including the type of file (i.e resource.png)
	 * post: A full file path as string
	 */
	public String GetResourceFilePath(String Name) {
		Class<?> currentClass;
		String currentPackageName;
		String fullFilePathString;
		URL resourceURL;
		URI resourceURI;
		File resource = null;
		
		currentClass = this.getClass();
		currentPackageName = currentClass.getPackageName();
		currentPackageName = currentPackageName.replace('.', '/');
		fullFilePathString = "/" + currentPackageName + "/" + Name;
		
		return fullFilePathString;
	}
}
