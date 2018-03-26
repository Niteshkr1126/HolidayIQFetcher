package org.jsoup;

import java.io.BufferedReader;
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

public class TripAdvisorFetchReviews {
	
	private static class DefaultTrustManager implements X509TrustManager {

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

	public static void main(String[] args) throws Exception {
		try {
			SSLContext ctx = SSLContext.getInstance("TLS");
			ctx.init(new KeyManager[0], new TrustManager[] { new DefaultTrustManager() }, new SecureRandom());
			SSLContext.setDefault(ctx);
			URL url = new URL(
					"https://www.tripadvisor.in/Hotel_Review-g297608-d3504960-Reviews-Radisson_Blu_Hotel_Ahmedabad-Ahmedabad_Ahmedabad_District_Gujarat.html");
			Proxy proxy = new Proxy(Proxy.Type.HTTP,
					new InetSocketAddress("xxxxxxxxx", 80));
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection(proxy);
			conn.connect();
			String line = null;
			StringBuffer tmp = new StringBuffer();
			HtmlCleaner htmlCleaner = new HtmlCleaner();
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			while ((line = in.readLine()) != null) {
				tmp.append(line);
			}
			TagNode[] root = htmlCleaner.clean(String.valueOf(tmp)).getElementsByName("div", true);
			for (TagNode reviewcontainer : root) {
				if (reviewcontainer.getAttributeByName("class") != null) {
					if (reviewcontainer.getAttributeByName("class").equalsIgnoreCase("ui_column is-9")) {
						TagNode allreviews = reviewcontainer.findElementByAttValue("class", "prw_rup prw_reviews_text_summary_hsx", true, true);
						TagNode firstreview = allreviews.findElementByName("p", true);
						if (allreviews.getAttributeByName("class") != null) {
							if (firstreview.getAttributeByName("class").equalsIgnoreCase("partial_entry")) {
								System.out.println(firstreview.getText());
							}
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
