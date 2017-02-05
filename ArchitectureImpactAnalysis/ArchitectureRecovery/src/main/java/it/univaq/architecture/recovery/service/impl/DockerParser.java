package it.univaq.architecture.recovery.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.github.dockerjava.core.dockerfile.Dockerfile;
import com.github.dockerjava.core.dockerfile.DockerfileStatement;

import it.univaq.architecture.recovery.model.DockerComposeFile;
import it.univaq.architecture.recovery.model.MicroService;
import it.univaq.architecture.recovery.model.MicroserviceArch;
import it.univaq.architecture.recovery.service.Parser;

public class DockerParser implements Parser {

	final static Logger logger = Logger.getLogger(DockerParser.class);
	private String basDirectory;
	private String dockerFile;
	private String dockerComposeFile;
	private MicroserviceArch microServicesArch;

	public String getBasDirectory() {
		return basDirectory;
	}

	public void setBasDirectory(String basDirectory) {
		this.basDirectory = basDirectory;
	}

	public String getDockerFile() {
		return dockerFile;
	}

	public void setDockerFile(String dockerFile) {
		this.dockerFile = dockerFile;
	}

	public DockerParser() {
		super();
	}

	public DockerParser(String basDirectory, String dockerFile) {
		super();
		this.basDirectory = basDirectory;
		this.dockerFile = dockerFile;
	}

	public DockerParser(MicroserviceArch microServicesArch) {
		this.microServicesArch = microServicesArch;
	}

	public MicroserviceArch getMicroServicesArch() {
		return microServicesArch;
	}

	public void setMicroServicesArch(MicroserviceArch microServicesArch) {
		this.microServicesArch = microServicesArch;
	}

	public String getDockerComposeFile() {
		return dockerComposeFile;
	}

	public void setDockerComposeFile(String dockerComposeFile) {
		this.dockerComposeFile = dockerComposeFile;
	}

	public void find() {
		logger.info("Inizio Ricerca per Docker-Compose");
		if (findDockerCompose()) {
			dockerComposeReader();
		} else {
			logger.info("No DockerCompose File");
			if (findDockerFile()) {
				logger.info("File Docker Trovato, iniziare Parsing");
			} else {
				logger.info("No DockerFile");
			}
		}

	}

	public boolean findDockerCompose() {
		File localPath = new File(getBasDirectory());
		File[] files = localPath.listFiles();
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			if (file.getName().contains("docker-compose")) {
				logger.info("Trovato Docker-Compose: " + file.getName());
				setDockerComposeFile(getBasDirectory() + "/" + file.getName());
				return true;
			}

		}
		return false;

	}

	public boolean findDockerFile() {
		File localPath = new File(getBasDirectory());
		File[] files = localPath.listFiles();
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			// logger.info("File name: " + file.getName());
			if (file.getName().contains("Dockerfile")) {
				logger.info("Trovato DockerFile:" + file.getName());
				setDockerFile(getBasDirectory() + "/" + file.getName());
				return true;
			}

		}
		return false;
	}

	@SuppressWarnings("rawtypes")
	public void dockerComposeReader() {
		try {
			Dockerfile dockerfile = new Dockerfile(new File(getDockerComposeFile()), new File(getBasDirectory()));
			Iterable<DockerfileStatement> statements = dockerfile.getStatements();
			List<DockerComposeFile> containerName = new ArrayList<DockerComposeFile>();
			
			// CHECK FOR CONTAINER NAME
			for (Iterator<DockerfileStatement> iterator = statements.iterator(); iterator.hasNext();) {
				DockerfileStatement temp = (DockerfileStatement) iterator.next();
				//System.out.println("DOCKERITEM: " + temp.toString());
				// If Contains the word cotainer_name, this represent a
				// Microservices
				if (temp.toString().contains("container_name")) {
					DockerComposeFile container = new DockerComposeFile();
					container.setContainerName(temp.toString());
					containerName.add(container);
				}

			}
			// //CHECK ASSOCIATE CONTAINER NAME TO DOCKER FILE
			int containerCounter = 0;
			Iterator<DockerComposeFile> containerIterator = containerName.iterator();
			while (containerIterator.hasNext()) {
				DockerComposeFile containerTemp = containerIterator.next();
				//System.out.println("Prendo " + containerTemp.getContainerName());
				for (Iterator<DockerfileStatement> iterator = statements.iterator(); iterator.hasNext();) {
					DockerfileStatement temp = (DockerfileStatement) iterator.next();
					//System.out.println(containerTemp.getContainerName() + " Compared to " + temp.toString());
					if (containerTemp.getContainerName().equals(temp.toString())) {
						containerCounter = 1;
						while (iterator.hasNext()) {
							DockerfileStatement temp2 = iterator.next();
							//System.out.println("New Item: " + temp2.toString());
							if (!temp2.toString().contains("container_name")) {

								if (temp2.toString().contains("build")) {
									//System.out
											//.println("Is the " + containerTemp.getContainerName() + " relative build ");
									containerTemp.setBuild(temp2.toString());

								} else if (temp2.toString().contains("dockerfile")) {
									//System.out.println(
											//"Is the " + containerTemp.getContainerName() + " relative Dcokerfile ");
									containerTemp.setDockerFile(temp2.toString());
								}
							} else {
								break;
							}
						}
					}

				}
			}

			// Now i have a
			parseDockerCompose(containerName);
		} catch (IOException e) {
			// TODO: handle exception
		}
	}

	private void parseDockerCompose(List<DockerComposeFile> containerName) {

		List<MicroService> microServices = new ArrayList<MicroService>();
		Iterator<DockerComposeFile> iterator = containerName.iterator();
		while (iterator.hasNext()) {

			DockerComposeFile dockercompose = iterator.next();
			String ContainerName = extractvalue(dockercompose.getContainerName());
			String buildPoint = null;
			String dockerFileName = null;
			File dockerFile = null;
			File baseDirectory = null;
			Dockerfile dockerInst = null;
			MicroService microservice = new MicroService();
			
			microservice.setName(ContainerName);
			if (dockercompose.getBuild() != null) {
				buildPoint = extractvalue(dockercompose.getBuild());
				if (buildPoint.equals(".")) {
					baseDirectory = new File(this.basDirectory + "/");
				}else{
					baseDirectory = new File(this.basDirectory + "/" + buildPoint);
				}
				
			}else{
				baseDirectory = new File(this.basDirectory + "/");
			}
			//System.out.println("baseDirectory: " + baseDirectory.getAbsolutePath());
			if (dockercompose.getDockerFile() != null) {
				
				dockerFileName = extractvalue(dockercompose.getDockerFile());
				//System.out.println("dockerFileName: " + dockerFileName);
				dockerFile = new File(this.basDirectory + "/" +dockerFileName);
				//System.out.println("dockerFile: " + dockerFile);
				dockerInst = new Dockerfile(dockerFile, new File(this.basDirectory + "/"));
				microservice.setDockerfile(dockerInst);
			}
			
			
			microServices.add(microservice);
		}
		this.microServicesArch.setServices(microServices);

	}

	@SuppressWarnings("rawtypes")
	public void dockerFilereader() {
		
		List<MicroService> microservices = getMicroServicesArch().getServices();
		Iterator<MicroService> it = microservices.iterator();
		while (it.hasNext()) {
			MicroService microService = (MicroService) it.next();
			try {
				Dockerfile dockerfile = microService.getDockerfile();
				@SuppressWarnings("rawtypes")
				Iterable<DockerfileStatement> statements = dockerfile.getStatements();
				@SuppressWarnings("rawtypes")
				DockerfileStatement expose = null;
				for (@SuppressWarnings("rawtypes")
				Iterator iterator = statements.iterator(); iterator.hasNext();) {
					@SuppressWarnings("rawtypes")
					DockerfileStatement temp = (DockerfileStatement) iterator.next();
					//System.out.println("DOCKERITEM: " + temp.toString());
					if (temp.toString().contains("EXPOSE")) {
						expose = (DockerfileStatement) temp;
					}

				}
				List<Integer> ports = extractPorts(expose.toString());
				microService.setPorts(ports);
				
 
			} catch (IOException e) {
				// TODO: handle exception
			} catch (NullPointerException nu) {
				nu.getStackTrace();
			}
			
		}
		
	}

	// public String extractBuild(String key) {
	// if (key.isEmpty()|| key.equals(null)) return "";
	// Scanner in = new Scanner(key).useDelimiter("[^build]: [a-z]+");
	// String result = null;
	// while(in.hasNext()){
	// result = in.next();
	// //System.out.println("Build " + result);
	// }
	// return result;
	// }
	//
	// public String extractDockerFile(String key) {
	// if (key.isEmpty()|| key.equals(null)) return "";
	// Scanner in = new Scanner(key).useDelimiter("[^dockerfile]: [a-z]+");
	//
	// return result;
	// }
	public String extractvalue(String key) {
		if (key.isEmpty() || key.equals(null))
			return "";
		int indexSemiColon = key.indexOf(":");
		String substring = key.substring(indexSemiColon + 2);
		return substring;
	}

	public List<Integer> extractPorts(String expose) {

		Scanner in = new Scanner(expose).useDelimiter("[^0-9]+");
		List<Integer> ports = new ArrayList<Integer>();
		while (in.hasNext()) {
			ports.add(new Integer(in.nextInt()));
		}
		Iterator<Integer> iter = ports.iterator();
		while (iter.hasNext()) {
			Integer temp = iter.next();
			//System.out.println("Port: " + temp.toString());

		}

		return ports;
	}

	private void servicesCreation() {

	}

}
