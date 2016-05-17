package voting.ui;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class MyDocumentFilter extends DocumentFilter {
	
	//Фильтр для вводимых символов
	
	
	private int maxLenghtText;
	
	//Конструктор принимает значение максимального кол-ва символов
	 MyDocumentFilter(int maxLenghtText) throws BadLocationException{
		 this.maxLenghtText = maxLenghtText;
		 
	 }
	 		@Override
		 public void replace(
					FilterBypass fb, int offset, int lenght, String string, AttributeSet attr) throws BadLocationException{
				
	 			//проверка на позицию вводимого символа и длины вводимой строки
	 			// при превышении значения maxLenghtText все остальные символы игнорируются
	 			if(offset < maxLenghtText & string.length() + offset < maxLenghtText + 1){
					string = string.replaceAll("[^a-zA-Z0-9_-]", "");
					//разрешенные символы буквы, цифры, "_", "-"
					//все остальные заменяются на пустой символ ""
					
				fb.replace(offset, lenght, string, attr);}
	 }
}
