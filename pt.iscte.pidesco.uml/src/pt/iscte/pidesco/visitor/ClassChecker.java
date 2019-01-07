package pt.iscte.pidesco.visitor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringJoiner;

import org.eclipse.draw2d.Label;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.swt.graphics.Image;

import pt.iscte.pidesco.uml.UMLClass;
import pt.iscte.pidesco.uml.UMLView;

public class ClassChecker extends ASTVisitor {

	UMLClass umlClass;
	List<UMLClass> innerClasses = new ArrayList<>();
	private String path;

	public ClassChecker(String path) {
		super();
		this.path = path;
	}

	public List<UMLClass> getInnerClasses() {
		return innerClasses;
	}

	public UMLClass getUMLClass() {
		return umlClass;
	}

	// visits class/interface declaration
	@Override
	public boolean visit(TypeDeclaration node) {
		String type;
		Label classLabel;
		Label classType;
		String name = node.getName().toString();

		if(node.isInterface()) {
			type = "Interface";
			classLabel = new Label(name, new Image(
					null, UMLView.class.getResourceAsStream("images\\interface.png")));
			classType = new Label("<<Java " + type + ">>");
		}else {
			type = "Class";
			classLabel = new Label(name, new Image(
					null, UMLView.class.getResourceAsStream("images\\class.png")));
			classType = new Label("<<Java " + type + ">>");
		}

		UMLClass newUmlClass;
		newUmlClass = new UMLClass(classLabel, classType);

		if(node.getSuperclassType() != null) {
			newUmlClass.setConnection(node.getSuperclassType().toString(), "extends");
		}
		List superInterfaceTypes = node.superInterfaceTypes();

		for (Iterator itSuperInterfacesIterator = superInterfaceTypes.iterator(); itSuperInterfacesIterator.hasNext();) {
			Object next = itSuperInterfacesIterator.next();
			if (next instanceof SimpleType) {
				newUmlClass.setConnection(next.toString(), "implements");
			}
		}
		
		for(MethodDeclaration method: node.getMethods()) {
			String methodName = method.getName().toString();
			StringBuilder methodStr = new StringBuilder(methodName);

			StringJoiner params = new StringJoiner(",", "(", ")");

			class ParamsVisitor extends ASTVisitor {

				// visits variable declarations in parameters
				@Override
				public boolean visit(SingleVariableDeclaration node) {
					params.add(node.getType().toString());
					return true;

				}

			}

			ParamsVisitor paramsVisitor = new ParamsVisitor();
			method.accept(paramsVisitor);

			methodStr.append(params.toString());

			Label methodLabel;
			if(method.getReturnType2()!=null) {
				methodStr.append(": " + method.getReturnType2().toString());
			}

			methodLabel = new Label(methodStr.toString());
			newUmlClass.getMethodsCompartment().add(methodLabel);
		}
		
		newUmlClass.setPath(path);

		if(umlClass==null)
			umlClass = newUmlClass;
		else {
			umlClass.setConnection(node.getName().toString(), "nested");
			innerClasses.add(newUmlClass);
		}

		return true;
	}

	@Override
	public boolean visit(AnonymousClassDeclaration node) {
		return false;
	}

	// visits Enum declaration
	@Override
	public boolean visit(EnumDeclaration node) {
		String type = "Enumeration";

		String name = node.getName().toString();

		Label classLabel = new Label(name, new Image(
				null, UMLView.class.getResourceAsStream("images\\enum.png")));
		Label classType = new Label("<<Java" + type + ">>");

		UMLClass newUmlClass;
		newUmlClass = new UMLClass(classLabel, classType);
		
		newUmlClass.setPath(path);
		
		for(int i = 0; i < node.enumConstants().size(); i++) {
			Label enumDec = new Label(node.enumConstants().get(i).toString() + ": " + name);
			newUmlClass.getAttributesCompartment().add(enumDec);
		}

		if(umlClass == null)
			umlClass = newUmlClass;
		else
			innerClasses.add(newUmlClass);

		return true;
	}

	// visits attributes
	@Override
	public boolean visit(FieldDeclaration node) {

		// loop for several variables in the same declaration
		for(Object o : node.fragments()) {
			VariableDeclarationFragment var = (VariableDeclarationFragment) o;			
			
			String name = var.getName().toString();
			Label attribute = new Label(name + ": " + node.getType().toString(), new Image(
					null, UMLView.class.getResourceAsStream("images\\field_private.png")));

			if(innerClasses.size() == 0)
				umlClass.getAttributesCompartment().add(attribute);
			else
				innerClasses.get(innerClasses.size()-1).getAttributesCompartment().add(attribute);
			
		}
		return false;
	}
}
