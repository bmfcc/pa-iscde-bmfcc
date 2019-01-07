package pt.iscte.pidesco.uml.service;

import java.util.List;

import org.eclipse.draw2d.Figure;

/**
 * Services offered by the UML component.
 */
public interface UMLServices {
	
	/**
	 * Get the UML Figure of a class.
	 * @param classPath (non-null) path of the class
	 * @return a Figure representing the UML of the class
	 */
	Figure getUMLFigure(String classPath);
	
	/**
	 * Get a List of UML Figures of classes.
	 * @param classesList (non-null) list of paths to classes
	 * @return a List of Figures representing the UML of the classes
	 */
	List<Figure> getUMLFigure(List<String> classesList);

}
