package it.univaq.architecture.recovery.granchelli;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import it.univaq.architecture.recovery.model.MicroserviceArch;
import it.univaq.architecture.recovery.service.impl.DockerParser;


@SpringBootApplication
public class ArchitectureRecoveryApplication {

	final static Logger logger = Logger.getLogger(ArchitectureRecoveryApplication.class);
	
	public static void main(String[] args) throws IOException, InvalidRemoteException, TransportException, GitAPIException {
		SpringApplication.run(ArchitectureRecoveryApplication.class, args);
		
//		System.out.println("INSTANZIAZIONE MANAGER GITHUB");
//		TestJGit test = new TestJGit("/home/grankellowsky/Tesi/Codice/prova2","https://github.com/yanglei99/acmeair-nodejs.git");
//		test.testClone();		
//		File localPath = new File(test.getLocalPath());
		logger.info("microServicesArch Element Created");
		MicroserviceArch microServicesArch = new MicroserviceArch();
		logger.info("DockerParser Started");
		DockerParser dockerParser = new DockerParser(microServicesArch);
//		dockerParser.setBasDirectory("/home/grankellowsky/Tesi/Codice/prova2");
		dockerParser.setBasDirectory("/home/grankellowsky/Tesi/Codice/dockerProject/acmeair-nodejs");
		
		dockerParser.find();
		logger.info("DockerCompose Extractor:");
		microServicesArch.toString();
		logger.info("=========================");
		logger.info("Docker Reader Starting:");	
		dockerParser.dockerFilereader();
		microServicesArch.toString();
		
		
	}
}
