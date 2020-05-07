package webviewselenium.structureUtilities;

import java.util.ArrayList;
import java.util.List;

public class ArrayToListConverter {

	/**
	 * Method allows to convert the Array to the List.
	 * 
	 * @param <T>
	 * @param array Array that need to be converted.
	 * @return List that has been converted from Array.
	 */
	public static <T> List<T> covertArrayToList(T array[]) {
		List<T> convertedList = new ArrayList<T>();
		
		for(T tElement : array) {
			convertedList.add(tElement);
		}
		
		return convertedList;
	}
	
}
