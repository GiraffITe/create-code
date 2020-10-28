package com.example.web.createQRcode;

import javax.validation.constraints.NotEmpty;

public class CreateCode {

	@NotEmpty
	private String contents;
	private boolean flag;

	public CreateCode() {
		contents = "";
		flag = false;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

}
