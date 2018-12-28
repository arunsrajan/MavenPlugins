# MavenPlugins

Camel Salesforce plugin
-----------------------
The camel salesforce plugin generates camel-salesforce DTO java classes. The input parameters to the plugin are

salesforceClientId - Salesforce Application Client Id. <BR/>
salesforceClientSecret - Salesforce Application Client Secret <BR/>
salesforceLoginUrl - Login URL to pull the salesforce Objects from Salesforce Site. <BR/>
salesforceUsername - username to login. <BR/>
salesforcePassword - Password with token. <BR/>
salesforceApiVersion - Default version is v33.0. <BR/>
javaPackage - The java package in which the java classes are generated. <BR/>
outputDirectory - The output directory in which the java classes are generated for the given java package. The default directory is ${project.basedir}/src/main/java. <BR/>
sObjects - Any Specific Salesforce Objects to get generated. <BR/>

The sample plugin tag in POM can look like,
```xml
<plugin>
	<groupId>org.singam.maven</groupId>
	<artifactId>salesforcedtogen-maven-plugin</artifactId>
	<version>1.0.0</version>
	<executions>
		<execution>
			<phase>compile</phase>
			<goals>
				<goal>generatesobjects</goal>
			</goals>
		</execution>
	</executions>
	<configuration>
		<salesforceClientId></salesforceClientId>
		<salesforceClientSecret></salesforceClientSecret>
		<salesforceLoginUrl>https://ap5.salesforce.com</salesforceLoginUrl>
		<salesforceUsername></salesforceUsername>
		<salesforcePassword></salesforcePassword>
		<salesforceApiVersion>v35.0</salesforceApiVersion>
		<javaPackage>org.singam.salesforce.dto.sobjects</javaPackage>
		<sObjects>
			<sObject>Account</sObject>
			<sObject>Contact</sObject>
			<sObject>Order</sObject>
		</sObjects>
	</configuration>
</plugin>
```
The maven goal to generate the Salesforce to Java Objects is salesforcedtogen:generatesobjects


Fuse Deploy plugin
------------------
The Fuse deploy plugin deploys the generated artifacts to the redhat fuse server in three modes. The modes are

SFTP - Directly upload the artifacts in to the deploy directory of redhat fuse server.
SSH - Upload the jar and pom in User directory .m2 folder and uninstall and install the artifacts in redhat fuse server.
MAVEN - Upload the artifacts in to the maven repository and uninstall and install the artifacts in redhat fuse server.

The input parameters to deploy plugin is 

buildDirectory - Default value is ${project.build.directory} where the target bundle is built. <BR/>
groupId - Default value is ${project.groupId}. The project group id in which the plugin is configured in pom.xml. <BR/>
artifactId - Default value is ${project.artifactId}. The project artifact id in which the plugin is configured in pom.xml. <BR/>
bundleSymbolicName - Default value is ${project.name}. The bundle is uninstalled using this name. <BR/>
currentLoginUser - Default value is ${user.name}. SSH user to upload file in .m2 folder. <BR/>
currentLoginUserHome - Default value is ${user.home}. SSH user home directory to upload file in .m2 folder. <BR/>
computerName - Default value is ${env.COMPUTERNAME}. <BR/>
version - Default value is ${project.version}. The project version in which the plugin is configured in pom.xml. <BR/>
userName - username of fuse server. Default value is admin. <BR/>
password - password of fuse server. Default value is admin. <BR/>
serverHost - Fuse server host. Default value is 127.0.0.1. <BR/>
serverPort - Fuse server port. Default value is 8101. <BR/>
sshPort - SSH port to upload files. <BR/>
sshUsername - SSH username to upload files. <BR/>
sshPassword - SSH password to upload files. <BR/>
deploymentArtifacts -Artifacts to deploy. <BR/>
deploymentMode - SFTP, SSH or MAVEN. <BR/>
mavenRepoId - maven repository ID to upload artifacts. <BR/>
mavenRepoServerUrl - Maven repository URL. <BR/>
mavenRepoUsername - Maven repository username. <BR/>
mavenRepoPassword - Maven repository password. <BR/>


The sample plugin tag in POM can look like,
```xml
<plugin>
	<groupId>org.singam.maven</groupId>
	<artifactId>FuseDeploy-maven-plugin</artifactId>
	<version>1.0.0</version>
	<executions>
		<execution>
			<phase>deploy</phase>
			<goals>
				<goal>deploy</goal>
			</goals>
		</execution>
	</executions>
	<configuration>
		<serverHost>127.0.0.1</serverHost>
		<serverPort>8101</serverPort>
		<jbossVersion>7.0</jbossVersion>
		<deploymentMode>MAVEN</deploymentMode>
		<mavenRepoServerUrl>http://localhost:8080/repository/MyRepo/</mavenRepoServerUrl>
		<mavenRepoUsername>admin</mavenRepoUsername>
		<mavenRepoPassword></mavenRepoPassword>
		<mavenRepoId></mavenRepoId>
		<deploymentArtifacts>
			<deploymentArtifact>AtomFeed</deploymentArtifact>
		</deploymentArtifacts>
		<sshUsername>admin</sshUsername>
		<sshPassword></sshPassword>
	</configuration>

</plugin>
```
The maven goal to upload the artifacts in Maven repository or directly to fuse server and start deploying it is FuseDeploy:deploy




