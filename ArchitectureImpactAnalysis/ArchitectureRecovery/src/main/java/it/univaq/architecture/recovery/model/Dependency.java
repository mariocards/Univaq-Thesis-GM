package it.univaq.architecture.recovery.model;

import java.util.List;

public class Dependency {

	//Le Sue Dipendenze
	List<MicroService> dependOn;
	//Chi dipende da lui
	List<MicroService> dependent;
	
	
	public List<MicroService> getDependOn() {
		return dependOn;
	}
	public void setDependOn(List<MicroService> dependOn) {
		this.dependOn = dependOn;
	}
	public List<MicroService> getDependent() {
		return dependent;
	}
	public void setDependent(List<MicroService> dependent) {
		this.dependent = dependent;
	}
	
	public void addDependency(MicroService dependOn){
		this.dependOn.add(dependOn);
	}
	
	public void addDependent(MicroService dependent){
		this.dependent.add(dependent);
	}
	
}
