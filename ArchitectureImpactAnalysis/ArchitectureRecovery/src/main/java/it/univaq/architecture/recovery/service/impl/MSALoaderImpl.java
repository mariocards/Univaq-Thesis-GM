package it.univaq.architecture.recovery.service.impl;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import MicroservicesArchitecture.Developer;
import MicroservicesArchitecture.Interface;
import MicroservicesArchitecture.Link;
import MicroservicesArchitecture.MicroService;
import MicroservicesArchitecture.MicroservicesArchitectureFactory;
import MicroservicesArchitecture.MicroservicesArchitecturePackage;
import MicroservicesArchitecture.Product;
import MicroservicesArchitecture.Team;
import MicroservicesArchitecture.impl.MicroservicesArchitectureFactoryImpl;
import it.univaq.architecture.recovery.service.MSALoader;

public class MSALoaderImpl implements MSALoader {

	MicroservicesArchitectureFactoryImpl factory = new MicroservicesArchitectureFactoryImpl();

	public void Loader() {
		EFactory factory = MicroservicesArchitecturePackage.eINSTANCE.getEFactoryInstance();
		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		Map<String, Object> m = reg.getExtensionToFactoryMap();
		m.put("microservicesarchitecture", new XMIResourceFactoryImpl());
		ResourceSet resSet = new ResourceSetImpl();
		Resource resource = resSet.getResource(URI.createURI("src/main/model/acmeair.microservicesarchitecture"), true);
	}

	public Product createProduct() {
		return factory.createProduct();
	}

	public MicroService createMicroService() {
		return factory.createMicroService();
	}

	public Interface createInterface() {
		return factory.createInterface();
	}

	public Link createLink() {
		return factory.createLink();
	}

	public Team createTeam() {
		return factory.createTeam();
	}

	public Developer createDeveloper() {
		return factory.createDeveloper();
	}

	public void saveModel(Product product) {
		
		MicroservicesArchitectureFactoryImpl.init();
 
		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
        Map<String, Object> m = reg.getExtensionToFactoryMap();
        m.put("microservicesarchitecture", new XMIResourceFactoryImpl());
        ResourceSet resSet = new ResourceSetImpl();
        Resource resource = resSet.createResource(URI
                .createURI("src/main/resources/model/mymodel.microservicesarchitecture"));
                resource.getContents().add(product);
		// now save the content.
		try {
			resource.save(Collections.EMPTY_MAP);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
