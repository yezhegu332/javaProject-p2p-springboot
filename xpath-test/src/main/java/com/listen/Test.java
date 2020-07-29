package com.listen;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;

/**
 * @Author: Listen
 * @Date: 2020/7/28
 */
public class Test {
    public static void main(String[] args) throws DocumentException {
        String xmlString =
                "<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n" +
                "<returnsms>\n" +
                "\t<returnstatus>Success</returnstatus>\n" +
                "\t<message>ok</message>\n" +
                "\t<remainpoint>-1111611</remainpoint>\n" +
                "\t<taskID>101609164</taskID>\n" +
                "\t<successCounts>1</successCounts>\n" +
                "</returnsms>";

        //将xml格式的字符串转换为document对象
        Document document = DocumentHelper.parseText(xmlString);

        Node node = document.selectSingleNode("/returnsms/returnstatus");
        System.out.println("node.getDocument() = " + node.getDocument());
        System.out.println("node.getName() = " + node.getName());
        System.out.println("node.getNodeType() = " + node.getNodeType());
        System.out.println("node.getNodeTypeName() = " + node.getNodeTypeName());
        System.out.println("node.getParent() = " + node.getParent());
        System.out.println("node.getPath() = " + node.getPath());
        System.out.println("node.getStringValue() = " + node.getStringValue());
        System.out.println("node.getText() = " + node.getText());



    }
}
