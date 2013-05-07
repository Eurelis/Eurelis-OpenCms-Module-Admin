package com.eurelis.tools.xml.transformation.processors;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.dom4j.Attribute;
import org.dom4j.CDATA;
import org.dom4j.CharacterData;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.Text;
import org.dom4j.XPath;
import org.dom4j.tree.DefaultAttribute;
import org.dom4j.tree.DefaultElement;

import com.eurelis.tools.xml.transformation.Journal;
import com.eurelis.tools.xml.transformation.model.Destination;
import com.eurelis.tools.xml.transformation.model.Destination.Position;
import com.eurelis.tools.xml.transformation.model.TemplateTransformation;
import com.eurelis.tools.xml.transformation.model.UnitaryTransformation;
import com.eurelis.tools.xml.transformation.nodeselection.SXPath;
import com.eurelis.tools.xml.transformation.nodeselection.SXPathNode;
import com.eurelis.tools.xml.transformation.nodeselection.SXPathNodeAttribute;
import com.eurelis.tools.xml.transformation.nodeselection.XPATHDecomposition;
import com.eurelis.tools.xml.transformation.nodeselection.XPATHParser;

/**
 * Processor class for unitary transformations.
 */
public class UnitaryTransformationProcessor extends Processor implements DestinationProcessor {

	public static final String UNITARYPROCESSOR_ERROR_DESTINATION_NOT_FOUND_0 = "UNITARYPROCESSOR_ERROR_DESTINATION_NOT_FOUND_0";
	public static final String UNITARYPROCESSOR_ERROR_TEMPLATE_ROOTNODE_CREATION_IMPOSSIBLE_0 = "UNITARYPROCESSOR_ERROR_TEMPLATE_ROOTNODE_CREATION_IMPOSSIBLE_0";
	public static final String UNITARYPROCESSOR_ERROR_DESTINATION_XPATH_NODE_NOTFOUND_0 = "UNITARYPROCESSOR_ERROR_DESTINATION_XPATH_NODE_NOTFOUND_0";
	public static final String UNITARYPROCESSOR_ERROR_DESTINATION_XPATH_UNIQUE_NODE_NOTFOUND_0 = "UNITARYPROCESSOR_ERROR_DESTINATION_XPATH_UNIQUE_NODE_NOTFOUND_0";
	public static final String UNITARYPROCESSOR_ERROR_DESTINATION_SXPATH_UNIQUE_NODE_NOTFOUND_0 = "UNITARYPROCESSOR_ERROR_DESTINATION_SXPATH_UNIQUE_NODE_NOTFOUND_0";
	public static final String UNITARYPROCESSOR_ERROR_LASTNODE_CREATION_IMPOSSIBLE_INCOMPATIBLE_SOURCE_0 = "UNITARYPROCESSOR_ERROR_LASTNODE_CREATION_IMPOSSIBLE_INCOMPATIBLE_SOURCE_0";
	public static final String UNITARYPROCESSOR_ERROR_LASTNODE_CREATION_IMPOSSIBLE_NOTEMPTY_NODE_ALREADYEXISTS_0 = "UNITARYPROCESSOR_ERROR_LASTNODE_CREATION_IMPOSSIBLE_NOTEMPTY_NODE_ALREADYEXISTS_0";
	public static final String UNITARYPROCESSOR_ERROR_REFERENCE_NODE_NOTFOUND_0 = "UNITARYPROCESSOR_ERROR_REFERENCE_NODE_NOTFOUND_0";
	public static final String UNITARYPROCESSOR_ERROR_SXPATH_FIRSTSECTION_INCOMPATIBLE_WITH_DOCUMENT_ROOTNODE_0 = "UNITARYPROCESSOR_ERROR_SXPATH_FIRSTSECTION_INCOMPATIBLE_WITH_DOCUMENT_ROOTNODE_0";
	public static final String UNITARYPROCESSOR_ERROR_MERGENODES_INCOMPATIBLE_SOURCE_DESTINATION_0 = "UNITARYPROCESSOR_ERROR_MERGENODES_INCOMPATIBLE_SOURCE_DESTINATION_0";
	public static final String UNITARYPROCESSOR_ERROR_MERGE_NOTEMPTY_DESTINATION_0 = "UNITARYPROCESSOR_ERROR_MERGE_NOTEMPTY_DESTINATION_0";
	public static final String UNITARYPROCESSOR_ERROR_DESTINATION_XPATH_FIRSTNODE_NOTFOUND_0 = "UNITARYPROCESSOR_ERROR_DESTINATION_XPATH_FIRSTNODE_NOTFOUND_0";
	
	
	/** The unitaryTransformation to process. */
	private UnitaryTransformation uTransformation = null;
	
	/**
	 * Instantiates a new unitary transformation processor.
	 *
	 * @param journal the journal
	 * @param xmlTransformationProcessor the xml transformation processor
	 * @param unitaryTransformation the unitary transformation
	 */
	public UnitaryTransformationProcessor(Journal journal, XMLTransformationProcessor xmlTransformationProcessor, UnitaryTransformation unitaryTransformation) {
		super(journal, xmlTransformationProcessor);
		this.uTransformation = unitaryTransformation;
	}

	/**
	 * Process the transformation.
	 *
	 * @param original the original document
	 * @param target the target document
	 */
	public void processTransformation(Document original, Document target) {
	
		XPath source = this.uTransformation.getSource();
		
		@SuppressWarnings("unchecked")
		List<Node> nodeList = source.selectNodes(original);
		
		for (Node n : nodeList) {
			processTransformationForElement(original, target, n);
		}
		
	}

	/**
	 * Process transformation for a source node.
	 *
	 * @param original the original document
	 * @param target the target document
	 * @param source the source node
	 */
	public void processTransformationForElement(Document original, Document target, Node source) {
		
		
		Destination dst = uTransformation.getDestination();
		Node destinationElement = null;
		
		boolean destinationElementNotFound = false;
		
		if (dst != null) {
			destinationElement = processDestination(target, source, dst);
			
			if (destinationElement == null) {
				// destination specifiee mais non trouvee = erreur
				journal.error(this, UNITARYPROCESSOR_ERROR_DESTINATION_NOT_FOUND_0,  dst.toString());
				destinationElementNotFound = true;
			}
			
		}
		
		TemplateTransformation tt = uTransformation.getTemplateTransformation();
		if (!destinationElementNotFound) {
			if (tt != null) {
				// si transformation de template
				
				
				TemplateTransformationProcessor ttp = new TemplateTransformationProcessor(journal, this, tt);
				
				Element transformationResult = ttp.processTransformation(source);
				
				
				Node transformationTarget = null;
						
			
				if (destinationElement != null) {	
					// la transformation de template est applique au niveau de la source
					String newName = transformationResult.getName();
					
					@SuppressWarnings("rawtypes")
					List test = destinationElement.selectNodes(newName);
					
					
					if (test.isEmpty() && destinationElement instanceof Element) {
						transformationTarget = ((Element)destinationElement).addElement(newName);
						
					}
					else {
						journal.error(this, UNITARYPROCESSOR_ERROR_TEMPLATE_ROOTNODE_CREATION_IMPOSSIBLE_0, newName);
						transformationTarget = null;
					}
					
				}
				else {
					
					
					// le resultat de la transformation de template est place ailleurs
					Element sourceParent = source.getParent();
					//String sourceName = source.getName();
					
					String parentXPATH = sourceParent.getUniquePath();
					
					Element dstParent = (Element) target.selectSingleNode(parentXPATH);
					
					
					if (dstParent != null) {
						String newName = transformationResult.getName();
						
						
						@SuppressWarnings("rawtypes")
						List test = dstParent.selectNodes(newName);
						
						if (test.isEmpty()) {
							transformationTarget = dstParent.addElement(newName);
							
						}
						else {
							journal.error(this, UNITARYPROCESSOR_ERROR_TEMPLATE_ROOTNODE_CREATION_IMPOSSIBLE_0, newName);
							transformationTarget = null;
						}
						
						
					}
					else {
						
						
					}
					
					
				}
				
				
				if (transformationTarget != null) {
					this.mergeNodes(transformationTarget, transformationResult);
				}
			
			}
			else {
		
				if (destinationElement != null) {
					this.mergeNodes(destinationElement, source);
					
				}
				else {
					// suppression !
					
				}
			
			}
			
		}
		
		
		
	}

	/**
	 * Find or create de destination node according to a Destination instance.
	 *
	 * @param target the target document
	 * @param source the source document
	 * @param dst the destination instance
	 * @return the found or created node if successful, null otherwise
	 */
	public Node processDestination(Document target, Node source, Destination dst) {
		return dst.beProcessed(target, source, this);
	}

	/* (non-Javadoc)
	 * @see com.eurelis.tools.xml.transformation.processors.DestinationProcessor#processXPathDestination(org.dom4j.Document, org.dom4j.Node, java.lang.String)
	 */
	public Node processXPathDestination(Document target, Node source, String dst, Position pos) {
		
		Element originalParentNode = source.getParent();
		String originalParentXPath = originalParentNode.getUniquePath();
		
		Node parentNode = target.selectSingleNode(originalParentXPath);
		Node tmpParentNode = null;
		
		Set<Node> parentNodeSet = new HashSet<Node>();
		parentNodeSet.add(parentNode);
		
		do {
			tmpParentNode = parentNode.getParent();
			
			if (tmpParentNode != null) {
				parentNode = tmpParentNode;
				parentNodeSet.add(parentNode);
			}
			
		} while(tmpParentNode != null);
		
		
		Node resultNode = null;
		
		XPATHDecomposition pathDecomposition = XPATHParser.getInstance().decomposeXpath(dst, pos);
		
		
		XPath firstXpath = pathDecomposition.getFirstElement();
		
		Node workNode = firstXpath.selectSingleNode(target);
		
		if (workNode != null) {
		
			for (XPath xpath : pathDecomposition.getElements()) {
				// avec le mode xpath, le noeud suivant DOIT exister
				String position = workNode.getUniquePath();
				
				@SuppressWarnings("unchecked")
				List<Node> subNodes = xpath.selectNodes(workNode);
				
				
				workNode = xpath.selectSingleNode(workNode);
			
				if (subNodes.isEmpty()) {
					//journal.error(this, "noeud " + (xpath.toString()) + " non trouve dans la decomposition xpath de " + dst + " position actuelle : " + position);
					journal.error(this, UNITARYPROCESSOR_ERROR_DESTINATION_XPATH_NODE_NOTFOUND_0, xpath.toString(), dst, position);	
					return null;
				}
				else if (subNodes.size() == 1) {
					workNode = subNodes.get(0);
					
				}
				
				else {
					for (Node sn : subNodes) {
						
						if (parentNodeSet.contains(sn)) {
							workNode = sn;
						}
						
					}
					
					if (workNode == null) {
						journal.error(this, UNITARYPROCESSOR_ERROR_DESTINATION_XPATH_UNIQUE_NODE_NOTFOUND_0, xpath.toString(), dst, position);
						//journal.error(this, "noeud " + (xpath.toString()) + " non trouve dans la decomposion xpath de " + dst + " position actuelle : " + position + " plusieurs candidats trouves, aucun n'est parent de la source");
					}
					
				}
				
			}
			
			String lastElement = pathDecomposition.getLastElement();
			if (lastElement == null) {
				lastElement = "";
			}
			
						
			if (lastElement.isEmpty() && workNode instanceof Element) {
				
				if (source instanceof Attribute) {
					
						resultNode = ((Element)workNode).addAttribute(source.getName(), ""); // attribut vide pour le merge
					
					
					
				}
				else if (source instanceof Element) {
					
					if (pos == Position.FIRST) {
						Element element = new DefaultElement(source.getName());
						((Element)workNode).getParent().elements().add(0, element);
						resultNode = element;
					}
					else if (pos == Position.LAST) {
						resultNode = ((Element)workNode).addElement(source.getName());
					}
					
					
					
				}
				
				else {
					journal.error(this, UNITARYPROCESSOR_ERROR_LASTNODE_CREATION_IMPOSSIBLE_INCOMPATIBLE_SOURCE_0);
					
				}	
			}
			else if (lastElement.charAt(0) == '@') {
				// attribut
				String attributeName = lastElement.substring(1);
				Attribute attribute = new DefaultAttribute(attributeName, "");
				((Element)workNode).add(attribute);
				//resultNode =  ((Element)workNode).addAttribute(attributeName, ""); // attribut vide pour le merge
				resultNode = attribute;
				
			}
			else {
				// existe t'il un noeud vide unique portant ce nom ?
				@SuppressWarnings("unchecked")
				List<Element> testList = workNode.selectNodes(lastElement);
				
				
				// TODO GESTION DE LA POSITION !!!!
				
				if (testList.size() == 1) {
					Element testElement = testList.get(0);
					
					if (pos == Position.FIRST || pos == Position.LAST) {
					
						if (testElement.attributeCount() == 0 && testElement.elements().isEmpty() && testElement.getTextTrim().isEmpty()) {
							resultNode = testList.get(0);
						}
						else {
							journal.error(this, UNITARYPROCESSOR_ERROR_LASTNODE_CREATION_IMPOSSIBLE_NOTEMPTY_NODE_ALREADYEXISTS_0);
							return null;
						}
					
					}
					
					else {
						int index = testElement.getParent().elements().indexOf(testElement);
						if (pos == Position.AFTER) {
							index++;
						}
						
						Element newElement = new DefaultElement(lastElement);
						testElement.getParent().elements().add(index, newElement);
						
						resultNode = newElement;
						
					}
					
				}
				else if (testList.isEmpty()) {
					if (pos == Position.FIRST) {
						Element newElement = new DefaultElement(lastElement);
						((Element)workNode).elements().add(0, newElement);
						resultNode = newElement;
						
					}
					else if (pos == Position.LAST) {
						resultNode = ((Element)workNode).addElement(lastElement);
					}
					else {
						journal.error(this, UNITARYPROCESSOR_ERROR_REFERENCE_NODE_NOTFOUND_0);
					}
					
					
				}
				else {
					
					
					if (pos == Position.FIRST || pos == Position.LAST) {
						journal.error(this, UNITARYPROCESSOR_ERROR_DESTINATION_XPATH_UNIQUE_NODE_NOTFOUND_0);
						return null;
					}
					else if (pos == Position.BEFORE) {
						Element firstElement = testList.get(0);
						int index = ((Element)workNode).elements().indexOf(firstElement);
						
						Element newElement = new DefaultElement(lastElement);
						((Element)workNode).elements().add(index, newElement);
						resultNode = newElement;
					}
					else if (pos == Position.AFTER) {
						Element lastEl = testList.get(testList.size() - 1);
						int index = ((Element)workNode).elements().indexOf(lastEl) + 1;
						
						Element newElement = new DefaultElement(lastElement);
						((Element)workNode).elements().add(index, newElement);
						resultNode = newElement;
					}
				}
				
			}
		
		}
		else {
			journal.error(this, UNITARYPROCESSOR_ERROR_DESTINATION_XPATH_FIRSTNODE_NOTFOUND_0);
		}
		
		
		return resultNode;
	}

	/* (non-Javadoc)
	 * @see com.eurelis.tools.xml.transformation.processors.DestinationProcessor#processSXPathDestination(org.dom4j.Document, org.dom4j.Node, java.lang.String)
	 */
	public Node processSXPathDestination(Document target, Node source, String dst, Position position) {
		
		Node resultNode = null;
		
		// le parentNodeSet doit etre construit a partir du document de destination !!!
		
		SXPath sxpath = XPATHParser.getInstance().parseFailOverPath(dst, position);
		
		
		Element originalParentNode = source.getParent();
		String originalParentXPath = originalParentNode.getUniquePath();
		
		
		
		Node parentNode = target.selectSingleNode(originalParentXPath);
		Node tmpParentNode = null;
		
		Set<Node> parentNodeSet = new HashSet<Node>();
		parentNodeSet.add(parentNode);
		
		
		
		
		do {
			tmpParentNode = parentNode.getParent();
			
			if (tmpParentNode != null) {
				parentNode = tmpParentNode;
				parentNodeSet.add(parentNode);
			}
			
		} while(tmpParentNode != null);
		
		if (parentNode != null) {
			List<SXPathNode> sxPathNodeList = sxpath.getNodes();
			Node workNode = parentNode;
			
			Iterator<SXPathNode> sxPathNodeIterator = sxPathNodeList.iterator();
			
			SXPathNode firstNode = sxPathNodeIterator.next();
			
			if (firstNode.equals((Element)workNode)) {
			
				while (workNode != null && sxPathNodeIterator.hasNext()) {
					SXPathNode pathNode = sxPathNodeIterator.next();
				
					workNode = processSXPathNode(workNode, pathNode, parentNodeSet, position);
				
				}
			}
			else {
				journal.error(this, UNITARYPROCESSOR_ERROR_SXPATH_FIRSTSECTION_INCOMPATIBLE_WITH_DOCUMENT_ROOTNODE_0);
				workNode = null;
			}
			
			if (workNode != null) {
				// on va creer le dernier segment
				
				String lastElement = sxpath.getLastElement();
				
				if (lastElement == null) {
					lastElement = "";
				}
				
				
				if (lastElement.isEmpty() && workNode instanceof Element) {
					
					if (source instanceof Attribute) {
						resultNode = ((Element)workNode).addAttribute(source.getName(), ""); // attribut vide pour le merge
					}
					else if (source instanceof Element) {
						resultNode = ((Element)workNode).addElement(source.getName());
					}
					
					else {
						journal.error(this, UNITARYPROCESSOR_ERROR_LASTNODE_CREATION_IMPOSSIBLE_INCOMPATIBLE_SOURCE_0);
						
					}	
				}
				else if (lastElement.charAt(0) == '@') {
					// attribut
					String attributeName = lastElement.substring(1);
					
					Attribute attribute = new DefaultAttribute(attributeName, "");
					((Element)workNode).add(attribute);
					//resultNode =  ((Element)workNode).addAttribute(attributeName, ""); // attribut vide pour le merge
					resultNode = attribute;
				}
				else {
					//resultNode = ((Element)workNode).addElement(lastElement);
					
					// existe t'il un noeud vide unique portant ce nom ?
					@SuppressWarnings("unchecked")
					List<Element> testList = workNode.selectNodes(lastElement);
					
					if (testList.size() == 1) {
						Element testElement = testList.get(0);
						
						if (testElement.attributeCount() == 0 && testElement.elements().isEmpty() && testElement.getTextTrim().isEmpty()) {
							resultNode = testList.get(0);
						}
						else {
							journal.error(this, UNITARYPROCESSOR_ERROR_LASTNODE_CREATION_IMPOSSIBLE_NOTEMPTY_NODE_ALREADYEXISTS_0);
							return null;
						}
						
					}
					else if (testList.isEmpty()) {
						resultNode = ((Element)workNode).addElement(lastElement);
					}
					else {
						journal.error(this, UNITARYPROCESSOR_ERROR_DESTINATION_XPATH_UNIQUE_NODE_NOTFOUND_0);
						return null;
					}
					
					
				}
				
				
				
				
			}
		
		}
		
		return resultNode;
		
		// throw new UnsupportedOperationException();
	}
	
	/**
	 * Process a SXPathNode to found or create the desired subnode.
	 *
	 * @param dst the current workNode
	 * @param sxPathNode the sxPathNode
	 * @param parentNodeSet a Set of node representing all parent nodes of the source one in the destination document
	 * @return the found or created node if successful, null otherwise
	 */
	@SuppressWarnings("unchecked")
	public Node processSXPathNode(Node dst, SXPathNode sxPathNode, Set<Node> parentNodeSet, Position position) {
		
		Node returnNode = null;
		
		if (dst instanceof Element) {
			Element dstElement = (Element)dst;
			
			List<Node> firstNodeList = new ArrayList<Node>();
			List<Node> secondNodeList = new ArrayList<Node>();
			@SuppressWarnings("unchecked")
			List<Node> subNodeList = dstElement.selectNodes(sxPathNode.getName());
			
			for (Node sn : subNodeList) {
				if (sn instanceof Element) {
					Element snElement = (Element)sn;
					
					if (sxPathNode.equals(snElement)) {
				
						firstNodeList.add(snElement); // liste des noeuds correspondant au sxPathNode
						
						if (parentNodeSet.contains(snElement)) {
							secondNodeList.add(snElement); // liste des noeuds correspondant au sxPathNode et etant en plus ancetre du noeud source						
						}
					}
				}
			}
			
			
			if (firstNodeList.isEmpty()) {
				Element newElement = null;
				
				if (position == Position.LAST) {
					newElement = dstElement.addElement(sxPathNode.getName());
				}
				
				else if (position == Position.FIRST) {
					newElement = new DefaultElement(sxPathNode.getName());
					dstElement.elements().add(0, newElement);
				}
				
				
				for (SXPathNodeAttribute sxAttribute : sxPathNode.getAttributes()) {
					newElement.addAttribute(sxAttribute.getName(), sxAttribute.getValue());
				}
				
				returnNode = newElement;
			}
			
			else if (firstNodeList.size() == 1) {
				returnNode = firstNodeList.get(0);	
			}
			
			else if (firstNodeList.size() > 1) {
				if (secondNodeList.size() == 1) {
					returnNode = secondNodeList.get(0);
				}
				
				else {
					journal.error(this, UNITARYPROCESSOR_ERROR_DESTINATION_SXPATH_UNIQUE_NODE_NOTFOUND_0, sxPathNode.toString(), dstElement.getName());
				}
				
			}
			
			
			
		}
		
		return returnNode;
		
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.eurelis.tools.xml.transformation.processors.Processor#getName()
	 */
	public String getName() {
		return "UnitaryTransformation";
	}
	
	
	
	/**
	 * Merge nodes.
	 *
	 * @param dst the dst node
	 * @param src the src node
	 * @return true, if successful, false otherwise
	 */
	protected boolean mergeNodes(Node dst, Node src) {
		boolean success = false;
		
		
		if (dst instanceof Attribute && src instanceof Attribute) {
			success = mergeAttributes((Attribute)dst, (Attribute)src);
		}
		else if (dst instanceof CDATA && src instanceof CDATA) {
			success = mergeCDATA((CDATA)dst, (CDATA)src);
		}
		else if (dst instanceof CharacterData && src instanceof CharacterData) {
			success = mergeCharacterData((CharacterData)dst, (CharacterData)src);
		}
		else if (dst instanceof Element && src instanceof Element) {
			success = mergeElements((Element)dst, (Element)src);
		}
		else if (dst instanceof Text && src instanceof Text) {
			success = mergeText((Text)dst, (Text)src);
		}
		
		else {
			journal.error(this, UNITARYPROCESSOR_ERROR_MERGENODES_INCOMPATIBLE_SOURCE_DESTINATION_0,  dst.getClass().getName(), src.getClass().getName());
		}
		
		return success;
	}
	
	
	/**
	 * Merge attributes nodes.
	 *
	 * @param dst the destination attribute
	 * @param src the source attribute
	 * @return true, if successful
	 */
	protected boolean mergeAttributes(Attribute dst, Attribute src) {
		boolean success = false;
		
		if (dst.getValue().isEmpty()) {
			dst.setValue(src.getValue());
			success = true;
		}
		else {
			journal.error(this, UNITARYPROCESSOR_ERROR_MERGE_NOTEMPTY_DESTINATION_0);
		}
		
		
		return success;
	}
	
	/**
	 * Merge CDATA nodes
	 *
	 * @param dst the destination nodes
	 * @param src the source nodes
	 * @return true, if successful
	 */
	protected boolean mergeCDATA(CDATA dst, CDATA src) {
		boolean success = false;
		
		if (!dst.hasContent()) {
			dst.setText(src.getText());
			success = true;
		}
		else {
			journal.error(this, UNITARYPROCESSOR_ERROR_MERGE_NOTEMPTY_DESTINATION_0);
		}
		
		
		return success;	
	}
	
	/**
	 * Merge character data nodes
	 *
	 * @param dst the destination CharacterData
	 * @param src the source CharacterData
	 * @return true, if successful
	 */
	protected boolean mergeCharacterData(CharacterData dst, CharacterData src) {
		boolean success = false;
		
		if (dst.getText().trim().isEmpty()) {
			dst.setText(src.getText());
			success = true;
		}
		else {
			journal.error(this, UNITARYPROCESSOR_ERROR_MERGE_NOTEMPTY_DESTINATION_0);
		}
		
		return success;	
	}
	
	/**
	 * Merge elements.
	 *
	 * @param dst the destination Element
	 * @param src the source Element
	 * @return true, if successful
	 */
	protected boolean mergeElements(Element dst, Element src) {
		boolean success = false;
		
		if (dst.attributeCount() == 0 && dst.elements().isEmpty() && dst.getTextTrim().isEmpty()) {
			
			@SuppressWarnings("unchecked")
			List<Attribute> attributeList = src.attributes();
			for (Attribute attribute : attributeList) {
				dst.addAttribute(attribute.getName(), attribute.getValue());
			}
			
			
			@SuppressWarnings("unchecked")
			List<Node> contentList = src.content();
			
			for (Node n : contentList) {
				Node copy = (Node)n.clone();
				copy.detach();
				
				dst.add(copy);
			}
			
			
		}
		else {
			journal.error(this, UNITARYPROCESSOR_ERROR_MERGE_NOTEMPTY_DESTINATION_0);
		}
		
		return success;	
	}
	
	/**
	 * Merge text nodes.
	 *
	 * @param dst the destination Text
	 * @param src the source Text
	 * @return true, if successful
	 */
	protected boolean mergeText(Text dst, Text src) {
		boolean success = false;
		
		if (!dst.hasContent()) {
			dst.setText(src.getText());
			
			success = true;
		}
		else {
			journal.error(this, UNITARYPROCESSOR_ERROR_MERGE_NOTEMPTY_DESTINATION_0);
		}
		
		
		return success;	
	}
	
}