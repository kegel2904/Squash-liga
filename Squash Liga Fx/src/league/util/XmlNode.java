package league.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class XmlNode {
	private final Node node;
	private List<XmlNode> children;

	private XmlNode(Node node) {
		this.node = node;
	}

	public String attribute(String name) {
		NamedNodeMap map = node.getAttributes();
		if (map == null)
			return null;
		Node attributeNode = map.getNamedItem(name);
		if (attributeNode == null)
			return null;
		return attributeNode.getNodeValue();
	}

	public List<String> attributes() {
		NamedNodeMap map = node.getAttributes();
		if (map == null)
			return Collections.emptyList();

		List<String> result = new ArrayList<>();
		for (int i = 0, n = map.getLength(); i < n; i++) {
			Node node = map.item(i);
			String name = node.getNodeName();
			result.add(name);
		}
		return result;
	}

	public List<XmlNode> children() {
		List<XmlNode> children = this.children;
		if (children == null) {
			children = new ArrayList<>();

			for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling())
				switch (child.getNodeType()) {
					case Node.DOCUMENT_NODE:
					case Node.ELEMENT_NODE:
						children.add(new XmlNode(child));
				}

			this.children = children;
		}
		return children;
	}

	public List<XmlNode> children(String name) {
		return Util.filter(children(), x -> x.name().equals(name));
	}

	public XmlNode firstChild(String name) {
		for (XmlNode node : children())
			if (node.name().equals(name))
				return node;
		return null;
	}

	public XmlNode onlyChild(String name) {
		XmlNode result = null;
		for (XmlNode node : children())
			if (node.name().equals(name)) {
				if (result != null)
					return null;
				result = node;
			}
		return result;
	}

	public String name() {
		return node.getNodeName();
	}

	@Override
	public String toString() {
		return node.getNodeName();
	}

	public static XmlNode parse(InputStream input) throws IOException, XmlException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new XmlException(e);
		}
		Document document;
		try {
			document = builder.parse(input);
		} catch (SAXException e) {
			throw new XmlException(e);
		}
		return new XmlNode(document);
	}

	public static class XmlException extends Exception {
		private static final long serialVersionUID = 1L;

		private XmlException(Throwable cause) {
			super(cause);
		}
	}

}
