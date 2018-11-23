package org.singam.maven.plugin.deploy;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Logger;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;

@Mojo(name = "deploy", defaultPhase = LifecyclePhase.DEPLOY)
public class FuseDeployMojo extends AbstractMojo {

	public static final Logger log = Logger.getLogger(FuseDeployMojo.class.getName());
	
	public static final String FILESEPARATOR = "/";
	
	public static final String m2RepoNative=File.separator+".m2"+File.separator+"repository"+File.separator;

	public static final String m2RepoSSHConsole = "/.m2/repository/";
	
	@Parameter(name = "buildDirectory", defaultValue = "${project.build.directory}", required = true)
	private String buildDirectory;

	@Parameter(name = "groupId", defaultValue = "${project.groupId}", required = true)
	private String groupId;

	@Parameter(name = "artifactId", defaultValue = "${project.artifactId}", required = true)
	private String artifactId;

	@Parameter(name = "bundleSymbolicName", defaultValue = "${project.name}", required = true)
	private String bundleSymbolicName;

	@Parameter(name = "currentLoginUser", defaultValue = "${user.name}", required = true)
	private String currentLoginUser;

	@Parameter(name = "currentLoginUserHome", defaultValue = "${user.home}", required = true)
	private String currentLoginUserHome;

	@Parameter(name = "computerName", defaultValue = "${env.COMPUTERNAME}", required = true)
	private String computerName;

	@Parameter(name = "version", defaultValue = "${project.version}", required = true)
	private String version;

	@Parameter(name = "userName", defaultValue = "admin", required = true)
	private String userName;

	@Parameter(name = "password", defaultValue = "admin", required = true)
	private String password;

	@Parameter(name = "serverHost", defaultValue = "127.0.0.1", required = true)
	private String serverHost;

	@Parameter(name = "serverPort", defaultValue = "8101", required = true)
	private String serverPort;

	@Parameter(name = "sshPort", defaultValue = "22", required = true)
	private String sshPort;

	@Parameter(name = "sshUsername", required = true)
	private String sshUsername;

	@Parameter(name = "sshPassword", required = true)
	private String sshPassword;

	@Parameter(name = "fuseVersion", defaultValue = "7.1", required = true)
	private String fuseVersion;

	@Parameter(name = "deploymentArtifacts", required = true)
	private List<String> deploymentArtifacts;

	@Parameter(name = "deploymentMode", defaultValue = "SSH", required = true)
	private String deploymentMode;

	public void execute() throws MojoExecutionException {
		try {
			JSch jsch = new JSch();
			jsch.setKnownHosts(serverHost);
			Session session = jsch.getSession(userName, serverHost, new Integer(serverPort));
			session.setConfig("StrictHostKeyChecking", "no");
			session.setPassword(password);
			session.connect();
			if (deploymentMode.equals("SFTP")) {
				Channel channel = session.openChannel("sftp");
				channel.connect();
				ChannelSftp channelSftp = (ChannelSftp) channel;
				for (String deploymentArtifact : deploymentArtifacts) {
					FileInputStream fis = new FileInputStream(buildDirectory + File.separator + deploymentArtifact);
					channelSftp.put(fis, "./deploy/" + deploymentArtifact);
					fis.close();
				}
				channelSftp.disconnect();
			} else if (deploymentMode.equals("SSH")) {

				uploadFiles();

				sendCommand(session, "uninstall \"" + bundleSymbolicName + "\"\n");

				log.info(sendCommand(session, "install -s mvn:" + groupId + FILESEPARATOR + artifactId + FILESEPARATOR + version + "\n"));
			}
			session.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
			throw new MojoExecutionException("Error deploying file ", e);
		} finally {
		}
	}

	public void uploadFiles() throws MojoExecutionException {
		Channel channel;
		try {
			JSch jsch = new JSch();
			jsch.setKnownHosts(serverHost);
			Session session = jsch.getSession(sshUsername, serverHost, new Integer(sshPort));
			session.setConfig("StrictHostKeyChecking", "no");
			session.setPassword(sshPassword);
			session.connect();
			String userHome = sendCommand(session, "eval echo ~$USER");
			userHome = userHome.replaceAll("\n", "");
			session.disconnect();

			jsch = new JSch();
			jsch.setKnownHosts(serverHost);
			session = jsch.getSession(sshUsername, serverHost, new Integer(sshPort));
			session.setConfig("StrictHostKeyChecking", "no");
			session.setPassword(sshPassword);
			session.connect();

			channel = session.openChannel("sftp");
			channel.connect();
			ChannelSftp channelSftp = (ChannelSftp) channel;

			String path = "";
			for (String dir : (userHome + m2RepoSSHConsole + groupId.replace(".", FILESEPARATOR) + FILESEPARATOR
					+ artifactId.replace(".", FILESEPARATOR) + FILESEPARATOR + version).split(FILESEPARATOR)) {
				if (!dir.equals(""))
					path = path + FILESEPARATOR + dir;
				if (path.equals(FILESEPARATOR))
					continue;
				try {
					channelSftp.mkdir(path);
				} catch (Exception ex) {
				}
			}

			for (String deploymentArtifact : deploymentArtifacts) {
				FileInputStream fis = new FileInputStream(currentLoginUserHome + m2RepoNative
						+ groupId.replace(".", File.separator) + File.separator + deploymentArtifact.replace(".", File.separator) + File.separator + version
						+ File.separator + deploymentArtifact + "-" + version + ".jar");
				System.out.println("Uploading File"+currentLoginUserHome + m2RepoNative + groupId.replace(".", File.separator) + File.separator
						+ artifactId.replace(".", File.separator) + File.separator + version + File.separator + artifactId + "-" + version + ".jar");
				

				channelSftp.put(fis, userHome + m2RepoSSHConsole + groupId.replace(".", FILESEPARATOR) + FILESEPARATOR
						+ artifactId.replace(".", FILESEPARATOR) + FILESEPARATOR + version + FILESEPARATOR + artifactId + "-" + version + ".jar");
				fis.close();
				System.out.println(currentLoginUserHome + m2RepoNative + groupId.replace(".", File.separator) + File.separator
						+ artifactId.replace(".", File.separator) + File.separator + version + File.separator + artifactId + "-" + version + ".pom");
				fis = new FileInputStream(currentLoginUserHome + m2RepoNative + groupId.replace(".", File.separator)
						+ File.separator + deploymentArtifact.replace(".", File.separator) + File.separator + version + File.separator + deploymentArtifact
						+ "-" + version + ".pom");
				channelSftp.put(fis, userHome + m2RepoSSHConsole + groupId.replace(".", FILESEPARATOR) + FILESEPARATOR
						+ artifactId.replace(".", FILESEPARATOR) + FILESEPARATOR + version + FILESEPARATOR + artifactId + "-" + version + ".pom");
				fis.close();
			}
			channelSftp.disconnect();
			session.disconnect();

		} catch (Exception e) {
			throw new MojoExecutionException("Error deploying file ", e);
		}
	}

	
	public String sendCommand(Session session, String command) throws MojoExecutionException {
		StringBuilder outputBuffer = new StringBuilder();

		try {
			Channel channel = session.openChannel("exec");
			((ChannelExec) channel).setCommand(command);
			InputStream commandOutput = channel.getInputStream();
			channel.connect();
			int readByte = commandOutput.read();

			while (readByte != 0xffffffff) {
				outputBuffer.append((char) readByte);
				readByte = commandOutput.read();
			}
			channel.disconnect();
		} catch (IOException ioex) {
			throw new MojoExecutionException("Error deploying file ", ioex);
		} catch (JSchException jschex) {
			throw new MojoExecutionException("Error deploying file ", jschex);
		}

		return outputBuffer.toString();
	}

	public static class MyUserInfo implements UserInfo, UIKeyboardInteractive {

		@Override
		public String getPassphrase() {
			return null;
		}

		@Override
		public String getPassword() {
			return null;
		}

		@Override
		public boolean promptPassphrase(String arg0) {
			return false;
		}

		@Override
		public boolean promptPassword(String arg0) {
			return false;
		}

		@Override
		public boolean promptYesNo(String arg0) {
			return false;
		}

		@Override
		public void showMessage(String arg0) {
		}

		@Override
		public String[] promptKeyboardInteractive(String arg0, String arg1, String arg2, String[] arg3,
				boolean[] arg4) {
			return null;
		}
	}
}
