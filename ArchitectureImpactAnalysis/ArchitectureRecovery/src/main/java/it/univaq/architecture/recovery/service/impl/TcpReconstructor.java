package it.univaq.architecture.recovery.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import it.univaq.architecture.recovery.model.MicroService;
import it.univaq.architecture.recovery.model.ServiceInterface;

public class TcpReconstructor {
	
	private static final String IPADDRESS_PATTERN =
			"^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
			"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
			"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
			"([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
	
	Process p;

	public String startAnalisys() {
		String s, log= null;
		
		
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

	public List<MicroService> valutateResult(List<MicroService> services) {
		
		
		List<MicroService> richService = services;
		String s = null;
		File file = null;
		Iterator<MicroService> it = services.iterator();
		while (it.hasNext()) {
			MicroService microService = (MicroService) it.next();
			try {

				BufferedReader br = new BufferedReader(new FileReader(file));

				while ((s = br.readLine()) != null) {
					if (s.contains(microService.getIp())) {
						int indiceMaggiore = s.indexOf(">");
						Scanner in = new Scanner(s.substring(indiceMaggiore, indiceMaggiore+20)).useDelimiter(IPADDRESS_PATTERN);
						ServiceInterface consumes = new ServiceInterface();
						//Aggiungere una mappa per coppia Servizio - Servizio
						//validare l'approccio con questa espressione regolare
						//Concepire Classe Dependency?
						in.next();
//						microService.setConsumingInterface(consumingInterface);
						
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		return richService;
	}

	public Process getP() {
		return p;
	}

	public void setP(Process p) {
		this.p = p;
	}

	
}
