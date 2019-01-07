package pt.iscte.pidesco.uml;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

//import pt.iscte.pidesco.codegenerator.internal.CodeGeneratorServicesImpl;
import pt.iscte.pidesco.codegenerator.service.CodeGeneratorServices;
import pt.iscte.pidesco.uml.internal.UMLServicesImpl;
import pt.iscte.pidesco.uml.service.UMLServices;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		//context.registerService(CodeGeneratorServices.class, new CodeGeneratorServicesImpl(), null);
		context.registerService(UMLServices.class, new UMLServicesImpl(), null);
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
