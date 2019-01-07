package pt.iscte.pidesco.uml.internal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.Figure;

import pt.iscte.pidesco.uml.service.UMLServices;
import pt.iscte.pidesco.visitor.ClassChecker;
import pt.iscte.pidesco.visitor.JavaParser;

public class UMLServicesImpl implements UMLServices{

	@Override
	public Figure getUMLFigure(String classPath) {
		// TODO Auto-generated method stub
		
		Figure figure;

		ClassChecker checker = new ClassChecker(classPath);
		JavaParser.parse(classPath, checker);
		figure = checker.getUMLClass();		
		
		return figure;
	}

	@Override
	public List<Figure> getUMLFigure(List<String> classesList) {
		// TODO Auto-generated method stub
		
		List<Figure> figureList = new ArrayList<>();

		for(String classToFigure : classesList) {
			ClassChecker checker = new ClassChecker(classToFigure);
			JavaParser.parse(classToFigure, checker);
			figureList.add(checker.getUMLClass());
			figureList.addAll(checker.getInnerClasses());
		}
		
		return figureList;
	}

}
