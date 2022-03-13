package com.aim.project.sdsstp.instance.reader;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Random;

import com.aim.project.sdsstp.SDSSTPObjectiveFunction;
import com.aim.project.sdsstp.instance.SDSSTPInstance;
import com.aim.project.sdsstp.instance.SDSSTPLocation;
import com.aim.project.sdsstp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.sdsstp.interfaces.SDSSTPInstanceInterface;
import com.aim.project.sdsstp.interfaces.SDSSTPInstanceReaderInterface;

/**
 * 
 * @author Warren G. Jackson
 * @since 26/03/2021
 * 
 * Methods needing to be implemented:
 * - public SDSSTPInstanceInterface readSDSSTPInstance(Path path, Random random)
 */
public class SDSSTPInstanceReader implements SDSSTPInstanceReaderInterface {

	private static SDSSTPInstanceReader oInstance;
	
	private SDSSTPInstanceReader() {
		
	}
	
	public static SDSSTPInstanceReader getInstance() {
		
		if(oInstance == null) {
			
			oInstance = new SDSSTPInstanceReader();
		}
		
		return oInstance;
	}
	
	
	@Override
	public SDSSTPInstanceInterface readSDSSTPInstance(Path path, Random random) {
		
		// TODO
		ArrayList<String> rows = fileReader(path);					// Read the file line by line and store it in a array list.
		
		/* This block of code is used to obtain the number of rows of
		 * the corresponding title to locate the location of its content. */
		int nameIndex = rows.indexOf("NAME:");
		int numberIndex = rows.indexOf("LANDMARKS:");
		int matrixIndex = rows.indexOf("TIME_MATRIX:");
		int fromIndex = rows.indexOf("TIME_FROM_OFFICE:");
		int toIndex = rows.indexOf("TIME_TO_OFFICE:");
		int durationIndex = rows.indexOf("VISIT_DURATION:");
		int officeIndex = rows.indexOf("OFFICE_LOCATION:");
		int landmarkIndex = rows.indexOf("LANDMARK_LOCATIONS:");
		
		String instanceName = rows.get(nameIndex + 1);							// Get the instance name located on the second line of the file. 
		
		int numberOfLandmarks = Integer.parseInt(rows.get(numberIndex + 1));	// Get the number of landmarks located on the fourth line of the file. 
		
		int abscissa = 0, ordinate = 0;									// Initialize the horizontal and vertical coordinates of the office and landmarks.
		
		String tourOfficeStr = rows.get(officeIndex + 1);				// Get the row of the office location.
		abscissa = Integer.parseInt(tourOfficeStr.split(" ")[0]);		// Get the abscissa of the office.
		ordinate = Integer.parseInt(tourOfficeStr.split(" ")[1]);		// Get the ordinate of the office.
		SDSSTPLocation tourOffice = new SDSSTPLocation(abscissa, ordinate);
		
		String landmarkStr = null;										// The string used to temporarily store the line of the location of each landmarks. 
		SDSSTPLocation[] landmarks = new SDSSTPLocation[rows.size() - landmarkIndex];
		for (int i = landmarkIndex + 1; i < rows.size(); i++) {			// Iterate through each row of locations. 
			landmarkStr = rows.get(i);
			abscissa = Integer.parseInt(landmarkStr.split(" ")[0]); 	// Get the abscissa of the landmark.
			ordinate = Integer.parseInt(landmarkStr.split(" ")[1]); 	// Get the ordinate of the landmark.
			SDSSTPLocation landmark = new SDSSTPLocation(abscissa, ordinate);
			landmarks[i-landmarkIndex-1] = landmark;					// Store the location of the landmark in an array.
		}
		
		int[][] timeMatrix = new int[numberOfLandmarks][numberOfLandmarks];
		for (int i = matrixIndex + 1; i < fromIndex; i++) {				// Iterate through each row of the time matrix.
			String[] timeMatrixStr = rows.get(i).split(" ");			// Store each time of the row in an array. 
			for (int j = 0; j < numberOfLandmarks; j++) {				// Traverse the array and store each time in the corresponding position of the two-dimensional array. 
				timeMatrix[i - matrixIndex - 1][j] = Integer.parseInt(timeMatrixStr[j]);
			}
		}
		
		String[] timeFromOfficeStr = rows.get(fromIndex + 1).split(" ");	// Store the time of the office to each landmark in an array.
		int[] timeFromOffice = new int[timeFromOfficeStr.length];
		for (int i = 0; i < timeFromOffice.length; i++) {					// Convert the strings in the original array to numbers.
			timeFromOffice[i] = Integer.parseInt(timeFromOfficeStr[i]);
		}
		
		String[] timeToOfficeStr = rows.get(toIndex + 1).split(" ");		// Store the time of each landmark to the office in an array.
		int[] timeToOffice = new int[timeToOfficeStr.length];
		for (int i = 0; i < timeToOffice.length; i++) {
			timeToOffice[i] = Integer.parseInt(timeToOfficeStr[i]);
		}
		
		String[] visitDurationStr = rows.get(durationIndex + 1).split(" "); // Store the visit duration of each landmark in an array.
		int[] visitDuration = new int[visitDurationStr.length];
		for (int i = 0; i < visitDuration.length; i++) {
			visitDuration[i] = Integer.parseInt(visitDurationStr[i]);
		}
		
		ObjectiveFunctionInterface f = new SDSSTPObjectiveFunction(timeMatrix, timeFromOffice, timeToOffice, visitDuration);
		
		SDSSTPInstanceInterface instance = new SDSSTPInstance(instanceName, numberOfLandmarks, tourOffice, landmarks, random, f);
		
		return instance;
	}
	
	/* Read the file line by line and store each line into the array list as a string. */
	private ArrayList<String> fileReader(Path path) {
		
		ArrayList<String> arrayList = new ArrayList<>();
		try {
			File file = path.toFile();
			InputStreamReader inputReader = new InputStreamReader(new FileInputStream(file));
            BufferedReader bufferedReader = new BufferedReader(inputReader);
            String string;
            while ((string = bufferedReader.readLine()) != null) {
            	arrayList.add(string);
            }
            bufferedReader.close();
            inputReader.close();
		} catch (IOException e) {
            e.printStackTrace();
        }
        
        return arrayList;
    }
	
}
