package it.univaq.architecture.recovery.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.eclipse.emf.common.util.EList;

import MicroservicesArchitecture.Interface;
import MicroservicesArchitecture.Service;
import MicroservicesArchitecture.SystemMSA;
import it.univaq.architecture.recovery.model.MicroService;
import it.univaq.architecture.recovery.model.MicroserviceArch;

public class TcpReconstructor {

	// private static final String IPADDRESS_PATTERN =
	// "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
	// "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
	// "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
	// "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
	private static final String IPADDRESS_PATTERN = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])";
	Process p;
	Conversion converter = new Conversion();
	MSALoaderImpl factory = new MSALoaderImpl();

	public String startAnalisys() {
		String s, log = null;

		try {
			p = Runtime.getRuntime().exec("tcpdump -i " + "Interfaccia da analizzare");
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));

			while ((s = br.readLine()) != null) {
				log = log.concat(s);
			}
			p.waitFor();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return log;
	}

	public void stopAnalisys() {
		p.destroy();
	}

	// Service Discovery Well Known
	public List<MicroService> valutateResult(MicroserviceArch msa, String clientIP) {

		// List<MicroService> richService = msa.getServices();
		String s = null;

		File file = new File("/home/grankellowsky/Tesi/Codice/logging/tcptrack/tcpdump_log.txt");
		// Iterator<MicroService> it = msa.getServices().services.iterator();
		Scanner in = null;

		// Creazione Testa

		SystemMSA system = factory.createSystemMSA();
		system.setName("AcmeAIR");
		converter.getNode(system, msa.getServices());

		// while (it.hasNext()) {
		// MicroService microService = (MicroService) it.next();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			// Map<MicroService, List<MicroService>> mappa = new
			// HashMap<MicroService, List<MicroService>>();
			String filtedLog[] = new String[10000000];
			int index = 0;
			while ((s = br.readLine()) != null) {
				if (s.contains("HTTP:") && !s.contains(".js") && !s.contains(".css") && !s.contains(".png")
						&& !s.contains(".jpg") && !s.contains("304 Not Modified")) {
					System.out.println(s);
					filtedLog[index] = s;
					index++;
				}
			}
			br.close();
			System.out.println("Read Size Log" + index);
			String realLog[] = new String[index];
			realLog = Arrays.copyOf(filtedLog, index);

			String serviceDiscovery = getServiceDiscovery();
			system = reconstructPhysicArchitecture(realLog, system);
			converter.printConversionState(system);
			converter.printDependencies(system);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		// }

		return null;
	}

	private void reconstructLogicArchitecture(String[] filtedLog, SystemMSA system, String serviceDiscovery) {
		// TODO Auto-generated method stub

	}

	private SystemMSA reconstructPhysicArchitecture(String[] filtedLog, SystemMSA system) {

		EList<Service> services = system.getComposedBy();
		Iterator<Service> it = services.iterator();

		while (it.hasNext()) {
			Service service = (Service) it.next();


			for (int i = 0; i < filtedLog.length - 1; i++) {
				String line = filtedLog[i];

				if (service.getHost() != null) {
					if (line.contains("IP " + service.getHost())) {
						
						// Il servizio service Consuma un servizio da qualcuno
						// Usa una interfaccia dell'indirizzo di destra
						
						int indexOfDestination = line.indexOf(">");
						String IpDestination = extractIp(line.substring(indexOfDestination + 1, indexOfDestination + 15));
						// System.out.println("IpDestination: " +
						// IpDestination);
						// Chi ho Chiamato?
						Service destination = converter.detectService(services, IpDestination.trim());
						if (destination != null) {
							// System.out.println();
							// Destination espone un interfaccia che sara usata
							// da service
							if (!line.contains("HTTP: HTTP/1.1")) {
								//Check Existence of an Interface, if not Exists it will create a new one, otherwise it will check if
								//this messagge is a new operation
								converter.checkInterfaceUsed(line, service, destination);
								//Check if the interface already exist
								
								
							}
							
						}

						// Manca assegnamento ed estrazione operazione

					} else if (line.contains("> " + service.getHost())) {
						// System.out.println("Serve:");
						// Il service viene Chiamato
						// Espone un interfaccia, usata dall' indirizzo alla sua
						// sinistra
						int indexOfOrigin = line.indexOf(">");
						// Ip Chiamante
						String ipSource = extractIp(line.substring(indexOfOrigin - 18, indexOfOrigin - 1));
						// Chi mi ha Chiamato?
						// System.out.println("IP Source: ");
						Service source = converter.detectService(services, ipSource);
						if (source != null) {

							// il Servizio source Utilizza una interfaccia che
							// service.host espone
							// Dichiaro l'interfaccia
							// need to check the existence of the same interface
							if (!line.contains("HTTP: HTTP/1.1")) {

								converter.checkInterfaceRequested(line, service, source);
								
								
							}

						}

					}
				}

			}
		}
		return system;
	}

	private String getServiceDiscovery() {
		// TODO Auto-generated method stub
		return "172.18.0.11";

	}

	public String extractIp(String ip) {

		String newIP = ip.substring(0, ip.lastIndexOf("."));
		return newIP;
	}

	public Process getP() {
		return p;
	}

	public void setP(Process p) {
		this.p = p;
	}

}
