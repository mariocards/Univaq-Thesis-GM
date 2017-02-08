package it.univaq.architecture.recovery.model;

import java.util.Iterator;
import java.util.List;

public class MicroserviceArch {

	private String name;

	private List<MicroService> services;

	private String clientIp;
	
	private List<String> networkName;
	
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

	public void addService(MicroService service) {
		this.services.add(service);
	}

	public String toString() {
		Iterator<MicroService> it = this.services.iterator();
		while (it.hasNext()) {
			MicroService microService = (MicroService) it.next();
			System.out.println(microService.getName());
			System.out.println(microService.getDockerfile());
			if (microService.getPorts() != null)
				System.out.println(microService.getPorts().toString());

		}
		return "";
	}

	public MicroService getServiceByIP(String ip) {

		Iterator<MicroService> it = this.services.iterator();
		while (it.hasNext()) {
			MicroService temp = (MicroService) it.next();
			if (temp.getIp().contains(ip)) {
				return temp;
			}

		}
		return null;
	}

	public MicroService getServiceByContainerID(String containerID) {

		Iterator<MicroService> it = this.services.iterator();
		while (it.hasNext()) {
			MicroService temp = (MicroService) it.next();
			if (temp.getContainerID().contains(containerID)) {
				return temp;
			}

		}
		return null;
	}

	public MicroService getServiceByName(String name) {

		Iterator<MicroService> it = this.services.iterator();
		while (it.hasNext()) {
			MicroService temp = (MicroService) it.next();
			if (temp.getContainerID().contains(name)) {
				return temp;
			}

		}
		return null;
	}

	public String getClientIp() {
		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	public List<String> getNetworkName() {
		return networkName;
	}

	public void setNetworkName(List<String> networkName) {
		this.networkName = networkName;
	}

	public void addNetworkName(String networkName) {
		this.networkName.add(networkName);
	}

}
