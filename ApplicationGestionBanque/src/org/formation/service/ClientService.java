package org.formation.service;

import java.util.ArrayList;
import java.util.List;

import org.formation.beanMetier.Client;
import org.formation.dao.ClientDao;

public class ClientService {
	ClientDao clientDao;

	// var
	public static ClientService instance;

	public static ClientService getInstance() throws Exception {
		if (instance == null) {
			instance = new ClientService();
		}

		return instance;
	}

	private ClientService() throws Exception {
		clientDao = ClientDao.getInstance();
	}

	public List<Client> getClients() throws Exception {
		return clientDao.getClients();
	}

	public void ajouterClient(Client client) throws Exception {
		clientDao.ajouterClient(client);

	}

	public Client loadClient(int id) throws Exception {
		return clientDao.loadClient(id);
	}
	
	public void msjClient (Client client) throws Exception{
		clientDao.msjClient(client);
	}
	
	

	public void supClient(int id) throws Exception {
		clientDao.supClient(id);
	}
}
