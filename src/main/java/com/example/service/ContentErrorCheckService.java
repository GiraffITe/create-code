package com.example.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
public class ContentErrorCheckService {

	public BindingResult itfErrorCheck(BindingResult result, String content) {

		for (int i = 0; i < content.length(); i++) {
			if (Character.isDigit(content.charAt(i))) {
				continue;
			} else {
				result.reject("model");
				break;
			}
		}

		if (content.length() > 14) {
			result.reject("digits");
		}
		return result;
	}

}
