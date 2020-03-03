package org.isf.sms.providers;

/*
    If you use maven, add the folowing dependency to your pom.xml.
    <dependency>
        <groupId>org.apache.httpcomponents</groupId>
        <artifactId>httpclient</artifactId>
        <version>4.1.1</version>
    </dependency>

    Otherwise download Apache HttpComponents from http://hc.apache.org/
    and add the libs to your classpath.
*/

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParamBean;
import org.apache.http.util.EntityUtils;
import org.isf.generaldata.SmsParameters;
import org.isf.sms.model.Sms;
import org.isf.sms.service.SmsSenderInterface;
import org.isf.utils.sms.Ping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mwithi
 * Starting from example model at http://www.skebby.it/business/index/code-examples/
 *
 */
public class SkebbyGateway implements SmsSenderInterface {

	private static Logger logger = LoggerFactory.getLogger(SkebbyGateway.class);
	
	public static void main(String[] args) throws Exception {
//      Single dispatch
//      String [] recipients = new String[]{"391234567890"};
//      Multiple dispatch
        String [] recipients = new String[]{"391234567890","391234567891"};

        String username = "username";
        String password = "password";

        // SMS CLASSIC dispatch with custom alphanumeric sender
        String result = skebbyGatewaySendSMS(username, password, recipients, "Hi Mike, how are you?", "send_sms_classic", null, "John");
        
        // SMS Basic dispatch
        // String result = skebbyGatewaySendSMS(username, password, recipients, "Hi Mike, how are you?", "send_sms_basic", null, null);
         
        // SMS CLASSIC dispatch with custom numeric sender
        // String result = skebbyGatewaySendSMS(username, password, recipients, "Hi Mike, how are you?", "send_sms_classic", "393471234567", null);
         
        // SMS CLASSIC PLUS dispatch (with delivery report) with custom alphanumeric sender
        // String result = skebbyGatewaySendSMS(username, password, recipients, "Hi Mike, how are you?", "send_sms_classic_report", null, "John");

        // SMS CLASSIC PLUS dispatch (with delivery report) with custom numeric sender
        // String result = skebbyGatewaySendSMS(username, password, recipients, "Hi Mike, how are you?", "send_sms_classic_report", "393471234567", null);

		// ------------------------------------------------------------------
		// Check the complete documentation at http://www.skebby.com/business/index/send-docs/
		// ------------------------------------------------------------------
		// For eventual errors see http:#www.skebby.com/business/index/send-docs/#errorCodesSection
		// WARNING: in case of error DON'T retry the sending, since they are blocking errors
		// ------------------------------------------------------------------	
        logger.debug("result: "+result);
    }
    
    /**
     * 
     * @param username
     * @param password
     * @param recipients
     * @param text
     * @param smsType
     * @param senderNumber
     * @param senderString
     * @return
     * @throws IOException
     */
    public static String skebbyGatewaySendSMS(String username, String password, String [] recipients, String text, String smsType, String senderNumber, String senderString) throws IOException{
        return skebbyGatewaySendSMS(username, password, recipients, text, smsType,  senderNumber,  senderString, "UTF-8");
    }
    
    protected static String skebbyGatewaySendSMS(String username, String password, String [] recipients, String text, String smsType, String senderNumber, String senderString, String charset) throws IOException{
        
        if (!charset.equals("UTF-8") && !charset.equals("ISO-8859-1")) {
        
            throw new IllegalArgumentException("Charset not supported.");
        }
        
        String endpoint = "http://gateway.skebby.it/api/send/smseasy/advanced/http.php";
        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, 10*1000);
        DefaultHttpClient httpclient = new DefaultHttpClient(params);
        HttpProtocolParamBean paramsBean = new HttpProtocolParamBean(params);
        paramsBean.setVersion(HttpVersion.HTTP_1_1);
        paramsBean.setContentCharset(charset);
        paramsBean.setHttpElementCharset(charset);
        
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("method", smsType));
        formparams.add(new BasicNameValuePair("username", username));
        formparams.add(new BasicNameValuePair("password", password));
        if(null != senderNumber)
            formparams.add(new BasicNameValuePair("sender_number", senderNumber));
        if(null != senderString)
            formparams.add(new BasicNameValuePair("sender_string", senderString));
        
        for (String recipient : recipients) {
            formparams.add(new BasicNameValuePair("recipients[]", recipient));
        }
        formparams.add(new BasicNameValuePair("text", text));
        formparams.add(new BasicNameValuePair("charset", charset));

    
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, charset);
        HttpPost post = new HttpPost(endpoint);
        post.setEntity(entity);
        
        HttpResponse response = httpclient.execute(post);
        HttpEntity resultEntity = response.getEntity();
        if(null != resultEntity){
            return EntityUtils.toString(resultEntity);
        }
        return null;
    }

	@Override
	public boolean sendSMS(Sms sms, boolean debug) {
		String username = SkebbyParameters.USR;
		String password = SkebbyParameters.PWD;
		String sender_number = SkebbyParameters.SENDER_NUMBER.equals("") ? null : SkebbyParameters.SENDER_NUMBER;
		String sender_string = SkebbyParameters.SENDER_STRING.equals("") ? null : SkebbyParameters.SENDER_STRING;
		String internationalNumberFormat = sms.getSmsNumber().replace("+", "");
		if (debug) {
			username = ""; password = "";
		}
		String result = null;
		try {
			result = skebbyGatewaySendSMS(username, password, new String[]{internationalNumberFormat}, sms.getSmsText(), SkebbyParameters.TYPE, sender_number, sender_string);
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.debug(result);
		return debug || result.contains("status=success");
	}

	@Override
	public boolean initialize() {
		SkebbyParameters.getSkebbyParameters();
		String url = SkebbyParameters.URL;
		if (url.equals("")) {
			logger.error("No HTTP URL has been set for the Gateway: " + SmsParameters.GATEWAY);
			logger.error("Please check Skebby.properties file");
			return false;
		}
		return Ping.ping(url, SmsParameters.TIMEOUT);
	}
}
					