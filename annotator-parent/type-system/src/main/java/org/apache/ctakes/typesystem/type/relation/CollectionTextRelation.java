

/* First created by JCasGen Fri Jan 03 13:40:16 CST 2014 */
package org.apache.ctakes.typesystem.type.relation;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.FSList;


/** 
 * Updated by JCasGen Fri Jan 03 13:40:16 CST 2014
 * XML source: C:/WKT/git/schorndorfer/uima-components/annotator-parent/type-system/src/main/resources/org/apache/ctakes/typesystem/types/TypeSystem.xml
 * @generated */
public class CollectionTextRelation extends Relation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(CollectionTextRelation.class);
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
  protected CollectionTextRelation() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public CollectionTextRelation(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public CollectionTextRelation(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** <!-- begin-user-doc -->
    * Write your own initialization here
    * <!-- end-user-doc -->
  @generated modifiable */
  private void readObject() {/*default - does nothing empty block */}
     
 
    
  //*--------------*
  //* Feature: members

  /** getter for members - gets A super-type for relationships between multiple spans
						of text.
   * @generated */
  public FSList getMembers() {
    if (CollectionTextRelation_Type.featOkTst && ((CollectionTextRelation_Type)jcasType).casFeat_members == null)
      jcasType.jcas.throwFeatMissing("members", "org.apache.ctakes.typesystem.type.relation.CollectionTextRelation");
    return (FSList)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((CollectionTextRelation_Type)jcasType).casFeatCode_members)));}
    
  /** setter for members - sets A super-type for relationships between multiple spans
						of text. 
   * @generated */
  public void setMembers(FSList v) {
    if (CollectionTextRelation_Type.featOkTst && ((CollectionTextRelation_Type)jcasType).casFeat_members == null)
      jcasType.jcas.throwFeatMissing("members", "org.apache.ctakes.typesystem.type.relation.CollectionTextRelation");
    jcasType.ll_cas.ll_setRefValue(addr, ((CollectionTextRelation_Type)jcasType).casFeatCode_members, jcasType.ll_cas.ll_getFSRef(v));}    
  }

    