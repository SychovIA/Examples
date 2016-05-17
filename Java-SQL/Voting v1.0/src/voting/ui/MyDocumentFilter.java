package voting.ui;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class MyDocumentFilter extends DocumentFilter {
	
	//������ ��� �������� ��������
	
	
	private int maxLenghtText;
	
	//����������� ��������� �������� ������������� ���-�� ��������
	 MyDocumentFilter(int maxLenghtText) throws BadLocationException{
		 this.maxLenghtText = maxLenghtText;
		 
	 }
	 		@Override
		 public void replace(
					FilterBypass fb, int offset, int lenght, String string, AttributeSet attr) throws BadLocationException{
				
	 			//�������� �� ������� ��������� ������� � ����� �������� ������
	 			// ��� ���������� �������� maxLenghtText ��� ��������� ������� ������������
	 			if(offset < maxLenghtText & string.length() + offset < maxLenghtText + 1){
					string = string.replaceAll("[^a-zA-Z0-9_-]", "");
					//����������� ������� �����, �����, "_", "-"
					//��� ��������� ���������� �� ������ ������ ""
					
				fb.replace(offset, lenght, string, attr);}
	 }
}
