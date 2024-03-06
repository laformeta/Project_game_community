package com.cogamers.recaptcha;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.net.ssl.HttpsURLConnection;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecaptchaService {
 
	final RecaptchaConfig recaptchaConfig;
	
	public boolean verifyRecaptcha(String recaptcha) {
		String secretKey = recaptchaConfig.getSecret();
		String url = recaptchaConfig.getUrl();	
		
		 try {
	            URL obj = new URL(url);
	            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
	            con.setRequestMethod("POST");

	            String postParams = "secret=" + secretKey + "&response=" + recaptcha;
	            con.setDoOutput(true);

	            log.info("***********시크릿키 = {}", secretKey);
	            log.info("***********url = {}", url);
	            log.info("***********response키 = {}", postParams);
	            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
	            wr.writeBytes(postParams);
	            wr.flush();
	            wr.close();

	            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
	            String inputLine;
	            StringBuffer response = new StringBuffer();

	            while ((inputLine = in.readLine()) != null) {
	                response.append(inputLine);
	            }
	            in.close();

	            JsonReader jsonReader = Json.createReader(new StringReader(response.toString()));
	            JsonObject jsonObject = jsonReader.readObject();
	            jsonReader.close();
	            return jsonObject.getBoolean("success"); //최종 Return 값 : true or false

	        } catch (Exception e) {
	            e.printStackTrace();
	            return false;
	        }
	}
}
