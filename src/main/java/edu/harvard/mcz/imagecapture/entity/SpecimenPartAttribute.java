package edu.harvard.mcz.imagecapture.entity;

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Proxy object for SpecimenPartAttribute
 */
public class SpecimenPartAttribute implements Cloneable {

  private static final Logger log = LoggerFactory.getLogger(SpecimenPart.class);

  private Long specimenPartAttributeId;
  private SpecimenPart specimenPartId;
  private String attributeType = "caste";
  private String attributeValue;
  private String attributeUnits = "";
  private String attributeRemark;
  private String attributeDeterminer;
  private Date attributeDate;

  /**
   * @param specimenPartAttributeId
   * @param specimenPartId
   * @param attributeType
   * @param attributeValue
   * @param attributeUnits
   * @param attributeRemark
   * @param attributeDeterminer
   * @param attributeDate
   */
  public SpecimenPartAttribute(Long specimenPartAttributeId,
                               SpecimenPart specimenPartId,
                               String attributeType, String attributeValue,
                               String attributeUnits, String attributeRemark,
                               String attributeDeterminer, Date attributeDate) {
    this(specimenPartId, attributeType, attributeValue, attributeUnits,
         attributeRemark, attributeDeterminer, attributeDate);
    this.specimenPartAttributeId = specimenPartAttributeId;
  }

  /**
   * @param specimenPartId
   * @param attributeType
   * @param attributeValue
   * @param attributeUnits
   * @param attributeRemark
   * @param attributeDeterminer
   * @param attributeDate
   */
  public SpecimenPartAttribute(SpecimenPart specimenPartId,
                               String attributeType, String attributeValue,
                               String attributeUnits, String attributeRemark,
                               String attributeDeterminer, Date attributeDate) {
    super();
    this.specimenPartId = specimenPartId;
    this.attributeType = attributeType;
    this.attributeValue = attributeValue;
    this.attributeUnits = attributeUnits;
    this.attributeRemark = attributeRemark;
    this.attributeDeterminer = attributeDeterminer;
    this.attributeDate = attributeDate;
  }

  /**
   *
   */
  public SpecimenPartAttribute() {
    // TODO Auto-generated constructor stub
  }

  /**
   * @return the specimenPartAttributeId
   */
  public Long getSpecimenPartAttributeId() { return specimenPartAttributeId; }

  /**
   * @param specimenPartAttributeId the specimenPartAttributeId to set
   */
  public void setSpecimenPartAttributeId(Long specimenPartAttributeId) {
    this.specimenPartAttributeId = specimenPartAttributeId;
  }

  /**
   * @return the specimenPartId
   */
  public SpecimenPart getSpecimenPart() { return specimenPartId; }

  /**
   * @param specimenPart the specimenPartId to set
   */
  public void setSpecimenPart(SpecimenPart specimenPart) {
    this.specimenPartId = specimenPart;
  }

  // legacy as long as the hibernate property is called specimenPartId
  public SpecimenPart getSpecimenPartId() { return specimenPartId; }

  // legacy as long as the hibernate property is called specimenId
  public void setSpecimenPartId(SpecimenPart specimenPartId) {
    this.specimenPartId = specimenPartId;
  }

  /**
   * @return the attributeType
   */
  public String getAttributeType() { return attributeType; }

  /**
   * @param attributeType the attributeType to set
   */
  public void setAttributeType(String attributeType) {
    this.attributeType = attributeType;
  }

  /**
   * @return the attributeValue
   */
  public String getAttributeValue() { return attributeValue; }

  /**
   * @param attributeValue the attributeValue to set
   */
  public void setAttributeValue(String attributeValue) {
    this.attributeValue = attributeValue;
  }

  /**
   * @return the attributeUnits
   */
  public String getAttributeUnits() { return attributeUnits; }

  /**
   * @param attributeUnits the attributeUnits to set
   */
  public void setAttributeUnits(String attributeUnits) {
    this.attributeUnits = attributeUnits;
  }

  /**
   * @return the attributeRemark
   */
  public String getAttributeRemark() { return attributeRemark; }

  /**
   * @param attributeRemark the attributeRemark to set
   */
  public void setAttributeRemark(String attributeRemark) {
    this.attributeRemark = attributeRemark;
  }

  /**
   * @return the attributeDeterminer
   */
  public String getAttributeDeterminer() { return attributeDeterminer; }

  /**
   * @param attributeDeterminer the attributeDeterminer to set
   */
  public void setAttributeDeterminer(String attributeDeterminer) {
    this.attributeDeterminer = attributeDeterminer;
  }

  /**
   * @return the prepTypeAttributeDate
   */
  public Date getAttributeDate() { return attributeDate; }

  /**
   * @param prepTypeAttributeDate the prepTypeAttributeDate to set
   */
  public void setAttributeDate(Date prepTypeAttributeDate) {
    this.attributeDate = prepTypeAttributeDate;
  }

  @Override
  public Object clone() {
    SpecimenPartAttribute newAttr = new SpecimenPartAttribute(
        null, attributeType, attributeValue,
        attributeUnits, attributeRemark, attributeDeterminer,
        attributeDate == null ? null : (Date)attributeDate.clone());
    return newAttr;
  }
}
