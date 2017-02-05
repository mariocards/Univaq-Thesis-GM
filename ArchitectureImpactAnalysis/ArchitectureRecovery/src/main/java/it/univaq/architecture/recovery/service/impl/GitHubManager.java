package it.univaq.architecture.recovery.service.impl;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import it.univaq.architecture.recovery.service.RepositoryManager;

public class GitHubManager implements RepositoryManager {

	private String repoGIT;
	private String path;
	private String folder;
	private String username;
	private String password;

	public GitHubManager(String repoGIT, String folder, String username, String password) {
		super();
		this.repoGIT = repoGIT;
		this.folder = folder;
		this.username = username;
		this.password = password;
	}

	public String getRepoGIT() {
		return repoGIT;
	}

	public void setRepoGIT(String repoGIT) {
		this.repoGIT = repoGIT;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void connect() throws IOException {

		
	}
	
	public void cloneRepo() throws IOException{
		CloneCommand cloneCommand = Git.cloneRepository();
		cloneCommand.setDirectory(new File(getFolder()));
		cloneCommand.setNoCheckout(true);
		cloneCommand.setRemote( getRepoGIT() );
		cloneCommand.setCredentialsProvider( new UsernamePasswordCredentialsProvider( getUsername(), getPassword()) );
		try {
			cloneCommand.call();
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			e.getCause();
			e.getMessage();
		}
	}
}
