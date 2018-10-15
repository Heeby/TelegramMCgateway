package ga.mmme.tiki.TelegramMCgateway.Telegram;

public class Chat {
	private long id;
	private String type;
	private String title;
	private String username;
	private String first_name;
	private String last_name;
	private String invite_link;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getFirst_name() {
		return first_name;
	}
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	public String getLast_name() {
		return last_name;
	}
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	public String getInvite_link() {
		return invite_link;
	}
	public void setInvite_link(String invite_link) {
		this.invite_link = invite_link;
	}
}
