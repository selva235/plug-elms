package com.example.controller;


import org.apache.http.client.utils.URIBuilder;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;


enum urlParameter 
{ 
	Action, DurationSeconds, Version, RoleSessionName, RoleArn, WebIdentityToken, SessionToken,
	AccessKeyId, SecretAccessKey, SessionType, Session, SigninToken, Issuer, Destination,
	sessionId, sessionKey, sessionToken; 
} 

@RestController
public class TestController {
	
	@Value("${aws.roleCredUrl}")
	private String roleCredUrl;
	
	@Value("${aws.signInURL}")
	private String signInURL;
	
	@Value("${aws.consoleURL}")
	private String consoleURL;
	
	@Value("${oidc.issuerURL}")
	private String issuerURL;	
	
	@Value("${aws.roleArn}")
	private String roleArn;

	@Value("${aws.roleName}")
	private String roleName;
	
	@Value("${aws.roleAction}")
	private String roleAction;
	
	@Value("${aws.roleDurationSec}")
	private String roleDurationSec;
	
	@Value("${aws.roleVersion}")
	private String roleVersion;
	
	@Value("${aws.sessionTokenAction}")
	private String sessionTokenAction;
	
	@Value("${aws.sessionTokenDuration}")
	private String sessionTokenDuration;
	
	@Value("${aws.sessionTokenType}")
	private String sessionTokenType;
	
	@Value("${aws.loginAction}")
	private String loginAction;
	
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@GetMapping(value = "/loginUrl")
	public ResponseEntity<String> testcode(HttpServletRequest req) throws Exception {
		
		
		try {
		
		System.out.println("ID TOKEN    :"+req.getQueryString().replace("id_token=", ""));
		String idtoken = req.getQueryString().replace("id_token=", "");

		URIBuilder roleUrlBuilder = new URIBuilder();
		roleUrlBuilder.setScheme("https");
		roleUrlBuilder.setHost(this.roleCredUrl);
		roleUrlBuilder.addParameter(urlParameter.Action.name(), this.roleAction);
		roleUrlBuilder.addParameter(urlParameter.DurationSeconds.name(), this.roleDurationSec);
		roleUrlBuilder.addParameter(urlParameter.RoleSessionName.name(), this.roleName);
		roleUrlBuilder.addParameter(urlParameter.RoleArn.name(), this.roleArn);
		roleUrlBuilder.addParameter(urlParameter.WebIdentityToken.name(), idtoken);
		roleUrlBuilder.addParameter(urlParameter.Version.name(), this.roleVersion);

		
		System.out.println("\n\nROLE URL"+roleUrlBuilder.build().toURL().toString()+"\n\n");
		
		URL roleUrl = roleUrlBuilder.build().toURL();
		URLConnection roleConn = roleUrl.openConnection ();
		BufferedReader roleBufferReader = new BufferedReader(new 
		InputStreamReader(roleConn.getInputStream()));  		
		String  singleLineofXml = null;
		StringBuilder strFullXmlResponse =new StringBuilder();
		
		while((singleLineofXml = roleBufferReader.readLine())!= null){
			strFullXmlResponse.append(singleLineofXml);
	    }
		
		System.out.println("LENGT :"+ strFullXmlResponse.toString().getBytes().length);
		InputStream streamOfXmlResponse = new ByteArrayInputStream(strFullXmlResponse.toString().getBytes());

		String SessionToken = null;
		String AccessKeyId = null;
		String SecretAccessKey = null;
		
		
		try {

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(streamOfXmlResponse);
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("AssumeRoleWithWebIdentityResponse");
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				Element eElement = (Element) nNode;
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					 SessionToken = eElement.getElementsByTagName(urlParameter.SessionToken.name()).item(0).getTextContent();
					 AccessKeyId = eElement.getElementsByTagName(urlParameter.AccessKeyId.name()).item(0).getTextContent();
					 SecretAccessKey = eElement.getElementsByTagName(urlParameter.SecretAccessKey.name()).item(0).getTextContent();
					System.out.println("SessionToken      " + SessionToken);
					System.out.println("AccessKeyId       " + AccessKeyId);
					System.out.println("SecretAccessKey   " + SecretAccessKey);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	String sessionJson = String.format(
	  "{\"%1$s\":\"%2$s\",\"%3$s\":\"%4$s\",\"%5$s\":\"%6$s\"}",
	  urlParameter.sessionId.name(), AccessKeyId,
	  urlParameter.sessionKey.name(), SecretAccessKey,
	  urlParameter.sessionToken.name(), SessionToken);

	URIBuilder SignTokenUrlBuilder = new URIBuilder();
	SignTokenUrlBuilder.setScheme("https");
	SignTokenUrlBuilder.setHost(this.signInURL);
	SignTokenUrlBuilder.setPath("/federation");
	SignTokenUrlBuilder.addParameter(urlParameter.Action.name(), this.sessionTokenAction);
	SignTokenUrlBuilder.addParameter(urlParameter.DurationSeconds.name(), this.sessionTokenDuration);
	SignTokenUrlBuilder.addParameter(urlParameter.SessionType.name(), this.sessionTokenType);
	SignTokenUrlBuilder.addParameter(urlParameter.Session.name(), sessionJson);

	System.out.println("\n\nROLE URL"+SignTokenUrlBuilder.build().toURL().toString()+"\n\n");

	URL SignTokenurl = SignTokenUrlBuilder.build().toURL();
	URLConnection SignTokenConn = SignTokenurl.openConnection ();
	BufferedReader SignTokenBufferReader = new BufferedReader(new 
	  InputStreamReader(SignTokenConn.getInputStream()));  
	String returnContent = SignTokenBufferReader.readLine();

	String signinToken = new JSONObject(returnContent).getString("SigninToken");

	URIBuilder loginUrlBuilder = new URIBuilder();
	loginUrlBuilder.setScheme("https");
	loginUrlBuilder.setHost(this.signInURL);
	loginUrlBuilder.setPath("/federation");
	loginUrlBuilder.addParameter(urlParameter.Action.name(), this.loginAction);
	loginUrlBuilder.addParameter(urlParameter.SigninToken.name(), signinToken);
	loginUrlBuilder.addParameter(urlParameter.Issuer.name(), this.issuerURL);
	loginUrlBuilder.addParameter(urlParameter.Destination.name(), this.consoleURL);
	URL loginUrl = loginUrlBuilder.build().toURL();

	System.out.println("\n\nloginURL"+loginUrl.toString()+"\n\n");
	return new ResponseEntity<>(loginUrl.toString(), HttpStatus.OK);
	
		}catch(Exception e) {
			
			return new ResponseEntity<>("ERROR_INCODE "+e.getMessage(), HttpStatus.METHOD_FAILURE);
		}

		
	}
		

	
	
}
