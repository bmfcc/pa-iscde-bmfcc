package pt.iscte.pidesco.uml.extensibility;

/**
 * Represents Button with a action that appears in the options of each class at UML.
 */
public interface IUMLActions {
	
	/**
	 * Action for button
	 * @param className is the name of the class where the action should be performed
	 * @param classPath is the path to the class where the action should be performed
	 */
	void buttonAction(String className, String classPath);

}
