package org.jsoup;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
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


public class TripAdvisorFetchHotelsByCity {

	private static final String input_city_urls = ".\\input\\City_Urls.txt";
	private static final String output_hotel_urls = ".\\input\\Hotels_Urls.txt";

	String readFile() throws IOException {
		String sCityUrl;
		FileReader fr = new FileReader(input_city_urls);
		BufferedReader br = new BufferedReader(fr);
		
		try {
			while ((sCityUrl = br.readLine()) != null) {
				break;
			}
		} finally {
			try {
				if (fr != null)
					fr.close();
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return sCityUrl;
	}
	
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
		//TripAdvisorFetchHotelsByCity rf  = new TripAdvisorFetchHotelsByCity();
		//String cityurl = rf.readFile();
		//FileWriter fw = new FileWriter(output_hotel_urls);
		//BufferedWriter bw = new BufferedWriter(fw);
		try {
			SSLContext ctx = SSLContext.getInstance("TLS");
			ctx.init(new KeyManager[0], new TrustManager[] { new DefaultTrustManager() }, new SecureRandom());
			SSLContext.setDefault(ctx);
			URL url = new URL("https://www.tripadvisor.in/Hotel_Review-g297608-d3504960-Reviews-Radisson_Blu_Hotel_Ahmedabad-Ahmedabad_Ahmedabad_District_Gujarat.html");
			Proxy proxy = new Proxy(Proxy.Type.HTTP,
					new InetSocketAddress("xxxxxxxxxxxxxxxxxxx", 80));
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
			for (TagNode hotelLists : root) {
				if (hotelLists.getAttributeByName("id") != null) {
					if (hotelLists.getAttributeByName("id").equalsIgnoreCase("taplc_hsx_hotel_list_lite_dusty_hotels_combined_sponsored_0")) {
						TagNode[] hotelUrls = hotelLists.getElementsByName("a", true);
						for (TagNode hotelUrl : hotelUrls) {
							if (hotelUrl.getAttributeByName("class") != null) {
								if (hotelUrl.getAttributeByName("class").equalsIgnoreCase("property_title prominent")) {
									String hotellink = "https://www.tripadvisor.in" + hotelUrl.getAttributeByName("href");
									System.out.println(hotellink);
									//bw.write(hotellink+"\n");
								}
							}
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} /*finally {
			try {
				if (bw != null)
					bw.close();
				if (fw != null)
					fw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}*/
	}
}
