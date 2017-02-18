package it.univaq.architecture.recovery.granchelli;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import MicroservicesArchitecture.Developer;
import MicroservicesArchitecture.Product;
import it.univaq.architecture.recovery.model.MicroserviceArch;
import it.univaq.architecture.recovery.service.impl.Converter;
import it.univaq.architecture.recovery.service.impl.DockerManager;
import it.univaq.architecture.recovery.service.impl.DockerParser;
import it.univaq.architecture.recovery.service.impl.Extraction;
import it.univaq.architecture.recovery.service.impl.GitHubManager;
import it.univaq.architecture.recovery.service.impl.MSALoaderImpl;

@SpringBootApplication
@ComponentScan(basePackages = { "it.univaq.architecture.recovery.service.impl.*" })
@EnableAutoConfiguration
@Configuration
public class ArchitectureRecoveryApplication {

	final static Logger logger = Logger.getLogger(ArchitectureRecoveryApplication.class);
	public static MSALoaderImpl factory = new MSALoaderImpl();
	public Extraction extractor = new Extraction();

	public static void main(String[] args)
			throws IOException, InvalidRemoteException, TransportException, GitAPIException, InterruptedException {
		SpringApplication.run(ArchitectureRecoveryApplication.class, args);

		// this.repoManager.setLocalPath("/home/grankellowsky/Tesi/Codice/prova2");
		// this.repoManager.setRemotePath("https://github.com/yanglei99/acmeair-nodejs.git");
		// System.out.println("INSTANZIAZIONE MANAGER GITHUB");
		GitHubManager test = new GitHubManager("/home/grankellowsky/Tesi/Codice/prova3",
				"https://github.com/yanglei99/acmeair-nodejs.git");
		test.init();
//		test.testClone();
		// File localPath = new File(test.getLocalPath());
		EList<Developer> devs = test.getCommits();
		logger.info("microServicesArch Element Created");
		MicroserviceArch microServicesArch = new MicroserviceArch();
		logger.info("DockerParser Started");
		DockerParser dockerParser = new DockerParser(microServicesArch);
		dockerParser.setBasDirectory("/home/grankellowsky/Tesi/Codice/dockerProject/acmeair-nodejs");
		dockerParser.find();
		logger.info("=========================");
		logger.info("Docker Reader Starting:");
		dockerParser.dockerFilereader();

		DockerManager manager = new DockerManager();
		manager.getContainerId(microServicesArch.getServices());
		manager.getNetwork(microServicesArch.getServices());
		microServicesArch.setNetworkName(manager.checkIfContainerHasTheSameNetwork(microServicesArch.getServices()));
		microServicesArch.setClientIp(manager.getClientIP(microServicesArch.getNetworkName().get(0)));
		// Da Gli pseudo Microservice Ottenuti da Docker, Creo un istanza di
		// Product
		// Questo sarà il primo passo iterativo
		Product product = Converter.createProduct(microServicesArch.getServices(), microServicesArch.getClientIp());
		product.getDevelopers().addAll(devs);
		Extraction extract = new Extraction();
		// extract.dynamicAnalysis(product, microServicesArch.getClientIp());
		// extract.showDependency(product);
		extract.dynamicAnalysisWithServiceDiscovery(product, microServicesArch.getClientIp(), "172.18.0.11");
		extract.showDependency(product);
		factory.saveModel(product);
	}
}
