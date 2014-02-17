import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

class IterateExample {

	private static final int NODE_TYPE_ELEMENT = 1;
	private static ArrayList<Node> attributeList1 = new ArrayList<Node>();
	private static ArrayList<Node> attributeList2 = new ArrayList<Node>();

	public static void main(String[] args) throws ParserConfigurationException,
			SAXException, IOException, TransformerException {
		final Document document1 = openDocument("src/sample.xml");
		final Document document2 = openDocument("src/sample2.xml");
		process(document1, document2);
	
	}

	private static void process(Document document, Document document2) {
		final Node root1 = document.getFirstChild();
		final Node root2 = document2.getFirstChild();

		fillListFromChildren(attributeList1, root1);
		fillListFromChildren(attributeList2, root2);

		compareLists(attributeList1, attributeList2);

	}

	private static void compareLists(ArrayList<Node> list1,
			ArrayList<Node> list2) {
		for (Node node1 : list1) {
			for (Node node2 : list2) {
				if (node1.getNodeValue().equals(node2.getNodeValue())) {
					System.out.println(node2.getNodeValue());
				}
			}
		}
	}

	private static void fillListFromChildren(ArrayList<Node> list, Node node) {

		NodeList children = node.getChildNodes();

		fillList(list, node);

		if (children.getLength() == 0) {
			return;
		}

		for (int i = 0; i < children.getLength(); i++) {
			fillListFromChildren(list, children.item(i));
		}

	}

	private static void fillList(ArrayList<Node> list, Node node) {
		final NamedNodeMap attributes = node.getAttributes();
		if (attributes == null)
			return;
		for (int i = 0; i < attributes.getLength(); i++) {
			if (!list.contains(attributes.item(i)))
				list.add(attributes.item(i));
		}

	}

	private static Document openDocument(String filename)
			throws ParserConfigurationException, SAXException, IOException {
		final DocumentBuilderFactory factory = DocumentBuilderFactory
				.newInstance();
		final DocumentBuilder builder = factory.newDocumentBuilder();
		return builder.parse(new File(filename));
	}
}
/*Имам я и в репозитори в github до 06.03.14 мисля да направя така че да се записват стойностите на атрибутите в отделен xml  файл и ако може да ми предложите продължение на проекта и до 26.02.14 трябва да съм готов и с другото домашно за което имам доста въпроси ,но сега трябва да излизам ,защото ходя и на уроци, ще съм си вкъщи към 16:00 */ 