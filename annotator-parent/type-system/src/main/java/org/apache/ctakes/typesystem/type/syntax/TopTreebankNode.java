

/* First created by JCasGen Fri Jan 03 13:40:16 CST 2014 */
package org.apache.ctakes.typesystem.type.syntax;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.FSArray;


/** A Penn Treebank Node; as the top node, this stores the
				whole subsumed sentence's parse tree as a string. It also stores all
				the terminals, allowing for traversal of the tree bottom-up
				(top-down is implied through the TreebankNode's 'children'
				attribute).
 * Updated by JCasGen Fri Jan 03 13:40:16 CST 2014
 * XML source: C:/WKT/git/schorndorfer/uima-components/annotator-parent/type-system/src/main/resources/org/apache/ctakes/typesystem/types/TypeSystem.xml
 * @generated */
public class TopTreebankNode extends TreebankNode {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(TopTreebankNode.class);
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;
  /** @generated  */
  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected TopTreebankNode() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public TopTreebankNode(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public TopTreebankNode(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public TopTreebankNode(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** <!-- begin-user-doc -->
    * Write your own initialization here
    * <!-- end-user-doc -->
  @generated modifiable */
  private void readObject() {/*default - does nothing empty block */}
     
 
    
  //*--------------*
  //* Feature: treebankParse

  /** getter for treebankParse - gets A bracketed sentence string representing the parse
						tree.
   * @generated */
  public String getTreebankParse() {
    if (TopTreebankNode_Type.featOkTst && ((TopTreebankNode_Type)jcasType).casFeat_treebankParse == null)
      jcasType.jcas.throwFeatMissing("treebankParse", "org.apache.ctakes.typesystem.type.syntax.TopTreebankNode");
    return jcasType.ll_cas.ll_getStringValue(addr, ((TopTreebankNode_Type)jcasType).casFeatCode_treebankParse);}
    
  /** setter for treebankParse - sets A bracketed sentence string representing the parse
						tree. 
   * @generated */
  public void setTreebankParse(String v) {
    if (TopTreebankNode_Type.featOkTst && ((TopTreebankNode_Type)jcasType).casFeat_treebankParse == null)
      jcasType.jcas.throwFeatMissing("treebankParse", "org.apache.ctakes.typesystem.type.syntax.TopTreebankNode");
    jcasType.ll_cas.ll_setStringValue(addr, ((TopTreebankNode_Type)jcasType).casFeatCode_treebankParse, v);}    
   
    
  //*--------------*
  //* Feature: terminals

  /** getter for terminals - gets Stores the terminal nodes of the parse tree. This
						allows for bottom-up traversal of a tree.
   * @generated */
  public FSArray getTerminals() {
    if (TopTreebankNode_Type.featOkTst && ((TopTreebankNode_Type)jcasType).casFeat_terminals == null)
      jcasType.jcas.throwFeatMissing("terminals", "org.apache.ctakes.typesystem.type.syntax.TopTreebankNode");
    return (FSArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((TopTreebankNode_Type)jcasType).casFeatCode_terminals)));}
    
  /** setter for terminals - sets Stores the terminal nodes of the parse tree. This
						allows for bottom-up traversal of a tree. 
   * @generated */
  public void setTerminals(FSArray v) {
    if (TopTreebankNode_Type.featOkTst && ((TopTreebankNode_Type)jcasType).casFeat_terminals == null)
      jcasType.jcas.throwFeatMissing("terminals", "org.apache.ctakes.typesystem.type.syntax.TopTreebankNode");
    jcasType.ll_cas.ll_setRefValue(addr, ((TopTreebankNode_Type)jcasType).casFeatCode_terminals, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for terminals - gets an indexed value - Stores the terminal nodes of the parse tree. This
						allows for bottom-up traversal of a tree.
   * @generated */
  public TerminalTreebankNode getTerminals(int i) {
    if (TopTreebankNode_Type.featOkTst && ((TopTreebankNode_Type)jcasType).casFeat_terminals == null)
      jcasType.jcas.throwFeatMissing("terminals", "org.apache.ctakes.typesystem.type.syntax.TopTreebankNode");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((TopTreebankNode_Type)jcasType).casFeatCode_terminals), i);
    return (TerminalTreebankNode)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((TopTreebankNode_Type)jcasType).casFeatCode_terminals), i)));}

  /** indexed setter for terminals - sets an indexed value - Stores the terminal nodes of the parse tree. This
						allows for bottom-up traversal of a tree.
   * @generated */
  public void setTerminals(int i, TerminalTreebankNode v) { 
    if (TopTreebankNode_Type.featOkTst && ((TopTreebankNode_Type)jcasType).casFeat_terminals == null)
      jcasType.jcas.throwFeatMissing("terminals", "org.apache.ctakes.typesystem.type.syntax.TopTreebankNode");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((TopTreebankNode_Type)jcasType).casFeatCode_terminals), i);
    jcasType.ll_cas.ll_setRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((TopTreebankNode_Type)jcasType).casFeatCode_terminals), i, jcasType.ll_cas.ll_getFSRef(v));}
  }

    