package pt.iscte.pidesco.uml;

import java.util.HashMap;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import pt.iscte.pidesco.codegenerator.service.CodeGeneratorServices;
import pt.iscte.pidesco.uml.Compartment;
import pt.iscte.pidesco.uml.extensibility.IUMLActions;

public class UMLClass extends Figure{
	
	public static Color classColor = new Color(null,255,255,206);
	private Compartment attributeFigure = new Compartment(ToolbarLayout.ALIGN_TOPLEFT);
	private Compartment methodFigure = new Compartment(ToolbarLayout.ALIGN_TOPLEFT);
	private Compartment optionsFigure = new Compartment(ToolbarLayout.ALIGN_CENTER);
	
	private Font font = new Font(null, "sansserif", 11, SWT.BOLD);
	
	private HashMap<String, String> connectionsList;
	
	private static final String EXT_POINT_ACTIONS = "pt.iscte.pidesco.uml.actions";
	
	private String path = "";
	
	public UMLClass(Label name, Label classType) {
		ToolbarLayout layout = new ToolbarLayout();
		setLayoutManager(layout);	
		setBorder(new LineBorder(ColorConstants.black,1));
		setBackgroundColor(classColor);
		setOpaque(true);
		
		connectionsList = new HashMap<>();
		
		name.setFont(font);
		
		add(classType);
		add(name);
		add(attributeFigure);
		add(methodFigure);
		
		Label optionsLabel = new Label(new Image(
				null, UMLClass.class.getResourceAsStream("images\\options.png")));
		setOptionsListener(optionsLabel, name);
			
		optionsFigure.add(optionsLabel);
		
		add(optionsFigure);
	}
	
	public Compartment getAttributesCompartment() {
		return attributeFigure;
	}
	public Compartment getMethodsCompartment() {
		return methodFigure;
	}
	
	public void setConnection(String className, String connectionType) {
		connectionsList.put(className+".java", connectionType);
	}
	public HashMap<String, String> getConnectionsList() {
		return connectionsList;
	}
	public void setConnectionsList(HashMap<String, String> connectionsList) {
		this.connectionsList = connectionsList;
	}
	
	private void setOptionsListener(Figure figure, Label name) {
		figure.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent me) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent me) {
				// TODO Auto-generated method stub
				System.out.println("Clickei -> " + name.getText());
				
				buildActions(name.getText());
								
			}
			
			@Override
			public void mouseDoubleClicked(MouseEvent me) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	private void buildActions(String name) {
		Shell shell = new Shell(SWT.SHELL_TRIM | SWT.CENTER);	
		shell.setLayout(new GridLayout());

        shell.setText(name);
        shell.setSize(name.length()*5+300, 200);
        
        BundleContext context = Activator.getContext();

		ServiceReference<CodeGeneratorServices> serviceReference = context.getServiceReference(CodeGeneratorServices.class);
		CodeGeneratorServices codeGen = context.getService(serviceReference);
        
        Button bn = new Button(shell, SWT.FLAT);
        bn.setText("Generate Getter and Setter");
        
        bn.addMouseListener(new org.eclipse.swt.events.MouseListener() {
			
			@Override
			public void mouseUp(org.eclipse.swt.events.MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseDown(org.eclipse.swt.events.MouseEvent e) {
				// TODO Auto-generated method stub
				Shell shellField = new Shell(SWT.SHELL_TRIM);
				shellField.setLayout(new GridLayout());
				shellField.setText(name);
		        
		        org.eclipse.swt.widgets.Label fieldLabel = new org.eclipse.swt.widgets.Label(shellField, SWT.NONE);
		        fieldLabel.setText("Please insert the field. For example: \\\"private String name\\\"");

		        GridData dataField = new GridData();
		        dataField.grabExcessHorizontalSpace = true;
		        dataField.horizontalAlignment = GridData.FILL;

		        Text fieldValue = new Text(shellField, SWT.BORDER);
		        fieldValue.setLayoutData(dataField);
		        
		        Composite buttonComp = new Composite(shellField, SWT.NONE);
		        GridLayout buttonCompLayout = new GridLayout(2, false);
		        buttonComp.setLayout(buttonCompLayout);
		        buttonComp.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));

		        Button okButton = new Button(buttonComp, SWT.PUSH);
		        okButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		        okButton.setText("&OK");
		        shellField.setDefaultButton(okButton);
		        
		        okButton.addSelectionListener(new SelectionListener() {
					
					@Override
					public void widgetSelected(SelectionEvent e) {
						// TODO Auto-generated method stub
						codeGen.addSettersAndGetters(path, fieldValue.getText());//"private String name;");
						shellField.dispose();
						shell.setFocus();
						
					}
					
					@Override
					public void widgetDefaultSelected(SelectionEvent e) {
						// TODO Auto-generated method stub
						
					}
				});

		        Button cancelButton = new Button(buttonComp, SWT.PUSH);
		        cancelButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		        cancelButton.setText("&Cancel");
		        
		        cancelButton.addSelectionListener(new SelectionListener() {
					
					@Override
					public void widgetSelected(SelectionEvent e) {
						// TODO Auto-generated method stub
						shellField.dispose();
						
					}
					
					@Override
					public void widgetDefaultSelected(SelectionEvent e) {
						// TODO Auto-generated method stub
						
					}
				});
		        
		        shellField.pack();
		        shellField.open();
				
			}
			
			@Override
			public void mouseDoubleClick(org.eclipse.swt.events.MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	    
        loadActions(shell);
            
        shell.open();
	}
	
	private void loadActions(Shell shell) {
		IExtensionRegistry reg = Platform.getExtensionRegistry();
		IConfigurationElement[] elements = reg.getConfigurationElementsFor(EXT_POINT_ACTIONS);
		for(IConfigurationElement e : elements) {
			String name = e.getAttribute("buttonLabel");
			try {
				Button btAux = new Button(shell, SWT.PUSH);
				btAux.setText(name);
				
				IUMLActions action = (IUMLActions) e.createExecutableExtension("buttonAction");
				
				btAux.addMouseListener(new org.eclipse.swt.events.MouseListener() {
					
					@Override
					public void mouseUp(org.eclipse.swt.events.MouseEvent e) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void mouseDown(org.eclipse.swt.events.MouseEvent e) {
						// TODO Auto-generated method stub
						action.buttonAction(shell.getText(), path);
						
					}
					
					@Override
					public void mouseDoubleClick(org.eclipse.swt.events.MouseEvent e) {
						// TODO Auto-generated method stub
						
					}
				});
				
			} catch (CoreException e1) {
				e1.printStackTrace();
			}
			
		}
		
	}

	public void setPath(String path) {
		this.path = path;
	}

}
