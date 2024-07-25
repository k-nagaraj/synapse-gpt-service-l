package com.smartdocs.gpt.document.model;

import org.bson.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QnA {
	
	 private String question;
	    private String answer;
	    private boolean trained;
	    private String uuid;
	    
	    public static QnA fromDocument(Document doc) {
	        QnA qna = new QnA();
	        qna.setQuestion(doc.getString("question"));
	        qna.setAnswer(doc.getString("answer"));
	        qna.setTrained(doc.getBoolean("trained"));
	        qna.setUuid(doc.getString("uuid"));
	        return qna;
	    }

}
