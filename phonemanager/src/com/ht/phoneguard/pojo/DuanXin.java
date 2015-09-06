package com.ht.phoneguard.pojo;



public class DuanXin {
    private int id;
    private String phonenumber;
    private String time;
    private String name;
    private String Type;
    public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	private  String content;
    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
