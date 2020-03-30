package beans;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

public class CreateApiRequest {
	private HttpURLConnection con;
	
	
	public CreateApiRequest(String method, String inUrl, Map<String, String> parameters) throws Exception {
		//Create params string
		StringBuilder paramStringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
          paramStringBuilder.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
          paramStringBuilder.append("=");
          paramStringBuilder.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
          paramStringBuilder.append("&");
        }
        String paramString = paramStringBuilder.toString();
        paramString = paramString.length() > 0 ? paramString.substring(0, paramString.length() - 1) : paramString;
        
        //Create Request
		URL url = parameters.size() == 0 ? new URL(inUrl) : new URL(inUrl + "?" + paramString);
		HttpURLConnection con = (HttpURLConnection) (url).openConnection();
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setRequestMethod(method);
        con.setRequestProperty("Content-Type", "application/json");
        
		this.con = con;
		/*
        POST write body
        con.getOutputStream().write(jsonBodyString.getBytes());
        */
        
        /*
		GET Read response
		int status = con.getResponseCode();
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer content = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
		    content.append(inputLine);
		}
		in.close();
		con.disconnect();
		String rep = content.toString();
		*/
		
	}
	public HttpURLConnection getCon() {return con;}
}
