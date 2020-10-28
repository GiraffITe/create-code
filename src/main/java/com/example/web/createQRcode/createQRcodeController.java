package com.example.web.createQRcode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.service.ContentErrorCheckService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.ITFWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

@Controller
public class createQRcodeController {
	
	@Autowired
	private ContentErrorCheckService errorCheck;
	
	@ModelAttribute(value = "createCode")
	public CreateCode setCreateCode() {
		return new CreateCode();
	}
	
	@RequestMapping(value="create-qrcode")
	public String createCode() {
		return "view/createQRcode";
	}
	
	@RequestMapping(value="code")
	public String reCreateCode(@RequestParam("kindCode") String kindCode, @Validated @ModelAttribute("createCode") CreateCode contents
			,BindingResult result,Model model){
		
		if(kindCode.equals("TIFバーコード")) {
			result = errorCheck.itfErrorCheck(result, contents.getContents());
		}else {
			
		}
		
		
		if(result.hasErrors()) {
			return "view/createQRcode";
		}else {
			
			try {
				
				if(kindCode.equals("TIFバーコード")) {
					byte[] res = toByteArrayITF(contents.getContents());
		            String encodedStr64 = Base64.encodeBase64String(res);
		            model.addAttribute("dataUrl", encodedStr64);
		            
				}else {
					byte[] res = toByteArrayQR(contents.getContents());
		            String encodedStr64 = Base64.encodeBase64String(res);
		            model.addAttribute("dataUrl", encodedStr64);
				}
				
				contents.setFlag(true);
	            model.addAttribute("flag",contents.isFlag());     
				
			}
			catch(IOException e) {
				e.printStackTrace();
			}
			catch(WriterException e) {
				e.printStackTrace();
			}
			
			return "view/createQRcode";
		}
			
	}
	
	private byte[] toByteArrayITF(String contents) throws IOException, WriterException{
		BarcodeFormat format = BarcodeFormat.ITF;
		int width = 200;
		int height = 50;    
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);

        try (ByteArrayOutputStream output = new ByteArrayOutputStream()){
        	ITFWriter writer = new ITFWriter();
            BitMatrix bitMatrix = writer.encode(contents, format, width, height, hints);
            MatrixToImageWriter.writeToStream(bitMatrix,  "png",  output);
            return output.toByteArray();
        }
    }
	
	private byte[] toByteArrayQR(String contents) throws IOException, WriterException{
		BarcodeFormat format = BarcodeFormat.QR_CODE;
		int width = 160;
		int height =160;    
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);

        try (ByteArrayOutputStream output = new ByteArrayOutputStream()){
        	QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bitMatrix = writer.encode(contents, format, width, height, hints);
            MatrixToImageWriter.writeToStream(bitMatrix,  "png",  output);
            return output.toByteArray();
        }
    }
	
	@RequestMapping(value="save")
	public String saveImage(@Validated @ModelAttribute("saveImage") SaveImage saveImage
			,BindingResult result,Model model){
		
		System.out.println(saveImage.getDateUrl());
		System.out.println(saveImage.getFileUrl());
		
		
		return "view/createQRcode";
		
	}
	
	
}
