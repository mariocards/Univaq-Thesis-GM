package it.univaq.architecture.recovery.service.impl;

import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import MicroservicesArchitecture.Interface;
import MicroservicesArchitecture.MicroservicesArchitecturePackage;
import MicroservicesArchitecture.Operation;
import MicroservicesArchitecture.Service;
import MicroservicesArchitecture.SystemMSA;
import MicroservicesArchitecture.impl.MicroservicesArchitectureFactoryImpl;
import it.univaq.architecture.recovery.service.MSALoader;

public class MSALoaderImpl implements MSALoader {

	public MicroservicesArchitecture.SystemMSA loader() {

		MicroservicesArchitecturePackage.eINSTANCE.eClass();
		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		Map<String, Object> m = reg.getExtensionToFactoryMap();
		m.put("microservicesarchitecture", new XMIResourceFactoryImpl());
		ResourceSet resSet = new ResourceSetImpl();

		Resource resource = resSet.getResource(URI.createURI("src/main/model/acmeair.microservicesarchitecture"), true);

		SystemMSA system = (SystemMSA) resource.getContents().get(0);

		return system;
	}
	
	public SystemMSA createSystemMSA(){
		MicroservicesArchitectureFactoryImpl factory = new MicroservicesArchitectureFactoryImpl();
		return factory.createSystemMSA();
		
	}
	public Service createService(){
		MicroservicesArchitectureFactoryImpl factory = new MicroservicesArchitectureFactoryImpl();
		return factory.createService();
		
	}

	public Interface createInterface() {
		MicroservicesArchitectureFactoryImpl factory = new MicroservicesArchitectureFactoryImpl();
		
		return factory.createInterface();
	}
	
	public Operation createOperation(){
		MicroservicesArchitectureFactoryImpl factory = new MicroservicesArchitectureFactoryImpl();
		
		return factory.createOperation();
	}
	
}
