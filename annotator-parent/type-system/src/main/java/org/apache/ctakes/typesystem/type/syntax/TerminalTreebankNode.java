

/* First created by JCasGen Fri Jan 03 13:40:16 CST 2014 */
package org.apache.ctakes.typesystem.type.syntax;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;



/** A Penn Treebank Node; as a terminal, there is an
				associated word, and the index of the word is a feature.
 * Updated by JCasGen Fri Jan 03 13:40:16 CST 2014
 * XML source: C:/WKT/git/schorndorfer/uima-components/annotator-parent/type-system/src/main/resources/org/apache/ctakes/typesystem/types/TypeSystem.xml
 * @generated */
public class TerminalTreebankNode extends TreebankNode {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(TerminalTreebankNode.class);
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
  protected TerminalTreebankNode() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public TerminalTreebankNode(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public TerminalTreebankNode(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public TerminalTreebankNode(JCas jcas, int begin, int end) {
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
  //* Feature: index

  /** getter for index - gets The index of this node in the top node's list of
						terminals.
   * @generated */
  public int getIndex() {
    if (TerminalTreebankNode_Type.featOkTst && ((TerminalTreebankNode_Type)jcasType).casFeat_index == null)
      jcasType.jcas.throwFeatMissing("index", "org.apache.ctakes.typesystem.type.syntax.TerminalTreebankNode");
    return jcasType.ll_cas.ll_getIntValue(addr, ((TerminalTreebankNode_Type)jcasType).casFeatCode_index);}
    
  /** setter for index - sets The index of this node in the top node's list of
						terminals. 
   * @generated */
  public void setIndex(int v) {
    if (TerminalTreebankNode_Type.featOkTst && ((TerminalTreebankNode_Type)jcasType).casFeat_index == null)
      jcasType.jcas.throwFeatMissing("index", "org.apache.ctakes.typesystem.type.syntax.TerminalTreebankNode");
    jcasType.ll_cas.ll_setIntValue(addr, ((TerminalTreebankNode_Type)jcasType).casFeatCode_index, v);}    
   
    
  //*--------------*
  //* Feature: tokenIndex

  /** getter for tokenIndex - gets The tokenIndex of a terminal is n, where the terminal
						is the nth token in a sentence. The value -1 means that this
						terminal does not correspond to a token (because it is a trace.)
						We assume that each token corresponds to a terminal.
   * @generated */
  public int getTokenIndex() {
    if (TerminalTreebankNode_Type.featOkTst && ((TerminalTreebankNode_Type)jcasType).casFeat_tokenIndex == null)
      jcasType.jcas.throwFeatMissing("tokenIndex", "org.apache.ctakes.typesystem.type.syntax.TerminalTreebankNode");
    return jcasType.ll_cas.ll_getIntValue(addr, ((TerminalTreebankNode_Type)jcasType).casFeatCode_tokenIndex);}
    
  /** setter for tokenIndex - sets The tokenIndex of a terminal is n, where the terminal
						is the nth token in a sentence. The value -1 means that this
						terminal does not correspond to a token (because it is a trace.)
						We assume that each token corresponds to a terminal. 
   * @generated */
  public void setTokenIndex(int v) {
    if (TerminalTreebankNode_Type.featOkTst && ((TerminalTreebankNode_Type)jcasType).casFeat_tokenIndex == null)
      jcasType.jcas.throwFeatMissing("tokenIndex", "org.apache.ctakes.typesystem.type.syntax.TerminalTreebankNode");
    jcasType.ll_cas.ll_setIntValue(addr, ((TerminalTreebankNode_Type)jcasType).casFeatCode_tokenIndex, v);}    
  }

    