package pt.iscte.pidesco.uml.ext;

import pt.iscte.pidesco.uml.extensibility.IUMLActions;

public class UMLAction1 implements IUMLActions{

	@Override
	public void buttonAction(String className, String classPath) {
		// TODO Auto-generated method stub
		System.out.println("---- Action 1 ----");
		System.out.println("Class Name: " + className);
		System.out.println("Class Path: " + classPath);
		System.out.println("------------------");
	}

}
