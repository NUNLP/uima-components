

/* First created by JCasGen Fri Jan 03 13:40:16 CST 2014 */
package org.apache.ctakes.typesystem.type.textsem;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;



/** The Generic modifier indicates that a named entity
				mention is generic, i.e., not related to the instance of a disorder,
				sign/symptom, etc.
 * Updated by JCasGen Fri Jan 03 13:40:16 CST 2014
 * XML source: C:/WKT/git/schorndorfer/uima-components/annotator-parent/type-system/src/main/resources/org/apache/ctakes/typesystem/types/TypeSystem.xml
 * @generated */
public class GenericModifier extends Modifier {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(GenericModifier.class);
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
  protected GenericModifier() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public GenericModifier(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public GenericModifier(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public GenericModifier(JCas jcas, int begin, int end) {
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
  //* Feature: indicated

  /** getter for indicated - gets 
   * @generated */
  public boolean getIndicated() {
    if (GenericModifier_Type.featOkTst && ((GenericModifier_Type)jcasType).casFeat_indicated == null)
      jcasType.jcas.throwFeatMissing("indicated", "org.apache.ctakes.typesystem.type.textsem.GenericModifier");
    return jcasType.ll_cas.ll_getBooleanValue(addr, ((GenericModifier_Type)jcasType).casFeatCode_indicated);}
    
  /** setter for indicated - sets  
   * @generated */
  public void setIndicated(boolean v) {
    if (GenericModifier_Type.featOkTst && ((GenericModifier_Type)jcasType).casFeat_indicated == null)
      jcasType.jcas.throwFeatMissing("indicated", "org.apache.ctakes.typesystem.type.textsem.GenericModifier");
    jcasType.ll_cas.ll_setBooleanValue(addr, ((GenericModifier_Type)jcasType).casFeatCode_indicated, v);}    
  }

    