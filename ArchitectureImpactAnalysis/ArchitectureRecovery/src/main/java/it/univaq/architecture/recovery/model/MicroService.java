package it.univaq.architecture.recovery.model;

import java.util.List;

import com.github.dockerjava.core.dockerfile.Dockerfile;

public class MicroService {

	private String name;

	private List<Integer> ports;

	private Dockerfile dockerfile;

	private List<ServiceInterface> exposedInterface;

	private List<ServiceInterface> consumingInterface;

	private List<Develeper> devs;

	public MicroService() {
		super();
		// TODO Auto-generated constructor stub
	}

	public List<ServiceInterface> getExposedInterface() {
		return exposedInterface;
	}

	public void setExposedInterface(List<ServiceInterface> exposedInterface) {
		this.exposedInterface = exposedInterface;
	}

	public List<ServiceInterface> getConsumingInterface() {
		return consumingInterface;
	}

	public void setConsumingInterface(List<ServiceInterface> consumingInterface) {
		this.consumingInterface = consumingInterface;
	}

	public List<Develeper> getDevs() {
		return devs;
	}

	public void setDevs(List<Develeper> devs) {
		this.devs = devs;
	}

	public Dockerfile getDockerfile() {
		return dockerfile;
	}

	public void setDockerfile(Dockerfile dockerfile) {
		this.dockerfile = dockerfile;
	}

	public List<Integer> getPorts() {
		return ports;
	}

	public void setPorts(List<Integer> ports) {
		this.ports = ports;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
