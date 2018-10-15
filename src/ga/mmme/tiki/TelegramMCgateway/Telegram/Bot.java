package ga.mmme.tiki.TelegramMCgateway.Telegram;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;

import org.bukkit.plugin.java.JavaPlugin;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;


public class Bot {
	private String token;
	private int offset;
	private Gson gson = new Gson();
	private long groupChatId;
	private JavaPlugin plugin;
	
	public Bot(String token, int offset, long groupChatId, JavaPlugin plugin) {
		this.token = token;
		this.offset = offset;
		this.groupChatId = groupChatId;
		this.plugin = plugin;
	}
	public void sendMessage(final long chat_id, final String text, final int parsemode) {
		URL url;
		try {
			url = new URL(String.format("https://api.telegram.org/bot%s/sendMessage",token));
			plugin.getServer().getScheduler().runTaskAsynchronously(plugin, ()->{
				try {
					HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
					conn.setDoOutput(true);
					conn.setRequestMethod("POST");
					conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
					OutputStream os = conn.getOutputStream();
					OutputStreamWriter osw = new OutputStreamWriter(os,StandardCharsets.UTF_8);
					JsonWriter jsonw = new JsonWriter(osw);
					jsonw.beginObject();
					jsonw.name("chat_id");
					jsonw.value(chat_id);
					jsonw.name("text");
					jsonw.value(text);
					switch (parsemode) {
					case 2:
						jsonw.name("parse_mode");
						jsonw.value("HTML");
						break;
					case 1:
						jsonw.name("parse_mode");
						jsonw.value("MARKDOWN");
						break;
					default:
						break;
					}
					jsonw.endObject();
					jsonw.flush();
					jsonw.close();
					osw.close();
					os.close();
					conn.getResponseCode();
					conn.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	public void sendMessage(String text, ParseModes pm) throws IOException {
		sendMessage(groupChatId,text,pm.ordinal());
	}
	public void sendMessage(String text, int pm) throws IOException {
		sendMessage(groupChatId,text,pm);
	}
//	public Update[] getUpdates(int offset) throws IOException {
//		URL url = new URL(String.format("https://api.telegram.org/bot%s/getUpdates?offset=%d",token,offset));
//		HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
//		conn.connect();
//		if(conn.getResponseCode() == HttpsURLConnection.HTTP_OK) {
//			JsonParser parser = new JsonParser();
//			JsonObject jsonObj = parser.parse(new InputStreamReader(conn.getInputStream())).getAsJsonObject();
//			Update[] ret = gson.fromJson(jsonObj.getAsJsonArray("result"), Update[].class);
//			conn.disconnect();
//			return ret;
//		}
//		else {
//			conn.disconnect();
//			return new Update[0];
//		}
//		
//	}
	public Update[] getUpdates() throws IOException {
		URL url = new URL(String.format("https://api.telegram.org/bot%s/getUpdates?offset=%d",token,this.offset));
		//?offset=%d
		HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
		conn.connect();
		if(conn.getResponseCode() == HttpsURLConnection.HTTP_OK) {
			JsonParser parser = new JsonParser();
			InputStream is = conn.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			JsonObject jsonObj = parser.parse(isr).getAsJsonObject();
			//System.out.println(jsonObj.toString());
			Update[] ret = gson.fromJson(jsonObj.getAsJsonArray("result"), Update[].class);
			isr.close();
			is.close();
			conn.disconnect();
			if(ret.length!=0) {
				this.offset = ret[ret.length-1].getUpdate_id()+1;
			}
			return ret;
		}
		else {
			conn.disconnect();
			return new Update[0];
		}
	}
}
