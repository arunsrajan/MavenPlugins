package org.singam.maven.plugin;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Goal which touches a timestamp file.
 */
@Mojo( name = "generatesobjects", defaultPhase = LifecyclePhase.PROCESS_SOURCES )
public class CamelSalesforceDTOMojo
    extends AbstractMojo
{
    /**
     * Location of the file.
     */
    @Parameter( defaultValue = "${project.build.directory}", property = "outputDir", required = true )
    private File outputDirectory;

    @Parameter(required = true)
    private String salesforceClientId;
    
    @Parameter(required = true)
    private String salesforceClientSecret;
    
    @Parameter(required = true)
    private String salesforceLoginUrl;
    
    @Parameter(required = true)
    private String salesforceUsername;
    
    @Parameter(required = true)
    private String salesforcePassword;
    
    @Parameter(required = true)
    private List<String> sObjects;
    
    @Parameter(defaultValue="v33.0")
    private String salesforceApiVersion;
    
    @Parameter(defaultValue="org.singam.salesforce.dto")
    private String javaPackage;
    
    public void execute()
        throws MojoExecutionException
    {
        File f = outputDirectory;

        try
        {
        	if(salesforceClientId ==null ) throw new Exception("Salesforce Client Id should be provided");
        	else if(salesforceClientSecret ==null ) throw new Exception("Salesforce Client Secret should be provided");
        	else if(salesforceLoginUrl ==null ) throw new Exception("Salesforce Login Url should be provided");
        	else if(salesforceUsername ==null ) throw new Exception("Salesforce Username should be provided");
        	else if(salesforcePassword ==null ) throw new Exception("Salesforce Password should be provided");
        	String token = login();
        	
        	if(token ==null)throw new Exception("Unable to obtain token for generating sources"); 
        	
            List<SalesforceObjectMetadata> sObjects=generate(token);
            
            generateJavaClasses(sObjects);
        }
        catch ( Exception e )
        {
            throw new MojoExecutionException( "Error creating Java Sources:\n", e );
        }
        finally
        {
        }
    }
    
    protected void generateJavaClasses(List<SalesforceObjectMetadata> sObjects) {
    	outputDirectory.mkdirs();
    	File javaPackagePath = new File(outputDirectory.getAbsolutePath()+"/"+javaPackage.replace(".", "/"));
    	javaPackagePath.mkdirs();
    	
    	try
    	{
    		VelocityEngine velocityEngine = new VelocityEngine();
    		velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath"); 
    		velocityEngine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
    		final Template template = velocityEngine.getTemplate("/sObject-Gen.vm");
    		final Template enumTemplate = velocityEngine.getTemplate("/sObject-EnumGen.vm");
    		StringWriter sw = new StringWriter();
      		sObjects.stream().forEach(sObject->{
  	    	VelocityContext context = new VelocityContext();
  	    	context.put( "name", sObject.getName() );
  	    	context.put("fields",sObject.getFields());
  	    	context.put("packageName",javaPackage);
  	    	template.merge( context, sw );
  	    	try {
				FileOutputStream fout = new FileOutputStream(new File(javaPackagePath.getAbsolutePath()+"/"+sObject.getName()+".java"));
				fout.write(sw.getBuffer().toString().getBytes());
				fout.close();
				if(sObject.getFields()!=null) {
					List<Fields> fields = Arrays.asList(sObject.getFields());
					fields.stream().filter(field->field.getType().equals("picklist")).forEach(field->{
						try {
							VelocityContext velCtx = new VelocityContext();
							velCtx.put( "name", field.getName() );
							velCtx.put("pickListValues",field.getPicklistValues());
							velCtx.put("packageName",javaPackage);
							long size = field.getPicklistValues().length;
							velCtx.put("size",size);
							StringWriter enumFileGenerator = new StringWriter();
							enumTemplate.merge(velCtx, enumFileGenerator);
							String name=field.getName();
							name = name.endsWith("__c")?name.replace("__c", "")+"Custom":name;
							FileOutputStream enumFile = new FileOutputStream(new File(javaPackagePath.getAbsolutePath()+"/"+name+"Enum.java"));
							enumFile.write(enumFileGenerator.getBuffer().toString().getBytes());
							enumFile.close();
						}
						catch(Exception ex) {
							ex.printStackTrace();
						}
					});
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
      	});
    	}
    	catch( ResourceNotFoundException rnfe )
    	{
    	  // couldn't find the template
    	}
    	catch( ParseErrorException pee )
    	{
    	  // syntax error: problem parsing the template
    	}
    	catch( MethodInvocationException mie )
    	{
    	  // something invoked in the template
    	  // threw an exception
    	}
    	catch( Exception e )
    	{}

    	
    }
    
    protected String login() {
    	HttpClient httpclient = new HttpClient();
		httpclient.getParams().setParameter(HttpClientParams.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);

		PostMethod post = new PostMethod(salesforceLoginUrl + "/services/oauth2/token");
		post.addParameter("grant_type", "password");
		post.addParameter("client_id", salesforceClientId);
		post.addParameter("client_secret", salesforceClientSecret);
		post.addParameter("username", salesforceUsername);
		post.addParameter("password", salesforcePassword);

		try {
			httpclient.executeMethod(post);
			JSONObject authResponse = new JSONObject(
					new JSONTokener(new InputStreamReader(post.getResponseBodyAsStream())));
			System.out.println("Auth Response :-" + authResponse.toString(2));
			
			return authResponse.getString("access_token");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception during Connect" + e);
		}
    	return null;
    }
    
    protected List<SalesforceObjectMetadata> generate(String accessToken) throws Exception {
    	List<SalesforceObjectMetadata> salesforceObjects = new ArrayList<>();
    	try {
    		
    		for(String sObjectName:sObjects) {
		    	String restUrlGetMetadata = salesforceLoginUrl + "/services/data/"+salesforceApiVersion+"/sobjects/"+sObjectName+"/describe";
		    	System.out.println("Generating Salesforce Object for the URL: "+restUrlGetMetadata);
		    	GetMethod get = new GetMethod(restUrlGetMetadata);
				get.addRequestHeader("Authorization","Bearer "+ accessToken);
				HttpClient httpclient = new HttpClient();
				httpclient.executeMethod(get);
				JSONObject authResponse = new JSONObject(
						new JSONTokener(new InputStreamReader(get.getResponseBodyAsStream())));
				ObjectMapper objMapper = new ObjectMapper();
				SalesforceObjectMetadata sObject=objMapper.readValue(authResponse.toString(), SalesforceObjectMetadata.class);
				salesforceObjects.add(sObject);
    		}
    	}
    	catch(Exception ex) {
    		throw new MojoExecutionException("Exception in generating salesforce DTO objects. Please check below the cause.\n\n",ex);
    	}
    	finally {
    		return salesforceObjects;
    	}
    }
    
}
