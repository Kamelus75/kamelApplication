package org.formation.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.formation.beanMetier.Client;

import org.formation.service.ClientService;

@ManagedBean(name="clientController")
@SessionScoped
public class ClientController {

	private List<Client> Clients;
	private ClientService clientservice;
	private Logger logger = Logger.getLogger(getClass().getName());
	
	public ClientController() throws Exception {
		Clients = new ArrayList<>();
		
		clientservice = ClientService.getInstance();
	}
	
	public List<Client> getClients() {
		return Clients;
	}

	public void loadClients() {

		logger.info("Loading Clients");
		
		Clients.clear();

		try {
			
			// Avoir tout les clients depuis la BDD
			Clients = clientservice.getClients();
			
		} catch (Exception exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "Error loading Clients", exc);
			
			// add error message for JSF page
			addErrorMessage(exc);
		}
	}
		
	public String ajouterClient(Client client) {

		logger.info("Ajout Client: " + client);

		try {
			
			// ajouetr client à la base
			clientservice.ajouterClient(client);
			
		} catch (Exception exc) {
			// envoyer le logs au serveur
			logger.log(Level.SEVERE, "Error adding Clients", exc);
			
			// on rajoute des messages d'eereurs????
			addErrorMessage(exc);

			return null;
		}
		
		return "liste-Clients?faces-redirect=true";
	}

	public String loadClient(int id) {
		
		logger.info("chergement Client: " + id);
		
		try {
			// avoir  un client de la bdd
			Client client = clientservice.loadClient(id);
			
			// on le met dans la requette ... so we can use it on the form page???
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();		

			Map<String, Object> requestMap = externalContext.getRequestMap();
			requestMap.put("Client", client);	
			
		} catch (Exception exc) {
			// envoyer les logs au serveur
			logger.log(Level.SEVERE, "Error loading Client id:" + id, exc);
			
			// ajouetr les messages d'éerreurs
			addErrorMessage(exc);
			
			return null;
		}
				
		return "update-client.xhtml";
	}	
	
	public String msjClient(Client client) {

		logger.info("mise à jour Client: " + client);
		
		try {
			
			// update Client in the database
			clientservice.msjClient(client);
			
		} catch (Exception exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "Error updating Client: " + client, exc);
			
			// add error message for JSF page
			addErrorMessage(exc);
			
			return null;
		}
		
		return "liste-Clients?faces-redirect=true";		
	}
	
	
	//Suppression client
	public String supClient(int id) {

		logger.info("Deleting Client id: " + id);
		
		try {

			// delete the Client from the database
			clientservice.supClient(id);
			
		} catch (Exception exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "Error deleting Client id: " + id, exc);
			
			// add error message for JSF page
			addErrorMessage(exc);
			
			return null;
		}
		
		return "list-Clients";	
	}	
	// ajouet les message d'erreurs
	private void addErrorMessage(Exception exc) {
		FacesMessage message = new FacesMessage("Error: " + exc.getMessage());
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
}
