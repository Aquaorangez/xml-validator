package xmlvalidator;

import static java.lang.System.*;
import static sbcc.Core.*;

import java.io.*;
import java.util.regex.*;

public class BasicXmlValidator implements XmlValidator {

	private BasicStringStack xmlDoc = new BasicStringStack();
	private BasicStringStack tagLines = new BasicStringStack();


	@Override
	public String[] validate(String xmlDocument) {
		String expr = "<[^<>]+>";
		Pattern p = Pattern.compile(expr);
		String text = xmlDocument;
		Matcher m = p.matcher(text);
		String[] check = new String[5];
		check[0] = "pass";

		while (m.find()) {
			String group = m.group();
			int pos = 1;

			if (group.charAt(group.length() - 1) == '/' || group.charAt(1) == '!' || group.charAt(1) == '?')
				continue;

			while (group.charAt(pos) != ' ' && group.charAt(pos) != '\t' && group.charAt(pos) != '\n'
					&& group.charAt(pos) != '>') {
				pos++;
			}

			String html = group.substring(1, pos);

			String[] lineBlock = text.substring(0, m.start()).split("\n");
			String lines = Integer.toString(lineBlock.length + 1);

			if (html.charAt(0) == '/') {
				String closeTag = html.substring(1, html.length());
				check = errorCheck(closeTag, lines, tagLines.peek(0));
				if (check[0].equals("pass"))
					continue;
				else
					break;
			} else {
				xmlDoc.push(html);
				tagLines.push(lines);
			}
		}
		if (check[0].equals("pass"))
			return null;

		return check;
	}


	public String[] errorCheck(String closedTag, String line, String stackLine) {
		String[] error = new String[5];

		if (xmlDoc.peek(0) == null || xmlDoc.peek(0).equals(closedTag) == false) {
			if (xmlDoc.peek(0) == null) {
				error[0] = "Orphan closing tag";
				error[1] = closedTag;
				error[2] = line;
			} else {
				error[0] = "Tag mismatch";
				error[1] = xmlDoc.peek(0);
				error[2] = stackLine;
				error[3] = closedTag;
				error[4] = line;
			}
		}

		else {
			error[0] = "pass";
			xmlDoc.pop();
			tagLines.pop();
		}
		return error;
	}
}
