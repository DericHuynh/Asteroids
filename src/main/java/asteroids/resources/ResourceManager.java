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
		String relativeFilePathString;
		URL resourceURL;
		URI resourceURI;
		File resource = null;
		
		currentClass = this.getClass();
		currentPackageName = currentClass.getPackageName();
		currentPackageName = currentPackageName.replace('.', '/');
		relativeFilePathString = "/" + currentPackageName + "/" + Name;

		try {
			resourceURL = currentClass.getResource(relativeFilePathString);
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
	 * post: A relative file path as string is returned
	 */
	public String GetResourceFilePath(String Name) {
		Class<?> currentClass;
		String currentPackageName;
		String relativeFilePathString;
		
		currentClass = this.getClass();
		currentPackageName = currentClass.getPackageName();
		currentPackageName = currentPackageName.replace('.', '/');
		relativeFilePathString = "/" + currentPackageName + "/" + Name;
		
		return relativeFilePathString;
	}
	
	/**
	 * Returns a resources full file path as a string
	 * pre: Name representing the name of the resource, including the type of file (i.e resource.png)
	 * post: A full file path as string is returned
	 */
	public String GetResourceFullFilePath(String Name) {
		Class<?> currentClass;
		String currentPackageName;
		String relativeFilePathString;
		String fullFilePathString;
		URL resourceURL = null;
		
		currentClass = this.getClass();
		currentPackageName = currentClass.getPackageName();
		currentPackageName = currentPackageName.replace('.', '/');
		relativeFilePathString = "/" + currentPackageName + "/" + Name;
		
		try {
			resourceURL = currentClass.getResource(relativeFilePathString);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		fullFilePathString = resourceURL.getPath();
		
		return fullFilePathString;
	}
}
