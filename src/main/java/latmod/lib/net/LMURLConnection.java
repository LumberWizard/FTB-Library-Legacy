package latmod.lib.net;

import latmod.lib.*;

import java.io.*;
import java.net.*;

public class LMURLConnection
{
	public final RequestMethod type;
	public final String url;
	public final ByteIOStream data;
	
	public LMURLConnection(RequestMethod t, String s)
	{
		type = t;
		url = s;
		data = new ByteIOStream();
	}
	
	public Response connect() throws Exception
	{
		if(type == null) return null;
		
		long startTime = LMUtils.millis();
		
		if(type == RequestMethod.SIMPLE_GET)
		{
			URL con = new URL(url);
			InputStream is = con.openStream();
			return new Response(LMUtils.millis() - startTime, 200, is);
		}
		
		HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
		con.setRequestMethod(type.name());
		con.setRequestProperty("User-Agent", "HTTP/1.1");
		con.setDoInput(true);
		
		if(data.getDataPos() > 0)
		{
			//System.out.println("Sending '" + con.getRequestMethod() + "' data '" + new String(data.toByteArray()) + "'");
			con.setDoOutput(true);
			OutputStream os = con.getOutputStream();
			os.write(data.toByteArray());
			os.flush();
			os.close();
		}
		
		int responseCode = con.getResponseCode();
		return new Response(LMUtils.millis() - startTime, responseCode, con.getInputStream());
	}
}