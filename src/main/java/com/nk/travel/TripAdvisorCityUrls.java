package org.jsoup;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

public class TripAdvisorCityUrls {

	private static final String city_urls_writer = ".\\input\\City_Urls.txt";

	public static void main(String[] args) throws Exception {
		
		FileWriter fw = new FileWriter(city_urls_writer);
		BufferedWriter bw = new BufferedWriter(fw);
		
		try {
			SSLContext ctx = SSLContext.getInstance("TLS");
			ctx.init(new KeyManager[0], new TrustManager[] { new DefaultTrustManager() }, new SecureRandom());
			SSLContext.setDefault(ctx);
			URL url = new URL("https://www.tripadvisor.in/");
			Proxy proxy = new Proxy(Proxy.Type.HTTP,
					new InetSocketAddress("xxxxx", 80));
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection(proxy);
			conn.connect();
			String line = null;
			StringBuffer tmp = new StringBuffer();
			HtmlCleaner htmlCleaner = new HtmlCleaner();
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			while ((line = in.readLine()) != null) {
				tmp.append(line);
			}
			TagNode[] root = htmlCleaner.clean(String.valueOf(tmp)).getElementsByName("a", true);
			for (TagNode cityUrls : root) {
				if (cityUrls.getAttributeByName("class") != null) {
					if (cityUrls.getAttributeByName("class").equalsIgnoreCase("ui_link")) {
						String cityurl = "https://www.tripadvisor.in" + cityUrls.getAttributeByName("href");
						bw.write(cityurl+"\n");
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();
				if (fw != null)
					fw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	public static class DefaultTrustManager implements X509TrustManager {

		@Override
		public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
		}

		@Override
		public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
	}
}
