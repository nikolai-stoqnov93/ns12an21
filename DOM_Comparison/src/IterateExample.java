import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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

import org.w3c.dom.Attr;
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
		final Document output = process(document1, document2);
		saveDocument(output, "src/output.xml");
	}

	private static Document process(Document document, Document document2)
			throws ParserConfigurationException, SAXException, IOException {

		final Node root1 = document.getFirstChild();
		final Node root2 = document2.getFirstChild();

		fillListFromChildren(attributeList1, root1);
		fillListFromChildren(attributeList2, root2);

		Document output = openDocument("src/output.xml");
		compareLists(output, attributeList1, attributeList2);

		return output;
	}

	private static void compareLists(Document output, ArrayList<Node> list1,
			ArrayList<Node> list2) {
		final Node root = output.getFirstChild();
		for (Node node1 : list1) {
			for (Node node2 : list2) {

				if (node1.getNodeValue().equals(node2.getNodeValue())) {
					final Element child = output.createElement("movie");
					Attr attr = output.createAttribute("name");
					attr.setNodeValue(node1.getNodeValue());
					child.setAttributeNode(attr);
					root.appendChild(child);
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

	private static void saveDocument(Document document, String filename)
			throws FileNotFoundException, TransformerException {
		final TransformerFactory factory = TransformerFactory.newInstance();
		factory.setAttribute("indent-number", 2);
		Transformer transformer = factory.newTransformer();
		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

		final StreamResult out = new StreamResult(new FileOutputStream(
				new File(filename)));
		transformer.transform(new DOMSource(document), out);
	}

}
