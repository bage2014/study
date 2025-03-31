package com.bage.study.pdf;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;

import java.io.File;
import java.io.IOException;

public class PdfFormFiller {
    public static void main(String[] args) throws IOException {
        // 加载包含表单的PDF文件
        PDDocument document = PDDocument.load(new File("form_template.pdf"));
        
        // 获取表单
        PDAcroForm acroForm = document.getDocumentCatalog().getAcroForm();
        
        if (acroForm != null) {
            // 填写表单字段
            PDField field = acroForm.getField("name_field"); // 替换为您的字段名
            if (field != null) {
                field.setValue("张三");
            }
            
            field = acroForm.getField("address_field");
            if (field != null) {
                field.setValue("北京市朝阳区");
            }
            
            field = acroForm.getField("phone_field");
            if (field != null) {
                field.setValue("13800138000");
            }
            
            // 设置所有字段为只读，防止进一步编辑
            acroForm.flatten();
        }
        
        // 保存填写后的PDF
        document.save("filled_form.pdf");
        document.close();
    }
}