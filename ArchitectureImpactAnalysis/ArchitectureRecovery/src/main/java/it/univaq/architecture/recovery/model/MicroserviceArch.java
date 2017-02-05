package it.univaq.architecture.recovery.model;

import java.util.Iterator;
import java.util.List;import org.hibernate.validator.internal.util.privilegedactions.GetMethodFromPropertyName;


public class MicroserviceArch {

	private String name;

	private List<MicroService> services;

	public MicroserviceArch() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<MicroService> getServices() {
		return services;
	}

	public void setServices(List<MicroService> services) {
		this.services = services;
	}
	
	public void addService(MicroService service){
		this.services.add(service);
	}
	public String toString(){
		Iterator<MicroService> it = this.services.iterator();
		while (it.hasNext()) {
			MicroService microService = (MicroService) it.next();
			System.out.println(microService.getName());
			System.out.println(microService.getDockerfile());
			if(microService.getPorts() != null)	System.out.println(microService.getPorts().toString());
			
		}
		return "";
	}

}
