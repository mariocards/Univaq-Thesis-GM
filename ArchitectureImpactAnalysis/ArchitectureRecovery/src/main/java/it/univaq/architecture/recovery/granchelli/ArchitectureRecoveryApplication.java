package it.univaq.architecture.recovery.granchelli;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;

import it.univaq.architecture.recovery.model.MicroserviceArch;
import it.univaq.architecture.recovery.service.impl.DockerManager;
import it.univaq.architecture.recovery.service.impl.DockerParser;
import it.univaq.architecture.recovery.service.impl.TcpReconstructor;

@SpringBootApplication
@ComponentScan(basePackages = { "it.univaq.architecture.recovery.service.impl.*" })
@EnableAutoConfiguration
@Configuration
public class ArchitectureRecoveryApplication {

	final static Logger logger = Logger.getLogger(ArchitectureRecoveryApplication.class);

//	@Autowired
//	private GitHubManager repoManager;

	
	public static void main(String[] args)
			throws IOException, InvalidRemoteException, TransportException, GitAPIException {
		SpringApplication.run(ArchitectureRecoveryApplication.class, args);
		
//		this.repoManager.setLocalPath("/home/grankellowsky/Tesi/Codice/prova2");
//		this.repoManager.setRemotePath("https://github.com/yanglei99/acmeair-nodejs.git");
//		 System.out.println("INSTANZIAZIONE MANAGER GITHUB");
//		 TestJGit test = new
//		 TestJGit("/home/grankellowsky/Tesi/Codice/prova2","https://github.com/yanglei99/acmeair-nodejs.git");
//		 test.testClone();
//		 File localPath = new File(test.getLocalPath());
		logger.info("microServicesArch Element Created");
		MicroserviceArch microServicesArch = new MicroserviceArch();
		logger.info("DockerParser Started");
		DockerParser dockerParser = new DockerParser(microServicesArch);
		// dockerParser.setBasDirectory("/home/grankellowsky/Tesi/Codice/prova2");
		dockerParser.setBasDirectory("/home/grankellowsky/Tesi/Codice/dockerProject/acmeair-nodejs");

		dockerParser.find();
		logger.info("DockerCompose Extractor:");
		microServicesArch.toString();
		logger.info("=========================");
		logger.info("Docker Reader Starting:");
		dockerParser.dockerFilereader();
		microServicesArch.toString();
		DockerManager manager = new DockerManager();
		manager.getContainerId(microServicesArch.getServices());
		manager.getNetwork(microServicesArch.getServices());
		microServicesArch.setNetworkName(manager.checkIfContainerHasTheSameNetwork(microServicesArch.getServices()));
		
		microServicesArch.setClientIp(manager.getClientIP(microServicesArch.getNetworkName().get(0)));
		
		
		TcpReconstructor reconstructore = new TcpReconstructor();
		
		reconstructore.valutateResult(microServicesArch, microServicesArch.getClientIp());

	}
}
